# Giai đoạn 1: Build ứng dụng bằng Maven và Java 21
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app
COPY pom.xml .
COPY .mvn .mvn
COPY mvnw .
COPY src ./src
RUN mvn clean package -DskipTests

# Giai đoạn 2: Tạo image cuối cùng chỉ chứa JRE và file .jar
FROM eclipse-temurin:21-jre-jammy
WORKDIR /app
COPY --from=build /app/target/bikestore-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
CMD ["java", "-jar", "app.jar"]
