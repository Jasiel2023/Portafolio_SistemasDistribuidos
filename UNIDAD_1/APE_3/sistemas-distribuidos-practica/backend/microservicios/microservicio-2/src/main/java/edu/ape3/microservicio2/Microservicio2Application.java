package edu.ape3.microservicio2;

import java.util.Map;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
public class Microservicio2Application {
    public static void main(String[] args) { SpringApplication.run(Microservicio2Application.class, args); }
}

@RestController
@CrossOrigin(origins = "*")
class Microservicio2Controller {
    @RequestMapping("/health")
    Map<String, String> health() { return Map.of("service", "microservicio-2", "status", "ok", "exercise", "Sincronizacion y coordinacion"); }

    @RequestMapping({"/api", "/api/**"})
    Map<String, String> api() { return Map.of("service", "microservicio-2", "exercise", "Sincronizacion y coordinacion", "message", "Respuesta del microservicio 2"); }
}
