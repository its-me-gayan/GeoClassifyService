# Use an official Maven image as the base image
FROM maven:3.9.8-eclipse-temurin-21 AS build
#FROM openjdk:21 AS build
# Set the working directory in the container
WORKDIR /app
# Copy the pom.xml and the project files to the container
COPY pom.xml .
COPY deployment.yaml .
COPY src ./src
# Build the application using Maven
RUN mvn clean package
# Use an official OpenJDK image as the base image
FROM openjdk:21

# Copy the built JAR file from the previous stage to the container
COPY --from=build /app/target/GeoClassifyService-1.0-SNAPSHOT.jar .
WORKDIR /app
EXPOSE 8080

ENTRYPOINT ["java","-jar","/GeoClassifyService-1.0-SNAPSHOT.jar"]
