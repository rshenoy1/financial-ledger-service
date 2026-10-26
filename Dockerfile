# ===================================================================
# STAGE 1: Compilation and Build Environment (Official Java 25 LTS Base)
# ===================================================================
FROM maven:3.9.11-eclipse-temurin-25-alpine AS builder
WORKDIR /build

# Copy build definition files first to optimize Docker layer caching
COPY pom.xml .

# Fetch dependencies in a separate step to cache them locally
RUN mvn dependency:go-offline -B

# Copy application source files
COPY src ./src

# Compile and package the application artifact, skipping unit tests for speed
RUN mvn clean package -DskipTests -B

# ===================================================================
# STAGE 2: Minimal Production Runtime Environment (Java 25 LTS Runtime)
# ===================================================================
FROM eclipse-temurin:25-jre-alpine
WORKDIR /app

# Create a non-privileged system user to run the application securely
RUN addgroup -S appgroup && adduser -S appuser -G appgroup
USER appuser

# Copy only the compiled executable JAR from the builder stage
COPY --from=builder /build/target/financial-ledger-service-1.0.0-SNAPSHOT.jar app.jar

# Expose the standard microservice HTTP endpoint port
EXPOSE 8080

# Configure production-optimized JVM flags for container awareness
ENTRYPOINT ["java", \
            "-XX:+UseContainerSupport", \
            "-XX:MaxRAMPercentage=75.0", \
            "-Djava.security.egd=file:/dev/./urandom", \
            "-jar", "app.jar"]
