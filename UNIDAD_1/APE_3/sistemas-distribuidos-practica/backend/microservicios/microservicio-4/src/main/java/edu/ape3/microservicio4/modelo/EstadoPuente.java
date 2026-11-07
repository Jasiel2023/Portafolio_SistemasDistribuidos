package edu.ape3.microservicio4.modelo;

public record EstadoPuente(
        String direccion,
        int vehiculosNorte,
        int vehiculosSur,
        boolean equidadActiva,
        int esperandoNorte,
        int esperandoSur) {
}
