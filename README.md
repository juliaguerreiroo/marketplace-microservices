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

## Author

Julia Dalanôra Guerreiro
