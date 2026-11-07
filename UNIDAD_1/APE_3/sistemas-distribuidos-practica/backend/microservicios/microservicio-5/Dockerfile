FROM maven:3.9.9-eclipse-temurin-17 AS build
WORKDIR /app
COPY backend/microservicios/microservicio-5/pom.xml .
RUN mvn dependency:go-offline -B
COPY backend/microservicios/microservicio-5/src ./src
RUN mvn package -DskipTests -B

FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=build /app/target/microservicio-5-1.0.0.jar app.jar
EXPOSE 3005
ENTRYPOINT ["java", "-jar", "app.jar"]
