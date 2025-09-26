# Gym CRM System

A Customer Relationship Management (CRM) system for gym operations built with **Spring Boot**, **Spring Security**, **JWT Authentication**, **JPA/Hibernate**, and **PostgreSQL**.  
This system manages trainers, trainees, and training sessions with a **clean layered architecture**, REST controllers, DTO-based request/response handling, global exception management, **Spring Boot Actuators with custom health and metrics indicators**, environment-specific profiles, and Swagger documentation.

## Features

### Authentication & Security
- **JWT-based Authentication**: Stateless authentication using JSON Web Tokens
- **Role-based Access Control**: Trainer/Trainee role-based authorization
- **Brute Force Protection**: Account lockout after 3 failed login attempts for 5 minutes
- **Secure Login/Logout**: JWT token generation and blacklisting for logout
- **Spring Security Integration**: Custom JWT filter with SecurityContext population
- **CORS Configuration**: Cross-Origin Resource Sharing for frontend integration
- **Method-level Authorization**: Custom validation for trainers, trainees, and resource owners

### Password Security
- **BCrypt Encryption**: All user passwords are securely hashed using BCrypt algorithm
- **Spring Security Crypto**: Utilizes `BCryptPasswordEncoder` for industry-standard password hashing
- **Salt Generation**: BCrypt automatically generates unique salts for each password
- **Password Verification**: Secure password matching without storing plain text passwords
- **No Plain Text Storage**: Raw passwords are never stored in the database

```java
// Password encryption utility
public static String encryptPasswordBCrypt(String password) {
    return bcryptEncoder.encode(password); // BCrypt hashing with automatic salt
}

public static boolean verifyPasswordBCrypt(String plainPassword, String hashedPassword) {
    return bcryptEncoder.matches(plainPassword, hashedPassword); // Secure verification
}
```

### JWT Token Management
- **Token Generation**: 24-hour expiration with username and user type claims
- **Token Validation**: Signature verification and expiration checking
- **Token Blacklisting**: Server-side logout functionality through token invalidation
- **Automatic Cleanup**: Expired tokens are automatically removed from blacklist
- **Bearer Token Format**: Standard `Authorization: Bearer <token>` header format

```json
// JWT token structure
{
  "sub": "username",           // Subject (username)
  "userType": "TRAINER",       // Custom claim for role
  "iat": 1640995200,          // Issued at
  "exp": 1641081600           // Expiration (24 hours)
}
```

### Brute Force Protection
- **Failed Login Tracking**: In-memory storage of failed login attempts per username
- **Account Lockout**: 3 failed attempts triggers 5-minute lockout
- **Automatic Expiration**: Lockouts expire automatically without manual intervention
- **User Enumeration Protection**: Failed attempts counted even for non-existent users
- **Thread-Safe**: ConcurrentHashMap for concurrent request handling

### Trainer Management
- Create new trainers with specialization (CrossFit, Zumba, Functional, Boxing, Pilates, Bouldering)
- Retrieve specific trainer by username
- Update trainer information including personal details, specialization, and password
- Change trainer active state (activate/deactivate)
- Retrieve trainees assigned to a trainer
- Find unassigned trainers for a specific trainee

---

### Trainee Management
- Create new trainees with personal information (name, address, date of birth)
- Retrieve specific trainee by username
- Update trainee information including personal details and password
- Change trainee active state (activate/deactivate)
- Delete trainees by username
- Retrieve trainers assigned to a trainee
- Track trainee training history

---

### Training Management
- Create training sessions linking trainers and trainees
- Define training details including name, type, date, and duration
- Retrieve specific training session by ID
- Update training session details (name, type, date, duration)
- Retrieve trainings for a trainer with optional date range filters
- Retrieve trainings for a trainee with optional date range filters

---

### Monitoring & Observability
- **Spring Boot Actuator** endpoints for health, metrics, info, and custom indicators
- **Custom Health Indicator** to check database connectivity
- **Custom Endpoint (`/actuator/jvmInfo`)** for JVM memory and processor statistics
- **Custom Prometheus Metric** (`app_users_total`) tracking the current number of registered users

### CORS Support
- **Cross-Origin Resource Sharing**: Configured for frontend integration
- **Environment-specific Origins**: Different allowed origins for default, local, and test
- **Credential Support**: Allows cookies and authorization headers
- **Preflight Handling**: Automatic OPTIONS request handling for complex CORS requests

## Technology Stack

