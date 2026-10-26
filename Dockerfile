# ===================================================================
# STAGE 1: COMPILATION SANDBOX ENVIRONMENT
# ===================================================================
FROM maven:3.9.6-eclipse-temurin-21-alpine AS builder
WORKDIR /build

# Copy the pom.xml declaration mapping first to establish explicit caching boundaries
COPY pom.xml .

# Download dependencies using standard verification parameters
RUN mvn dependency:go-offline -B

# Copy the source tree context layers into the workspace
COPY src ./src

# Hardened Fix: Remove the 'clean' lifecycle flag to reuse cached dependencies safely
RUN mvn package -DskipTests -Ddependency-check.skip=true -B

# ===================================================================
# STAGE 2: PRODUCTION RUNTIME IMAGE ENVIRONMENT
# ===================================================================
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Provision a restricted non-root runtime system user
RUN addgroup -S appgroup && adduser -S appuser -G appgroup
USER appuser

# Extract strictly the compiled executable JAR file from the builder phase
COPY --from=builder /build/target/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]