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
src
├── common
│   ├── enums
│   └── exceptions
├── config
├── controller
├── dto
│   ├── request
│   └── response
├── entity
├── mapper
├── repository
│   └── specification
├── security
├── service
```

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

✅ Stable and actively maintained

Roadmap:

* Authentication
* Docker
* Monitoring
* Centralized Logging
* CI/CD
* Test Coverage
* Future enhancements
