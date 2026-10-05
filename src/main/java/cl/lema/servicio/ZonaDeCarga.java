package cl.lema.servicio;

import cl.lema.models.Pedido;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Gestiona la cola compartida de pedidos utilizada en la simulación con hilos.
 * Se conserva como parte de las funcionalidades desarrolladas anteriormente.
 */

public class ZonaDeCarga {

    private final BlockingQueue<Pedido> colaPedidos;
    private final ReentrantLock lock;

    public ZonaDeCarga() {
        colaPedidos = new LinkedBlockingQueue<>();
        lock = new ReentrantLock();
    }

    public void agregarPedido(Pedido pedido) throws InterruptedException {
        lock.lock();

        try {
            colaPedidos.put(pedido);
            System.out.println("Pedido agregado a zona de carga #" + pedido.getIdPedido());
        } finally {
            lock.unlock();
        }
    }

    public Pedido retirarPedido() {
        lock.lock();

        try {
            return colaPedidos.poll();
        } finally {
            lock.unlock();
        }
    }

    public int cantidadPedidos() {
        return colaPedidos.size();
    }
}
