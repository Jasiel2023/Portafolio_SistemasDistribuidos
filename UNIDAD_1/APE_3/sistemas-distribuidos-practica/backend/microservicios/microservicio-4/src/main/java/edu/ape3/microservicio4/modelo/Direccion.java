package edu.ape3.microservicio4.modelo;

public enum Direccion {
    NINGUNA,
    NORTE,
    SUR;

    public Direccion opuesta() {
        return switch (this) {
            case NORTE -> SUR;
            case SUR -> NORTE;
            case NINGUNA -> NINGUNA;
        };
    }
}
