# Stage 1: Build the application with Maven and OpenJDK 17
FROM amd64/maven:3.8.1-openjdk-17 AS build

# Set the working directory in the Docker image filesystem.
WORKDIR /app

# Copy the pom.xml file into the image to cache dependencies.
COPY pom.xml .

# Download all required dependencies into one layer.
RUN mvn dependency:go-offline -B

# Copy the project source code into the image.
COPY src ./src

# Package the application without running tests for speed.
RUN mvn package -DskipTests

# Stage 2: Create a minimal runtime image with OpenJDK 17
FROM amd64/openjdk:17-slim

# Copy the built jar file from the build stage into this image.
COPY --from=build /app/target/*.jar /app/app.jar

# Set the command to run the application.
CMD ["java", "-jar", "/app/app.jar"]
