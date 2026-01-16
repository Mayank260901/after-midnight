# After Midnight Backend

Backend for the After Midnight project, a platform for sharing poems, songs, and thoughts.

## Tech Stack

- **Language:** Java 17
- **Framework:** Spring Boot 3.2.1
- **Build Tool:** Maven
- **Database:** MySQL (Production/Dev), H2 (Testing)
- **Migrations:** Flyway
- **Security:** Spring Security with JWT
- **Documentation:** OpenAPI 3.0 (Swagger UI)
- **Hibernate:** `ddl-auto=validate` (Schema changes prevented; Fail fast on mismatch)
- **Database Safety:** Silent database creation disabled; Flyway validation enforced

## Project Structure

```text
src/main/java/com/aftermidnight/
├── config/         # Configuration classes (Security, OpenAPI, etc.)
├── controller/     # REST Controllers (API endpoints)
├── dto/            # Data Transfer Objects (Request/Response contracts)
├── entity/         # JPA Entities
├── exception/      # Custom exceptions and Global Exception Handler
├── repository/     # Spring Data JPA Repositories
├── security/       # JWT and Security implementation
├── service/        # Business logic interfaces
│   └── impl/       # Service implementations
└── util/           # Utility classes
```

## Getting Started

### Requirements

- Java 17
- Maven 3.x
- MySQL (optional, defaults to H2 if not configured)

### Setup

1. Clone the repository.
2. Configure your database in `src/main/resources/application.properties` (or use the default H2 configuration).
3. Set environment variables if necessary (see [Environment Variables](#environment-variables)).

### Run

Using Maven:
```bash
./mvnw spring-boot:run
```

Or using the jar:
```bash
./mvnw clean package
java -jar target/after-midnight-backend-0.0.1-SNAPSHOT.jar
```

## API Contracts

All API endpoints are prefixed with `/api/v1`. The API contracts are **FROZEN**.

### API Freeze Policy

The Backend API for version 1 is officially **LOCKED**.
- **No breaking changes** are allowed in `v1`.
- Any modification that breaks existing client integrations will require a major version bump to `v2`.
- All Request and Response DTOs are final for `v1`.

### Fail-Fast Database Policy

The application is configured to fail explicitly during startup under the following conditions:
- **Missing Database:** If the database specified in the JDBC URL does not exist, the application will not attempt to create it and will fail to start.
- **Migration Failure:** Any error during Flyway migrations will stop the startup process.
- **Schema Mismatch:** Hibernate is set to `validate`. If the database schema does not exactly match the JPA entities, the application will fail with a `SchemaManagementException`.

### Endpoints

- **Auth:** `/api/v1/auth/*` (Register, Login)
- **Poems:** `/api/v1/poems/*` (GET is public)
- **Songs:** `/api/v1/songs/*` (GET is public)
- **Thoughts:** `/api/v1/thoughts/*` (GET is public)
- **Health:** `/api/v1/health` (Public)
- **Public:** `/api/public/*` (Public)

Documentation is available at: `http://localhost:8080/swagger-ui.html` (Available only in `dev` profile; Requires authentication).

## Scripts

- `./mvnw clean` - Clean build artifacts.
- `./mvnw test` - Run unit and integration tests.
- `./mvnw package` - Package the application into a JAR.

## Environment Variables

- `JWT_SECRET` - Secret key for JWT signing (Required for production).
- `DB_URL` - Database connection URL (Default: `jdbc:mysql://localhost:3306/after_midnight` in dev).
- `DB_USERNAME` - Database username (Default: `root` in dev).
- `DB_PASSWORD` - Database password (Default: `root` in dev).

## Tests

Tests are located in `src/test/java`.
Run all tests with:
```bash
./mvnw test
```

## License

TODO: Add license information.
