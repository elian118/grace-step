# GEMINI.md - GraceStep 백엔드 프로젝트 컨텍스트 및 작성 규칙

## 1. 프로젝트 개요 (Project Overview)
- **프로젝트명:** GraceStep Backend
- **목적:** 초등 5학년 ~ 중학교 3학년 영문법 수업 관리 및 AI 학습 보조 시스템
- **기술 스택:**
  - **언어:** Java 21
  - **프레임워크:** Spring Boot 3.4.x
  - **ORM / 데이터베이스:** Spring Data JPA, PostgreSQL (public 스키마)
  - **API 문서화:** Springdoc OpenAPI v3 (Swagger)
  - **환경 변수 관리:** Dotenv (`.env` 파일 이용)

---

## 2. 아키텍처 의도 및 패키지 레이어링 (Architectural Intent)

본 프로젝트는 **도메인 주도 패키지 구조(Domain-Driven Package Structure)**를 따르며, 각 패키지의 명확한 역할 분담을 지향합니다. 새로운 기능이나 클래스를 작성할 때는 아래 위치 규칙을 엄격히 준수합니다.

```text
com.postelian.backend
├── config              # [전역 설정 Layer]
│                       # - Spring, Security, Swagger, JPA 등의 Bean 설정
│                       # - 애플리케이션 전반에 적용되는 서드파티 및 프레임워크 설정 위치
│
├── global              # [전역 공통 Layer]
│   ├── common          # - 여러 도메인에서 공통으로 상속/사용하는 Base Entity (BaseEntity), 공통 Response DTO
│   └── error           # - 전역 예외 처리기(GlobalExceptionHandler) 및 Custom Business Exception
│
└── domain              # [비즈니스 도메인 Layer]
    ├── user            # - 사용자 관리, 회원가입/인증/권한 영역
    ├── student         # - 학생 프로필, 수강 관리, 출석 기록 영역
    └── ai              # - LLM(Gemini) 연동, 프롬프트 엔지니어링 및 AI 서비스 영역

```

---

## 3. Gemini 코드 작성 규칙 (Code Generation Rules)

새로운 기능을 추가하거나 코드를 생성할 때 아래 의도에 맞추어 적절한 위치에 코드를 작성합니다.

### **1) 도메인 영역 (`domain/{domain_name}`) 작성 규칙**

* **`entity` 패키지:**
* DB 테이블과 1:1 매핑되는 JPA 엔티티 위치.
* 모든 엔티티는 `global.common.BaseEntity`를 상속받아야 함.
* `BaseEntity`는 등록자/수정자/삭제자(ID)와 등록/수정/삭제 일시, 그리고 삭제 여부(`isDeleted`)를 관리함.
* 소프트 딜리트(Soft Delete)를 위해 클래스 상단에 `@SQLRestriction("is_deleted = false")` 적용 필수.
* 엔티티 생성자는 `protected`로 제한하며, 생성자 상단에 `@Builder` 적용.
* 필드 변경은 Setter 대신 명시적인 도메인 메서드(예: `updateProfile()`)로 처리하며, 이때 수정자 정보를 반드시 함께 전달받아 `recordModification()`을 호출해야 함.
* 삭제 시에도 실제 DB 행을 삭제하지 않고 `delete(userId)` 메서드를 호출하여 상태를 변경함.


* **`repository` 패키지:**
* `JpaRepository` 인터페이스 위치.


* **`dto` 패키지:**
* Controller $\leftrightarrow$ Service 간 데이터 전달 객체.
* Request/Response DTO는 Entity와 철저히 분리.


* **`service` 패키지:**
* 비즈니스 로직 처리. `@Transactional`을 명확히 명시.


* **`controller` 패키지:**
* REST API 엔드포인트 정의 및 Swagger 어노테이션 적용.



### **2) AI 서비스 영역 (`domain/ai`) 작성 규칙**

* Gemini 연동 시 프롬프트 정의, LLM API 호출, 응답 DTO 파싱 로직을 명확히 분리하여 구현.
* API Key나 모델 설정 값은 코드에 하드코딩하지 않고 `.env` 및 `application.properties`의 `${GEMINI_API_KEY}` 구조를 참조.

### **3) 전역 처리 영역 (`global`) 작성 규칙**

* 비즈니스 예외 발생 시 직접 HTTP Status를 반환하지 않고, `global.error` 패키지의 공통 Exception 및 Custom ErrorCode를 정의하여 `GlobalExceptionHandler`에서 일관되게 처리.

---

## 4. 코딩 스타일 기본 가이드

* **Lombok:** `@Getter`, `@Builder`, `@NoArgsConstructor(access = AccessLevel.PROTECTED)` 위주로 활용.
* **JPA 관계:** `@ManyToOne`, `@OneToOne` 연관관계는 N+1 문제 방지를 위해 반드시 `FetchType.LAZY`로 지정.
* **Enum 매핑:** Enum 필드는 유지보수성을 위해 반드시 `@Enumerated(EnumType.STRING)` 사용.
