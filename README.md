# 👣 GRACE-Step

> **GRACE-Step**은 Spring Boot 기반의 AI 백엔드와 React 19 기반의 프론트엔드로 구성된 모노레포(Monorepo) 애플리케이션 서비스입니다.

---

## 프로젝트 소개 (Overview)

**GRACE-Step**은 사용자 경험과 시스템 확장성을 극대화하기 위해 백엔드와 프론트엔드가 분리된 모노레포 구조를 채택하고 있습니다. 

- **Backend**: Java 26과 Spring Boot를 기반으로 구축되었으며, **LangChain4j (Google Gemini)**를 활용한 AI 어시스턴스 기능과 PostgreSQL 데이터 연동, OpenPDF 리포트 생성 및 OpenAPI 문서화를 제공합니다.
- **Frontend**: React 19, TypeScript, Vite, Redux Toolkit을 기반으로 구축되었으며, DaisyUI 및 AG Grid를 활용한 고성능 UI 인터페이스를 제공합니다.

---

## 기술 스택 (Tech Stack)

### 🔹 Backend
- **Language**: Java 26
- **Framework**: Spring Boot 4.1.1 (Spring Data JPA, Spring WebMVC)
- **AI Integration**: LangChain4j (`langchain4j-google-ai-gemini`)
- **Database**: PostgreSQL (JPA / P6Spy)
- **Documentation & Tools**: Springdoc OpenAPI UI, OpenPDF, Lombok

### 🔹 Frontend
- **Core**: React 19, TypeScript 6, Vite 8
- **State Management**: Redux Toolkit, React-Redux (RTK Query OpenAPI Codegen)
- **UI & Styling**: Tailwind CSS v4, DaisyUI v5, Phosphor Icons
- **Data Grid**: AG Grid (Community & React)
- **Routing & HTTP**: React Router v7, Axios

---

## 프로젝트 구조 (Monorepo Structure)

```text
grace-step/
├── backend/            # Java/Spring Boot 기반 백엔드 애플리케이션
│   ├── build.gradle    # Gradle 빌드 및 의존성 설정
│   ├── GEMINI.md       # 백엔드 세부 가이드
│   └── ...
├── frontend/           # React/Vite 기반 프론트엔드 애플리케이션
│   ├── package.json    # pnpm/npm 의존성 설정
│   ├── GEMINI.md       # 프론트엔드 세부 가이드
│   └── ...
└── README.md           # 루트 안내 문서

```

---

## 🚀 시작하기 (Quick Start)

### 1. Repository Clone

```bash
git clone https://github.com/elian118/grace-step.git
cd grace-step

```

### 2. Backend 실행 (Java 26 / Spring Boot)

> **사전 요구사항**: Java 26 SDK 설치 및 PostgreSQL DB 실행 필요
> (필요 시 `.env` 또는 `application.yml`에 Google Gemini API Key 및 DB 접속 정보를 설정하세요)

```bash
cd backend

# Gradle 래퍼를 사용한 앱 실행
./gradlew bootRun

```

### 3. Frontend 실행 (React 19 / Vite)

> **사전 요구사항**: Node.js 및 pnpm 설치 권장

```bash
cd frontend

# 의존성 패키지 설치
pnpm install

# OpenAPI 기반 RTK Query 코드 생성 (선택 사항)
pnpm codegen

# 개발 서버 실행
pnpm dev

```

---

## 상세 가이드 (Documentation)

각 애플리케이션의 세부 아키텍처, 디렉터리 구조 및 컨벤션은 하위 문서에서 확인할 수 있습니다.

* **Backend Architecture & Guide**: [`backend/GEMINI.md`](https://github.com/elian118/grace-step/blob/main/backend/GEMINI.md)
* **Frontend Architecture & Guide**: [`frontend/GEMINI.md`](https://github.com/elian118/grace-step/blob/main/frontend/GEMINI.md)

---

## License

This project is licensed under the MIT License.