- **Java 21** – Latest LTS version for optimal performance
- **Spring Boot 3.2** – Core application framework with auto-configuration and embedded server
- **Spring Security 6** – Authentication, authorization, and security features
- **JWT (JSON Web Tokens)** – Stateless authentication with JJWT library
- **Spring Boot Starters** – Simplified dependencies for web, JPA, validation, security, and actuator
- **Spring Data JPA (JpaRepository)** – Repository abstraction with query generation and pagination
- **Hibernate (Jakarta)** – JPA implementation and ORM provider
- **PostgreSQL** – Production-ready relational database
- **H2 Database** – In-memory database for testing
- **Jakarta Validation (Hibernate Validator 8)** – Bean validation
- **Jackson (Databind + JSR-310)** – JSON serialization/deserialization including Java 8+ date/time
- **MapStruct** – Mapper framework for DTO–Entity transformations
- **Spring Security Crypto (BCrypt)** – Secure password hashing
- **Spring Boot Actuator** – Monitoring endpoints with custom health checks and Prometheus metrics
- **Micrometer + Prometheus** – Metrics collection and scraping
- **Swagger (springdoc-openapi)** – API documentation (OpenAPI 3)

### Testing
- **JUnit 5** – Unit testing framework
- **Mockito** – Mocking framework (core + JUnit 5 integration)
- **Spring Boot Test** – Bootstrapped test utilities
- **Spring Security Test** – Security testing utilities
- **JsonPath** – JSON assertions and schema validation

### Logging
- **SLF4J 2 + Logback** – Unified logging facade and implementation

## Project Structure

```
├───src
│   ├───main
│   │   ├───java/com/jsalva/gymsystem
│   │   │   ├───config          # Application config (Security, Swagger, CORS)
│   │   │   ├───controller      # REST controllers
│   │   │   │   └───advise      # Exception advice
│   │   │   ├───dto             # DTOs for requests/responses
│   │   │   │   ├───request
│   │   │   │   └───response
│   │   │   ├───entity          # JPA entities
│   │   │   ├───exception       # Custom exceptions
│   │   │   ├───facade          # Facade pattern for service orchestration
│   │   │   │   └───impl
│   │   │   ├───filter          # JWT authentication filter
│   │   │   ├───indicator       # Custom Actuator health/metrics indicators
│   │   │   ├───mapper          # MapStruct mappers
│   │   │   ├───repository      # JPA repositories
│   │   │   ├───service         # Business logic services
│   │   │   │   └───impl
│   │   │   └───utils           # Utilities (JWT, password encoder, brute force protection)
│   │   └───resources           # application.properties / SQL scripts
│   │       └───sql
│   └───test/java/com/jsalva/gymsystem
│       ├───controller          # Controller tests
│       ├───service             # Service layer tests
│       └───utils               # Utility tests
```

## Security Architecture

### JWT Authentication Flow
1. **Login**: User submits credentials to `/api/v1/auth/login`
2. **Validation**: Credentials verified and brute force protection checked
3. **Token Generation**: JWT token created with 24-hour expiration
4. **Token Usage**: Client includes token in `Authorization: Bearer <token>` header
5. **Token Validation**: `JwtAuthenticationFilter` validates token on each request
6. **Security Context**: Authentication object populated in Spring Security context
7. **Logout**: Token added to blacklist, invalidating future usage

### Authorization Levels
- **Public Endpoints**: Login, registration, actuator health
- **Authenticated**: All other endpoints require valid JWT token
- **Role-Based**: Trainer-specific operations require `ROLE_TRAINER`
- **Owner-Based**: Users can only modify their own resources
- **Combined**: Some operations allow trainer OR resource owner access

### Security Components
- **SecurityConfig**: Spring Security configuration with JWT filter chain
- **JwtAuthenticationFilter**: Extracts and validates JWT tokens from requests
- **JwtUtils**: Token generation, validation, and claim extraction utilities
- **BruteForceProtectionService**: Failed login attempt tracking and lockout management
- **TokenBlacklistService**: Logout functionality through token invalidation
- **AuthService**: Authentication business logic and authorization validation

## Database Schema

The system uses JPA inheritance strategy with the following entity relationships:

### Core Entities
- **User** (base entity) - Contains common user information
- **Trainer** (extends User) - Specializes in specific training types
- **Trainee** (extends User) - Gym members with personal details
- **Training** - Training sessions linking trainers and trainees
- **TrainingType** - Available training specializations

### Entity Relationships
- User (1) ← (1) Trainer/Trainee (Table per class inheritance)
- Trainer (1) → (N) Training
- Trainee (1) → (N) Training
- TrainingType (1) → (N) Training
- TrainingType (1) → (N) Trainer (specialization)

![img_2.png](img_2.png)

## Getting Started

### Prerequisites

