# # Use a base image with JDK, Maven, and JRE installed
# FROM maven:3.8.4-openjdk-11

# # Set working directory
# WORKDIR /app

# # Copy the Maven project file
# COPY pom.xml .

# # Download dependencies
# RUN mvn dependency:go-offline -B

# # Copy the application source code
# COPY src ./src

# # Build the application
# RUN mvn package -DskipTests

# # Set the working directory for the runtime environment
# WORKDIR /app/runtime

# # Copy the JAR file from the build directory
# COPY target/your-application-name.jar ./app.jar

# # Expose the port
# EXPOSE 8080

# # Command to run the application
# CMD ["java", "-jar", "app.jar"]


# Use an OpenJDK base image
FROM openjdk:11-jdk-slim

# Set the working directory inside the container
WORKDIR /app

# Copy the Maven project file
COPY pom.xml /app

# Copy the source code
COPY src /app/src

# Build the application
RUN mvn package

# Expose the port the Spring Boot app runs on
EXPOSE 8080

# Run the JAR file
CMD ["java", "-jar", "target/my-spring-boot-app.jar"]

