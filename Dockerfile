# Build stage
FROM maven:3.8.6-openjdk-17-slim AS build
WORKDIR /app
COPY . .
RUN chmod +x mvnw
RUN ./mvnw package -Pprod -DskipTests

# Run stage
FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
