package cl.lema.models;

import cl.lema.interfaces.Asignable;
import cl.lema.interfaces.Cancelable;
import cl.lema.interfaces.Despachable;

/**
 * Representa un pedido express.
 * Incluye la tienda asociada al pedido.
 */
public class PedidoExpress extends Pedido implements Asignable, Cancelable, Despachable {

    private String tienda;

    public PedidoExpress(int idPedido, String cliente, String direccionEntrega, double distanciaKm, String tienda) {
        super(idPedido, cliente, direccionEntrega, distanciaKm);
        this.tienda = tienda;
    }

    public PedidoExpress(
            String cliente,
            String direccionEntrega,
            double distanciaKm,
            String tienda) {

        super(cliente, direccionEntrega, distanciaKm);

        this.tienda = tienda;
    }

    @Override
    public int calcularTiempoEntrega() {
        if (distanciaKm > 5) {
            return 15;
        }
        return 10;
    }

    @Override
    public void asignarRepartidor() {
        System.out.println("Preparando rápidamente su pedido express y asignando repartidor...");
    }

    @Override
    public void cancelar() {
        System.out.println("Despacho express cancelado");
    }

    @Override
    public void despachar() {
        System.out.println("¡Su despacho va en camino a toda velocidad!");
    }
}
