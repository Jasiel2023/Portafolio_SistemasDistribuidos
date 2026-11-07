package edu.ape3.microservicio2.controller;

import edu.ape3.microservicio2.service.CuentaBancariaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/simulacion")
public class SimulacionController {

    @Autowired
    private CuentaBancariaService cuentaService;

    @GetMapping("/ejecutar")
    public String ejecutarSimulacion() throws InterruptedException {
        // Restauramos el saldo a 1000 para poder probar varias veces
        cuentaService.resetearSaldo();
        System.out.println("\n--- INICIANDO SIMULACIÓN CON SALDO: $1000 ---");

        // Crear los tres procesos (hilos)
        Thread proceso1 = new Thread(() -> cuentaService.retirar(1, 300));
        Thread proceso2 = new Thread(() -> cuentaService.retirar(2, 250));
        Thread proceso3 = new Thread(() -> cuentaService.depositar(3, 200));

        // Ejecutar los procesos simultáneamente
        proceso1.start();
        proceso2.start();
        proceso3.start();

        // Esperar a que terminen los 3 procesos antes de dar la respuesta
        proceso1.join();
        proceso2.join();
        proceso3.join();

        double saldoFinal = cuentaService.getSaldo();
        System.out.println("--- SIMULACIÓN FINALIZADA. SALDO FINAL: $" + saldoFinal + " ---\n");
        
        return "Simulación completada. Revisa la consola. Saldo final: $" + saldoFinal;
    }
}