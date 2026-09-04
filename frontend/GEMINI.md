# Project Overview & Gemini CLI Guidelines

이 프로젝트(`grace-step`)는 React 19, TypeScript 6, Redux Toolkit(RTK Query), TailwindCSS v4, DaisyUI v5를 기반으로 구축된 프론트엔드 애플리케이션입니다.

---

## 1. Gemini CLI 필수 준수 규칙

- **언어 표준**: 모든 응답, 코드 설명 및 문서화는 **한국어**를 기본으로 사용합니다.
- **실행 및 테스트 금지**: 코드 작성, 수정, 구조 제안만 수행하며 명령어를 통한 직접적인 코드 실행이나 테스트 동작을 수행하지 않습니다.
- **과도한 아이콘 사용 금지**: 직관적인 가독성을 해치는 불필요하거나 과도한 이모지 및 아이콘 사용을 지양합니다.
- **계층 구조 우선순위**: 동일한 역할의 폴더 구조라도 상위 Depth에 위치할수록 프로젝트 전역(Global) 범위를 의미하며, `features/*` 하위에 위치할수록 해당 도메인 전용(Feature-scoped) 범위를 의미합니다.

---

## 2. 기술 스택 (Tech Stack)

### Core & Framework

- **Library**: React v19
- **Language**: TypeScript v6
- **Build Tool**: Vite v8
- **Routing**: React Router v7 (`createBrowserRouter` 사용)

### State Management & Data Fetching

- **Global State & Query**: Redux Toolkit v2, React Redux v9
- **HTTP Client**: Axios v1 (RTK Query 및 커스텀 API 훅에 활용)
- **Code Generation**: `@rtk-query/codegen-openapi` (OpenAPI Spec 기반 RTK Query 코드 자동 생성)

### UI & Styling

- **CSS Framework**: TailwindCSS v4 (`@tailwindcss/vite` 플러그인 연동)
- **UI Component Library**: DaisyUI v5
- **Icons**: Phosphor Icons (`@phosphor-icons/react`)
- **Data Grid**: AG-Grid (`ag-grid-community`, `ag-grid-react` v36)

### Developer Tools & Automation

- **Execution**: `tsx` (TypeScript 스크립트 실행)
- **Linting & Formatting**: ESLint v10, Prettier v3
- **Package Manager**: `pnpm`

---

## 3. 코드 생성 및 자동화 전략 (Codegen Strategy)

프로젝트는 OpenAPI 스펙을 기반으로 RTK Query 코드를 자동 생성하는 워크플로우를 갖추고 있습니다.

- **`pnpm codegen`**: 다음 3가지 빌드 전처리 단계를 순차적으로 실행합니다.
  1. `codegen:prep` (`tsx scripts/ensure-dir.ts`): 생성될 파일들의 디렉토리 존재 여부 확인 및 생성
  2. `codegen:api` (`rtk-query-codegen-openapi openapi-config.cjs`): OpenAPI 정의서를 기반으로 `src/api/generated/` 위치에 DTO 및 RTK Query API 훅 자동 생성
  3. `codegen:docs` (`tsx scripts/generate-docs.ts`): API 관련 코드 문서화 자동 생성

> **개발 수칙**: `src/api/generated/` 내 자동 생성된 코드 파일은 직접 수정하지 않으며, 추가적인 API 핸들링이나 `try-catch` 및 토스트 제어 로직은 `src/features/[feature-name]/api/` 하위의 커스텀 Api 훅(예: `useExamApi`)에서 래핑하여 작성합니다.

---

## 4. 프로젝트 폴더 구조 및 역할

