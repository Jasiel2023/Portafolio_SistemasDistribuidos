package edu.ape3.gateway;

import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClient;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@CrossOrigin(origins = "*")
public class GatewayController {
    private final RestClient restClient = RestClient.create();
    private final Map<Integer, String> services = new LinkedHashMap<>();

    public GatewayController(
            @Value("${service.1.url}") String service1,
            @Value("${service.2.url}") String service2,
            @Value("${service.3.url}") String service3,
            @Value("${service.4.url}") String service4,
            @Value("${service.5.url}") String service5) {
        services.put(1, service1);
        services.put(2, service2);
        services.put(3, service3);
        services.put(4, service4);
        services.put(5, service5);
    }

    @RequestMapping("/health")
    public Map<String, String> health() {
        return Map.of("service", "gateway", "status", "ok");
    }

    @RequestMapping("/api/services")
    public Map<String, Object> serviceList() {
        return Map.of("gateway", "ok", "services", services);
    }

    @RequestMapping(value = "/api/microservicio-{number}", method = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.PATCH, RequestMethod.DELETE})
    public ResponseEntity<String> proxyRoot(@PathVariable int number, @RequestBody(required = false) String body, HttpMethod method) {
        return forward(number, "/api", body, method);
    }

    @RequestMapping(value = "/api/microservicio-{number}/{path:\\S+}", method = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.PATCH, RequestMethod.DELETE})
    public ResponseEntity<String> proxy(@PathVariable int number, @PathVariable String path, @RequestBody(required = false) String body, HttpMethod method, HttpServletRequest request) {
        if (!services.containsKey(number)) {
            return ResponseEntity.notFound().build();
        }
        String queryString = request.getQueryString();
        String targetPath = "health".equals(path)
                ? "/health"
                : "/api/" + path;
        if (queryString != null) {
            targetPath += "?" + queryString;
        }
        return forward(number, targetPath, body, method);
    }

    @RequestMapping(value = "/api/microservicio-3/fabrica/**", method = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE})
    public ResponseEntity<String> proxyFabrica(@RequestBody(required = false) String body, HttpMethod method, HttpServletRequest request) {
        String fullPath = request.getRequestURI();
        String targetPath = fullPath.replace("/microservicio-3", "");
        String queryString = request.getQueryString();
        String targetUrl = services.get(3) + targetPath + (queryString != null ? "?" + queryString : "");
        return restClient.method(method).uri(targetUrl).body(body != null ? body : "").retrieve().toEntity(String.class);
    }

    @RequestMapping(value = "/api/microservicio-{number}/estacionamiento/estado", method = RequestMethod.GET)
    public ResponseEntity<String> parkingState(@PathVariable int number) {
        return forward(number, "/api/estacionamiento/estado", null, HttpMethod.GET);
    }

    @RequestMapping(value = "/api/microservicio-2/simulacion/ejecutar", method = RequestMethod.GET)
    public ResponseEntity<String> accountSimulation() {
        return forward(2, "/api/simulacion/ejecutar", null, HttpMethod.GET);
    }

    @RequestMapping(value = "/api/microservicio-{number}/estacionamiento/llegar/{id}", method = RequestMethod.POST)
    public ResponseEntity<String> vehicleArrival(@PathVariable int number, @PathVariable int id) {
        return forward(number, "/api/estacionamiento/llegar/" + id, null, HttpMethod.POST);
    }

    @RequestMapping(value = "/api/microservicio-{number}/estacionamiento/salir/{id}", method = RequestMethod.POST)
    public ResponseEntity<String> vehicleExit(@PathVariable int number, @PathVariable int id) {
        return forward(number, "/api/estacionamiento/salir/" + id, null, HttpMethod.POST);
    }

    @RequestMapping(value = "/api/microservicio-{number}/estacionamiento/reiniciar", method = RequestMethod.POST)
    public ResponseEntity<String> resetParking(@PathVariable int number) {
        return forward(number, "/api/estacionamiento/reiniciar", null, HttpMethod.POST);
    }

    private ResponseEntity<String> forward(int number, String path, String body, HttpMethod method) {
        try {
            var request = restClient.method(method).uri(services.get(number) + path);
            if (body == null || body.isBlank()) {
                return request.retrieve().toEntity(String.class);
            }
            return request.header("Content-Type", "application/json").body(body).retrieve().toEntity(String.class);
        } catch (Exception exception) {
            return ResponseEntity.status(502).body("{\"error\":\"Microservicio no disponible\",\"detail\":\"" + escape(exception.getMessage()) + "\"}");
        }
    }

    private String escape(String value) {
        return value == null ? "" : value.replace("\\", "\\\\").replace("\"", "\\\"");
    }
}
