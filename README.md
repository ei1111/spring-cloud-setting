# 🚀 MSA 프로젝트 구성

Spring Boot 기반 MSA(Microservices Architecture) 프로젝트입니다.  
Java 21, Spring Boot 3.5.7 기준으로 구성되어 있습니다.

---

## 🏗️ 프로젝트 구조

| 서비스 이름        | Java Version | Spring Boot Version | Dependencies / Notes |
|------------------|-------------|------------------|---------------------|
| ![Eureka](https://img.shields.io/badge/Eureka-Server-blue) **Eureka Server** | 21 | 3.5.7 | `spring-cloud-starter-netflix-eureka-server` |
| ![Gateway](https://img.shields.io/badge/API-Gateway-green) **API Gateway** | 21 | 3.5.7 | `spring-cloud-starter-gateway-server-webflux` |
| ![Service](https://img.shields.io/badge/Service-First-orange) **First Service** | 21 | 3.5.7 | 기본 서비스 |
| ![Service](https://img.shields.io/badge/Service-Second-orange) **Second Service** | 21 | 3.5.7 | 기본 서비스 |
| ![Service](https://img.shields.io/badge/Service-User-orange) **User Service** | 21 | 3.5.7 | 기본 서비스 |

---

## ⚙️ MSA 구조 다이어그램

```mermaid
graph TD
    %% Eureka 서버
    EurekaServer[Eureka Server]:::eureka

    %% API Gateway
    APIGateway[API Gateway]:::gateway

    %% Microservices
    FirstService[First Service]:::service
    SecondService[Second Service]:::service
    UserService[User Service]:::service

    %% 연결 관계
    EurekaServer --> APIGateway
    APIGateway --> FirstService
    APIGateway --> SecondService
    APIGateway --> UserService

    %% 서비스 등록
    FirstService --> EurekaServer
    SecondService --> EurekaServer
    UserService --> EurekaServer

    %% 스타일 정의
    classDef eureka fill:#1E90FF,stroke:#000,color:#fff,font-weight:bold
    classDef gateway fill:#32CD32,stroke:#000,color:#fff,font-weight:bold
    classDef service fill:#FFA500,stroke:#000,color:#000,font-weight:bold
