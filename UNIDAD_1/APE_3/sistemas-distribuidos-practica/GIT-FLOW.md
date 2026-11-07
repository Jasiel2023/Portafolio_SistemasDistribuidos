# Flujo de trabajo Git

## Ramas principales

- `main`: versión estable y entregable.
- `develop`: integración del trabajo del equipo.
- `feature/*`: desarrollo de una funcionalidad o ejercicio.
- `release/*`: preparación de una entrega.
- `hotfix/*`: corrección urgente sobre `main`.

## Inicio para cada compañero

```bash
git clone https://github.com/WagnerBalcazar/APE3-SISTEMAS-DISTRIBUIDOS.git
cd APE3-SISTEMAS-DISTRIBUIDOS/sistemas-distribuidos-practica
git switch develop
git pull origin develop
```

## Trabajar en un ejercicio

Cada compañero debe usar una rama propia basada en `develop`:

```bash
git switch -c feature/ejercicio-1-comunicacion
```

Nombres sugeridos:

```text
feature/ejercicio-1-comunicacion
feature/ejercicio-2-sincronizacion
feature/ejercicio-3-tolerancia-fallos
feature/ejercicio-4-consistencia
feature/ejercicio-5-integrador
```

Guardar cambios frecuentemente con commits pequeños:

```bash
git add .
git commit -m "Implementar ejercicio 1 de comunicacion"
git push -u origin feature/ejercicio-1-comunicacion
```

Después se debe abrir un Pull Request desde la rama `feature/*` hacia `develop`. No se debe trabajar directamente sobre `main` ni `develop`.

## Actualizar una rama de trabajo

Antes de continuar, incorporar los cambios recientes de `develop`:

```bash
git switch develop
git pull origin develop
git switch feature/mi-ejercicio
git merge develop
```

Si aparecen conflictos, resolverlos, probar el proyecto y completar el merge:

```bash
git add .
git commit
git push
```

## Entrega

1. Las ramas `feature/*` se integran mediante Pull Request a `develop`.
2. El equipo prueba la integración en `develop`.
3. Para entregar, se crea un Pull Request de `develop` hacia `main`.
4. Después de aprobarlo, se etiqueta la versión estable.

```bash
git switch main
git pull origin main
git tag v1.0.0
git push origin v1.0.0
```

## Reglas del equipo

- Un ejercicio o tarea por rama.
- No subir `node_modules`, `target`, `dist` ni archivos `.env`.
- Ejecutar el build antes de crear un Pull Request.
- Escribir mensajes de commit claros y breves.
- Revisar el código de otro compañero antes de integrar.
