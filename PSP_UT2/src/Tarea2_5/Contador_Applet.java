package Tarea2_5;

import java.applet.Applet;
import java.awt.Button;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Contador_Applet extends Applet implements ActionListener {
    private HiloContador hilo1, hilo2;
    private Button btnHilo1, btnHilo2;

    public void init() {
        // Inicializamos los botones
        btnHilo1 = new Button("Finalizar Hilo 1");
        btnHilo2 = new Button("Finalizar Hilo 2");

        // Añadimos los botones al applet
        add(btnHilo1);
        add(btnHilo2);

        // Registramos los botones para escuchar eventos
        btnHilo1.addActionListener(this);
        btnHilo2.addActionListener(this);
    }

    public void start() {
        // Creamos y lanzamos los hilos, inicializando los contadores en valores diferentes
        hilo1 = new HiloContador(100);
        hilo2 = new HiloContador(120);
        hilo1.start();
        hilo2.start();
    }

    public void paint(Graphics g) {
        // Verificamos que el hilo no sea null antes de intentar acceder a su contador
        if (hilo1 != null && hilo1.estaEnEjecucion()) {
            g.drawString("Hilo1: " + hilo1.getContador(), 50, 100);
        } else {
            g.drawString("Hilo1: Finalizado", 50, 100);
        }

        if (hilo2 != null && hilo2.estaEnEjecucion()) {
            g.drawString("Hilo2: " + hilo2.getContador(), 50, 120);
        } else {
            g.drawString("Hilo2: Finalizado", 50, 120);
        }
    }

    public void actionPerformed(ActionEvent e) {
        // Acción cuando se pulsa el botón de "Finalizar Hilo 1"
        if (e.getSource() == btnHilo1 && hilo1 != null && hilo1.estaEnEjecucion()) {
            hilo1.detener(); // Cambiamos la variable de control en lugar de usar stop()
            btnHilo1.setLabel("Finalizado Hilo 1");
        }

        // Acción cuando se pulsa el botón de "Finalizar Hilo 2"
        if (e.getSource() == btnHilo2 && hilo2 != null && hilo2.estaEnEjecucion()) {
            hilo2.detener(); // Cambiamos la variable de control en lugar de usar stop()
            btnHilo2.setLabel("Finalizado Hilo 2");
        }

        // Redibujamos la pantalla para reflejar los cambios
        repaint();
    }

    // Clase interna que representa un contador en un hilo
    class HiloContador extends Thread {
        private int contador;
        private volatile boolean enEjecucion = true; // Variable de control de ejecución

        public HiloContador(int inicio) {
            this.contador = inicio;
        }

        public void run() {
            while (enEjecucion) { // Usamos la variable de control en lugar de true
                contador++;
                try {
                    // Hacemos una pausa de 1 segundo entre cada incremento
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt(); // Restauramos el estado de interrupción
                    break; // Salimos del bucle si se interrumpe el hilo
                }
                repaint(); // Redibujamos el applet en cada incremento
            }
        }

        public int getContador() {
            return contador;
        }

        public void detener() {
            enEjecucion = false; // Cambiamos la variable de control para detener el hilo
            interrupt(); // Interrumpimos el hilo para salir de sleep()
        }

        public boolean estaEnEjecucion() {
            return enEjecucion;
        }
    }
}