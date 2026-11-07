package edu.ape3.microservicio3.modelo;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Almacen de la fabrica: implementa el patron Productor-Consumidor.
 *
 * <p>Regla de concurrencia critica (anti-deadlock): los llamados a
 * {@link Semaphore#acquire()} se realizan SIEMPRE FUERA del bloque
 * {@link ReentrantLock#lock()}. El ReentrantLock solo protege las operaciones
 * directas sobre la cola ({@link Queue#add}, {@link Queue#poll},
 * {@link Queue#size}).
 *
 * <p>Razon: si un productor adquiriera un semaforo DENTRO del lock y se
 * bloqueara esperando un hueco, mantendria el lock impidiendo que un
 * consumidor entrara a liberar ese hueco (deadlock). Al adquirir los
 * semaforos fuera del lock, ningun hilo queda reteniendo el monitor mientras
 * espera recursos.
 */
public class Almacen {

    private static final int CAPACIDAD_MAXIMA = 5;

    private static Almacen instancia;

    private final Queue<String> cola;
    private final Semaphore slots;   // huecos libres disponibles (inicial = 5)
    private final Semaphore piezas;  // piezas disponibles para consumir (inicial = 0)
    private final ReentrantLock lock;

    private Almacen() {
        this.cola = new LinkedList<>();
        this.slots = new Semaphore(CAPACIDAD_MAXIMA);
        this.piezas = new Semaphore(0);
        this.lock = new ReentrantLock();
    }

    /**
     * Obtiene la instancia unica del Almacen (Singleton thread-safe).
     */
    public static synchronized Almacen getInstance() {
        if (instancia == null) {
            instancia = new Almacen();
        }
        return instancia;
    }

    /**
     * Produce una pieza en el almacen.
     *
     * <ol>
     *   <li>{@code slots.acquire()} FUERA del lock: reserva un hueco sin
     *       retener el monitor.</li>
     *   <li>{@code lock.lock()} protege solo el {@code cola.add(...)}.</li>
     *   <li>{@code piezas.release()} senala que hay una pieza nueva lista para
     *       consumir.</li>
     * </ol>
     */
    public void producir(String pieza) throws InterruptedException {
        // 1. Esperar por un hueco libre (SIEMPRE fuera del lock).
        slots.acquire();

        // 2. Insertar en la cola bajo proteccion del ReentrantLock.
        lock.lock();
        try {
            cola.add(pieza);
        } finally {
            lock.unlock();
        }

        // 3. Señalar que hay una pieza disponible para los consumidores.
        piezas.release();
    }

    /**
     * Consume una pieza del almacen.
     *
     * <ol>
     *   <li>{@code piezas.acquire()} FUERA del lock: espera una pieza sin
     *       retener el monitor.</li>
     *   <li>{@code lock.lock()} protege solo el {@code cola.poll()}.</li>
     *   <li>{@code slots.release()} libera un hueco para futuras producciones.</li>
     * </ol>
     */
    public String consumir() throws InterruptedException {
        // 1. Esperar por una pieza disponible (SIEMPRE fuera del lock).
        piezas.acquire();

        // 2. Extraer de la cola bajo proteccion del ReentrantLock.
        lock.lock();
        try {
            return cola.poll();
        } finally {
            lock.unlock();
            // Liberar un hueco ahora que se consumo una pieza.
            slots.release();
        }
    }

    /**
     * Devuelve la cantidad actual de piezas en el almacen.
     */
    public int getCantidadActual() {
        lock.lock();
        try {
            return cola.size();
        } finally {
            lock.unlock();
        }
    }

    /**
     * Devuelve la capacidad maxima del almacen.
     */
    public int getCapacidadMaxima() {
        return CAPACIDAD_MAXIMA;
    }
}
