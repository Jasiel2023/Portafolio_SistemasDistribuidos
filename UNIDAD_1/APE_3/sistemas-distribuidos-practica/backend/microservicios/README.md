# Microservicios

Cada carpeta es un proyecto Spring Boot independiente y corresponde a un ejercicio de la práctica.

| Proyecto | Puerto | Ejercicio |
|---|---:|---|
| `microservicio-1` | 3001 | Comunicación entre procesos |
| `microservicio-2` | 3002 | Sincronización y coordinación |
| `microservicio-3` | 3003 | Tolerancia a fallos |
| `microservicio-4` | 3004 | Consistencia de datos |
| `microservicio-5` | 3005 | Caso integrador |

Cada compañero puede trabajar en su propia carpeta y rama `feature/*`. Todos exponen `/health` y `/api`; el gateway los publica mediante `/api/microservicio-N`.
