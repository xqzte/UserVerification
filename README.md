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

2. Set environment variables
   DB_USERNAME_USER_VERIFICATION=postgres
   DB_PASSWORD=your_postgres_password
   JWT_SECRET=your_secret_key_here_make_it_long

3. Run the application
   mvn spring-boot:run

4. API is available at http://localhost:8080

## How to Run with Docker

### Prerequisites
- Docker Desktop installed and running

### Steps
1. Clone the repository
   git clone https://github.com/xqzte/UserVerification.git
   cd UserVerification

2. Run with Docker Compose
   docker-compose up

3. API is available at http://localhost:8080

## API Endpoints

| Method | Endpoint | Access | Description |
|--------|----------|--------|-------------|
| POST | /api/auth/register | Public | Register a new user |
| POST | /api/auth/login | Public | Login and get token |
| GET | /api/auth/users/{email}/status | Public | Check verification status |
| GET | /api/admin/users | Admin only | Get all users |
| GET | /api/admin/users/pending | Admin only | Get pending users |
| PATCH | /api/admin/users/{email}/status | Admin only | Update user status |

## CI Workflow

The GitHub Actions workflow runs on every push to any branch.

It does the following:
- Checks out the code
- Sets up Java 21
- Runs mvn clean package -DskipTests to verify the project builds without errors

If the build fails, the workflow reports an error so issues are caught early.

## Architectural Decision

### Stateless Authentication with JWT

This API uses JWT tokens instead of sessions for authentication.

The reason for this decision is that stateless authentication fits containerised deployments better. Since the application runs inside Docker containers, there is no shared session storage between instances. JWT tokens carry all the necessary information inside the token itself, meaning any container instance can validate a request without needing to talk to a central session store. This makes the system easier to scale and deploy.