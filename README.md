# Marketplace Microservices

A production-inspired marketplace built with Java and Spring Boot using a microservices architecture.

This project was created to study and demonstrate modern backend development practices, including distributed systems, event-driven communication, authentication, containerization and observability.

## Architecture

The system is composed of independent services communicating through REST and Apache Kafka.

## High-Level Architecture

Client
   │
   ▼
API Gateway
   │
   ├── Auth Service
   ├── User Service
   ├── Product Service
   └── Order Service
           │
           ▼
         Kafka
           │
    ┌──────┼──────┐
    ▼      ▼      ▼
Payment Inventory Notification

### Services

- Discovery Service
- API Gateway
- Auth Service
- User Service
- Product Service
- Order Service
- Inventory Service
- Payment Service
- Notification Service

## Technologies

- Java 21
- Spring Boot 3
- Spring Cloud
- Spring Security
- OAuth2 Authorization Server
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
- Implement OAuth2 Authentication
- Build Event-Driven Communication with Kafka
- Apply Distributed Systems Concepts
- Use Containerization with Docker
- Practice CI/CD and Testing Strategies

## Current Status

🚧 In Development

## Roadmap

- [ ] Discovery Service
- [ ] API Gateway
- [ ] OAuth2 Authentication
- [ ] User Service
- [ ] Product Service
- [ ] Order Service
- [ ] Kafka Integration
- [ ] Inventory Service
- [ ] Payment Service
- [ ] Notification Service
- [ ] Docker Compose
- [ ] Redis Cache
- [ ] Observability
- [ ] Saga Pattern

## Author

Julia Guerreiro
