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
   DB_PASSWORD=Anonymous67
   JWT_SECRET=mySecretKeyHereMakeItLongEnough123456
   ADMIN_EMAIL=bilalidris011@gmail.com
   ADMIN_PASSWORD=Anonymous67
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

## Default Admin Credentials

The system automatically creates an admin user on startup.

| Variable | Default Value |
|---|---|
| ADMIN_EMAIL | bilalidris011@gmail.com |
| ADMIN_PASSWORD | Anonymous67 |

To login as admin:
POST /api/auth/login
{
"email": "bilalidris011@gmail.com",
"password": "Anonymous67"
}

---

## API Endpoints

### Core Verification Endpoints (Brief Specification)

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | /verify | Accepts userId and category, returns verification status object |
| GET | /status/{userId} | Returns current verification status for a user |
| PATCH | /status/{userId} | Updates verification status (PENDING, APPROVED, REJECTED, CANCELLED) |
| GET | /health | Returns service status and uptime |

### Auth Endpoints (Public)

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | /api/auth/register | Register a new user |
| POST | /api/auth/login | Login and receive JWT token |
| GET | /api/auth/users/{email}/status | Check verification status by email |

### Admin Endpoints (Requires JWT Token with ADMIN role)

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | /api/admin/users | Get all users |
| GET | /api/admin/users/pending | Get all pending users |
| PATCH | /api/admin/users/{id}/status | Update a user verification status |

---

## Example Requests

### POST /verify
```json
{
    "userId": "1",
    "category": "identity"
}
```
Response:
```json
{
    "userId": 1,
    "category": "identity",
    "status": "PENDING",
    "email": "john@email.com",
    "fullName": "John Doe"
}
```

### GET /status/{userId}
Response:
```json
{
    "userId": 1,
    "email": "john@email.com",
    "fullName": "John Doe",
    "status": "PENDING"
}
```

### PATCH /status/{userId}
```json
{
    "status": "APPROVED"
}
```
Response:
```json
{
    "userId": 1,
    "email": "john@email.com",
    "fullName": "John Doe",
    "status": "APPROVED"
}
```

### GET /health
Response:
```json
{
    "status": "UP",
    "uptime": "0 hours 5 minutes 30 seconds"
}
```

### Verification Status Values
- PENDING — default status on registration
- APPROVED — verification approved
- REJECTED — verification rejected
- CANCELLED — verification cancelled

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