# Mini Blog API

A RESTful API for creating and managing blog posts, comments, tags, and user authentication. Built with Spring Boot, the API supports JWT-based authentication, PostgreSQL persistence, and Redis caching for improved performance.

🔗 **Live API:** [https://mini-blog-api-production-13c1.up.railway.app/api/v1/swagger-ui/index.html](https://mini-blog-api-production-13c1.up.railway.app/api/v1/swagger-ui/index.html)

---

## Features

- 🔐 JWT authentication with access & refresh tokens
- 👤 User registration and login
- 📝 Post CRUD operations with slug-based retrieval
- 💬 Comment system tied to posts
- 🏷️ Tag-based filtering and search
- 📄 Paginated responses
- 🗄️ Redis caching layer to reduce database load on frequent reads
- 🗃️ Flyway-managed database migrations
- 📖 Interactive Swagger/OpenAPI documentation
- 🐳 Dockerized local development environment

---

## Tech Stack

| Layer | Technology                    |
|---|-------------------------------|
| Language | Java 21                       |
| Framework | Spring Boot                   |               
| Security | Spring Security, JWT          |
| Database | PostgreSQL                    |
| Migrations | Flyway                        |
| Caching | Redis (Upstash)               |
| Documentation | springdoc-openapi (Swagger UI) |
| Containerization | Docker, Docker Compose        |
| Deployment | Railway                       |
| Build Tool | Maven                         |

---

## Getting Started

### Prerequisites

- Java 21
- Docker & Docker Compose
- Maven (or use the included `mvnw` wrapper)

### Local Setup

1. **Clone the repository**
   ```bash
   git clone https://github.com/your-username/mini-blog-api.git
   cd mini-blog-api
   ```

2. **Start PostgreSQL and Redis via Docker Compose**
   ```bash
   docker compose up -d db redis
   ```

3. **Set required environment variables**
   ```bash
   export DB_PASSWORD=your_local_db_password
   export JWT_SECRET=your_jwt_secret_at_least_32_characters
   export MAIL_USERNAME=your_mailtrap_username
   export MAIL_PASSWORD=your_mailtrap_password
   ```

4. **Run the application**
   ```bash
   ./mvnw spring-boot:run -Dspring-boot.run.profiles=dev
   ```

   Or run it from your IDE with the `dev` profile active.

5. **Access the API**
    - Base URL: `http://localhost:8080/api/v1`
    - Swagger UI: `http://localhost:8080/api/v1/swagger-ui/index.html`

---

## Authentication

Most endpoints require a valid JWT. To authenticate:

1. Register a user via `POST /auth/register`
2. Log in via `POST /auth/login` to receive an `accessToken` and `refreshToken`
3. In Swagger UI, click **Authorize** and paste your `accessToken`
4. Use `POST /auth/refresh` with your `refreshToken` to obtain a new access token once it expires

---

## API Overview

| Method | Endpoint | Description |
|---|---|---|
| POST | `/auth/register` | Register a new user |
| POST | `/auth/login` | Log in and receive tokens |
| POST | `/auth/refresh` | Refresh an access token |
| POST | `/auth/logout` | Log out |
| GET | `/posts` | Search/list posts (paginated) |
| GET | `/posts/{slug}` | Get a post by slug |
| GET | `/posts/owned/{id}` | Get a post owned by the current user |
| POST | `/posts` | Create a post |
| PUT | `/posts/{id}` | Update a post |
| DELETE | `/posts/{id}` | Delete a post |
| GET | `/comments/post/{postId}` | Get comments for a post |
| POST | `/comments/post/{postId}` | Add a comment to a post |

Full request/response schemas are available in the [Swagger UI](https://mini-blog-api-production-13c1.up.railway.app/api/v1/swagger-ui/index.html).

---

## Project Structure

```
src/main/java/com/margaretnjoki/mini_blog_api/
├── config/          # OpenAPI, security, and app configuration
├── controller/       # REST controllers
├── dto/               # Request/response DTOs
├── model/             # JPA entities
├── repository/        # Spring Data JPA repositories
├── security/          # JWT filter, token service, user details service
└── service/           # Business logic
```

---

## Deployment

The application is deployed on [Railway](https://railway.app), with:
- **PostgreSQL** hosted as a Railway-managed service
- **Redis** hosted on [Upstash](https://upstash.com) (accessed over TLS)

Environment-specific configuration is managed via Spring profiles (`dev`, `prod`) and environment variables, keeping secrets out of source control.

---

## What I Learned

This project was as much about infrastructure as it was about API design. Key takeaways:

- Debugging a Spring Boot `@Configuration` bean that wasn't being applied due to a stale build artifact
- Configuring datasource and Redis connections correctly across local (Docker) and cloud (Railway + Upstash) environments
- Handling TLS requirements for a managed Redis provider
- Structuring JWT access/refresh token flows securely
- Managing environment variables and secrets across local, containerized, and deployed environments

