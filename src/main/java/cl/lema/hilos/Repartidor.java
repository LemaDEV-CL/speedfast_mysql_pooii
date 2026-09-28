package cl.lema.hilos;

import cl.lema.models.EstadoPedido;
import cl.lema.models.Pedido;
import cl.lema.servicio.ZonaDeCarga;

/**
 * Representa al repartidor con su ID y nombre consultados desde MySQL.
 */

public class Repartidor implements Runnable {

    private int id;
    private String nombre;
    private ZonaDeCarga zonaDeCarga;

    public Repartidor(String nombre, ZonaDeCarga zonaDeCarga) {
        this.nombre = nombre;
        this.zonaDeCarga = zonaDeCarga;
    }

    public Repartidor(int id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @Override
    public void run() {

        while (true){
            Pedido pedido = zonaDeCarga.retirarPedido();
            if (pedido == null) {
                break;
            }
            pedido.setEstado(EstadoPedido.EN_REPARTO);
            System.out.println("Repartidor " + nombre + " retirando pedido #" + pedido.getIdPedido());
            System.out.println("Estado: " + pedido.getEstado());

            try {
                Thread.sleep(2000L);
            } catch (InterruptedException e) {
                System.out.println("Repartidor " + nombre + " fue interrumpido");
                Thread.currentThread().interrupt();
                return;
            }

            pedido.setEstado(EstadoPedido.ENTREGADO);

            System.out.println("Repartidor " + nombre + " ha entregado satisfactoriamente el pedido #" + pedido.getIdPedido());
            System.out.println("Estado: " + pedido.getEstado());
        }
    }

    @Override
    public String toString() {
        return nombre;
    }
}
