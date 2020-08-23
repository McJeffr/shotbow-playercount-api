### Builder stage
FROM maven:3.6.3-jdk-11 AS builder

# Maven options to perhaps im
ENV MAVEN_OPTS="-XX:+TieredCompilation -XX:TieredStopAtLevel=1"

# Choose a directory to work in
WORKDIR /usr/src/app

# Dependency layer (changes less often)
COPY pom.xml /usr/src/app/
RUN mvn clean verify --fail-never

# Add rest of the code and build
COPY . /usr/src/app
RUN mvn -DskipTests package

### Deploy stage
FROM openjdk:11-jre-slim AS runner

# Get the created jar from the builder
COPY --from=builder /usr/src/app/target/*.jar app.jar

# Port on which the service will be available
EXPOSE 8080

# Java command to run the jar
ENTRYPOINT ["java", "-jar", "app.jar"]
