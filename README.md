# BridgeSpero – Online Education Platform (Backend) https://bridgespero.com

BridgeSpero is an early-stage EdTech startup project developed under the **ASIIP Foundation**. The platform focuses on connecting teachers and students through structured group lessons, learning resources, and secure access control. The backend is designed with a strong emphasis on clean architecture, security, and long-term scalability.

---

## Project Overview

BridgeSpero provides a robust backend foundation for an online education platform where:

* Teachers create and manage educational groups
* Students request access to groups and learning resources
* Administrators manage users, categories, and platform configuration

---

## Architecture

* Monolithic Spring Boot application with scalability considerations
* Layered architecture:

  * Controller layer for REST API exposure
  * Service layer for business logic
  * Repository layer for data persistence
* DTO-based request and response handling
* Clear separation of concerns following SOLID principles

---

## Security

* JWT-based authentication using access and refresh tokens
* Spring Security with role-based authorization
* Supported roles include SUPER_ADMIN, ADMIN, TEACHER, and STUDENT
* Redis-based token storage and validation
* Method-level access control using `@PreAuthorize`

---

## Core Functionality

### User Management

* User registration and authentication
* Role-based access control
* User profile management
* Soft delete support for user entities

### Teacher and Student Workflow

* Teachers create and manage educational groups
* Students submit join requests to groups
* Teachers approve or reject join requests

### Group Management

* Group creation with category, language, schedule, and pricing information
* Student capacity enforcement
* Group lifecycle management

### Category Management

* Hierarchical category structure with parent–child relationships
* Category-based organization of educational content

### Resources and Media

* Secure file upload and download
* Access-controlled learning resources
* Video and document handling
* HTTP range support for video streaming

---

## Persistence Layer

* PostgreSQL as the primary relational database
* JPA and Hibernate for object–relational mapping
* Complex entity relationships (One-to-Many, Many-to-Many)
* Soft delete implemented using custom SQL delete logic
* Database schema versioning managed with Liquibase (YAML format)

---

## Caching

* Redis used for token storage and caching frequently accessed data to improve performance

---

## Containerization and Local Development

* Dockerized application setup
* Docker Compose for local development
* Separate containers for:

  * Application service
  * PostgreSQL database
  * Redis cache

---

## Technology Stack

* Language: Java 21
* Framework: Spring Boot
* Security: Spring Security, JWT
* Database: PostgreSQL
* Caching: Redis
* Database Migrations: Liquibase
* Object Mapping: MapStruct
* Build Tools: Maven, Gradle
* Containerization: Docker, Docker Compose

---

## Roadmap

* Introduce asynchronous communication for decoupled processes
* Modularize the system further as the platform scales
* Add notification mechanisms (email and in-app)
* Improve observability through structured logging and metrics

---

## Project Status

BridgeSpero is actively under development as an early-stage startup project under the ASIIP Foundation. The backend is designed to support future growth and evolving business requirements.