```text
frontend/
├── .idea/                      # IDE(WebStorm/PyCharm) 설정 파일
├── public/                     # 정적 자원
├── scripts/                    # 빌드 및 코드 생성 관련 스크립트 (ensure-dir, generate-docs)
└── src/                        # 소스 코드 루트
    ├── api/                    # 전역 API/RTK Query 커스텀 훅 및 설정
    │   └── generated/          # RTK Query Codegen으로 생성된 자동 코드
    ├── assets/                 # 이미지, 폰트 등 정적 에셋
    ├── components/             # 전역 공통 컴포넌트
    │   ├── common/             # 공통 UI 요소 (dialog, grid, toast 등)
    │   ├── error/              # 에러 바운더리 및 에러 페이지 컴포넌트
    │   └── layout/             # 전체 앱 레이아웃 및 뷰
    ├── constants/              # 전역 상수 (열거형, 공통 그리드 설정 등)
    ├── features/               # 기능/도메인별 모듈 (Feature-driven)
    │   ├── attendance/         # 출석 관리 기능
    │   ├── classroom/          # 강의실 관리 기능
    │   ├── exam/               # 시험 관리 기능
    │   ├── home/               # 메인 대시보드 기능
    │   ├── student/            # 학생 관리 기능
    │   └── user/               # 사용자/계정 관리 기능
    ├── hooks/                  # 전역 커스텀 훅
    ├── router/                 # React Router v7 라우팅 설정
    ├── styles/                 # 전역 스타일 및 TailwindCSS v4 설정
    ├── types/                  # 전역 타입 정의
    └── utils/                  # 전역 유틸리티 함수

```

---

## 5. Feature 모듈 내 폴더별 상세 역할 및 작성 규칙

각 feature 모듈(`src/features/[feature-name]/`) 내부의 구조는 아래 표준 규격을 준수합니다.

### `api/`

- **역할**: RTK Query의 Lazy 훅, Mutation 훅을 가져와 `try-catch` 패턴으로 래핑한 특수 목적 훅 위치.
- **명명 규칙**: 파일 및 훅 명칭 끝에 반드시 `Api` 접미사를 붙입니다. (예: `useExamApi()`, `useAttendanceApi()`)
- **동작 규칙**:
- 주요 엔드포인트 호출을 관리합니다.
- 데이터 저장/수정 성공 시 성공 메시지 토스트를 호출합니다.
- `catch` 블록 내에서 에러 핸들링 및 에러 토스트를 처리합니다.

### `components/`

- **역할**: UI 뷰를 담당하는 컨테이너 컴포넌트 위치. 페이지별 루트 컴포넌트의 자식 위치입니다.
- **하위 디렉토리**:
- `views/`: 컨테이너를 구성하는 하위 조각 컴포넌트 집합.
- `dialogs/`: 다이얼로그 내부 콘텐츠를 구성하는 컴포넌트 집합.

- **참고**: 다이얼로그 시스템은 전역 Redux 상태로 관리되며, 각 내용 컴포넌트는 역할에 따라 도메인/페이지별로 구성합니다.

### `hooks/`

- **역할**: `components/`의 컨테이너 컴포넌트와 1:1 또는 1:N으로 대응되는 비즈니스 로직 및 상태 관리 훅 집합.

### `contexts/`

- **역할**: 페이지별 루트 컴포넌트 또는 특정 기능 범위를 감싸는 React Context 코드 위치.

### `styles/`

- **역할**: 페이지 및 컴포넌트 전용 CSS Module 위치.
- **규칙**: TailwindCSS 유틸리티 클래스들을 재조합(`@apply` 등)하여 선언한 클래스들을 작성하고 해당 페이지 내에서 가져와 사용합니다.

### `constants/`

- **역할**: 모듈 내에서 사용되는 상수 및 상수형 데이터 모음.
- **포함 항목**: 열거형(Enum) 상수, AG-Grid/테이블 구성을 위한 컬럼 정의 배열 상수 등.

### `types/`

- **역할**: RTK Query Codegen으로 생성된 DTO 외에 개발자가 필요에 따라 추가로 선언한 TypeScript 인터페이스 및 타입 정의.

### `utils/`

- **역할**: 해당 모듈 내에서 반복적으로 사용되는 순수 유틸리티 함수 위치.
