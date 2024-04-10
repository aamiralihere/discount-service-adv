# Use the official OpenJDK 17 image as a base image
FROM openjdk:17-jdk-alpine
# Working directory inside the container
WORKDIR /app
# Copy the jar file into the container in /app
COPY target/discount-service-0.0.1.jar /app/discount-service-0.0.1.jar
# Port the app listens on
EXPOSE 8080
# Command to run the application
CMD ["java", "-jar", "/app/discount-service-0.0.1.jar"]