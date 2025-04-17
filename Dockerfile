# Stage 1: Build
FROM maven:3.8.6-openjdk-21 AS build
WORKDIR /app
COPY pom.xml ./
COPY src ./src
RUN mvn clean package -DskipTests

# Stage 2: Run
FROM openjdk:21-jre-slim
WORKDIR /app
COPY --from=build /app/target/bank-transactions-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
CMD ["java", "-jar", "app.jar"]