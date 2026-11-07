package edu.ape3.microservicio3.controlador;

import edu.ape3.microservicio3.modelo.Almacen;
import java.util.Map;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controlador de la fabrica.
 *
 * Ruta base: {@code /api/fabrica}
 *
 * Endpoints:
 * <ul>
 *   <li>{@code POST /producir} - produce una pieza (param opcional {@code pieza}).</li>
 *   <li>{@code POST /consumir} - consume una pieza del almacen.</li>
 *   <li>{@code GET /estado} - devuelve el nivel de llenado del almacen.</li>
 * </ul>
 */
@RestController
@RequestMapping("/api/fabrica")
@CrossOrigin(origins = "*")
public class FabricaController {

    private final Almacen almacen = Almacen.getInstance();

    /**
     * Produce una pieza en el almacen.
     *
     * @param pieza nombre de la pieza (por defecto "Pieza Generica").
     * @return JSON {@code {"mensaje": "Pieza producida: {nombre}"}}
     */
    @PostMapping("/producir")
    public Map<String, String> producir(
            @RequestParam(defaultValue = "Pieza Generica") String pieza) {
        try {
            almacen.producir(pieza);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return Map.of("mensaje", "Error al producir la pieza");
        }
        return Map.of("mensaje", "Pieza producida: " + pieza);
    }

    /**
     * Consume una pieza del almacen.
     *
     * @return JSON {@code {"mensaje": "Pieza consumida: {nombre}"}}
     */
    @PostMapping("/consumir")
    public Map<String, String> consumir() {
        try {
            String pieza = almacen.consumir();
            return Map.of("mensaje", "Pieza consumida: " + pieza);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return Map.of("mensaje", "Error al consumir la pieza");
        }
    }

    /**
     * Devuelve el estado actual del almacen.
     *
     * @return JSON {@code {"cantidadActual": X, "capacidadMaxima": 5}}
     */
    @GetMapping("/estado")
    public Map<String, Object> estado() {
        return Map.of(
                "cantidadActual", almacen.getCantidadActual(),
                "capacidadMaxima", almacen.getCapacidadMaxima()
        );
    }
}
