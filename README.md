# Migrated Application: `kitchensink` Spring Boot Version

## Overview

This project is a migration of the legacy `kitchensink` JBoss application to Spring Boot using Java 21. It allows for member registration and displays registered members.

## Prerequisites

- Java 21
- Maven 3.6+
- Git

## Build Instructions
1. Clone the repository:
   ```bash
   git clone https://github.com/jmhohl13/kitchensink
2. Navigate to the project directory:
    ```bash
        cd kitchensink
    ```
3. Build the project:
    ```bash
    mvn clean package

## Running the Application
1. Run the application using Maven:

    ```bash
        mvn spring-boot:run
     ```
    Or run the packaged JAR file:
    
    ```bash
        java -jar target/kitchensink-springboot.jar
     ```

2. Access the application in a browser:

    ```bash
        http://localhost:8080/kitchensink
     ```

## Features

- Member registration form.
- Display of registered members.
- Validation for duplicate emails and other constraints.


## Testing
Run unit and integration tests with:
    ```mvn test```


## Features
Server settings can be changed in the application.properties:
```bash
server.port=8080
server.servlet.context-path=/kitchensink