# 1단계: 빌드
FROM gradle:8.12.1-jdk17 AS build
WORKDIR /app
COPY build.gradle settings.gradle /app/
COPY src/main/java ./src/main/java
COPY src/main/resources /app/src/main/resources
RUN gradle clean build -x test

# 2단계: 런타임
FROM eclipse-temurin:17-jdk
WORKDIR /app
COPY --from=build /app/build/libs/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","app.jar"]
