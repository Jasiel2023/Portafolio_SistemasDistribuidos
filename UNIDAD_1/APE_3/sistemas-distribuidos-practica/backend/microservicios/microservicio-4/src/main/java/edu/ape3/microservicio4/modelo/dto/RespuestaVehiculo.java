package edu.ape3.microservicio4.modelo.dto;

public record RespuestaVehiculo(
        String vehiculo,
        String direccion,
        int vehiculosNorte,
        int vehiculosSur,
        long esperoMs,
        String mensaje) {
}
