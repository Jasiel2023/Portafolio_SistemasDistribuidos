package edu.ape3.microservicio2.service;

import org.springframework.stereotype.Service;
import java.util.concurrent.locks.ReentrantLock;

@Service //
public class CuentaBancariaService {

    private double saldo = 1000.0;
    // Creamos el Mutex
    private final ReentrantLock mutexSaldo = new ReentrantLock();

    public void retirar(int idProceso, double cantidad) {
        mutexSaldo.lock(); // Adquiere el mutex
        try {
            System.out.println("Proceso " + idProceso + " intenta retirar $" + cantidad);
            if (saldo >= cantidad) {
                // Simulamos un pequeño retraso para forzar la concurrencia si no hubiera mutex
                Thread.sleep(50); 
                saldo = saldo - cantidad;
                System.out.println("Proceso " + idProceso + " - Retiro realizado. Saldo actual: $" + saldo);
            } else {
                System.out.println("Proceso " + idProceso + " - Saldo insuficiente. Saldo actual: $" + saldo);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            mutexSaldo.unlock(); // Libera el mutex SIEMPRE en el finally
        }
    }

    public void depositar(int idProceso, double cantidad) {
        mutexSaldo.lock(); // Adquiere el mutex
        try {
            System.out.println("Proceso " + idProceso + " intenta depositar $" + cantidad);
            Thread.sleep(50);
            saldo = saldo + cantidad;
            System.out.println("Proceso " + idProceso + " - Depósito realizado. Saldo actual: $" + saldo);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            mutexSaldo.unlock(); // Libera el mutex
        }
    }

    public double getSaldo() {
        return saldo;
    }
    
    public void resetearSaldo() {
        this.saldo = 1000.0;
    }
}