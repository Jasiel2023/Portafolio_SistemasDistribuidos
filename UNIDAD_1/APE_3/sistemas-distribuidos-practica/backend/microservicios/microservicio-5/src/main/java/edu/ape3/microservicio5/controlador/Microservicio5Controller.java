package edu.ape3.microservicio5.controlador;

import edu.ape3.microservicio5.modelo.EstadoCentro;
import edu.ape3.microservicio5.modelo.dto.RespuestaEtapa;
import edu.ape3.microservicio5.modelo.dto.SolicitudCiclos;
import edu.ape3.microservicio5.modelo.dto.SolicitudEtapa;
import edu.ape3.microservicio5.servicio.CentroImpresionServicio;
import java.util.Map;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "*")
public class Microservicio5Controller {

    private static final int DEMORA_POR_DEFECTO_MS = 300;
    private static final int DEMORA_MAXIMA_MS = 10000;
    private static final int CICLOS_POR_DEFECTO = 3;

    private final CentroImpresionServicio centro;

    public Microservicio5Controller(CentroImpresionServicio centro) {
        this.centro = centro;
    }

    @RequestMapping("/health")
    Map<String, String> health() {
        return Map.of("service", "microservicio-5", "status", "ok", "exercise", "Caso integrador");
    }

    @RequestMapping({"/api", "/api/**"})
    Map<String, String> api() {
        return Map.of(
                "service", "microservicio-5",
                "exercise", "Caso integrador",
                "message", "Centro de impresión: Diseño → Revisión → Impresión");
    }

    @PostMapping({"/diseno", "/api/diseno"})
    RespuestaEtapa disenar(@RequestBody(required = false) SolicitudEtapa solicitud) {
        return centro.disenar(solicitud, normalizarDemora(solicitud));
    }

    @PostMapping({"/revision", "/api/revision"})
    RespuestaEtapa revisar(@RequestBody(required = false) SolicitudEtapa solicitud) {
        return centro.revisar(solicitud, normalizarDemora(solicitud));
    }

    @PostMapping({"/impresion", "/api/impresion"})
    RespuestaEtapa imprimir(@RequestBody(required = false) SolicitudEtapa solicitud) {
        return centro.imprimir(solicitud, normalizarDemora(solicitud));
    }

    @GetMapping({"/estado", "/api/estado"})
    EstadoCentro estado() {
        return centro.estado();
    }

    @PostMapping({"/ciclos", "/api/ciclos"})
    EstadoCentro ciclos(@RequestBody(required = false) SolicitudCiclos solicitud) {
        int ciclos = (solicitud != null && solicitud.ciclos() != null) ? solicitud.ciclos() : CICLOS_POR_DEFECTO;
        return centro.setCiclos(ciclos);
    }

    @PostMapping({"/reset", "/api/reset"})
    EstadoCentro reset() {
        return centro.reset();
    }

    private int normalizarDemora(SolicitudEtapa solicitud) {
        if (solicitud == null || solicitud.demoraMs() == null) {
            return DEMORA_POR_DEFECTO_MS;
        }
        int demora = solicitud.demoraMs();
        if (demora < 0) {
            return 0;
        }
        return Math.min(demora, DEMORA_MAXIMA_MS);
    }
}
