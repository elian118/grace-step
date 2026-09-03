/// <reference types="node" />

import fs from 'fs';
import path from 'path';
import { fileURLToPath } from 'url';

// ESM 환경에서 __dirname 대체 구문
const __filename = fileURLToPath(import.meta.url);
const __dirname = path.dirname(__filename);

// 스웨거 JSON 규격 간단 인터페이스 정의
interface SwaggerSpec {
  paths: Record<string, Record<string, { summary?: string; tags?: string[]; description?: string }>>;
}

const OUTPUT_DIR = path.resolve(__dirname, '../src/api/generated');
const SCHEMA_URL = 'http://localhost:8080/api-docs/00.%20TOTAL-API';

// outputFiles 매핑과 동일하게 맞춤
const FILE_MAPPING: Record<string, { title: string; match: (path: string, tag: string) => boolean }> = {
  'userApi.md': {
    title: 'User API 명세',
    match: (url, tag) => tag === 'User API' || url.startsWith('/api/v1/users'),
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
  } catch (error) {
    console.error(`❌ Swagger 스키마를 불러오는데 실패했습니다. (${SCHEMA_URL})`);
    console.error(`👉 백엔드 서버(Spring)가 켜져 있는지 확인해주세요.`);
    process.exit(1); // 오류 상태 종료
  }

  const fileContents: Record<string, string[]> = {};

  // 파일별 헤더 초기화
  Object.keys(FILE_MAPPING).forEach((fileName) => {
    fileContents[fileName] = [
      `# ${FILE_MAPPING[fileName].title}\n`,
      `> 자동 생성된 API 문서입니다. (생성일시: ${new Date().toLocaleString()})\n`,
    ];
  });

  // OpenAPI Paths 파싱
  Object.entries(spec.paths || {}).forEach(([urlPath, methods]) => {
    Object.entries(methods).forEach(([method, operation]) => {
      const tags = operation.tags || [];
      const summary = operation.summary || '설명 없음';
      const upperMethod = method.toUpperCase();

      // Mapping 규칙 조건 체크
      Object.entries(FILE_MAPPING).forEach(([fileName, config]) => {
        const isMatched = tags.some((tag) => config.match(urlPath, tag)) || config.match(urlPath, '');
        if (isMatched) {
          fileContents[fileName].push(
            `### ${summary}\n- **Method**: \`${upperMethod}\`\n- **Endpoint**: \`${urlPath}\`\n`,
          );
        }
      });
    });
  });

  // 파일 쓰기
  Object.entries(fileContents).forEach(([fileName, contentLines]) => {
    const filePath = path.join(OUTPUT_DIR, fileName);

    // 🛡️ [안전장치 3] 파일 개별 저장 전 상위 폴더 체크
    const dir = path.dirname(filePath);
    if (!fs.existsSync(dir)) {
      fs.mkdirSync(dir, { recursive: true });
    }

    fs.writeFileSync(filePath, contentLines.join('\n'), 'utf-8');
    console.log(`✅ 생성 완료: ${filePath}`);
  });
};

generateMarkdownDocs().catch(console.error);
