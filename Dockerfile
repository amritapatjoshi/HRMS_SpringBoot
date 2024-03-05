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


# Use a base image with JDK, Maven, and JRE installed
FROM maven:3.8.4-openjdk-11 AS build

# Set working directory
WORKDIR /app

# Copy the Maven project file
COPY pom.xml .

# Download dependencies
RUN mvn dependency:go-offline -B

# Copy the application source code
COPY src ./src

# Build the application
RUN mvn package -DskipTests

# Use a lighter weight base image for runtime
FROM openjdk:11-jre-slim

# Set working directory for runtime
WORKDIR /app

# Copy the JAR file from the build stage
COPY --from=build /app/target/*.jar ./app.jar

# Expose the port
EXPOSE 8080

# Command to run the application
CMD ["java", "-jar", "app.jar"]
