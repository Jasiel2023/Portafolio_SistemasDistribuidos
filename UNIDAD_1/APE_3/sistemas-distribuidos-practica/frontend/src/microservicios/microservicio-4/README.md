# Microservicio 4

Esta carpeta contiene la interfaz de consistencia de datos: el cruce de vehículos en
un puente de una sola dirección activa.

## Componente

- `PuentePanel.jsx`: panel en vivo con la dirección actual, los contadores de vehículos
  (NORTE y SUR), la cola de espera, el control de equidad (anti-inanición) y los botones
  para enviar vehículos, ráfagas, activar/desactivar equidad y reiniciar.
- `puenteApi.js`: llamadas al Gateway para este microservicio.

## API

Las peticiones van al Gateway en la ruta base:

```text
/api/microservicio-4
```

- `GET  /estado`
- `POST /norte`  `{ id, demoraMs }`
- `POST /sur`    `{ id, demoraMs }`
- `POST /equidad` `{ activa }`
- `POST /reset`

El panel se muestra en la pestaña **Ejercicio 4** y se engancha desde `src/App.jsx`.
Se reutilizan las clases CSS existentes; no se modifican los estilos globales.
