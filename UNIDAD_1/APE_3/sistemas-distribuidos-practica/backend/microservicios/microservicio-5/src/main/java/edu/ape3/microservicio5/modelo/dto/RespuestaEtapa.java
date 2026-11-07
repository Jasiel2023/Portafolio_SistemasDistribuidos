package edu.ape3.microservicio5.modelo.dto;

public record RespuestaEtapa(
        String documento,
        String etapa,
        int documentosDisenados,
        int documentosRevisados,
        int documentosImpresos,
        long esperoMs,
        String mensaje) {
}
