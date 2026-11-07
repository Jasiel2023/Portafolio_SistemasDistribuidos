package edu.ape3.microservicio5.modelo;

public record EstadoCentro(
        String etapaActual,
        String documentoActual,
        int documentosDisenados,
        int documentosRevisados,
        int documentosImpresos,
        int ciclosTotales,
        boolean completado) {
}
