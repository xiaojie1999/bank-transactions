# Project Overview

A simple bank transaction management system built with Java 21 and Spring Boot.

## Folder Structure
```plaintext
banktransaction/
├── src/
│   ├── main/
│   │   ├── java/com/example/banktransactions/
│   │   │   ├── BankTransactionApplication.java
│   │   │   ├── controller/
│   │   │   │   └── TransactionController.java
│   │   │   │   └── WebController.java
│   │   │   ├── model/
│   │   │   │   └── Transaction.java
│   │   │   ├── repository/
│   │   │   │   └── LocalMemoryTransactionRepository.java
│   │   │   ├── service/
│   │   │   │   └── TransactionService.java
│   │   │   ├── exception/
│   │   │   │   ├── TransactionAlreadyExistsException.java
│   │   │   │   ├── TransactionNotFoundException.java
│   │   │   │   └── TransactionExceptionHandler.java
│   │   │   └── config/
│   │   │       └── CacheConfig.java
│   │   └── resources/
│   │       ├── application.properties
│   │       ├── application.yml
│   │       ├── static/
│   │       │    └── js
│   │       │        └── app.js
│   │       │    └── css
│   │       │        └── style.css
│   │       └── templates/
│   │           └── transactions.html
│   └── test/
│       └── java/com/example/banktransactions/
│           ├── TestCase.java
├── Dockerfile
├── docker-compose.yml
├── kubernetes/
│   ├── deployment.yml
│   └── service.yml
├── pom.xml
└── README.md
## Requirements

- Java 21+
- Maven 3.8.6+
- Docker (for containerization)

## Getting Started
## Running Locally with Docker Compose

### Steps
1. Navigate to the project folder:
   ```bash
   cd project-folder
   ```

2. Build and start the containers:
   ```bash
   docker-compose up --build
   ```

3. Access the applications:
    - **Frontend**: [http://localhost:8080](http://localhost:8080)
    - **Backend**: [http://localhost:8080/api/transactions](http://localhost:8080/api/transactions)

4. Stop the containers:
   ```bash
   docker-compose down
   ```

## Notes
- Use `docker-compose.yml` for local development.


# transaction Management System

## Overview
The Bank Transaction System provides a REST API for managing transaction. It allows creating, retrieving, updating, and deleting transaction while leveraging in-memory caching for improved performance.

---

## Imported Dependencies and Their Purpose

### **1. Spring Boot Starter Web**
- **Dependency**: `org.springframework.boot:spring-boot-starter-web`
- **Purpose**: Provides the necessary components for building and running a RESTful web service, including Spring MVC and embedded Tomcat server.

### **2. Caffeine**
- **Dependency**: `com.github.ben-manes.caffeine:caffeine`
- **Purpose**: Implements an in-memory caching layer for optimizing read-heavy APIs and reducing latency by caching frequently accessed data.

### **3. Spring Boot Starter Test**
- **Dependency**: `org.springframework.boot:spring-boot-starter-test`
- **Scope**: Test
- **Purpose**: Provides tools for testing, including JUnit 5, Mockito, and Spring TestContext Framework.

### **4. Spring Boot Starter Validation**
- **Dependency**: `org.springframework.boot:spring-boot-starter-validation`
- **Purpose**: Enables annotation-based validation for REST API inputs using `@Valid`.

### **5. Lombok**
- **Dependency**: `org.projectlombok:lombok`
- **Purpose**: Reduces boilerplate code in Java classes by generating getters, setters, constructors, and more at compile time.

---

## APIs Description

### **1. Create transaction**
- **Method**: `POST`
- **Endpoint**: `/api/transactions`
- **Description**: Creates a new transaction with a unique ID. Here we only checked if the transactions is duplicated to avoid duplication.
- **Request Body**:
  ```json
  {
    "accountId": "string",
    "amount": "string",
    "type": "string",
    "description": "string",
    "timestamp": "string"
  }
  ```
- **Response**:
  ```json
  {
    "id": "105d12fb-3f74-4f29-b29b-8b473ab9c504",
    "accountId": "1",
    "amount": "2",
    "type": "DEPOSIT",
    "description": "demo",
    "timestamp": "2025-01-01T12:30:00"
  }
  ```
- **Validation**:
    - `accountId` and `amount`,`type` must not be blank.

### **2. Find All transactions**
- **Method**: `GET`
- **Endpoint**: `/api/transactions`
- **Description**: Fetches all transactions.
- **Response**:
  ```json
  [
    {
    "id": "105d12fb-3f74-4f29-b29b-8b473ab9c504",
    "accountId": "1",
    "amount": "2",
    "type": "DEPOSIT",
    "description": "demo",
    "timestamp": "2025-01-01T12:30:00"
  },
    {
    "id": "65f54eaf-ff5e-4dad-b31e-dcb605bc2a64",
    "accountId": "2",
    "amount": "3",
    "type": "DEPOSIT",
    "description": "demo",
    "timestamp": "2025-01-01T12:30:00"
  }
  ]
  ```

### **3. Retrieve transactions by ID**
- **Method**: `GET`
- **Endpoint**: `/api/transactions/{id}`
- **Description**: Retrieves details of an transaction by its unique ID.
- **Response**:
  ```json
  {
    "id": "105d12fb-3f74-4f29-b29b-8b473ab9c504"
  }
  ```
- **Validation**:
    - `id` cannot be empty or null.

### **4. Update transaction**
- **Method**: `PUT`
- **Endpoint**: `/api/transactions/{id}`
- **Description**: Updates an existing transaction.
- **Request Body**:
  ```json
  {
    "accountId": "2",
    "amount": "3",
    "type": "DEPOSIT",
    "description": "demo"
  }
  ```
- **Response**:
  ```json
  {
    "accountId": "2",
    "amount": "3",
    "type": "DEPOSIT",
    "description": "demo"
  }
  ```
- **Validation**:
    - `id` must exist.
    - `accountId` cannot be changed.

### **5. Delete transaction**
- **Method**: `DELETE`
- **Endpoint**: `/api/transactions/{id}`
- **Description**: Deletes an transaction by its unique ID.
- **Response**:
  ```
  HTTP 204 No Content
  ```
- **Validation**:
    - `id` must exist.
### **6. Get transactions Count**
- **Method**: `DELETE`
- **Endpoint**: `/api/transactions/count`
- **Description**: Deletes an transaction by its unique ID.
- **Response**:
  10
  ```
---

## Build and Run Instructions

### **Prerequisites**
- Java 21
- Maven

### **Build**
Run the following command to package the application:
```bash
mvn clean package
```

### **Run**
Run the following command to start the application:
```bash
java -jar target/bank-transactions-0.0.1-SNAPSHOT.jar
```

The application will start on `http://localhost:8080` by default.

---




