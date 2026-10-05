package cl.lema.servicio;

import cl.lema.interfaces.Asignable;
import cl.lema.interfaces.*;
import cl.lema.models.*;
import java.util.ArrayList;
import java.util.List;
import cl.lema.dao.PedidoDAO;

/**
 * Coordina operaciones relacionadas con los pedidos de SpeedFast.
 * Conserva funcionalidades y lógica desarrolladas en semanas anteriores.
 */
public class ControladorDeEnvios implements Rastreable {

    private List<String> historial = new ArrayList<>();
    private final PedidoDAO pedidoDAO = new PedidoDAO();

    public boolean agregarPedido(Pedido pedido) {
        return pedidoDAO.guardar(pedido);
    }

    public void asignarPedido(Asignable pedido) {
        pedido.asignarRepartidor();
    }

    public void cancelarPedido(Cancelable pedido) { pedido.cancelar(); }
    public void despacharPedido(Despachable pedido) {
        pedido.despachar();
    }

    @Override
    public void verHistorial() {
        System.out.println("=== HISTORIAL DE ENTREGAS COMPLETADAS ===");
        if (historial.isEmpty()) {
            System.out.println("No existen despachos registrados.");
        } else {
            for (String evento : historial) {
                System.out.println(evento);
            }
        }
    }

    public void registrarEntrega(Pedido pedido) {
        historial.add("Pedido #" + pedido.getIdPedido() + " Entregado"
        );
    }
}
