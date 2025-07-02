# Power of Attorney (POA) API Documentation

##  Overview

The **POA App** is a secure, stateless REST API that allows grantors to authorize grantees with read/write access to bank accounts. Built using Spring Boot, MongoDB, and secured via OAuth2 JWT tokens, the service is containerized with Docker and ready for observability via Prometheus and OpenAPI Swagger UI.

---
## Assumptions for Power of Attorney (POA) Application

1. A single grantor can issue multiple POAs for the same account — Each POA may be for different grantees and access types.

2.  Multiple grantors can delegate POAs for the same account — Ownership or control may be shared across business or joint account holders.

3.  Each POA is uniquely defined by the combination of: accountNumber, grantee and accessType

4.  An account number must always map to exactly one account type — Once an account is designated as, e.g., PAYMENT, no other POA may associate it with a conflicting type like SAVINGS.

##  Features

- ✅ Grant delegated access (READ / WRITE)
- ✅ Query POA data by grantee
- ✅ Validates accessType and accountType case-insensitively
- ✅ Secured with OAuth2 + JWT
- ✅ Dockerized for local or cloud deployment
- ✅ Prometheus metrics via Actuator
- ✅ API documented with Swagger UI (OpenAPI 3)

---

##  Technology Stack

| Layer         | Tool / Framework              |
|---------------|-------------------------------|
| Language      | Java 17                       |
| Framework     | Spring Boot 2.7.x             |
| DB            | MongoDB 6                     |
| Security      | Spring Security + JWT (HS256) |
| Docs          | SpringDoc OpenAPI (Swagger)   |
| Monitoring    | Prometheus + Actuator         |
| Container     | Docker                        |
| Testing       | JUnit 5, MockMvc, Mockito     |
| Coverage      | JaCoCo                        |

---
Production-Readiness Checklist
- OAuth2 JWT secured
- Swagger UI + token auth enabled
- Case-insensitive input handling
- HTTPS SSL configured
- Dockerized and MongoDB-integrated
- Prometheus metrics enabled
- 80%+ test coverage (JaCoCo)

## Swagger (OpenAPI)
Available at: https://localhost:8443/swagger-ui/index.html

click on `Authorize` button to add jwt bearer token for authorization

##  Security Overview

### JWT Authentication

- JWT tokens required on all protected endpoints
#### Sample JWT Token 

eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ2YXJzaGEuYiIsInNjb3BlIjoicG9hLnJlYWQgcG9hLndyaXRlIiwiaXNzIjoicG9hLWFwcCIsImlhdCI6MTc1MTQ2NjExMiwiZXhwIjoxNzUyMDcwOTEyfQ.s5dAs0mDn0nBMcJvEa5u61X93VxQDy_Pya7HTQvWwxo

## Test Coverage
Run:
```
mvn clean test
mvn jacoco:report
```
View report at: target/site/jacoco/index.html

## Actuator Metrics
run docker command
```
 docker run -d -p 9090:9090 -v  .\src\main\resources\prometheus.yml:/etc/prometheus/prometheus.yml prom/prometheus
```
Access at: http://localhost:9090/


