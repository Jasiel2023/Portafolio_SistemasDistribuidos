# ==========================================
# Dockerfile - Microservicio 3 (Fabrica / Productor-Consumidor)
# ==========================================

# ---------------------------------------------------------------------------
# Etapa 1: Build (compilacion) con Maven y JDK 17
# ---------------------------------------------------------------------------
FROM maven:3.9.9-eclipse-temurin-17 AS build
WORKDIR /app

# Copia solo el POM primero para aprovechar el cache de capas de dependencias.
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copia el codigo fuente y compila/empaqueta la aplicacion.
COPY src ./src
RUN mvn package -DskipTests -B

# ---------------------------------------------------------------------------
# Etapa 2: Runtime con JRE ligero de Java 17 (Alpine)
# ---------------------------------------------------------------------------
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# Copia el JAR empaquetado desde la etapa de compilacion.
COPY --from=build /app/target/microservicio-3-1.0.0.jar app.jar

# Puerto configurado en src/main/resources/application.properties (server.port=3003).
EXPOSE 3003

ENTRYPOINT ["java", "-jar", "app.jar"]
