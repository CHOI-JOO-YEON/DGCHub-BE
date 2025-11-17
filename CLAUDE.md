# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

DGCHub Backend (Digimon Meta Site) - A Spring Boot 3.2.1 application for managing Digimon card game metadata, including card information, deck building, user collections, and web crawling for card data from various sources.

**Tech Stack:** Java 21, Spring Boot 3.2.1, Spring Security, JPA/Hibernate, MariaDB, QueryDSL, AWS S3 (MinIO for local dev), Lombok, JWT authentication

## Development Commands

### Building and Running

```bash
# Clean build
./gradlew clean build

# Run application (dev profile)
./gradlew bootRun

# Run tests
./gradlew test

# Run specific test class
./gradlew test --tests com.joo.digimon.crawling.service.CrawlingServiceImplTest

# Build Docker image
./gradlew bootJar
docker build -t digimon-meta-site .
```

### Local Development Setup

```bash
# Start local infrastructure (MinIO S3, MariaDB, WireMock for Kakao OAuth)
docker-compose up -d

# Application runs on port 50000 (default for dev profile)
# MinIO console: http://localhost:9001 (dummy-key-id / dummy-key-secret)
# WireMock (Kakao mock): http://localhost:8089
```

The dev profile (`application-dev.yml`) is configured for local development with:
- MariaDB at localhost:13306
- MinIO S3 at localhost:9000
- WireMock for Kakao OAuth mocking
- H2 console access at /h2-console

### Code Generation

QueryDSL Q-classes are generated during compilation. After adding/modifying JPA entities:

```bash
./gradlew clean compileJava
```

Generated Q-classes appear in `build/generated/sources/annotationProcessor/java/main/`

## Architecture Overview

### Modular Structure

The application follows a domain-driven modular structure under `com.joo.digimon`:

- **card**: Core card management (CRUD, search, types, notes, image handling)
- **crawling**: Web scraping for Korean/Japanese/English card data from external sites
- **deck**: Deck building, format management, TTS export functionality
- **collect**: User card collection tracking
- **limit**: Card ban/restriction list management by format
- **user**: User authentication, settings, OAuth2 (Kakao)
- **security**: JWT-based authentication with role hierarchy (USER → MANAGER → ADMIN)
- **global**: Shared enums (Color, Rarity, CardType, Attribute, Form, Locale), exceptions, validators
- **util**: S3 utilities, exception handlers, interceptors, converters

### Security Architecture

**Authentication Flow:**
1. OAuth2 login via Kakao (or username/password for admin)
2. JWT token issued on successful authentication, stored in HTTP-only cookie (`JWT_TOKEN`)
3. `JwtFilter` extracts token from cookie and validates on each request
4. Role hierarchy: ADMIN has MANAGER + USER roles, MANAGER has USER role (implemented via switch fallthrough in `JwtFilter:65-75`)

**Important:** The role hierarchy implementation uses intentional switch fallthrough without breaks. When modifying roles, maintain this pattern.

**Authorization:**
- `/api/crawling/**` and `/api/admin/**`: ADMIN only
- `/api/manager/**`: MANAGER+ (includes ADMIN)
- `/api/format/**` and `/api/limit/**`: GET=public, POST=ADMIN only
- `/api/deck/**`: GET=public, POST/PUT/DELETE=authenticated
- `/api/card/**`: Public read access

### Data Layer Patterns

**Repository Layer:**
- Standard JPA repositories extending `JpaRepository`
- QueryDSL for complex queries (see `CardImgRepository`, `DeckRepository`)
- Custom query methods following Spring Data JPA naming conventions
- Locale-specific tables: `EnglishCardEntity`, `JapaneseCardEntity` for translated data

**Entity Relationships:**
- `CardEntity` (main card) ← one-to-many → `CardImgEntity` (parallel/alternate art)
- `CardEntity` ← many-to-many → `TypeEntity` via `CardCombineTypeEntity`
- `DeckEntity` ← one-to-many → `DeckCardEntity` (deck composition)
- `User` ← many-to-many → `CardEntity` via `UserCard` (user collections)

### Crawling System

Multi-locale card data crawling with strategy pattern:

**Procedure Chain:**
1. `CrawlingProcedure` (KorCrawlingProcedure/EngCrawlingProcedure/JpnCrawlingProcedure) - Fetches HTML from source
2. `CardParseProcedure` - Parses HTML into `CrawlingCardDto`
3. `CardImgProcessor` - Downloads and processes card images
4. `SaveCardProcedure` - Persists to database (creates or updates `CardEntity`, locale entities, images)

Images are stored in S3 with two versions: original and small (resized). WebP format conversion is supported via Scrimage library.

### Image Processing

- **Storage:** AWS S3 (MinIO for local dev)
- **Paths:** Configured via `img.original` and `img.small` in application.yml
- **Processing:** Thumbnailator for resizing, Scrimage for WebP conversion
- **Utilities:** `S3Util` for upload/download, `ImageUtil` for transformations

### Feign Clients

OpenFeign is used for external API calls:
- `KakaoAuthClient` / `KakaoApiClient`: Kakao OAuth integration
- `ScalingClient`: Card image scaling service (external)

Configuration: `FeignClientConfig` sets up request/response logging and error handling.

## Key Configuration Notes

### Database

- **Dev:** MariaDB (docker-compose), Hibernate DDL auto-update enabled
- **Prod:** Should use managed MariaDB, `ddl-auto` should be `validate` or `none`
- **Dialect:** `org.hibernate.dialect.MariaDBDialect`

### File Upload

Max file size: 50MB (configured in `application-dev.yml`)

### CORS

Allowed origins are hardcoded in `SecurityConfig:94-98`. Update for new frontend domains.

### Custom Argument Resolvers

`@CurUser` annotation injects authenticated user ID into controller methods. Implemented by `UserArgumentResolver` registered in `WebConfig`.

## Common Development Patterns

### Adding a New Card Type or Enum

1. Add enum value to appropriate enum in `global.enums`
2. If used in entities, migration may be needed (Hibernate will auto-update in dev)
3. Update DTOs and converters using the enum
4. Add to `StringToEnumConverterFactory` if used as request parameter

### Adding a New API Endpoint

1. Create/update controller in appropriate module
2. Add DTO classes in module's `dto` package
3. Implement service interface and implementation
4. Add security rules in `SecurityConfig:50-65` if needed
5. Follow RESTful conventions: `/api/{module}/{resource}`

### Working with QueryDSL

After modifying entities:
```bash
./gradlew clean compileJava
```

Use Q-classes in repository custom implementations extending `QuerydslRepositorySupport` or via `JPAQueryFactory`.

### Testing Considerations

- Test infrastructure runs with H2 in-memory database
- Mock external Feign clients or use WireMock stubs
- S3Util tests may require MinIO running locally (see `S3UtilTest`)
