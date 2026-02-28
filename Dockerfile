# Stage 1: Build - dung Maven de compile va dong goi ung dung
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app

# Copy pom.xml truoc de tan dung layer cache (chi re-download khi pom.xml thay doi)
COPY pom.xml .
RUN mvn dependency:go-offline -q

# Copy source code va build
COPY src ./src
RUN mvn clean package -DskipTests -q

# Stage 2: Run - chi dung JRE (nhe hon JDK)
FROM eclipse-temurin:17-jre
WORKDIR /app

COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
