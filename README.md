# Gym CRM System

A Customer Relationship Management (CRM) system for gym operations built with **Spring Boot**, **JPA/Hibernate**, and **PostgreSQL**.  
This system manages trainers, trainees, and training sessions with a **clean layered architecture**, REST controllers, DTO-based request/response handling, global exception management, **Spring Boot Actuators with custom health and metrics indicators**, environment-specific profiles, and Swagger documentation.

## Features

### Authentication & Security
- Username/password authentication with role-based access control
- Secure login/logout
- Session validation via `X-Session-Id` header
- Method-level authorization for trainers, trainees, and resource owners
- Protected operations requiring authentication

### Password Security
- **BCrypt Encryption**: All user passwords are securely hashed using BCrypt algorithm
- **Spring Security Crypto**: Utilizes `BCryptPasswordEncoder` for industry-standard password hashing
- **Salt Generation**: BCrypt automatically generates unique salts for each password
- **Password Verification**: Secure password matching without storing plain text passwords
- **No Plain Text Storage**: Raw passwords are never stored in the database

```java
// Password encryption utility
public static String encryptPassword(String password) {
    return encoder.encode(password); // BCrypt hashing with automatic salt
}

public static boolean verifyPassword(String plainPassword, String hashedPassword) {
    return encoder.matches(plainPassword, hashedPassword); // Secure verification
}
```

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

## Technology Stack

- **Java 21** – Latest LTS version for optimal performance
- **Spring Boot 3.2** – Core application framework with auto-configuration and embedded server
- **Spring Boot Starters** – Simplified dependencies for web, JPA, validation, and actuator
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
- **JsonPath** – JSON assertions and schema validation

### Logging
- **SLF4J 2 + Logback** – Unified logging facade and implementation

## Project Structure

```
├───src
│   ├───main
│   │   ├───java/com/jsalva/gymsystem
│   │   │   ├───config          # Application config (Swagger, WebConfig)
│   │   │   ├───controller      # REST controllers
│   │   │   │   └───advise      # Exception advice
│   │   │   ├───dto             # DTOs for requests/responses
│   │   │   │   ├───request
│   │   │   │   └───response
│   │   │   ├───entity          # JPA entities
│   │   │   ├───exception       # Custom exceptions
│   │   │   ├───facade          # Facade pattern for service orchestration
│   │   │   │   └───impl
│   │   │   ├───filter          # Request filters
│   │   │   ├───indicator       # Custom Actuator health/metrics indicators
│   │   │   ├───mapper          # MapStruct mappers
│   │   │   ├───repository      # JPA repositories
│   │   │   ├───service         # Business logic services
│   │   │   │   └───impl
│   │   │   └───utils           # Utilities (e.g., password encoder)
│   │   └───resources           # application.properties / SQL scripts
│   │       └───sql
│   └───test/java/com/jsalva/gymsystem
│       ├───controller          # Controller tests
│       ├───service             # Service layer tests
│       └───utils               # Utility tests
```


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
2. The application will automatically:
   - Create/update database schema via Hibernate
   - Load initial training types from `data-pg.sql` or `data-h2.sql` depending on the selected profile.

3. Run the application using Maven:
```bash
mvn spring-boot:run
```

### Configuration

Database and JPA configuration in `application-local.properties`:

```properties
# Database Configuration
server.port=8080
spring.datasource.url=jdbc:postgresql://localhost:5432/gymdb
spring.datasource.username=postgres
spring.datasource.password=postgres
spring.datasource.driver-class-name=org.postgresql.Driver

# JPA/Hibernate Configuration
#spring.jpa.hibernate.ddl-auto=create-drop
spring.jpa.hibernate.ddl-auto=update

spring.jpa.show-sql=false
spring.jpa.properties.hibernate.format_sql=false
spring.jpa.properties.hibernate.jdbc.time_zone=UTC

# Initialize database with data-pg.sql AFTER Hibernate creates schema
spring.jpa.defer-datasource-initialization=true
spring.sql.init.mode=always
spring.sql.init.data-locations=classpath:sql/data-pg.sql

# For mappers that reference each other with nested lists
spring.main.allow-circular-references=true

# expose all actuator endpoints
#management.endpoints.web.exposure.include=*
management.endpoints.web.exposure.include=health,info,prometheus,jvmInfo

# always show health details
management.endpoint.health.show-details=always
```

