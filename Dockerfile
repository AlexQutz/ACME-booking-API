# Step 1: Use an official OpenJDK base image
FROM openjdk:17-jdk-alpine

# Step 2: Set the working directory inside the container
WORKDIR /app

# Step 3: Copy the application JAR file (target/*.jar for Maven projects) into the container
COPY target/mvp-*.jar /app/mvp.jar

# Step 4: Expose the port that the application will run on
EXPOSE 8080

# Step 5: Set the command to run the application
ENTRYPOINT ["java", "-jar", "mvp.jar"]