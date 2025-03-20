# First stage: Build the JAR file
FROM maven:3.8.6-openjdk-17 AS builder
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Second stage: Use the lightweight BellSoft Liberica runtime
FROM bellsoft/liberica-openjdk-alpine:17
WORKDIR /app
COPY --from=builder /app/target/*.jar app.jar

# Expose the default Heroku port
EXPOSE 8080

# Run the JAR file (using Heroku's PORT environment variable)
ENTRYPOINT ["java", "-Dserver.port=${PORT:-8080}", "-jar", "/app/app.jar"]

