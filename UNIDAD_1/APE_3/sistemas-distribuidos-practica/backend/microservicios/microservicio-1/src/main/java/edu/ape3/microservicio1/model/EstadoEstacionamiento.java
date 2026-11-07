package edu.ape3.microservicio1.model;

import java.util.List;

public class EstadoEstacionamiento {
    private int capacidad;
    private int espaciosOcupados;
    private int espaciosDisponibles;
    private List<Integer> vehiculosDentro;
    private List<Integer> vehiculosEsperando;
    private String mensaje;
    private boolean operacionExitosa;

    public EstadoEstacionamiento() {
    }

    public EstadoEstacionamiento(int capacidad, int espaciosOcupados, int espaciosDisponibles,
            List<Integer> vehiculosDentro, List<Integer> vehiculosEsperando,
            String mensaje, boolean operacionExitosa) {
        this.capacidad = capacidad;
        this.espaciosOcupados = espaciosOcupados;
        this.espaciosDisponibles = espaciosDisponibles;
        this.vehiculosDentro = vehiculosDentro;
        this.vehiculosEsperando = vehiculosEsperando;
        this.mensaje = mensaje;
        this.operacionExitosa = operacionExitosa;
    }

    public int getCapacidad() {
        return capacidad;
    }

    public void setCapacidad(int capacidad) {
        this.capacidad = capacidad;
    }

    public int getEspaciosOcupados() {
        return espaciosOcupados;
    }

    public void setEspaciosOcupados(int espaciosOcupados) {
        this.espaciosOcupados = espaciosOcupados;
    }

    public int getEspaciosDisponibles() {
        return espaciosDisponibles;
    }

    public void setEspaciosDisponibles(int espaciosDisponibles) {
        this.espaciosDisponibles = espaciosDisponibles;
    }

    public List<Integer> getVehiculosDentro() {
        return vehiculosDentro;
    }

    public void setVehiculosDentro(List<Integer> vehiculosDentro) {
        this.vehiculosDentro = vehiculosDentro;
    }

    public List<Integer> getVehiculosEsperando() {
        return vehiculosEsperando;
    }

    public void setVehiculosEsperando(List<Integer> vehiculosEsperando) {
        this.vehiculosEsperando = vehiculosEsperando;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public boolean isOperacionExitosa() {
        return operacionExitosa;
    }

    public void setOperacionExitosa(boolean operacionExitosa) {
        this.operacionExitosa = operacionExitosa;
    }
}
