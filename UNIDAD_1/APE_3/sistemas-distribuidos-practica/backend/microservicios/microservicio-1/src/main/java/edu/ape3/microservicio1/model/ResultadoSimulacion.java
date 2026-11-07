package edu.ape3.microservicio1.model;

import java.util.List;
import java.util.Map;

public class ResultadoSimulacion {
    private int capacidad;
    private int totalVehiculos;
    private int maximoVehiculosDentro;
    private int vehiculosQueEsperaron;
    private int permisosDisponiblesAlFinal;
    private List<ResultadoVehiculo> vehiculos;
    private List<String> eventos;
    private Map<String, String> analisis;

    public ResultadoSimulacion() {
    }

    public ResultadoSimulacion(int capacidad, int totalVehiculos, int maximoVehiculosDentro,
            int vehiculosQueEsperaron, int permisosDisponiblesAlFinal,
            List<ResultadoVehiculo> vehiculos, List<String> eventos,
            Map<String, String> analisis) {
        this.capacidad = capacidad;
        this.totalVehiculos = totalVehiculos;
        this.maximoVehiculosDentro = maximoVehiculosDentro;
        this.vehiculosQueEsperaron = vehiculosQueEsperaron;
        this.permisosDisponiblesAlFinal = permisosDisponiblesAlFinal;
        this.vehiculos = vehiculos;
        this.eventos = eventos;
        this.analisis = analisis;
    }

    public int getCapacidad() {
        return capacidad;
    }

    public void setCapacidad(int capacidad) {
        this.capacidad = capacidad;
    }

    public int getTotalVehiculos() {
        return totalVehiculos;
    }

    public void setTotalVehiculos(int totalVehiculos) {
        this.totalVehiculos = totalVehiculos;
    }

    public int getMaximoVehiculosDentro() {
        return maximoVehiculosDentro;
    }

    public void setMaximoVehiculosDentro(int maximoVehiculosDentro) {
        this.maximoVehiculosDentro = maximoVehiculosDentro;
    }

    public int getVehiculosQueEsperaron() {
        return vehiculosQueEsperaron;
    }

    public void setVehiculosQueEsperaron(int vehiculosQueEsperaron) {
        this.vehiculosQueEsperaron = vehiculosQueEsperaron;
    }

    public int getPermisosDisponiblesAlFinal() {
        return permisosDisponiblesAlFinal;
    }

    public void setPermisosDisponiblesAlFinal(int permisosDisponiblesAlFinal) {
        this.permisosDisponiblesAlFinal = permisosDisponiblesAlFinal;
    }

    public List<ResultadoVehiculo> getVehiculos() {
        return vehiculos;
    }

    public void setVehiculos(List<ResultadoVehiculo> vehiculos) {
        this.vehiculos = vehiculos;
    }

    public List<String> getEventos() {
        return eventos;
    }

    public void setEventos(List<String> eventos) {
        this.eventos = eventos;
    }

    public Map<String, String> getAnalisis() {
        return analisis;
    }

    public void setAnalisis(Map<String, String> analisis) {
        this.analisis = analisis;
    }
}
