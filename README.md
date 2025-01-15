> [!CAUTION]
> **Sequence package is not finished**. Missing bussiness logic and testing -> waiting for PokladnaBean 

# Migration of Entity Beans to Jakarta Persistence API (JPA)

## Overview
This repository contains the implementation and documentation of my master's thesis. The goal of the thesis is to modernize an existing application by migrating Entity Beans to the contemporary Jakarta Persistence API (JPA) standard, optimizing database structure, and improving data access performance.

## Objectives
1. **Migration to JPA:**
    - Rewrite or regenerate existing Entity Beans using the Jakarta Persistence API.
2. **Cleanup Unused Code:**
    - Detect and remove unused parts of the application to streamline the codebase.
3. **Database Optimization:**
    - Analyze and optimize the database schema. Potential changes include:
      - Splitting the database into more efficient parts.
      - Introducing NoSQL databases where appropriate.
      - Moving certain data from the database to the file system.
4. **Data Migration:**
    - Ensure data integrity and correctness while transitioning to the new schema.

## Features

- **Modernized Persistence**:
    - Migrated legacy Entity Beans to Jakarta Persistence API (JPA).
    - Utilized Hibernate as the JPA implementation for advanced ORM features.
- **DeltaSpike Integration**:
    - Streamlined repository and service layer development using DeltaSpike.
- **Database Optimization**:
    - Improved schema design for performance and scalability.
- **Dockerized Database**:
    - Database runs in a Docker container for easy setup and deployment using `docker-compose`.
- **Lombok**:
    - Simplified entity class development by leveraging Lombok annotations for boilerplate code reduction.

## Project Structure

The repository is organized as follows:
- `src/main/java`: Source code for the application, including updated JPA entities and repository layers.
- `src/test/java`: Unit and integration tests for the migrated code.
- `Old_ToBeRewritten/`: Contains old Beans which are currently being rewritten
- `clubspire.sql`: SQL script for original database schema for testing purposes.
- `docker-compose.yml`: Configures and runs the database in a Docker container.

## Technologies Used

- **DeltaSpike**: Simplified repository and service patterns.
- **Hibernate**: As the JPA implementation.
- **Jakarta Persistence API (JPA)**: Modern ORM standards.
- **Lombok**: For reducing boilerplate in entity classes.
- **PostgreSQL**: Database solutions for schema optimization.
- **Docker**: To containerize and manage the database.
- **JUnit**: For testing migrated and optimized code.
- **Maven/Gradle**: For dependency management and builds.

## Solution

### Bean rewritten into layers :
- **Entity layer**
- **Repository layer**
- **Service layer**
- **Facade layer**


## How to Use

### Prerequisites

- JDK 21 or higher
- Maven/Gradle
- Docker installed and running
- Clone this repository:
  ```bash
  git clone https://github.com/OndroL/Thesis.git
  cd Thesis
  ```

### Running the Database in Docker
The database is preconfigured with Docker. Use the `docker-compose.yml` file in the source directory.

1. Start the database:
   ```bash
   docker-compose up -d
   ```

2. Verify the database is running:
   ```bash
   docker ps
   ```

### Build and Run the Application

1. Build the project:
   ```bash
   mvn clean install
   ```

2. Run the application:
   ```bash
   java -jar target/thesis.jar
   ```

---

## Testing

Run all tests using:
```bash
mvn test
```


---

> [!NOTE]
> The Official Assignment is subject to change

## Official Assignment

The official assignment for this thesis includes:
1. Migration of Entity Beans to Jakarta Persistence API.
2. Detection and removal of unused code and database elements.
3. Optimization of the database schema, including potential database splits, introduction of NoSQL databases, or moving data to the file system.
4. Correction of data during migration to ensure compatibility with the new schema.
