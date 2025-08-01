﻿# Gym CRM System

A Customer Relationship Management (CRM) system for gym operations built with Spring Core framework. This system manages trainers, trainees, and training sessions with a clean layered architecture.

## Features

### Trainer Management
- Create new trainers with specialization (CrossFit, Zumba, Functional, Boxing, Pilates, Bouldering)
- Retrieve all trainers or specific trainer by ID
- Update trainer information including personal details, specialization, password, and active status
- Delete trainers from the system

### Trainee Management
- Create new trainees with personal information (name, address, date of birth)
- Retrieve all trainees or specific trainee by ID
- Update trainee information including personal details, password, and active status
- Delete trainees from the system

### Training Management
- Create training sessions linking trainers and trainees
- Define training details including name, type, date, and duration
- Retrieve all training sessions or specific training by ID
- Track training history and schedules

## Technology Stack

- **Java 21** - Latest LTS version for optimal performance
- **Spring Core 6.1.0** - Dependency injection and IoC container
- **Maven** - Build automation and dependency management
- **JUnit 5** - Unit testing framework
- **Mockito** - Mocking framework for testing
- **SLF4J + Logback** - Logging framework
- **CSV Data Storage** - File-based data persistence

## Project Structure

```
src/
├── main/
│   ├── java/com/jsalva/gymsystem/
│   │   ├── config/          # Spring configuration classes
│   │   ├── dao/             # Data Access Objects
│   │   │   └── impl/        # DAO implementations
│   │   ├── facade/          # Business facade layer
│   │   │   └── impl/        # Facade implementations
│   │   ├── model/           # Domain models (Trainer, Trainee, Training)
│   │   ├── service/         # Business logic layer
│   │   │   └── impl/        # Service implementations
│   │   ├── storage/         # Data storage utilities
│   │   └── utils/           # Utility classes
│   └── resources/
│       └── init/            # Initial data files (CSV)
└── test/                    # Unit and integration tests
```

## Getting Started

### Prerequisites

- Java 21 or higher
- Maven 3.6+
- Git

### Installation

1. Clone the repository:
```bash
git clone https://github.com/Jsalvar124/gym-crm-system.git
cd gym-crm-system
```

2. Build the project:
```bash
mvn clean compile
```

3. Run tests:
```bash
mvn test
```

4. Package the application:
```bash
mvn package
```

### Running Locally

1. Ensure all dependencies are installed:
```bash
mvn dependency:resolve
```

2. The application loads initial data from CSV files located in `src/main/resources/init/`:
    - `trainers.csv` - Initial trainer data
    - `trainees.csv` - Initial trainee data
    - `trainings.csv` - Initial training sessions

3. Run the application using Maven:
```bash
mvn exec:java -Dexec.mainClass="com.jsalva.gymsystem.Main"
```

### Configuration

The application uses `application.properties` for configuration:

```properties
# Data initialization file paths
storage.trainers.file=init/trainers.csv
storage.trainees.file=init/trainees.csv
storage.trainings.file=init/trainings.csv
```

## Data Format

### Trainers CSV Format
```csv
firstName,lastName,username,password,isActive,specialization
John,Smith,John.Smith,abc1234567,true,CROSSFIT
```

### Supported Training Types
- CROSSFIT
- ZUMBA
- FUNCTIONAL
- BOXING
- PILATES
- BOULDERING

## Architecture

The system follows a layered architecture pattern:

- **Facade Layer**: Provides a simplified interface for complex subsystem interactions
- **Service Layer**: Contains business logic and orchestrates operations
- **DAO Layer**: Handles data access and persistence operations
- **Model Layer**: Domain objects representing core entities
- **Storage Layer**: File-based data storage utilities

## Testing

The project includes comprehensive unit tests using JUnit 5 and Mockito:

```bash
# Run all tests
mvn test

# Run tests with coverage in IntelliJ IDEA
# Right-click on the test directory → "Run All Tests with Coverage"
```

The current test coverage provides good coverage across the service and DAO layers, ensuring reliable functionality of core business operations.

## License

This project is part of the EPAM Java Specialization program and is intended for educational purposes.

## Author

**Julián Salvá Ramírez** - [GitHub Profile](https://github.com/Jsalvar124)
