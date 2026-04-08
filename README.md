# JournalApp (Spring Boot Learning Project)

This project is a personal learning build created while following a YouTube Spring Boot playlist.
It has grown into a backend API with JWT auth, MongoDB persistence, Redis caching, Swagger docs, email support, scheduled jobs, and Google OAuth callback handling.

## What this project does

- User sign-up and login with JWT-based authentication
- Role-based access (`USER`, `ADMIN`) with Spring Security
- Journal CRUD for authenticated users
- Admin APIs to create admin users and list all users
- Google OAuth callback flow that provisions users on first login
- Weather endpoint with Redis caching
- Scheduled jobs for sentiment-analysis email workflow and config cache refresh
- OpenAPI/Swagger integration for API exploration

## Tech stack

- Java 17 - Core programming language used to build the backend application.
- Spring Boot 3.5.13 - Application framework used to bootstrap and run the service with auto-configuration.
- Spring Web - Web layer used to expose REST APIs and handle HTTP requests/responses.
- Spring Security - Security framework used for authentication, authorization, and endpoint protection.
- Spring Data MongoDB - Data access layer used to persist and query application data in MongoDB.
- Spring Data Redis - Integration layer used to read/write cached data in Redis.
- Spring Mail - Email utility used to send scheduled and transactional emails.
- JJWT (`io.jsonwebtoken`) - JWT library used to generate and validate authentication tokens.
- springdoc-openapi (Swagger UI) - API documentation tooling used to generate OpenAPI docs and interactive API testing UI.
- Maven - Build and dependency management tool used to compile, test, and package the project.
- Lombok - Boilerplate reduction library used to generate getters/setters/builders and related code.

## Architecture overview

The codebase follows a layered style:

- `controller` - HTTP APIs (`/public`, `/user`, `/journal`, `/admin`, `/auth/google`)
- `service` - business logic (users, journals, email, weather, redis)
- `repository` - Mongo repositories + custom query repository
- `entity` - MongoDB domain models (`User`, `Journal`, config docs)
- `filter` - JWT request filter
- `config` - security, swagger, redis template beans
- `scheduler` - periodic jobs
- `cache` - DB-backed app cache (`config_journal_app` collection)

Main app class: `src/main/java/com/sushant/journalapp/JournalApplication.java`

## Authentication and authorization

Security rules in `src/main/java/com/sushant/journalapp/config/SpringSecurity.java`:

- `/journal/**` and `/user/**` -> authenticated
- `/admin/**` -> requires `ROLE_ADMIN`
- all other endpoints -> public

JWT filter: `src/main/java/com/sushant/journalapp/filter/JwtFilter.java`

- Expects `Authorization: Bearer <token>`
- Extracts username from JWT and sets Spring Security context

JWT helper: `src/main/java/com/sushant/journalapp/utils/JwtUtil.java`

- Creates token with a 5-minute expiration
- Validates expiration only

## API summary

### Public APIs

Base: `/public`

- `GET /public/health-check`
- `POST /public/signUp`
- `POST /public/login`

### User APIs (JWT required)

Base: `/user`

- `PUT /user/updateUser`
- `DELETE /user/deleteUser`
- `GET /user/getWeather`

### Journal APIs (JWT required)

Base: `/journal`

- `GET /journal/getJournalEntries/{userName}`
- `POST /journal/addJournalEntry/{userName}`
- `GET /journal/getJournalEntryById/{id}`
- `PUT /journal/updateJournalEntryById/{id}`
- `DELETE /journal/deleteJournalEntryById/{id}`

### Admin APIs (ADMIN role required)

Base: `/admin`

- `GET /admin/getAllUsers`
- `POST /admin/createAdmin`

### Google auth callback

Base: `/auth/google`

- `GET /auth/google/callback?code=...`

### Legacy in-memory journal API (no DB)

Base: `/_journal`

