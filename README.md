
# Loan Management API

Loan Management API is a Spring Boot microservice designed to manage customer loans, installments, and payments. It supports JWT authentication, role-based access control (ADMIN and CUSTOMER), and enforces business rules for loan creation and payment. The project uses an in-memory H2 database for development and can be containerized via Docker.

----------

## Table of Contents

1.  [Features](#features)
    
2.  [Environment Variables](#environment-variables)
    
3.  [Building and Running](#building-and-running)
    
    -   [Local (Maven)](#local-maven)
        
    -   [Docker](#docker)
        
4.  [Swagger UI](#swagger-ui)
    
5.  [API Endpoints](#api-endpoints)
        
6.  [Validation & Business Rules](#validation--business-rules)
        
7.  [Error Handling](#error-handling)
    
8.  [Testing](#testing)
    
    1.  [Unit Tests](#unit-tests)
        
9.  [Dockerfile & Deployment](#dockerfile--deployment)
    

----------

## Features

-   **JWT Authentication**
    
    -   `POST /api/v1/auth/login` issues a JWT token after validating username/password.
        
    -   Protected endpoints require `Authorization: Bearer <token>`.
        
-   **Role-Based Access**
    
    -   **ADMIN** can perform operations (create, list, pay) on any customer’s loans.
        
    -   **CUSTOMER** can only perform operations on their own loans and installments.
        
-   **Loan Management**
    
    -   Create new loans with specified `amount`, `interestRate` (0.1–0.5), and `numberOfInstallment` (6, 9, 12, 24).
        
    -   Automatic generation of equal installments (principal × (1 + interestRate) ÷ numberOfInstallment).
        
    -   Enforces “next month’s 1st day” as the first installment due date.
        
-   **Installment Management**
    
    -   List all installments for a given loan (`GET /api/v1/loans/{loanId}/installments`).
        
    -   Pay installments via `POST /api/v1/loans/{loanId}/pay?amount={amount}`:
        
        -   Applies early-payment discounts (0.1% per day before due).
            
        -   Applies late-payment penalties (0.1% per day after due).
            
        -   Can cover multiple installments in a single payment.
            
        -   Restricts payment to installments due within the next 3 months.
            
-   **Business Rules Enforcement**
    
    -   Interest rate must be between 0.1 and 0.5.
        
    -   Number of installments allowed: 6, 9, 12, or 24.
        
    -   Customer’s available credit limit must cover the new loan principal.
        
    -   Upon loan creation, customer’s `usedCreditLimit` updates accordingly.
        
-   **In-Memory H2 Database**
    
    -   Default development profile uses H2.
        
    -   Schema and example data auto-generated via JPA/Hibernate.
        
-   **Swagger/OpenAPI Documentation**
    
    -   Accessible at `/swagger-ui.html` for interactive API exploration.
        
    -   OpenAPI JSON available at `/v3/api-docs`.
        
-   **Docker Support**
    
    -   Multi-stage `Dockerfile` builds the JAR and runs it in a lightweight OpenJDK container.
        
    -   Reads `.env` for JWT secret and expiration.
        
-   **Unit Tests**
    
    -   Controller layer tested via `@WebMvcTest` and MockMvc.
        
    -   Covers ADMIN and CUSTOMER scenarios for each endpoint.
----------

## Environment Variables

Create a `.env` file:

```dotenv
JWT_SECRET_KEY=your-secret-key
JWT_TOKEN_EXPIRATION_TIME=86400000

```

----------

## Building and Running

### Local (Maven)

```bash
git clone https://github.com/marikci/loan-management-api.git
cd loan-management-api
mvn clean install
java -jar target/loan-management-api-1.0.0.jar

```

### Docker

```bash
docker build -t loan-management-api .
docker run --env-file .env -p 8080:8080 loan-management-api

```

----------

## Swagger UI

-   Open browser at [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)
    

----------

## API Endpoints

-   `POST /api/v1/auth/login`
    
-   `POST /api/v1/customers/register`
    
-   `POST /api/v1/loans/customers/{customerId}/loans`
    
-   `GET /api/v1/loans/customers/{customerId}`
    
-   `GET /api/v1/loans/{loanId}/installments`
    
-   `POST /api/v1/loans/{loanId}/pay?amount=1000`
    

----------

## Validation & Business Rules

-   Interest rate: 0.1 to 0.5
    
-   Installments: 6, 9, 12, 24
    
-   Credit limit check on loan creation
    
-   Early payment discount / late payment penalty applied on each installment
    
-   Max payment covers 3 months ahead
    

----------

## Error Handling

-   Returns error codes:
    
    -   CUSTOMER_NOT_FOUND_EXCEPTION
        
    -   CUSTOMER_UPDATE_LIMIT_EXCEPTION
        
    -   LOAN_NOT_FOUND_EXCEPTION
        
    -   USER_NOT_FOUND_EXCEPTION
        

----------

## Testing

### Unit Tests

-   Written with JUnit 5 and Mockito
    
-   Uses `@WebMvcTest` for controller testing
----------

## Dockerfile & Deployment

-   Multi-stage build using Maven and OpenJDK base images
    
-   Copies `.env` and exposes port 8080
    
