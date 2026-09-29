# Sample Spring Boot API for AI Code Review

AI 코드 리뷰 도구의 성능 및 리뷰 피드백 시뮬레이션을 위한 샘플 Spring Boot 애플리케이션입니다.

## 기술 스택
- Java 25
- Spring Boot 4.1.x
- Spring Data JPA
- H2 In-memory Database
- Bean Validation
- JUnit 5 / AssertJ / Mockito

## 주요 기능
- 회원(User) 등록, 단건 조회, 목록 조회, 삭제 RESTful API (`/api/v1/users`)
- 유효성 검증 (Email 형식, 필드 길이 등)
- Service 및 Controller 단위/슬라이스 테스트

## 빌드 및 테스트
```bash
./gradlew test
./gradlew bootRun
```
