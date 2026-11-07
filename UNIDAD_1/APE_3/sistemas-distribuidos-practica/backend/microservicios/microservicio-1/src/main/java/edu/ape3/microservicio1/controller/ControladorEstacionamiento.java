package edu.ape3.microservicio1.controller;

import edu.ape3.microservicio1.model.EstadoEstacionamiento;
import edu.ape3.microservicio1.model.ResultadoSimulacion;
import edu.ape3.microservicio1.service.ServicioSimulacionEstacionamiento;
import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "*")
public class ControladorEstacionamiento {
    private final ServicioSimulacionEstacionamiento servicioSimulacion;

    public ControladorEstacionamiento(ServicioSimulacionEstacionamiento servicioSimulacion) {
        this.servicioSimulacion = servicioSimulacion;
    }

    @GetMapping("/health")
    public Map<String, String> health() {
        return Map.of(
                "service", "microservicio-1",
                "status", "ok",
                "exercise", "Control de acceso a un estacionamiento");
    }

    @GetMapping({"/api", "/api/simular"})
    public ResponseEntity<ResultadoSimulacion> simular() {
        return ResponseEntity.ok(servicioSimulacion.simular(4, 10));
    }

    @GetMapping("/api/estacionamiento/estado")
    public EstadoEstacionamiento estado() {
        return servicioSimulacion.estadoEstacionamiento();
    }

    @PostMapping("/api/estacionamiento/llegar/{id}")
    public EstadoEstacionamiento llegar(@PathVariable int id) {
        return servicioSimulacion.llegarVehiculo(id);
    }

    @PostMapping("/api/estacionamiento/salir/{id}")
    public EstadoEstacionamiento salir(@PathVariable int id) {
        return servicioSimulacion.salirVehiculo(id);
    }

    @PostMapping("/api/estacionamiento/reiniciar")
    public EstadoEstacionamiento reiniciar() {
        return servicioSimulacion.reiniciarEstacionamiento();
    }
}
