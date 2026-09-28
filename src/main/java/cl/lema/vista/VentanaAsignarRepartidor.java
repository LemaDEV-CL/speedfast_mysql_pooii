package cl.lema.vista;

import javax.swing.*;
import java.awt.GridLayout;
import cl.lema.dao.RepartidorDAO;
import cl.lema.hilos.Repartidor;
import java.util.List;
import cl.lema.dao.EntregaDAO;
import java.time.LocalDate;
import java.time.LocalTime;
import cl.lema.dao.PedidoDAO;

/**
 * Carga pedidos pendientes y repartidores desde MySQL mediante los DAO.
 * Guarda la entrega y actualiza el estado del pedido a EN_REPARTO.
 */
public class VentanaAsignarRepartidor extends JFrame {

    private JComboBox<Integer> comboPedidos;
    private JComboBox<Repartidor> comboRepartidores;
    private JButton btnIniciar;
    private final RepartidorDAO repartidorDAO = new RepartidorDAO();
    private final EntregaDAO entregaDAO = new EntregaDAO();
    private final PedidoDAO pedidoDAO = new PedidoDAO();

    public VentanaAsignarRepartidor() {
        setTitle("Asignar repartidor");
        setSize(450, 200);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new GridLayout(3, 2, 10, 10));

        comboPedidos = new JComboBox<>();

        comboRepartidores = new JComboBox<>();

        btnIniciar = new JButton("Iniciar entrega");

        btnIniciar.addActionListener(e -> iniciarEntrega());

        add(new JLabel("ID del pedido:"));
        add(comboPedidos);

        add(new JLabel("Repartidor:"));
        add(comboRepartidores);

        add(new JLabel(""));
        add(btnIniciar);

        cargarPedidosPendientes();
        cargarRepartidores();
        setLocationRelativeTo(null);
    }

    private void cargarPedidosPendientes() {

        comboPedidos.removeAllItems();

        List<Integer> pedidosPendientes =
                pedidoDAO.listarPendientes();

        for (Integer id : pedidosPendientes) {
            comboPedidos.addItem(id);
        }
    }

    private void cargarRepartidores() {

        comboRepartidores.removeAllItems();

        List<Repartidor> repartidores =
                repartidorDAO.listarTodos();

        for (Repartidor repartidor : repartidores) {
            comboRepartidores.addItem(repartidor);
        }
    }

    private void iniciarEntrega() {
        Integer id = (Integer) comboPedidos.getSelectedItem();
        Repartidor repartidor = (Repartidor) comboRepartidores.getSelectedItem();

        if (id == null || repartidor == null) {
            JOptionPane.showMessageDialog(
                    this,
                    "Selecciona un pedido y un repartidor."
            );
            return;
        }

        boolean entregaGuardada =
                entregaDAO.guardar(
                        id,
                        repartidor.getId(),
                        LocalDate.now(),
                        LocalTime.now()
                );
        if (!entregaGuardada) {
            JOptionPane.showMessageDialog(
                    this,
                    "El pedido fue asignado pero no se pudo guardar en la base de datos."
            );

            return;
        }

        boolean estadoActualizado =
                pedidoDAO.actualizarEstado(
                        id,
                        "EN_REPARTO"
                );

        if (!estadoActualizado) {

            JOptionPane.showMessageDialog(
                    this,
                    "La entrega se guardó, pero no se pudo actualizar el estado del pedido."
            );

            return;
        }

        cargarPedidosPendientes();

        JOptionPane.showMessageDialog(
                this,
                "Pedido #" + id + " asignado a " + repartidor.getNombre()
                        + ". Estado: EN_REPARTO."
        );
    }
}