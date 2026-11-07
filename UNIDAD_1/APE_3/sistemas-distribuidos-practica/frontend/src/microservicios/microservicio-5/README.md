# Microservicio 5

Interfaz del caso integrador: **Centro de impresión (sincronización de procesos)**.

- `centroApi.js`: cliente REST contra `/api/microservicio-5` (Gateway).
- `CentroImpresionPanel.jsx`: panel que muestra las tres etapas (Diseño → Revisión →
  Impresión) y habilita cada botón solo cuando el semáforo de turno lo permite.

El panel reutiliza las clases visuales existentes y se expone en la pestaña del
ejercicio 5 (índice `exercise-4`) desde `App.jsx`.
