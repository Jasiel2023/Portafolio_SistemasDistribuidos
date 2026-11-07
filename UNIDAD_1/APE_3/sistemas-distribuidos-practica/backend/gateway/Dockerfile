FROM maven:3.9.9-eclipse-temurin-17 AS build
WORKDIR /app
COPY backend/gateway/pom.xml .
RUN mvn dependency:go-offline -B
COPY backend/gateway/src ./src
RUN mvn package -DskipTests -B

FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=build /app/target/gateway-1.0.0.jar app.jar
EXPOSE 3000
ENTRYPOINT ["java", "-jar", "app.jar"]
