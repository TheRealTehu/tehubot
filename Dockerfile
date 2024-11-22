FROM maven:3.9.6-eclipse-temurin-21-alpine AS build-phase
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

FROM eclipse-temurin:21-jre-jammy
WORKDIR /app
COPY --from=build-phase /app/target/*.jar discord_bot.jar
ENTRYPOINT ["java", "-jar", "discord_bot.jar"]