## Swagger UI

Interactive API documentation is available through Swagger UI.

- **URL:** [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)


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
- **Configuration Layer**: Application, persistence, and framework setup.

### Cross-cutting Components
- **Filters**: Request/response logging, transaction IDs, etc.
- **Global Exception Handler**: Consistent error handling and responses.
- **API Documentation**: Swagger/OpenAPI for interactive documentation.


## Security Features

### Authentication
- Username/password based login returning a **session ID**
- Clients must include the session ID in request headers for all authenticated operations
- Logout invalidates the session
- Session tracking includes username and user type (Trainer/Trainee)

### Authorization
- All validations are performed using the session ID from headers
- Role-based access control:
    - `validateLogin(sessionId)` — Ensures valid session
    - `validateTrainerAuth(sessionId)` — Trainer-only operations
    - `validateOwnerAuth(sessionId, targetUsername)` — Validates resource ownership
    - `validateTrainerOrOwnerAuth(sessionId, targetUsername)` — Allows trainer or resource owner
- Password update operations validate old password before applying changes

## Testing

The project includes comprehensive unit tests covering:

- Repository layer functionality
- Service layer business logic
- Facade layer orchestration
- Controller layer with `MockMvc` for endpoint validation
- Utility classes and helpers

```bash
# Run all tests
mvn test

# Run tests with coverage (IDE)
# Right-click on test directory → "Run All Tests with Coverage"

# Run specific test class
mvn test -Dtest=TrainerServiceTest
```

### Actuators

Spring Boot Actuator is enabled for application monitoring and management.  
The application exposes several endpoints (default port: `9000`):

- **Actuator root**  
  `GET http://localhost:9000/actuator`

- **Health**  
  `GET http://localhost:9000/actuator/health`  
  Provides overall status plus database, disk, and ping checks.

- **Info**  
  `GET http://localhost:9000/actuator/info`

- **JVM Info**  
  `GET http://localhost:9000/actuator/jvmInfo`  
  Returns memory and processor details.

- **Prometheus**  
  `GET http://localhost:9000/actuator/prometheus`  
  Exposes application and system metrics in Prometheus format.  
  Includes the custom metric:
  HELP app_users_total Current number of registered users
  TYPE app_users_total gauge
  app_users_total 0.0


## Migration from Previous Version

This version expands the project with new infrastructure, security, and API features:

### What Changed
- **Data Layer**: Replaced manual JPA/Hibernate repository implementation with Spring Data `JpaRepository` for CRUD, using `@Query` annotations for custom logic
- **Monitoring**: Added Spring Boot Actuator and Prometheus endpoint for application metrics
- **Profiles**: Introduced multiple environments — `local` (PostgreSQL) and `test` (H2 in-memory database)
- **Configuration**: Replaced manual Spring Core configuration with Spring Boot auto-configuration
- **Server**: embedded Tomcat for running the application uses spring boot starter

### What Remained
- **Persistence**: JPA entities and repository-based access
- **Core Business Logic**: Trainer, trainee, and training management
- **Architecture Principles**: Layered design with separation of concerns
- **Build System**: Maven-based build and dependency management
- **Security**: session-based authentication and authorization
- **API Documentation**: Swagger/OpenAPI integration
- **Filters**: request filters for transaction ID tracking and login handling
- **DTOs**: request/response DTOs for clean API contracts
- **Controllers**: REST controllers exposing the Facade layer
- **Error Handling**: Custom exceptions and centralized error handling with `@ControllerAdvice`


## License

This project is part of the EPAM Java Specialization program and is intended for educational purposes.

## Author

**Julián Salvá Ramírez** - [GitHub Profile](https://github.com/Jsalvar124)