- Java 21 or higher
- Maven 3.6+
- PostgreSQL 12+
- Git

### Database Setup

1. Install PostgreSQL and create a database:
```sql
CREATE DATABASE gymdb;
```

2. The application will automatically create tables using Hibernate DDL generation.

### Installation

1. Clone the repository:
```bash
git clone https://github.com/Jsalvar124/gym-crm-system.git
cd gym-crm-system
```

2. Configure database connection in src/main/resources/application-local.properties:
   Username and password are set to postgres by default. Update as needed.
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/gymdb
spring.datasource.username={YOUR-USERNAME}
spring.datasource.password={YOUR-PASSWORD}
```

3. Build the project:
```bash
mvn clean compile
```

4. Run tests:
```bash
mvn test
```

5. Package the application:
```bash
mvn package
```

### Running Locally

1. Ensure PostgreSQL is running and the database is created
2. Set the active Spring profile (`local` or `test`):
   ```bash
   export SPRING_PROFILES_ACTIVE=local   # Unix/Mac
   set SPRING_PROFILES_ACTIVE=local      # Windows PowerShell
   ```
3. The application will automatically:
    - Create/update database schema via Hibernate
    - Load initial training types from `data-pg.sql` or `data-h2.sql` depending on the selected profile.

4. Run the application using Maven:
```bash
mvn spring-boot:run
```

### Configuration

Database and security configuration in `application-local.properties`:

```properties
# Database Configuration
server.port=8080
spring.datasource.url=jdbc:postgresql://localhost:5432/gymdb
spring.datasource.username=postgres
spring.datasource.password=postgres
spring.datasource.driver-class-name=org.postgresql.Driver

# JPA/Hibernate Configuration
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=false
spring.jpa.properties.hibernate.format_sql=false
spring.jpa.properties.hibernate.jdbc.time_zone=UTC

# Initialize database with data-pg.sql AFTER Hibernate creates schema
spring.jpa.defer-datasource-initialization=true
spring.sql.init.mode=always
spring.sql.init.data-locations=classpath:sql/data-pg.sql

# CORS Configuration
app.cors.allowed-origins=http://localhost:3000,http://localhost:8080
app.cors.allowed-methods=GET,POST,PUT,DELETE,PATCH,OPTIONS
app.cors.allowed-headers=Authorization,Content-Type,X-Requested-With
app.cors.allow-credentials=true
app.cors.max-age=3600

# For mappers that reference each other with nested lists
spring.main.allow-circular-references=true

# Actuator endpoints
management.endpoints.web.exposure.include=health,info,prometheus,jvmInfo
management.endpoint.health.show-details=always
```

## API Authentication

### Login
```bash
POST /api/v1/auth/login
Content-Type: application/json

{
  "username": "Juan.Perez",
  "password": "password123"
}

Response: 200 Success
{
  "token": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0cmFpbmVyLnNtaXRoIiwidXNlclR5cGUiOiJUUkFJTkVSIiwiaWF0IjoxNjQwOTk1MjAwLCJleHAiOjE2NDEwODE2MDB9.signature"
}
```

### Using the Token
```bash
GET /api/v1/trainers/Juan.Perez
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0cmFpbmVyLnNtaXRoIi...
```

### Logout
```bash
POST /api/v1/auth/logout
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0cmFpbmVyLnNtaXRoIi...

