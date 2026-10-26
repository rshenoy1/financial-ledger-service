# Financial Ledger Engine 🚀

A production-hardened, non-blocking financial mutation ledger microservice engineered for high-throughput write density utilizing **Java 21 Virtual Threads (Project Loom)**, **Spring Boot 3.4**, and **PostgreSQL**.

## 🏛️ System Core Architecture

```text
[ Client JSON Requests ]
           │ Asynchronous HTTP Bursts
           ▼
   [ Tomcat Container ] ──► Allocates 1 Lightweight Virtual Thread per Request
           │
           ▼
  [ JWT Security Gate ] ──► Cryptographic Signature Filtering (JJWT 0.12.x)
           │
           ▼
    [ LedgerService ]   ──► Optimistic Locking Retrier Loop (Non-Blocking Back-off)
           │
           ▼
[ AccountRepository ]   ──► Parameterized SQL Mapping (Spring Data JPA)
           │
           ▼
  [ Hikari Pool CP ]    ──► Strict outbound throttling (Hard-capped max pool = 50)
           │
           ▼
    [ PostgreSQL ]      ──► Automated schema evolution engine tracking (Flyway)
```

## 🛠️ Prerequisites & Stack Core Components

* **Operating System Engine**: Docker Desktop / Docker Compose
* **Shell Gateway Terminal**: PowerShell Core / Command Line
* **Development Language Environment**: Java 21 LTS / Apache Maven 3.x+

## 🚀 Execution & Stack Launch Blueprint

Run this command block inside your terminal window to clean old data volumes, compile code inside a multi-stage non-root container, and launch your network stack:

```bash
# Tear down dangling container clusters and wipe out historical data volumes
docker-compose down -v

# Compile code assets and generate your immutable production runtime image
docker build --no-cache -t financial-ledger-service:1.0.0 .

# Spin up the application stack on the private Docker bridge network
docker-compose up -d
```

## 🔍 Verification & Health Auditing Pathways

### 1. Interrogating Virtual Thread Execution States
Verify that the inbound servlet payload is caught and executed inside a lightweight user-space Virtual Thread rather than a heavy host OS thread:

```powershell
Invoke-RestMethod -Uri "http://localhost:8080/api/v1/ledger/thread-status"
```
*Expected Return Metric:* `isVirtualThread: true` mapping straight to a thread handle identifier like `tomcat-handler-0`.

### 2. Executing Authenticated Financial Transactions
Authenticate via the open public route to obtain a secure JSON Web Token signature, and pass it inside a protected Bearer header array to mutate balances:

```powershell
# 🔓 1. Log in programmatically to issue a secure token signature
\$authResponse = Invoke-RestMethod -Method Post -Uri "http://localhost:8080/api/v1/ledger/auth/login?username=ledger_admin"
jwtToken = authResponse.token

# 💸 2. Transmit the transactional payload under absolute token protection
\(headers = @{ "Authorization" = "Bearer \)jwtToken" }
\$bodyPayload = '{"amount": 250.50}'

Invoke-RestMethod -Method Post -Uri "http://localhost:8080/api/v1/ledger/accounts/1/transaction" -ContentType "application/json" -Headers headers -Body bodyPayload
```
*Expected Result:* The balance increments cleanly while the record version automatically updates via non-blocking Optimistic Locking bounds.
