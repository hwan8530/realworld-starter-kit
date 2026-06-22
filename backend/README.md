## RealWorld Backend Challenge

RealWorld의 Backend API 구현

### 🛠️ Tech Stack

| Tech Stack                             | details                      |
|----------------------------------------|------------------------------|
| **BackendFramework**                   | Spring Boot 4.0.6            |
| **Build Tool**                         | Gradle (Kotlin DSL)          |
| **Persistence & QueryDatabase**        | H2 Database (파일/로컬 개발 환경 전용) |
| **ORM**                                | Spring Data JPA / Hibernate  |
| **Query Builder**                      | QueryDSL 5.0.0               |
| **Libraries & SecurityAuthentication** | JWT (JSON Web Token)         |
| **Object Mapping**                     | MapStructAPI                 |
| **Documentation**                      | Swagger (Springdoc Open-API) |

### 🏗️ Directory Structure

본 프로젝트는 응집도를 높이고 유지보수성을 극대화하기 위해 도메인(Entity) 중심의 패키지 구조로 설계되었습니다. 각 도메인은 독립적인 계층(Controller,
Service, Repository)을 하위에 포함합니다.

```
src/main/com/realworld/backend/
│
├── article/                   # Article 도메인
│   ├── controller/            
│   ├── dto/
│   ├── entity/
│   ├── repository/
│   ├── service/
├── common/                    # 공통 모듈
│   ├── config/                # 공통 설정
│   ├── docs/                  # API DOCS
│   ├── errorhandling/         # Global Exception Handler
│   └── security/      
│       ├── jwt/               # JWT 인증
│       └── spring/            # Spring Security Filter Chain
├── mapper/                    # Entity to DTO Mapper
├── User/                      # User 도메인
│   ├── controller/            
│   ├── dto/
│   ├── entity/
│   ├── repository/
│   └── service/
```

### 📌 Key Features & Architecture

도메인 기반 설계 (Domain-Driven Structure): 엔티티(Entity) 중심으로 패키지를 분리하여 관련된 비즈니스 로직과 API 계층을 한곳에 응집시켰습니다.

JWT 기반 보안 아키텍처: 토큰 기반의 무상태(Stateless) 회원 인증 및 인가 처리를 구현했습니다.

타입 안정적 쿼리 작성: QueryDSL을 활용하여 복잡한 동적 쿼리를 컴파일 시점에 검증하고 유지보수성을 극대화했습니다.

고성능 객체 매핑: MapStruct를 사용하여 컴파일 시점에 DTO-Entity 간 매핑 코드를 생성, 반복적인 보일러플레이트 코드를 제거했습니다.

독립적 DB 환경: 외부 공개 목적이 아닌 독립 프로젝트 목적에 맞추어, 별도의 DBMS 설치 없이 즉시 실행 가능한 내장 H2 데이터베이스를 채택했습니다.

### 📄 API Documentation

Swagger UI를 통해 전체 API 사양을 시각적으로 확인하고 직접 테스트할 수 있습니다.

- Swagger UI URL: http://localhost:8080/swagger-ui.html(※ 로컬 실행 후 위 주소로 접속하면 JWT 인증을 포함한 API 명세서를
  확인할 수 있습니다.)

### 🧪 Test Status

통합 테스트(Integration Test) 중심의 테스트 환경을 구축했습니다.

현재 일부 엔티티에 대한 검증이 완료된 상태입니다.

비즈니스 로직이 복잡한 일부 엔티티 및 도메인에 대해서는 순차적으로 테스트 코드를 추가해 나갈 예정입니다

### 💻 Getting Started

Prerequisites

Java 21+

### Installation & Runbash

```
# 저장소 복제
git clone https://github.com

# 프로젝트 디렉토리 이동
cd your-repo-name

# 애플리케이션 빌드 및 실행
./gradlew bootRun
```