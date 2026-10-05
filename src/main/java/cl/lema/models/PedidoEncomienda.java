package cl.lema.models;
import cl.lema.interfaces.Asignable;
import cl.lema.interfaces.Cancelable;
import cl.lema.interfaces.Despachable;
/**
 * Representa un pedido de encomienda.
 * Incluye información relacionada con el peso y volumen transportado.
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
