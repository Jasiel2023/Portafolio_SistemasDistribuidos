package edu.ape3.microservicio3;

import java.util.Map;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
public class Microservicio3Application {
    public static void main(String[] args) { SpringApplication.run(Microservicio3Application.class, args); }
}

@RestController
@CrossOrigin(origins = "*")
class Microservicio3Controller {
    @RequestMapping("/health")
    Map<String, String> health() { return Map.of("service", "microservicio-3", "status", "ok", "exercise", "Tolerancia a fallos"); }

    @RequestMapping({"/api", "/api/**"})
    Map<String, String> api() { return Map.of("service", "microservicio-3", "exercise", "Tolerancia a fallos", "message", "Respuesta del microservicio 3"); }
}
