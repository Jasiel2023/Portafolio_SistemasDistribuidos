# Backend

El backend está compuesto por un API Gateway y cinco proyectos Spring Boot independientes.

## Componentes

- `gateway`: entrada única de la API en el puerto `3000`.
- `microservicios/microservicio-1`: comunicación entre procesos.
- `microservicios/microservicio-2`: sincronización y coordinación.
- `microservicios/microservicio-3`: tolerancia a fallos.
- `microservicios/microservicio-4`: consistencia de datos.
- `microservicios/microservicio-5`: caso integrador.

Cada proyecto tiene su propio `pom.xml`, código fuente, puerto y Dockerfile. Se ejecutan como procesos y contenedores separados.

## Compilar

```bash
mvn -f gateway/pom.xml clean package
mvn -f microservicios/microservicio-1/pom.xml clean package
mvn -f microservicios/microservicio-2/pom.xml clean package
mvn -f microservicios/microservicio-3/pom.xml clean package
mvn -f microservicios/microservicio-4/pom.xml clean package
mvn -f microservicios/microservicio-5/pom.xml clean package
```

## Ejecutar

```bash
java -jar microservicios/microservicio-1/target/microservicio-1-1.0.0.jar
java -jar gateway/target/gateway-1.0.0.jar
```

Para levantar todas las instancias se recomienda ejecutar desde la raíz:

```bash
docker compose up --build
```
