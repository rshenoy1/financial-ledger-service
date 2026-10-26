# ===================================================================
# STAGE 1: Compilation and Build Environment (Official Java 21 LTS Base)
# ===================================================================
FROM maven:3.9.6-eclipse-temurin-21-alpine AS builder
WORKDIR /build

# Copy build definition files first to optimize Docker layer caching
COPY pom.xml .

# 📥 Hardened fix: Force explicit resolution of new security dependencies
RUN mvn dependency:go-offline -B

# Copy application source files explicitly to bring in security source classes
COPY src ./src

# Compile and package the application artifact, bypassing tests and vulnerability scans inside the sandbox
RUN mvn clean package -DskipTests -Ddependency-check.skip=true -B

# ===================================================================
# STAGE 2: Minimal Production Runtime Environment (Java 21 LTS Runtime)
# ===================================================================
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

RUN addgroup -S appgroup && adduser -S appuser -G appgroup
USER appuser

COPY --from=builder /build/target/financial-ledger-service-1.0.0-SNAPSHOT.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", \
            "-XX:+UseContainerSupport", \
            "-XX:MaxRAMPercentage=75.0", \
            "-Djava.security.egd=file:/dev/./urandom", \
            "-jar", "app.jar"]