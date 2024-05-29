# Base image with JDK and Gradle
FROM gradle:7.5.0-jdk17 as build

# Set working directory
WORKDIR /home/rating-service

# Copy build files
COPY build.gradle settings.gradle /home/rating-service/
COPY src /home/rating-service/src

# Build the project
RUN gradle build --no-daemon

# Using OpenJDK 17 for the runtime
FROM openjdk:17-slim

ARG VERSION=0.0.1-SNAPSHOT
ARG SERVICE_NAME="rating-service"

# Set the deployment directory
WORKDIR /app

# Copy the built JAR from the previous stage
COPY --from=build /home/rating-service/build/libs/$SERVICE_NAME-$VERSION.jar /app/app.jar

# Expose the port
EXPOSE 8083

# Define the entry point
ENTRYPOINT ["java", "-Xms256m", "-Xmx3072m", "-jar", "app.jar"]