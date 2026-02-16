# DGCHub 백엔드 - 관리자 모드 실행 가이드

## 포트 분리 아키텍처

DGCHub는 일반 유저와 관리자의 완전한 독립성을 위해 **두 개의 포트**로 실행됩니다:

- **포트 8080**: 일반 유저 API (OAuth 로그인)
- **포트 8081**: 관리자 API (ID/PW 로그인)

이를 통해 쿠키 충돌 없이 두 시스템이 **완전히 독립적**으로 작동합니다.

## 실행 방법

### 1. IntelliJ IDEA

#### 일반 유저 API 서버 (포트 8080)
1. Run Configuration 생성
   - Main class: `com.joo.digimon.DigimonApplication`
   - Active profiles: `dev`
   - VM options: (없음)

#### 관리자 API 서버 (포트 8081)
1. Run Configuration 생성
   - Main class: `com.joo.digimon.DigimonApplication`
   - Active profiles: `admin`
   - VM options: (없음)

### 2. 명령줄 (Gradle)

```bash
# 일반 유저 API (포트 8080)
./gradlew bootRun --args='--spring.profiles.active=dev'

# 관리자 API (포트 8081) - 별도 터미널에서 실행
./gradlew bootRun --args='--spring.profiles.active=admin'
```

### 3. JAR 파일 실행

```bash
# 빌드
./gradlew build

# 일반 유저 API (포트 8080)
java -jar build/libs/digimon-0.0.1-SNAPSHOT.jar --spring.profiles.active=dev

# 관리자 API (포트 8081) - 별도 터미널에서 실행
java -jar build/libs/digimon-0.0.1-SNAPSHOT.jar --spring.profiles.active=admin
```

## 프론트엔드 연결

- **dgchub-fe-react** (일반 유저 앱) → `http://localhost:8080`
- **DGCHub-ADMIN** (관리자 앱) → `http://localhost:8081`

프론트엔드 `.env` 파일에서 `VITE_API_URL`이 올바른 포트로 설정되어 있는지 확인하세요.

## 쿠키 분리 메커니즘

| 구분 | 포트 | 쿠키 이름 | 특징 |
|------|------|-----------|------|
| 일반 유저 | 8080 | `JWT_TOKEN` | OAuth 로그인 (카카오) |
| 관리자 | 8081 | `ADMIN_JWT_TOKEN` | ID/PW 로그인 |

쿠키는 **포트별로 독립적**으로 저장되므로 충돌하지 않습니다.

## 로그아웃 API

- 일반 유저: `POST /api/account/logout` → `JWT_TOKEN` 삭제
- 관리자: `POST /api/account/logout/admin` → `ADMIN_JWT_TOKEN` 삭제

## 주의사항

- 개발 시 **두 서버를 동시에 실행**해야 합니다.
- 각 서버는 같은 데이터베이스를 공유하지만 **독립적인 세션**을 유지합니다.
- 포트 8080, 8081이 이미 사용 중이면 충돌이 발생할 수 있습니다.

## 프로덕션 배포

프로덕션 환경에서는:
- Nginx/프록시로 `/api/admin/**` 요청을 8081 포트로 라우팅
- 또는 별도 서버/컨테이너에서 관리자 API 실행
- CORS 설정에 프로덕션 도메인 추가 필요
