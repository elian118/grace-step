/// <reference types="node" />

import fs from 'fs';
import path from 'path';
import { fileURLToPath } from 'url';
import dotenv from 'dotenv';

dotenv.config();

// ESM 환경에서 __dirname 대체 구문
const __filename = fileURLToPath(import.meta.url);
const __dirname = path.dirname(__filename);

// OpenAPI 3.x 스펙 인터페이스 정의
interface SchemaRef {
  $ref?: string;
  type?: string;
  items?: SchemaRef;
}

interface MediaTypeObject {
  schema?: SchemaRef;
}

interface OperationObject {
  summary?: string;
  tags?: string[];
  description?: string;
  operationId?: string;
  requestBody?: {
    content?: Record<string, MediaTypeObject>;
  };
  responses?: Record<string, { content?: Record<string, MediaTypeObject> }>;
}

interface SwaggerSpec {
  paths: Record<string, Record<string, OperationObject>>;
}

const rawBaseUrl = process.env.VITE_API_BASE_URL || 'http://localhost:8080/api/v1';
const serverHost = rawBaseUrl.replace(/\/api\/v1\/?$/, '');

const OUTPUT_DIR = path.resolve(__dirname, '../src/api/generated');
const SCHEMA_URL = `${serverHost}/api-docs/00.%20TOTAL-API`;

// outputFiles 매핑과 동일하게 맞춤
const FILE_MAPPING: Record<string, { title: string; match: (path: string, tag: string) => boolean }> = {
  'userApi.md': {
    title: 'User API 명세',
    match: (url, tag) => tag === 'User API' || url.startsWith('/api/v1/user'),
  },
  'studentApi.md': {
    title: 'Student API 명세 (프로필/출석)',
    match: (url, tag) =>
      tag === 'Student Profile API' || tag === 'Student Attendance API' || url.startsWith('/api/v1/students'),
  },
  'examApi.md': {
    title: 'Exam API 명세 (시험지)',
    match: (url, tag) => tag === 'Exam API' || url.startsWith('/api/v1/classes/exams'),
  },
};

/**
 * $ref 또는 type 스키마에서 타입명 추출 (#/components/schemas/UserResponse -> UserResponse)
 */
const extractTypeName = (schema?: SchemaRef): string => {
  if (!schema) return '-';

  if (schema.$ref) {
    const parts = schema.$ref.split('/');
    return parts[parts.length - 1] || '-';
  }

  if (schema.type === 'array' && schema.items) {
    return `${extractTypeName(schema.items)}[]`;
  }

  return schema.type || '-';
};

/**
 * operationId 또는 Method+Path 기반으로 RTK Query Hook 이름 추론
 */
const resolveHookName = (method: string, pathUrl: string, operationId?: string): string => {
  const isQuery = method.toUpperCase() === 'GET';

  // eslint-disable-next-line no-useless-assignment
  let baseName = '';
  if (operationId) {
    baseName = operationId.charAt(0).toUpperCase() + operationId.slice(1);
  } else {
    baseName = pathUrl
      .replace(/\/api\/v1\/?/, '')
      .split('/')
      .filter(Boolean)
      .map((segment) =>
        segment
          .replace(/[{}]/g, '')
          .replace(/[-_]([a-z])/g, (_, group) => group.toUpperCase())
          .replace(/^[a-z]/, (first) => first.toUpperCase()),
      )
      .join('');
    baseName = method.toLowerCase().charAt(0).toUpperCase() + method.slice(1) + baseName;
  }

  if (isQuery) {
    // GET 요청인 경우 일반 Query 훅과 Lazy Query 훅을 함께 표시
    return `\`use${baseName}Query\`<br/>\`useLazy${baseName}Query\``;
  }

  return `\`use${baseName}Mutation\``;
};

/**
 * Operation에서 Request Type 추출
 */
