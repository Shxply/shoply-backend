# Use an official BellSoft Liberica runtime as a parent image
FROM bellsoft/liberica-openjdk-alpine:17

# Set the working directory inside the container
WORKDIR /app

# Copy the built JAR into the container
COPY target/shoply-backend-0.0.1-SNAPSHOT.jar app.jar

# Expose the default Heroku port
EXPOSE 8080

# Run the jar file (relying on Heroku's PORT environment variable)
ENTRYPOINT ["java", "-Dserver.port=${PORT:-8080}", "-jar", "/app/app.jar"]
