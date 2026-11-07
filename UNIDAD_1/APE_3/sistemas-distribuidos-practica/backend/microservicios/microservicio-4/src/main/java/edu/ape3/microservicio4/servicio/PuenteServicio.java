package edu.ape3.microservicio4.servicio;

import edu.ape3.microservicio4.modelo.Direccion;
import edu.ape3.microservicio4.modelo.EstadoPuente;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
import org.springframework.stereotype.Service;

@Service
public class PuenteServicio {

    private final ReentrantLock lock = new ReentrantLock();
    private final Condition libre = lock.newCondition();

    private Direccion direccion = Direccion.NINGUNA;
    private int vehiculosNorte = 0;
    private int vehiculosSur = 0;

    private Direccion ultimaDireccion = Direccion.NINGUNA;
    private Direccion turno = Direccion.NINGUNA;
    private boolean equidadActiva = false;
    private int esperandoNorte = 0;
    private int esperandoSur = 0;

    public long entrarNorte() {
        return entrar(Direccion.NORTE);
    }

    public long entrarSur() {
        return entrar(Direccion.SUR);
    }

    public long entrar(Direccion dir) {
        lock.lock();
        try {
            long inicio = System.currentTimeMillis();
            if (dir == Direccion.NORTE) {
                esperandoNorte++;
            } else {
                esperandoSur++;
            }
            try {
                while (!puedeEntrar(dir)) {
                    libre.await();
                }
                if (direccion == Direccion.NINGUNA) {
                    direccion = dir;
                    ultimaDireccion = dir;
                }
                if (dir == Direccion.NORTE) {
                    vehiculosNorte++;
                } else {
                    vehiculosSur++;
                }
                if (equidadActiva) {
                    turno = Direccion.NINGUNA;
                }
                return System.currentTimeMillis() - inicio;
            } finally {
                if (dir == Direccion.NORTE) {
                    esperandoNorte--;
                } else {
                    esperandoSur--;
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException("Vehículo interrumpido esperando el puente", e);
        } finally {
            lock.unlock();
        }
    }

    private boolean puedeEntrar(Direccion dir) {
        if (direccion == dir) {
            return !(equidadActiva && esperando(dir.opuesta()) > 0);
        }
        if (direccion == Direccion.NINGUNA) {
            return !equidadActiva || turno == Direccion.NINGUNA || turno == dir;
        }
        return false;
    }

    private int esperando(Direccion dir) {
        return switch (dir) {
            case NORTE -> esperandoNorte;
            case SUR -> esperandoSur;
            case NINGUNA -> 0;
        };
    }

    public void cruzar(int demoraMs) {
        if (demoraMs <= 0) {
            return;
        }
        try {
            Thread.sleep(demoraMs);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public void salir(Direccion dir) {
        lock.lock();
        try {
            if (dir == Direccion.NORTE) {
                if (vehiculosNorte > 0) {
                    vehiculosNorte--;
                }
            } else if (dir == Direccion.SUR) {
                if (vehiculosSur > 0) {
                    vehiculosSur--;
                }
            }
            if (vehiculosNorte == 0 && vehiculosSur == 0) {
                direccion = Direccion.NINGUNA;
                if (equidadActiva) {
                    turno = calcularTurno();
                }
            }
            libre.signalAll();
        } finally {
            lock.unlock();
        }
    }

    private Direccion calcularTurno() {
        boolean esperaNorte = esperandoNorte > 0;
        boolean esperaSur = esperandoSur > 0;
        if (esperaNorte && !esperaSur) {
            return Direccion.NORTE;
        }
        if (esperaSur && !esperaNorte) {
            return Direccion.SUR;
        }
        if (esperaNorte && esperaSur) {
            return ultimaDireccion.opuesta();
        }
        return Direccion.NINGUNA;
    }

    public void setEquidad(boolean activa) {
        lock.lock();
        try {
            this.equidadActiva = activa;
            this.turno = Direccion.NINGUNA;
            libre.signalAll();
        } finally {
            lock.unlock();
        }
    }

    public void reset() {
        lock.lock();
        try {
            direccion = Direccion.NINGUNA;
            ultimaDireccion = Direccion.NINGUNA;
            turno = Direccion.NINGUNA;
            vehiculosNorte = 0;
            vehiculosSur = 0;
            esperandoNorte = 0;
            esperandoSur = 0;
            equidadActiva = false;
            libre.signalAll();
        } finally {
            lock.unlock();
        }
    }

    public EstadoPuente estado() {
        lock.lock();
        try {
            return new EstadoPuente(
                    direccion.name(),
                    vehiculosNorte,
                    vehiculosSur,
                    equidadActiva,
                    esperandoNorte,
                    esperandoSur);
        } finally {
            lock.unlock();
        }
    }
}
