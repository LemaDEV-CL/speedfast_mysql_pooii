package cl.lema.models;

/**
 * Contiene los datos comunes y el estado de un pedido.
 * Al guardarlo mediante PedidoDAO recibe el ID generado por MySQL.
 */
public abstract class Pedido {

    protected int idPedido;
    protected String cliente;
    protected String direccionEntrega;
    protected double distanciaKm;
    protected EstadoPedido estado;
    private String nombreRepartidor;

    public Pedido(int idPedido, String cliente, String direccionEntrega, double distanciaKm) {
        this.idPedido = idPedido;
        this.cliente = cliente;
        this.direccionEntrega = direccionEntrega;
        this.distanciaKm = distanciaKm;
        this.estado = EstadoPedido.PENDIENTE;
    }

    public Pedido(String cliente, String direccionEntrega, double distanciaKm) {
        this.cliente = cliente;
        this.direccionEntrega = direccionEntrega;
        this.distanciaKm = distanciaKm;
        this.estado = EstadoPedido.PENDIENTE;
    }

    public int getIdPedido() {
        return idPedido;
    }

    public String getCliente() {
        return cliente;
    }

    public String getDireccion() {
        return direccionEntrega;
    }

    public double getDistanciaKm() {
        return distanciaKm;
    }

    public EstadoPedido getEstado() {
        return estado;
    }

    public String getNombreRepartidor() { return nombreRepartidor; }

    public void setIdPedido(int idPedido) { this.idPedido = idPedido; }

    public void setEstado(EstadoPedido estado) {
        this.estado = estado;
    }

    public void asignarRepartidor(String nombreRepartidor) {
        this.nombreRepartidor = nombreRepartidor;
        this.estado = EstadoPedido.EN_REPARTO;
    }

    public void mostrarResumen() {
        System.out.println("ID: " + getIdPedido());
        System.out.println("Cliente: " + getCliente());
        System.out.println("Direccion: " + getDireccion());
        System.out.println("Distancia Km: " + getDistanciaKm());
        System.out.println("Tiempo de entrega: " + calcularTiempoEntrega() + " minutos aprox.");
        System.out.println("Estado: " + getEstado());
        System.out.println("");
    }

    public abstract int calcularTiempoEntrega();

    @Override
    public String toString() {
        return "Pedido{" +
                "idPedido=" + idPedido +
                ", cliente='" + cliente + '\'' +
                ", direccionEntrega='" + direccionEntrega + '\'' +
                ", distanciaKm=" + distanciaKm +
                ", estado='" + estado + '\'' +
                '}';
    }
}
