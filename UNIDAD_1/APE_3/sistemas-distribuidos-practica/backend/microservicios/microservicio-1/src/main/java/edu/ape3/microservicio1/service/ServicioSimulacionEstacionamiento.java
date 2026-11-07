package edu.ape3.microservicio1.service;

import edu.ape3.microservicio1.model.EstadoEstacionamiento;
import edu.ape3.microservicio1.model.ResultadoSimulacion;
import edu.ape3.microservicio1.model.ResultadoVehiculo;
import edu.ape3.microservicio1.model.Vehiculo;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;
import org.springframework.stereotype.Service;

@Service
public class ServicioSimulacionEstacionamiento {
    private static final long TIEMPO_ESTACIONAMIENTO_MILLIS = 200;
    private static final int CAPACIDAD_ESTACIONAMIENTO = 4;
    private final Semaphore semaforoInteractivo = new Semaphore(CAPACIDAD_ESTACIONAMIENTO, true);
    private final Set<Vehiculo> vehiculosDentroInteractivos = new TreeSet<>();
    private final Set<Vehiculo> vehiculosEsperandoInteractivos = new TreeSet<>();

    public synchronized EstadoEstacionamiento estadoEstacionamiento() {
        return crearEstado("Estado actual del estacionamiento", true);
    }

    public synchronized EstadoEstacionamiento llegarVehiculo(int id) {
        Vehiculo vehiculo = new Vehiculo(id);
        if (vehiculosDentroInteractivos.contains(vehiculo)) {
            return crearEstado("El vehiculo " + id + " ya esta dentro", true);
        }
        if (vehiculosEsperandoInteractivos.contains(vehiculo)) {
            return crearEstado("El vehiculo " + id + " ya esta esperando", false);
        }
        if (semaforoInteractivo.tryAcquire()) {
            vehiculosDentroInteractivos.add(vehiculo);
            return crearEstado("El vehiculo " + id + " ingreso al estacionamiento", true);
        }
        vehiculosEsperandoInteractivos.add(vehiculo);
        return crearEstado("El vehiculo " + id + " quedo esperando por falta de espacio", false);
    }

    public synchronized EstadoEstacionamiento salirVehiculo(int id) {
        if (!vehiculosDentroInteractivos.remove(new Vehiculo(id))) {
            return crearEstado("El vehiculo " + id + " no esta dentro del estacionamiento", false);
        }
        semaforoInteractivo.release();
        if (!vehiculosEsperandoInteractivos.isEmpty()) {
            Vehiculo siguiente = vehiculosEsperandoInteractivos.iterator().next();
            vehiculosEsperandoInteractivos.remove(siguiente);
            semaforoInteractivo.acquireUninterruptibly();
            vehiculosDentroInteractivos.add(siguiente);
        }
        return crearEstado("El vehiculo " + id + " salio y libero un espacio", true);
    }

    public synchronized EstadoEstacionamiento reiniciarEstacionamiento() {
        vehiculosDentroInteractivos.clear();
        vehiculosEsperandoInteractivos.clear();
        semaforoInteractivo.drainPermits();
        semaforoInteractivo.release(CAPACIDAD_ESTACIONAMIENTO);
        return crearEstado("Estacionamiento reiniciado", true);
    }

    private EstadoEstacionamiento crearEstado(String mensaje, boolean operacionExitosa) {
        return new EstadoEstacionamiento(
                CAPACIDAD_ESTACIONAMIENTO,
                vehiculosDentroInteractivos.size(),
                semaforoInteractivo.availablePermits(),
                vehiculosDentroInteractivos.stream().map(Vehiculo::getId).toList(),
                vehiculosEsperandoInteractivos.stream().map(Vehiculo::getId).toList(),
                mensaje,
                operacionExitosa);
    }

    public ResultadoSimulacion simular(int capacidad, int cantidadVehiculos) {
        Semaphore espacios = new Semaphore(capacidad, true);
        AtomicInteger vehiculosDentro = new AtomicInteger();
        AtomicInteger maximoDentro = new AtomicInteger();
        List<String> eventos = Collections.synchronizedList(new ArrayList<>());
        ExecutorService ejecutor = Executors.newFixedThreadPool(cantidadVehiculos);
        List<Future<ResultadoVehiculo>> futuros = new ArrayList<>();

        for (int id = 1; id <= cantidadVehiculos; id++) {
            futuros.add(ejecutor.submit(new TareaVehiculo(
                    id, espacios, vehiculosDentro, maximoDentro, eventos)));
        }

        List<ResultadoVehiculo> vehiculos = new ArrayList<>();
        for (Future<ResultadoVehiculo> futuro : futuros) {
            try {
                vehiculos.add(futuro.get());
            } catch (InterruptedException exception) {
                Thread.currentThread().interrupt();
                throw new IllegalStateException("La simulacion fue interrumpida", exception);
            } catch (ExecutionException exception) {
                throw new IllegalStateException("No se pudo ejecutar un vehiculo", exception.getCause());
            }
        }
        ejecutor.shutdown();

        return new ResultadoSimulacion(
                capacidad,
                cantidadVehiculos,
                maximoDentro.get(),
                cantidadVehiculos - capacidad,
                espacios.availablePermits(),
                vehiculos,
                eventos,
                Map.of(
                        "semaphoreInitializedWithOne", "Solo un vehiculo puede permanecer dentro; los otros esperan.",
                        "semaphoreInitializedWithTen", "Los diez vehiculos pueden ingresar, superando la capacidad del estacionamiento."));
    }

    private static final class TareaVehiculo implements Callable<ResultadoVehiculo> {
        private final int id;
        private final Semaphore espacios;
        private final AtomicInteger vehiculosDentro;
        private final AtomicInteger maximoDentro;
        private final List<String> eventos;

        private TareaVehiculo(int id, Semaphore espacios, AtomicInteger vehiculosDentro,
                AtomicInteger maximoDentro, List<String> eventos) {
            this.id = id;
            this.espacios = espacios;
            this.vehiculosDentro = vehiculosDentro;
            this.maximoDentro = maximoDentro;
            this.eventos = eventos;
        }

        @Override
        public ResultadoVehiculo call() throws InterruptedException {
            eventos.add("Vehiculo " + id + " llega y solicita un espacio");
            long inicioEspera = System.nanoTime();
            espacios.acquire();
            long milisegundosEsperados = (System.nanoTime() - inicioEspera) / 1_000_000;
            int ocupados = vehiculosDentro.incrementAndGet();
            maximoDentro.accumulateAndGet(ocupados, Math::max);
            eventos.add("Vehiculo " + id + " ingresa (ocupados: " + ocupados + ")");

            try {
                Thread.sleep(TIEMPO_ESTACIONAMIENTO_MILLIS);
                return new ResultadoVehiculo(id, milisegundosEsperados, true);
            } finally {
                int restantes = vehiculosDentro.decrementAndGet();
                espacios.release();
                eventos.add("Vehiculo " + id + " sale y libera un espacio (ocupados: " + restantes + ")");
            }
        }
    }
}