Response: 204 No Content
```

## Swagger UI

Interactive API documentation is available through Swagger UI.

- **URL:** [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)
- **JWT Authentication**: Use the "Authorize" button to add your JWT token for protected endpoints

## Training Types

The system supports the following training specializations:

- **CROSSFIT** - High-intensity functional movements
- **ZUMBA** - Dance fitness program
- **FUNCTIONAL** - Functional movement training
- **BOXING** - Combat sports training
- **PILATES** - Low-impact flexibility and strength
- **BOULDERING** - Rock climbing training

## Architecture

The system follows a **layered architecture** pattern with clear separation of concerns:

### Layers
- **Controller Layer**: Exposes REST endpoints and delegates requests to the facade.
- **Facade Layer**: Provides a unified interface, handling orchestration and security.
- **Service Layer**: Encapsulates business logic and domain rules.
- **Repository Layer**: Manages persistence with JPA/Hibernate.
- **Entity Layer**: JPA entities modeling the domain (User, Trainer, Trainee, Training).
- **Configuration Layer**: Application, security, persistence, and framework setup.

### Cross-cutting Components
- **Security Filters**: JWT authentication and authorization
- **Filters**: Request/response logging, transaction IDs, etc.
- **Exception Handling**: Global exception handler with consistent error responses
- **API Documentation**: Swagger/OpenAPI for interactive documentation
- **CORS Configuration**: Cross-origin resource sharing for frontend integration

## Security Features

### Authentication
- **JWT-based Authentication**: Stateless token-based authentication
- **24-hour Token Expiration**: Automatic token expiration for security
- **Bearer Token Format**: Standard HTTP authorization header format
- **Secure Token Generation**: HMAC-SHA256 signature with secret key
- **Token Blacklisting**: Server-side logout through token invalidation

### Authorization
- **Role-based Access Control**: TRAINER/TRAINEE role-based permissions
- **Resource Ownership**: Users can only access their own resources
- **Method-level Security**: Custom authorization validation methods
- **Spring Security Integration**: SecurityContext population for framework features

### Brute Force Protection
- **Failed Login Tracking**: In-memory storage of failed attempts per user
- **Account Lockout**: 3 failed attempts = 5-minute lockout
- **Automatic Expiration**: Lockouts expire without manual intervention
- **Thread-safe Implementation**: ConcurrentHashMap for concurrent access

### CORS Configuration
- **Environment-specific Origins**: Different allowed origins per environment
- **Credential Support**: Allows authorization headers and cookies
- **Preflight Handling**: Automatic OPTIONS request processing
- **Configurable Security**: Customizable origins, methods, and headers

## Testing

The project includes comprehensive unit tests covering:

- Service layer business logic including security
- Controller layer with `MockMvc` for endpoint validation
- Security configuration and JWT functionality
- Utility classes and helpers

```bash
# Run all tests
mvn test

# Run tests with coverage (IDE)
# Right-click on test directory → "Run All Tests with Coverage"

# Run specific test class
mvn test -Dtest=TrainerServiceTest

# Run security-related tests
mvn test -Dtest=*Security*
```

### Actuators

Spring Boot Actuator is enabled for application monitoring and management.  
The application exposes several endpoints:

- **Actuator root**  
  `GET http://localhost:8080/actuator`

- **Health**  
  `GET http://localhost:8080/actuator/health`  
  Provides overall status plus database, disk, and ping checks.

- **Info**  
  `GET http://localhost:8080/actuator/info`

- **JVM Info**  
  `GET http://localhost:8080/actuator/jvmInfo`  
  Returns memory and processor details.

- **Prometheus**  
  `GET http://localhost:8080/actuator/prometheus`  
  Exposes application and system metrics in Prometheus format.  
  Includes the custom metric:
  ```
  HELP app_users_total Current number of registered users
  TYPE app_users_total gauge
  app_users_total 0.0
  ```

## Migration from Previous Version

This version expands the project with comprehensive security and frontend integration features:

### What's New
- **Spring Security Integration**: Complete JWT-based authentication and authorization
- **Brute Force Protection**: Account lockout mechanism for failed login attempts
- **Token Management**: JWT generation, validation, and blacklisting for logout
- **CORS Configuration**: Cross-origin resource sharing for frontend integration
- **Security Filters**: Custom JWT authentication filter with SecurityContext population
- **Enhanced Error Handling**: Security-aware exception handling with proper HTTP status codes
- **Environment Configuration**: Profile-based security and CORS settings

### Enhanced Security
- **Stateless Authentication**: JWT tokens replace session-based authentication
- **Role-based Authorization**: Spring Security roles with custom validation methods
- **Password Security**: BCrypt hashing with automatic salt generation
- **Request Filtering**: JWT validation on every authenticated request
- **Logout Functionality**: Server-side token invalidation through blacklisting

### What Changed
- **Authentication System**: Migrated from session ID to JWT token-based authentication
- **Security Architecture**: Implemented Spring Security with custom filters and configuration
- **Authorization Model**: Enhanced role-based and resource ownership validation
- **API Security**: All endpoints now properly secured with JWT authentication
- **Configuration Management**: Environment-specific security and CORS settings

### What Remained
- **Persistence**: JPA entities and repository-based access
- **Core Business Logic**: Trainer, trainee, and training management
- **Architecture Principles**: Layered design with separation of concerns
- **Build System**: Maven-based build and dependency management
- **API Documentation**: Swagger/OpenAPI integration
- **DTOs**: request/response DTOs for clean API contracts
- **Controllers**: REST controllers exposing the Facade layer
- **Error Handling**: Custom exceptions and centralized error handling with `@ControllerAdvice`
- **Monitoring**: Spring Boot Actuator with custom health and metrics indicators

## License

This project is part of the EPAM Java Specialization program and is intended for educational purposes.

## Author

**Julián Salvá Ramírez** - [GitHub Profile](https://github.com/Jsalvar124)