const getRequestType = (operation: OperationObject): string => {
  const jsonContent = operation.requestBody?.content?.['application/json'];
  return extractTypeName(jsonContent?.schema);
};

/**
 * Operation에서 Response Type (200/201 성공 응답) 추출
 */
const getResponseType = (operation: OperationObject): string => {
  const successResponse = operation.responses?.['200'] || operation.responses?.['201'];
  const jsonContent = successResponse?.content?.['application/json'] || successResponse?.content?.['*/*'];

  const typeName = extractTypeName(jsonContent?.schema);
  return typeName === '-' ? 'void' : typeName;
};

const generateMarkdownDocs = async () => {
  console.log('🔄 Swagger 스키마로부터 Markdown 문서 생성 중...');

  // 🛡️ [안전장치 1] Target 출력 디렉터리 사전 보장
  if (!fs.existsSync(OUTPUT_DIR)) {
    fs.mkdirSync(OUTPUT_DIR, { recursive: true });
    console.log(`📁 출력 디렉터리 생성: ${OUTPUT_DIR}`);
  }

  let spec: SwaggerSpec;

  try {
    // 🛡️ [안전장치 2] 백엔드 Swagger URL 요청 및 HTTP 응답 상태 확인
    const response = await fetch(SCHEMA_URL);
    if (!response.ok) {
      throw new Error(`HTTP 에러 발생! 상태 코드: ${response.status}`);
    }
    spec = (await response.json()) as SwaggerSpec;
  } catch {
    console.error(`❌ Swagger 스키마를 불러오는데 실패했습니다. (${SCHEMA_URL})`);
    console.error(`👉 백엔드 서버(Spring)가 켜져 있는지 확인해주세요.`);
    process.exit(1);
  }

  const fileRows: Record<string, string[]> = {};
  const processedKeys: Record<string, Set<string>> = {};

  // 파일별 헤더 및 표 타이틀 초기화
  Object.keys(FILE_MAPPING).forEach((fileName) => {
    fileRows[fileName] = [];
    processedKeys[fileName] = new Set();
  });

  // OpenAPI Paths 파싱
  Object.entries(spec.paths || {}).forEach(([urlPath, methods]) => {
    Object.entries(methods).forEach(([method, operation]) => {
      const tags = operation.tags || [];
      const summary = operation.summary || '설명 없음';
      const upperMethod = method.toUpperCase();

      const hookName = resolveHookName(method, urlPath, operation.operationId);
      const reqType = getRequestType(operation);
      const resType = getResponseType(operation);

      const endpointKey = `${upperMethod}:${urlPath}`;

      // Mapping 규칙 조건 체크
      Object.entries(FILE_MAPPING).forEach(([fileName, config]) => {
        const isMatched = tags.some((tag) => config.match(urlPath, tag)) || config.match(urlPath, '');

        if (isMatched && !processedKeys[fileName].has(endpointKey)) {
          processedKeys[fileName].add(endpointKey);

          const row = `| ${summary} | \`${upperMethod}\` | \`${urlPath}\` | ${hookName} | \`${reqType}\` | \`${resType}\` |`;
          fileRows[fileName].push(row);
        }
      });
    });
  });

  // 파일 쓰기
  Object.entries(FILE_MAPPING).forEach(([fileName, config]) => {
    const filePath = path.join(OUTPUT_DIR, fileName);

    const lines = [
      `# ${config.title}\n`,
      `> 자동 생성된 API 문서입니다. (생성일시: ${new Date().toLocaleString('ko-KR')})\n`,
      `| 기능 | Method | Endpoint | RTK Query Hook | Request Type | Response Type |`,
      `| :--- | :---: | :--- | :--- | :--- | :--- |`,
      ...(fileRows[fileName] || []),
      '\n',
    ];

    fs.writeFileSync(filePath, lines.join('\n'), 'utf-8');
    console.log(`✅ 문서 생성 완료: ${filePath}`);
  });
};

generateMarkdownDocs().catch(console.error);
