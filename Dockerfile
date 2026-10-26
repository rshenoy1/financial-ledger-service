# ===================================================================
# STAGE 1: Compilation and Build Environment (Official Java 21 LTS Base)
# ===================================================================
FROM maven:3.9.6-eclipse-temurin-21-alpine AS builder
WORKDIR /build

COPY pom.xml .
RUN mvn dependency:go-offline -B

COPY src ./src
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