# Use the official OpenJDK 21 runtime as a parent image
FROM openjdk:21-jdk-slim

# Set the working directory in the container
WORKDIR /app

# Copy the jar file from the host to the container
COPY target/app-0.0.2.jar /app/wessol_backend.jar

# Expose the application port
EXPOSE 3000

# Run the jar file
ENTRYPOINT ["java", "-jar", "/app/wessol_backend.jar"]