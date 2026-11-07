package edu.ape3.microservicio1.model;

public class ResultadoVehiculo {
    private int id;
    private long milisegundosEsperados;
    private boolean completado;

    public ResultadoVehiculo() {
    }

    public ResultadoVehiculo(int id, long milisegundosEsperados, boolean completado) {
        this.id = id;
        this.milisegundosEsperados = milisegundosEsperados;
        this.completado = completado;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getMilisegundosEsperados() {
        return milisegundosEsperados;
    }

    public void setMilisegundosEsperados(long milisegundosEsperados) {
        this.milisegundosEsperados = milisegundosEsperados;
    }

    public boolean isCompletado() {
        return completado;
    }

    public void setCompletado(boolean completado) {
        this.completado = completado;
    }
}
