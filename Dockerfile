# Stage 1: Build the application
FROM maven:3.9.1 AS build

# Set the working directory inside the container
WORKDIR /app

# Copy the pom.xml and install dependencies
COPY pom.xml .
COPY src ./src

# Run Maven to clean and build the project
RUN mvn clean install -DskipTests

# Stage 2: Create the final image
FROM openjdk:17-jdk-slim

# Set the working directory in the new image
WORKDIR /app

# Copy the jar file from the build stage
COPY --from=build /app/target/*.jar app.jar

# Expose the application port (change if needed)
EXPOSE 8080

# Command to run the application
CMD ["java", "-jar", "app.jar"]