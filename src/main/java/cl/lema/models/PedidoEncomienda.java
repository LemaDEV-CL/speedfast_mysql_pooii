package cl.lema.models;
import cl.lema.interfaces.Asignable;
import cl.lema.interfaces.Cancelable;
import cl.lema.interfaces.Despachable;
/**
 * Representa una encomienda con peso y volumen.
 * Calcula el tiempo de entrega según la distancia y se registra como ENCOMIENDA en MySQL.
 */
public class PedidoEncomienda extends Pedido implements Asignable, Cancelable, Despachable {

    private int peso;
    private int volumen;

    public PedidoEncomienda(int idPedido, String cliente, String direccionEntrega, double distanciaKm, int peso, int volumen) {
        super(idPedido, cliente, direccionEntrega, distanciaKm);
        this.peso = peso;
        this.volumen = volumen;
    }

    public PedidoEncomienda(
            String cliente,
            String direccionEntrega,
            double distanciaKm,
            int peso,
            int volumen) {

        super(cliente, direccionEntrega, distanciaKm);

        this.peso = peso;
        this.volumen = volumen;
    }

    @Override
    public int calcularTiempoEntrega() {
        double tiempoBase = 20;
        double tiempoExtra = 1.5 * distanciaKm;
        double resultado = tiempoExtra + tiempoBase;
        return (int) Math.round(resultado);
    }

    @Override
    public void asignarRepartidor() {
        System.out.println("Preparando su encomienda y asignando repartidor...");
    }

    @Override
    public void cancelar() {
        System.out.println("Despacho de encomienda cancelado");
    }

    @Override
    public void despachar() {
        System.out.println("¡Su encomienda va en camino!");
    }
}
