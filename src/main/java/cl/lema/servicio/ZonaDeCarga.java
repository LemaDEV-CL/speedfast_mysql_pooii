package cl.lema.servicio;

import cl.lema.models.Pedido;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Mantiene la cola compartida de pedidos para la simulación con hilos.
 * Se conserva de semanas anteriores y no participa en el flujo Swing con MySQL.
 */

public class ZonaDeCarga {

    private final BlockingQueue<Pedido> colaPedidos;
    private final ReentrantLock lock;

    /**
     * Crea una zona de carga vacía con su cola de pedidos y su mecanismo de bloqueo.
     */
    public ZonaDeCarga() {
        colaPedidos = new LinkedBlockingQueue<>();
        lock = new ReentrantLock();
    }

    /**
     * Agrega un pedido a la cola compartida de la zona de carga.
     *
     * @param pedido pedido que quedará disponible para los repartidores
     * @throws InterruptedException si el hilo es interrumpido mientras agrega el pedido
     */
    public void agregarPedido(Pedido pedido) throws InterruptedException {
        lock.lock();

        try {
            colaPedidos.put(pedido);
            System.out.println("Pedido agregado a zona de carga #" + pedido.getIdPedido());
        } finally {
            lock.unlock();
        }
    }

    /**
     * Retira el siguiente pedido disponible desde la cola compartida.
     *
     * @return pedido retirado o null si no existen pedidos disponibles
     */
    public Pedido retirarPedido() {
        lock.lock();

        try {
            return colaPedidos.poll();
        } finally {
            lock.unlock();
        }
    }

    /**
     * Consulta la cantidad de pedidos que quedan en la zona de carga.
     *
     * @return número de pedidos pendientes en la cola
     */
    public int cantidadPedidos() {
        return colaPedidos.size();
    }
}
