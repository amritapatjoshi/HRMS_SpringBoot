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







# # Use an OpenJDK base image
# FROM openjdk:11-jdk-slim

# # Set the working directory inside the container
# WORKDIR /app

# # Install Maven
# RUN apt-get update && \
#     apt-get install -y maven
 
# # Copy the Maven project file
# COPY pom.xml /app

# # Copy the source code
# COPY src /app/src

# # Build the application
# RUN mvn package

# # Expose the port the Spring Boot app runs on
# EXPOSE 8080

# # Run the JAR file
# CMD ["java", "-jar", "target/my-spring-boot-app.jar"]






#Stage 1
# initialize build and set base image for first stage
FROM maven:3.6.3-adoptopenjdk-11 as stage1
# speed up Maven JVM a bit
ENV MAVEN_OPTS="-XX:+TieredCompilation -XX:TieredStopAtLevel=1"
# set working directory
WORKDIR /opt/demo
# copy just pom.xml
COPY pom.xml .
# go-offline using the pom.xml
RUN mvn dependency:go-offline
# copy your other files
COPY ./src ./src
# compile the source code and package it in a jar file
RUN mvn clean install -Dmaven.test.skip=true
#Stage 2
# set base image for second stage
FROM adoptopenjdk/openjdk11:jre-11.0.9_11-alpine
# set deployment directory
WORKDIR /opt/demo
# copy over the built artifact from the maven image
COPY --from=stage1 /opt/demo/target/demo.jar /opt/demo
# expose the port your app runs on
EXPOSE 8080
# specify the command to run your application
CMD ["java", "-jar", "demo.jar"]

