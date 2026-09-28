package cl.lema.models;

import cl.lema.interfaces.Asignable;
import cl.lema.interfaces.Cancelable;
import cl.lema.interfaces.Despachable;

/**
 * Representa un pedido de comida con restaurante y tiempo de preparación.
 * Calcula el tiempo de entrega según la distancia y se registra como COMIDA en MySQL.
 */
public class PedidoComida extends Pedido implements Asignable, Cancelable, Despachable{

    private String restaurante;
    private int tiempoPreparacion;

    public PedidoComida(int idPedido, String cliente, String direccionEntrega, double distanciaKm, String restaurante, int tiempoPreparacion) {
        super(idPedido, cliente, direccionEntrega, distanciaKm);
        this.restaurante = restaurante;
        this.tiempoPreparacion = tiempoPreparacion;
    }

    public PedidoComida(
            String cliente,
            String direccionEntrega,
            double distanciaKm,
            String restaurante,
            int tiempoPreparacion) {

        super(cliente, direccionEntrega, distanciaKm);

        this.restaurante = restaurante;
        this.tiempoPreparacion = tiempoPreparacion;
    }

    @Override
    public int calcularTiempoEntrega() {
        double tiempoBase = 15;
        double tiempoExtra = 2 * distanciaKm;
        double resultado = tiempoExtra + tiempoBase;
        return (int) Math.round(resultado);
    }

    @Override
    public void asignarRepartidor() {
        System.out.println("Preparando su comida y asignando repartidor...");
    }

    @Override
    public void cancelar() {
        System.out.println("Despacho de comida cancelado");
    }

    @Override
    public void despachar() {
        System.out.println("¡Su comida va en camino!");
    }

}
