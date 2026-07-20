# 🚀 Task Manager API

A production-ready Task Management REST API built with **Java 21** and **Spring Boot 3**, following clean architecture principles and modern backend best practices.

The project demonstrates authentication with JWT, automated database migrations, testing strategy, containerization, monitoring, centralized logging, and CI automation.

---

# ✨ Features

* User registration and authentication
* JWT Access Token & Refresh Token
* Role-based authorization
* CRUD operations for tasks
* Task status management
* Task priority management
* Global exception handling
* Request validation
* API documentation with Swagger/OpenAPI
* Database migrations with Flyway
* Metrics with Prometheus
* Dashboards with Grafana
* Centralized logging with Loki & Promtail
* Unit tests
* Integration tests with Testcontainers
* Code coverage with JaCoCo
* Continuous Integration with GitHub Actions

---

# 🏗 Architecture

The application follows a layered architecture based on Spring Boot best practices, with a clear separation of responsibilities between layers.

```text
┌─────────────┐
│ Controller  │
└──────┬──────┘
       │
┌──────▼──────┐
│   Service   │
└──────┬──────┘
       │
┌──────▼──────┐
│ Repository  │
└──────┬──────┘
       │
┌──────▼──────┐
│ PostgreSQL  │
└─────────────┘
```

### Project modules

```text
common       → Shared components, exceptions and enums
config       → Application configuration
controller   → REST API endpoints
dto          → Request and response models
entity       → JPA entities
mapper       → Entity and DTO mapping
repository   → Data access layer
security     → Authentication and authorization (JWT)
service      → Business logic
```

---

# 🔐 Authentication

The API uses JWT authentication.

Authentication flow:

1. User registers
2. User authenticates
3. API generates Access Token and Refresh Token
4. Refresh Token allows session renewal

Security features:

* JWT authentication
* Role-based authorization
* Protected endpoints
* Global security exception handling

---

# 🛠 Tech Stack

| Category          | Technology                  |
| ----------------- | --------------------------- |
| Language          | Java 21                     |
| Framework         | Spring Boot 3               |
| Security          | Spring Security + JWT       |
| Database          | PostgreSQL                  |
| ORM               | Spring Data JPA / Hibernate |
| Migration         | Flyway                      |
| Build Tool        | Maven                       |
| Documentation     | Swagger / OpenAPI           |
| Testing           | JUnit 5 + Mockito           |
| Integration Tests | Testcontainers              |
| Coverage          | JaCoCo                      |
| Monitoring        | Prometheus                  |
| Dashboards        | Grafana                     |
| Logging           | Loki + Promtail             |
| Containerization  | Docker & Docker Compose     |
| CI                | GitHub Actions              |

---

# 📂 Project Structure

```text
.
├── alertmanager
│   └── alertmanager.yaml
├── grafana
│   ├── dashboards
│   └── provisioning
├── loki
│   └── config.yaml
├── prometheus
│   ├── alerts.yaml
│   └── prometheus.yaml
├── promtail
│   └── config.yaml
├── src
│   ├── main
│   │   ├── java
│   │   │   └── com
│   │   │       └── well
│   │   │           └── tech
│   │   │               └── task
│   │   │                   └── manager
│   │   │                       ├── common
│   │   │                       │   ├── enums
│   │   │                       │   └── exceptions
│   │   │                       ├── config
│   │   │                       ├── controller
│   │   │                       ├── dto
│   │   │                       │   ├── request
│   │   │                       │   └── response
│   │   │                       ├── entity
│   │   │                       ├── mapper
│   │   │                       ├── repository
│   │   │                       │   └── specification
│   │   │                       ├── security
│   │   │                       └── service
│   │   └── resources
│   │       ├── db
│   │       │   └── migration
│   │       ├── application.yml
│   │       └── ...
│   └── test
├── .env.example
├── .gitignore
├── docker-compose.yaml
├── Dockerfile
├── mvnw
├── mvnw.cmd
├── pom.xml
└── README.md
```

### Infrastructure

- **alertmanager/** → Alert routing configuration
- **prometheus/** → Metrics collection and alert rules
- **grafana/** → Dashboards and provisioning
- **loki/** → Centralized logging configuration
- **promtail/** → Log collection configuration

### Application

- **common/** → Shared components, enums and exceptions
- **config/** → Spring Boot configuration
- **controller/** → REST API endpoints
- **dto/** → Request and response models
- **entity/** → JPA entities
- **mapper/** → Entity ↔ DTO mapping
- **repository/** → Data access layer
- **security/** → JWT authentication and authorization
- **service/** → Business logic

---

# 🐳 Running the project

Clone the repository:

```bash
git clone https://github.com/wellsc88/task-manager.git
cd task-manager
```

Start the environment:

```bash
docker compose up -d
```

The following services will be available:

| Service      | URL                                   |
| ------------ | ------------------------------------- |
| API          | http://localhost:8000                 |
| Swagger      | http://localhost:8000/swagger-ui.html |
| Grafana      | http://localhost:3000                 |
| Prometheus   | http://localhost:9090                 |
| Alertmanager | http://localhost:9093                 |
| pgAdmin      | http://localhost:5050                 |

---

# 📊 Observability

The project includes a complete observability stack:

* Prometheus metrics
* Grafana dashboards
* Loki centralized logs
* Promtail log collection

---

# 🧪 Testing

Run all tests:

```bash
mvn clean verify
```

Reports generated:

* Unit Tests
* Integration Tests
* JaCoCo Code Coverage

---

# ⚙ Continuous Integration

GitHub Actions automatically:

* Builds the project
* Runs all tests
* Validates pull requests

---

# 📌 Project Status

✅ Completed

The project has reached its planned scope and is considered feature-complete. Future updates will focus on maintenance, performance improvements, and new features as needed.