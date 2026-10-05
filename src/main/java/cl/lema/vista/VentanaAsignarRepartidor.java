package cl.lema.vista;

import cl.lema.dao.EntregaDAO;
import cl.lema.dao.PedidoDAO;
import cl.lema.dao.RepartidorDAO;
import cl.lema.models.Entrega;
import cl.lema.models.Repartidor;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

/**
 * Permite asignar un repartidor a un pedido pendiente.
 * Registra la entrega y actualiza el estado del pedido en MySQL.
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

        setDefaultCloseOperation(
                JFrame.DISPOSE_ON_CLOSE
        );

        setLayout(
                new GridLayout(
                        3,
                        2,
                        10,
                        10
                )
        );

        comboPedidos =
                new JComboBox<>();

        comboRepartidores =
                new JComboBox<>();

        btnIniciar =
                new JButton(
                        "Iniciar entrega"
                );

        btnIniciar.addActionListener(
                e -> iniciarEntrega()
        );

        add(
                new JLabel(
                        "ID del pedido:"
                )
        );

        add(comboPedidos);

        add(
                new JLabel(
                        "Repartidor:"
                )
        );

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

        for (Integer id :
                pedidosPendientes) {

            comboPedidos.addItem(id);
        }
    }

    private void cargarRepartidores() {

        comboRepartidores.removeAllItems();

        List<Repartidor> repartidores =
                repartidorDAO.readAll();

        for (Repartidor repartidor :
                repartidores) {

            comboRepartidores.addItem(
                    repartidor
            );
        }
    }

    private void iniciarEntrega() {

        Integer idPedido =
                (Integer)
                        comboPedidos
                                .getSelectedItem();

        Repartidor repartidor =
                (Repartidor)
                        comboRepartidores
                                .getSelectedItem();

        if (idPedido == null
                || repartidor == null) {

            JOptionPane.showMessageDialog(
                    this,
                    "Selecciona un pedido y un repartidor."
            );

            return;
        }

        Entrega entrega =
                new Entrega(
                        idPedido,
                        repartidor.getId(),
                        LocalDate.now(),
                        LocalTime.now()
                );

        boolean entregaGuardada =
                entregaDAO.create(
                        entrega
                );

        if (!entregaGuardada) {

            JOptionPane.showMessageDialog(
                    this,
                    "No se pudo guardar la entrega."
            );

            return;
        }

        boolean estadoActualizado =
                pedidoDAO.actualizarEstado(
                        idPedido,
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
                "Pedido #"
                        + idPedido
                        + " asignado a "
                        + repartidor.getNombre()
                        + ". Estado: EN_REPARTO."
        );
    }
}