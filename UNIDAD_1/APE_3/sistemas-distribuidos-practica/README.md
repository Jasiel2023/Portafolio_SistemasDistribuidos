# APE 3 - Sistemas Distribuidos

Monorepo académico con un frontend React, un API Gateway y cinco microservicios Spring Boot.

## Estructura

```text
frontend/                 Plantilla React de la práctica
backend/gateway/          API Gateway Spring Boot
backend/microservicios/   Cinco proyectos Spring Boot independientes
docker-compose.yml        Orquestación de todo el sistema
```

## Tecnologías

- Frontend: React, Vite y Node.js.
- Backend: Java 17, Spring Boot 3.4 y Spring Web.
- Infraestructura: Docker Compose.

## Ejecutar en Visual Studio Code

1. Instala Java 17, Maven y Node.js LTS.
2. Abre esta carpeta en Visual Studio Code.
3. Compila el backend:

```bash
mvn -f backend/gateway/pom.xml package
mvn -f backend/microservicios/microservicio-1/pom.xml package
mvn -f backend/microservicios/microservicio-2/pom.xml package
mvn -f backend/microservicios/microservicio-3/pom.xml package
mvn -f backend/microservicios/microservicio-4/pom.xml package
mvn -f backend/microservicios/microservicio-5/pom.xml package
```

4. Instala las dependencias del frontend y ejecútalo:

```bash
npm --prefix frontend install
npm --prefix frontend run dev
```

5. En otras terminales ejecuta el gateway y los servicios Spring Boot:

```bash
java -jar backend/microservicios/microservicio-1/target/microservicio-1-1.0.0.jar
java -jar backend/gateway/target/gateway-1.0.0.jar
```

Para levantar los cinco servicios con sus valores completos se recomienda Docker Compose.

6. Abre `http://localhost:5173`. El Gateway queda disponible en `http://localhost:3000`.

## Rutas de integración

```text
GET http://localhost:3000/health
GET http://localhost:3000/api/services
GET http://localhost:3000/api/microservicio-1
GET http://localhost:3000/api/microservicio-2
GET http://localhost:3000/api/microservicio-3
GET http://localhost:3000/api/microservicio-4
GET http://localhost:3000/api/microservicio-5
```

## Ejecución con Docker

Requiere Docker Desktop instalado:

```bash
docker compose up --build
```

El frontend estará en `http://localhost:5173`.

## Personalizar la guía

Edita `frontend/src/data.js` para reemplazar los textos `TODO` por el contenido real de la práctica.

Las respuestas del estudiante se guardan automáticamente en `localStorage`. El botón **Imprimir / PDF** permite imprimir la pestaña actual o guardarla como PDF desde el diálogo del navegador.

El backend está implementado con Java 17, Spring Boot 3.4 y Spring Web. El gateway publica los microservicios mediante `/api/microservicio-N`.

## Estado del repositorio

No se deben subir `node_modules`, `dist`, `target` ni archivos `.env`. Estas rutas ya están excluidas mediante `.gitignore`.
