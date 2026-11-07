package edu.ape3.microservicio5.modelo;

public enum Etapa {
    NINGUNA,
    DISENO,
    REVISION,
    IMPRESION;

    public Etapa siguiente() {
        return switch (this) {
            case DISENO -> REVISION;
            case REVISION -> IMPRESION;
            case IMPRESION -> DISENO;
            case NINGUNA -> DISENO;
        };
    }

    public String etiqueta() {
        return switch (this) {
            case DISENO -> "Diseño";
            case REVISION -> "Revisión";
            case IMPRESION -> "Impresión";
            case NINGUNA -> "Ninguna";
        };
    }
}
