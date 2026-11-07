package edu.ape3.microservicio4.controlador;

import edu.ape3.microservicio4.modelo.Direccion;
import edu.ape3.microservicio4.modelo.EstadoPuente;
import edu.ape3.microservicio4.modelo.dto.RespuestaVehiculo;
import edu.ape3.microservicio4.modelo.dto.SolicitudEquidad;
import edu.ape3.microservicio4.modelo.dto.SolicitudVehiculo;
import edu.ape3.microservicio4.servicio.PuenteServicio;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "*")
public class Microservicio4Controller {

    private static final int DEMORA_POR_DEFECTO_MS = 500;
    private static final int DEMORA_MAXIMA_MS = 10000;
    private static final AtomicLong SECUENCIA = new AtomicLong();

    private final PuenteServicio puente;

    public Microservicio4Controller(PuenteServicio puente) {
        this.puente = puente;
    }

    @RequestMapping("/health")
    Map<String, String> health() {
        return Map.of("service", "microservicio-4", "status", "ok", "exercise", "Consistencia de datos");
    }

    @RequestMapping({"/api", "/api/**"})
    Map<String, String> api() {
        return Map.of(
                "service", "microservicio-4",
                "exercise", "Consistencia de datos",
                "message", "Respuesta del microservicio 4");
    }

    @PostMapping({"/norte", "/api/norte"})
    RespuestaVehiculo vehiculoNorte(@RequestBody(required = false) SolicitudVehiculo solicitud) {
        return cruzar(Direccion.NORTE, solicitud);
    }

    @PostMapping({"/sur", "/api/sur"})
    RespuestaVehiculo vehiculoSur(@RequestBody(required = false) SolicitudVehiculo solicitud) {
        return cruzar(Direccion.SUR, solicitud);
    }

    @GetMapping({"/estado", "/api/estado"})
    EstadoPuente estado() {
        return puente.estado();
    }

    @PostMapping({"/equidad", "/api/equidad"})
    EstadoPuente equidad(@RequestBody(required = false) SolicitudEquidad solicitud) {
        puente.setEquidad(solicitud != null && Boolean.TRUE.equals(solicitud.activa()));
        return puente.estado();
    }

    @PostMapping({"/reset", "/api/reset"})
    EstadoPuente reset() {
        puente.reset();
        return puente.estado();
    }

    private RespuestaVehiculo cruzar(Direccion direccion, SolicitudVehiculo solicitud) {
        String id = (solicitud != null && solicitud.id() != null && !solicitud.id().isBlank())
                ? solicitud.id()
                : generarId(direccion);
        int demora = normalizarDemora(solicitud);
        long espero = puente.entrar(direccion);
        puente.cruzar(demora);
        puente.salir(direccion);
        EstadoPuente estado = puente.estado();
        return new RespuestaVehiculo(
                id,
                direccion.name(),
                estado.vehiculosNorte(),
                estado.vehiculosSur(),
                espero,
                "Vehículo " + id + " cruza hacia el " + direccion.name());
    }

    private int normalizarDemora(SolicitudVehiculo solicitud) {
        if (solicitud == null || solicitud.demoraMs() == null) {
            return DEMORA_POR_DEFECTO_MS;
        }
        int demora = solicitud.demoraMs();
        if (demora < 0) {
            return 0;
        }
        return Math.min(demora, DEMORA_MAXIMA_MS);
    }

    private String generarId(Direccion direccion) {
        return "auto-" + direccion.name().toLowerCase() + "-" + SECUENCIA.incrementAndGet();
    }
}
