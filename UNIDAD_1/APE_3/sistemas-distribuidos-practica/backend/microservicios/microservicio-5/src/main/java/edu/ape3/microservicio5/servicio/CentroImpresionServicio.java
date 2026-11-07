package edu.ape3.microservicio5.servicio;

import edu.ape3.microservicio5.modelo.Etapa;
import edu.ape3.microservicio5.modelo.EstadoCentro;
import edu.ape3.microservicio5.modelo.dto.RespuestaEtapa;
import edu.ape3.microservicio5.modelo.dto.SolicitudEtapa;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;
import org.springframework.stereotype.Service;

@Service
public class CentroImpresionServicio {

    private static final int CICLOS_POR_DEFECTO = 3;

    private final Semaphore turnoDiseno = new Semaphore(1, true);
    private final Semaphore turnoRevision = new Semaphore(0, true);
    private final Semaphore turnoImpresion = new Semaphore(0, true);
    private final Object estadoLock = new Object();
    private final AtomicInteger secuencia = new AtomicInteger();

    private Etapa etapaActual = Etapa.NINGUNA;
    private String documentoActual = "";
    private int documentosDisenados = 0;
    private int documentosRevisados = 0;
    private int documentosImpresos = 0;
    private int ciclosTotales = CICLOS_POR_DEFECTO;

    public RespuestaEtapa disenar(SolicitudEtapa solicitud, int demoraMs) {
        long inicio = System.nanoTime();
        turnoDiseno.acquireUninterruptibly();
        RespuestaEtapa respuesta;
        synchronized (estadoLock) {
            if (documentosDisenados >= ciclosTotales) {
                turnoDiseno.release();
                return new RespuestaEtapa(
                        documentoActual,
                        Etapa.NINGUNA.name(),
                        documentosDisenados,
                        documentosRevisados,
                        documentosImpresos,
                        espera(inicio),
                        "El ciclo ya completó los " + ciclosTotales + " documentos");
            }
            String id = (solicitud != null && solicitud.id() != null && !solicitud.id().isBlank())
                    ? solicitud.id()
                    : generarId();
            documentoActual = id;
            etapaActual = Etapa.DISENO;
            documentosDisenados++;
            respuesta = new RespuestaEtapa(
                    id,
                    Etapa.DISENO.name(),
                    documentosDisenados,
                    documentosRevisados,
                    documentosImpresos,
                    espera(inicio),
                    "Diseño prepara el documento " + id);
        }
        esperar(demoraMs);
        turnoRevision.release();
        return respuesta;
    }

    public RespuestaEtapa revisar(SolicitudEtapa solicitud, int demoraMs) {
        long inicio = System.nanoTime();
        turnoRevision.acquireUninterruptibly();
        RespuestaEtapa respuesta;
        synchronized (estadoLock) {
            etapaActual = Etapa.REVISION;
            documentosRevisados++;
            respuesta = new RespuestaEtapa(
                    documentoActual,
                    Etapa.REVISION.name(),
                    documentosDisenados,
                    documentosRevisados,
                    documentosImpresos,
                    espera(inicio),
                    "Revisión verifica el documento " + documentoActual);
        }
        esperar(demoraMs);
        turnoImpresion.release();
        return respuesta;
    }

    public RespuestaEtapa imprimir(SolicitudEtapa solicitud, int demoraMs) {
        long inicio = System.nanoTime();
        turnoImpresion.acquireUninterruptibly();
        RespuestaEtapa respuesta;
        synchronized (estadoLock) {
            etapaActual = Etapa.IMPRESION;
            documentosImpresos++;
            respuesta = new RespuestaEtapa(
                    documentoActual,
                    Etapa.IMPRESION.name(),
                    documentosDisenados,
                    documentosRevisados,
                    documentosImpresos,
                    espera(inicio),
                    "Impresión imprime el documento " + documentoActual);
        }
        esperar(demoraMs);
        turnoDiseno.release();
        return respuesta;
    }

    public EstadoCentro estado() {
        synchronized (estadoLock) {
            return new EstadoCentro(
                    etapaActual.name(),
                    documentoActual,
                    documentosDisenados,
                    documentosRevisados,
                    documentosImpresos,
                    ciclosTotales,
                    documentosImpresos >= ciclosTotales);
        }
    }

    public EstadoCentro setCiclos(int ciclos) {
        synchronized (estadoLock) {
            if (ciclos > 0) {
                this.ciclosTotales = ciclos;
            }
            return estado();
        }
    }

    public EstadoCentro reset() {
        synchronized (estadoLock) {
            etapaActual = Etapa.NINGUNA;
            documentoActual = "";
            documentosDisenados = 0;
            documentosRevisados = 0;
            documentosImpresos = 0;
            secuencia.set(0);
            turnoDiseno.drainPermits();
            turnoRevision.drainPermits();
            turnoImpresion.drainPermits();
            turnoDiseno.release(1);
            return estado();
        }
    }

    private long espera(long inicioNanos) {
        return (System.nanoTime() - inicioNanos) / 1_000_000;
    }

    private void esperar(int demoraMs) {
        if (demoraMs <= 0) {
            return;
        }
        try {
            Thread.sleep(demoraMs);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private String generarId() {
        return "doc-" + secuencia.incrementAndGet();
    }
}
