FROM maven:3.9.8-eclipse-temurin-17-focal AS build

WORKDIR /app

COPY pom.xml .

COPY src ./src

RUN mvn clean package -DskipTests

FROM openjdk:17-slim

WORKDIR /app

ARG JAR_FILE=/app/target/loan-management-api-1.0.0.jar
COPY --from=build ${JAR_FILE} ./app.jar

COPY .env ./

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
