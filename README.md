# User Verification Status API

A containerised REST API built with Java and Spring Boot that manages user verification statuses.

## How to Run Locally

### Prerequisites
- Java 21
- Maven
- PostgreSQL

### Steps
1. Clone the repository
   git clone https://github.com/xqzte/UserVerification.git
   cd UserVerification

2. Create a PostgreSQL database
   CREATE DATABASE verificationdb;

3. Set environment variables
   DB_USERNAME=postgres
   DB_PASSWORD=postgres
   JWT_SECRET=mySecretKeyHereMakeItLongEnough123456789
   ADMIN_EMAIL=admin@verification.com
   ADMIN_PASSWORD=Admin1234
   ADMIN_USERNAME=System Admin

4. Run the application
   mvn spring-boot:run

5. API is available at http://localhost:8080

---

## How to Run with Docker

### Prerequisites
- Docker Desktop installed and running

### Steps
1. Clone the repository
   git clone https://github.com/xqzte/UserVerification.git
   cd UserVerification

2. Run with Docker Compose
   docker-compose up --build

3. API is available at http://localhost:8080

No additional setup needed. Docker Compose handles the database and application together.

---

## API Endpoints

### Auth Endpoints (Public)

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | /api/auth/register | Register a new user |
| POST | /api/auth/login | Login and receive JWT token |
| GET | /api/auth/users/{email}/status | Check verification status |

### Admin Endpoints (Requires JWT Token with ADMIN role)

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | /api/admin/users | Get all users |
| GET | /api/admin/users/pending | Get all pending users |
| PATCH | /api/admin/users/{id}/status | Update a user's verification status |

### Example Request — Register
POST /api/auth/register
```json
{
    "fullName": "John Doe",
    "email": "john@email.com",
    "password": "password123"
}
```

### Example Request — Login
POST /api/auth/login
```json
{
    "email": "john@email.com",
    "password": "password123"
}
```

### Example Request — Update Status (Admin only)
PATCH /api/admin/users/1/status
Authorization: Bearer eyJhbGci...
```json
{
    "status": "APPROVED"
}
```

### Verification Status Values
- PENDING — default on registration
- APPROVED — admin approved the user
- REJECTED — admin rejected the user
- CANCELLED — verification was cancelled

---

## CI Workflow

The GitHub Actions workflow runs on every push to any branch and does the following:

- Checks out the code
- Sets up Java 21
- Runs mvn clean package -DskipTests to verify the project builds without errors

A green checkmark means the build passed. A red cross means something is broken.

---

## Architectural Decision

### Stateless Authentication with JWT

This API uses JWT tokens instead of sessions for authentication.

Since the application runs inside Docker containers, there is no shared session storage between instances. JWT tokens carry all the necessary information inside the token itself, meaning any container instance can validate a request without needing to talk to a central session store. This makes the system easier to scale and deploy.

### Admin Seeding on Startup

Instead of exposing an admin registration endpoint, the system automatically seeds a single admin user on startup using Spring Boot's CommandLineRunner. The admin credentials are injected via environment variables, keeping them out of the codebase entirely. This prevents privilege escalation through the public API.