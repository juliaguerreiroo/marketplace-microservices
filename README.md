# Marketplace Microservices

A production-inspired marketplace built with Java and Spring Boot using a microservices architecture.

This project was created to study and demonstrate modern backend development practices, including distributed systems, event-driven communication, authentication, containerization and observability.

## Architecture

The system is composed of independent services communicating through REST and Apache Kafka.

## High-Level Architecture

```text
                    Client
                       │
                       ▼
               API Gateway
                       │
      ┌────────────────┼────────────────┐
      ▼                ▼                ▼
 Auth Service    User Service    Order Service
                                          │
                                          ▼
                                        Kafka
                                          │
               ┌──────────────┬──────────────┬──────────────┐
                              ▼              ▼
                       Payment Service  Product Service 
```

### Services

- Discovery Service
- API Gateway
- Auth Service
- User Service
- Product Service
- Order Service
- Payment Service

## Technologies

- Java 21
- Spring Boot 3
- Spring Cloud
- Spring Security
- JWT
- Kafka
- PostgreSQL
- Docker
- Redis
- OpenFeign
- Eureka Server
- Swagger / OpenAPI
- JUnit 5
- Testcontainers
- Prometheus
- Grafana

## Project Goals

- Learn Microservices Architecture
- Implement Spring Security Authentication
- Build Event-Driven Communication with Kafka
- Apply Distributed Systems Concepts
- Use Containerization with Docker
- Practice CI/CD and Testing Strategies

## Current Status

🚧 In Development


# Running the Project

## Prerequisites

- Java 21
- Docker
- Docker Compose

> Maven does not need to be installed globally because every service includes the Maven Wrapper.

---

## Database

Docker Compose automatically creates all required databases.

Default PostgreSQL credentials:

```text
Host: localhost
Port: 5432
Username: postgres
Password: postgres
```

---

## Start the Application

From the project root, run:

```bash 
docker compose up --build
```

This command will:

- Build all microservice images
- Start PostgreSQL
- Start Redis
- Start Kafka
- Start Eureka Discovery Server
- Start the API Gateway
- Start all application services

---

## Run in Detached Mode

To run the containers in the background:

```bash 
docker compose up --build -d
```

## Stop the Application

```bash 
docker compose down
```

## API Documentation

Each microservice exposes its own Swagger/OpenAPI documentation.

After starting the application with Docker Compose, access:

```text 
Auth Service Swagger: [http://localhost:9001/swagger-ui/index.html](http://localhost:9001/swagger-ui/index.html) 
User Service Swagger: [http://localhost:9002/swagger-ui/index.html](http://localhost:9002/swagger-ui/index.html) 
Product Service Swagger: [http://localhost:9003/swagger-ui/index.html](http://localhost:9003/swagger-ui/index.html) 
Order Service Swagger: [http://localhost:9004/swagger-ui/index.html](http://localhost:9004/swagger-ui/index.html) 
Payment Service Swagger: [http://localhost:9008/swagger-ui/index.html](http://localhost:9008/swagger-ui/index.html)
```

## Demo Users

The application includes two demo users for local API testing.

| Role | Email | Password |
|---|---|---|
| Admin | admin@example.com | senhaadmin |
| User | user@example.com | senhauser |

Use these credentials in the Auth Service login endpoint to generate a JWT token.

After receiving the token, use it in protected endpoints.

## Authentication Flow

To test protected endpoints:

1. Open the Auth Service Swagger:
```text 
[http://localhost:9001/swagger-ui/index.html](http://localhost:9001/swagger-ui/index.html)
```
2. Use the login endpoint with one of the demo users.
3. Copy the JWT token returned by the API.
4. Open the Swagger UI of the service you want to test.
5. Click **Authorize**.
6. Paste the bearer token
7. Execute the protected endpoints according to the user's role.

Admin users can access administrative endpoints, while regular users have limited permissions depending on each service security configuration.

## Business Flow Overview

The marketplace flow is organized around independent services:

1. Users authenticate through the Auth Service.
2. User data is managed by the User Service.
3. Products are managed by the Product Service.
4. Orders are created by the Order Service.
5. Order events are published to Kafka.
6. The Payment Service consumes payment-related events.
7. Payment results are published back through Kafka.
8. Services communicate through REST, OpenFeign and asynchronous Kafka events where applicable.

## Payment Simulation

The payment flow is simulated by the Payment Service when an order is created.

When registering a new order, the request must include payment data, including a token. This token is used by the Payment Service fake payment gateway to decide whether the payment should be approved or rejected.

For predictable testing, use the following token patterns:

```text 
tok_ok_anything
```
Approves the payment.


```text 
tok_fail_anything
```
Rejects the payment.


## Author

Julia Dalanôra Guerreiro
