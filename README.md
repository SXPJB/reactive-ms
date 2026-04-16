# reactive-ms

A reactive microservice built with Kotlin and Spring Boot, utilizing Spring WebFlux and Reactive MongoDB.

## Tech Stack

- **Language:** Kotlin 2.2.21
- **Framework:** Spring Boot 4.0.5
- **Reactive Stack:** Spring WebFlux, Project Reactor, Kotlin Coroutines
- **Database:** MongoDB (Reactive Driver)
- **Build Tool:** Gradle (Kotlin DSL)
- **Java Version:** 17

## Prerequisites

- Java 17 or higher
- Docker (for MongoDB)

## Getting Started

### 1. Run MongoDB

The project includes a `docker-compose.yml` file to quickly spin up a MongoDB instance.

```bash
docker-compose up -d
```

### 2. Build the application

Use the Gradle wrapper to build the project:

```bash
./gradlew build
```

### 3. Run the application

```bash
./gradlew bootRun
```

The application will be available at `http://localhost:8080`.

## API Endpoints

### Health Check
- `GET /healthcheck` - Check application status.

### App Configuration
- `POST /api/app-configuration` - Save a new configuration.
- `PUT /api/app-configuration/{id}` - Update an existing configuration.
- `GET /api/app-configuration/all` - List all configurations.
- `GET /api/app-configuration/{key}/{section}` - Get configuration by key and section.
- `DELETE /api/app-configuration/{id}` - Delete a configuration.

## Development

### Running Tests

```bash
./gradlew test
```