- `GET /_journal/getJournalEntries`
- `POST /_journal/addJournalEntry`
- `GET /_journal/getJournalEntryById/{id}`
- `PUT /_journal/updateJournalEntryById/{id}`
- `DELETE /_journal/deleteJournalEntryById/{id}`

## Swagger/OpenAPI

Swagger config: `src/main/java/com/sushant/journalapp/config/SwaggerConfig.java`

After starting the app, open:

- `http://localhost:8081/swagger-ui/index.html`
- OpenAPI JSON (default): `http://localhost:8081/v3/api-docs`

## Data model

- `User` (`users` collection)
  - `userName` (unique), `password`, `email`, `roles`, `sentimentAnalysis`, `journals`
- `Journal` (`journal_entries` collection)
  - `title`, `content`, `date`, `sentiment`
- `ConfigJournalAppEntity` (`config_journal_app` collection)
  - `key`, `value`

## Configuration and profiles

Files:

- `src/main/resources/application.yml`
- `src/main/resources/application-dev.yml`
- `src/main/resources/application-prod.yml`

Current defaults in repo:

- Active profile: `dev`
- Server port: `8081`
- MongoDB URI from profile yml
- Redis host/port from `application.yml`
- SMTP settings from `application.yml`
- Weather API key from `application.yml`

### App cache requirement

`WeatherService` expects a `config_journal_app` entry with key `weather-api`, where value contains placeholders:

`<CITY>` and `<API_KEY>`

Example pattern:

`http://api.weatherstack.com/current?access_key=<API_KEY>&query=<CITY>`

## Scheduled jobs

Defined in `src/main/java/com/sushant/journalapp/scheduler/UserScheduler.java`:

- Weekly (Sunday 9:00 AM IST): fetch users marked for sentiment analysis and send email
- Every 10 minutes: refresh app cache from `config_journal_app`

## Running locally

### Prerequisites

- JDK 17+
- Maven 3.9+ (or use `./mvnw`)
- MongoDB (or Atlas URI)
- Redis instance

### Start the app

```bash
cd /Users/sushanthande/Downloads/journalapp
./mvnw spring-boot:run
```

Or build then run:

```bash
cd /Users/sushanthande/Downloads/journalapp
./mvnw clean package -DskipTests
java -jar target/journalapp-0.0.1-SNAPSHOT.jar
```

## Testing

Run tests:

```bash
cd /Users/sushanthande/Downloads/journalapp
./mvnw test
```

Notes:

- Some tests are integration-style and depend on configured services (Mongo/Redis/Mail)
- Test coverage report artifacts are present under `htmlReport/`

## CI

GitHub Actions workflow: `.github/workflows/build.yml`

- Runs on pushes to `master` and pull requests
- Uses JDK 17
- Runs Maven verify + Sonar scan (`-DskipTests` currently)

## Known limitations and improvement ideas

- Secrets are committed in config files and controller code; they should be rotated and moved to environment variables/secrets manager
- `GoogleAuthController` contains hardcoded OAuth client details and redirect URI
- `JwtUtil` uses a hardcoded secret and short token TTL (5 minutes), and token validation checks only expiration
- Legacy `/_journal` endpoints are public and in-memory (not persistent)
- Some logging/debug messages and error handling can be improved for production readiness
- Several tests currently print values and rely on external infrastructure

## Security note (important)

Before any production or public deployment:

1. Rotate all exposed credentials (MongoDB, mail app password, weather key, OAuth secrets, JWT secret).
2. Externalize secrets via environment variables or a secret manager.
3. Update code to read secrets from config properties, not hardcoded literals.
4. Enable stricter validation/authorization checks and improve exception handling.

## Learning context

This repository is intentionally hands-on and iterative because it was built while learning Spring Boot concepts through a YouTube playlist.
Playlist followed while learning Spring Boot: [Spring Boot Learning Playlist](https://www.youtube.com/watch?v=1993zSY5UBI&list=PLA3GkZPtsafacdBLdd3p1DyRd5FGfr3Ue)
Refactors and hardening are expected as the project evolves.
