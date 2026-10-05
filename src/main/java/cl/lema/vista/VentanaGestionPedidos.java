package cl.lema.vista;

import cl.lema.dao.PedidoDAO;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * Permite gestionar pedidos mediante una interfaz gráfica Swing.
 * Integra las operaciones CRUD con PedidoDAO y MySQL.
 */

public class VentanaGestionPedidos extends JFrame {

    private JTextField txtDireccion;

    private JComboBox<String> comboTipo;
    private JComboBox<String> comboEstado;

    private JTable tablaPedidos;
    private DefaultTableModel modeloTabla;

    private final PedidoDAO pedidoDAO =
            new PedidoDAO();

    private int idSeleccionado = -1;

    public VentanaGestionPedidos() {

        setTitle("Gestión de Pedidos");
        setSize(800, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        inicializarComponentes();
        cargarPedidos();
    }

    private void inicializarComponentes() {

        setLayout(new BorderLayout(10, 10));

        /*
         * FORMULARIO
         */
        JPanel panelFormulario =
                new JPanel(new GridLayout(3, 2, 10, 10));

        panelFormulario.setBorder(
                BorderFactory.createEmptyBorder(
                        10, 10, 10, 10
                )
        );

        panelFormulario.add(
                new JLabel("Dirección:")
        );

        txtDireccion =
                new JTextField();

        panelFormulario.add(txtDireccion);

        panelFormulario.add(
                new JLabel("Tipo:")
        );

        comboTipo =
                new JComboBox<>(
                        new String[]{
                                "COMIDA",
                                "ENCOMIENDA",
                                "EXPRESS"
                        }
                );

        panelFormulario.add(comboTipo);

        panelFormulario.add(
                new JLabel("Estado:")
        );

        comboEstado =
                new JComboBox<>(
                        new String[]{
                                "PENDIENTE",
                                "EN_REPARTO",
                                "ENTREGADO"
                        }
                );

        panelFormulario.add(comboEstado);

        add(
                panelFormulario,
                BorderLayout.NORTH
        );

        /*
         * TABLA
         */
        modeloTabla =
                new DefaultTableModel(
                        new String[]{
                                "ID",
                                "Dirección",
                                "Tipo",
                                "Estado",
                                "Repartidor"
                        },
                        0
                ) {

                    @Override
                    public boolean isCellEditable(
                            int fila,
                            int columna
                    ) {
                        return false;
                    }
                };

        tablaPedidos =
                new JTable(modeloTabla);

        tablaPedidos.setSelectionMode(
                ListSelectionModel.SINGLE_SELECTION
        );

        add(
                new JScrollPane(tablaPedidos),
                BorderLayout.CENTER
        );

        /*
         * BOTONES
         */
        JPanel panelBotones =
                new JPanel();

        JButton btnGuardar =
                new JButton("Guardar");

        JButton btnEditar =
                new JButton("Editar");

        JButton btnEliminar =
                new JButton("Eliminar");

        JButton btnLimpiar =
                new JButton("Limpiar");

        panelBotones.add(btnGuardar);
        panelBotones.add(btnEditar);
        panelBotones.add(btnEliminar);
        panelBotones.add(btnLimpiar);

        add(
                panelBotones,
                BorderLayout.SOUTH
        );

        /*
         * EVENTOS
         */
        btnGuardar.addActionListener(
                e -> guardarPedido()
        );

        btnEditar.addActionListener(
                e -> editarPedido()
        );

        btnEliminar.addActionListener(
                e -> eliminarPedido()
        );

        btnLimpiar.addActionListener(
                e -> limpiarFormulario()
        );

        tablaPedidos
                .getSelectionModel()
                .addListSelectionListener(
                        e -> {

                            if (!e.getValueIsAdjusting()) {
                                cargarSeleccion();
                            }
                        }
                );
    }

    private void cargarPedidos() {

        modeloTabla.setRowCount(0);

        List<Object[]> pedidos =
                pedidoDAO.listarTodos();

        for (Object[] pedido : pedidos) {

            String repartidor =
                    pedido[4] == null
                            ? "Sin asignar"
                            : pedido[4].toString();

            modeloTabla.addRow(
                    new Object[]{
                            pedido[0],
                            pedido[1],
                            pedido[2],
                            pedido[3],
                            repartidor
                    }
            );
        }
    }

    private void guardarPedido() {

        String direccion =
                txtDireccion.getText().trim();

        if (direccion.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Debes ingresar una dirección."
            );

            return;
        }

        String tipo =
                comboTipo
                        .getSelectedItem()
                        .toString();

        String estado =
                comboEstado
                        .getSelectedItem()
                        .toString();

        boolean guardado =
                pedidoDAO.create(
                        direccion,
                        tipo,
                        estado
                );

        if (guardado) {

            JOptionPane.showMessageDialog(
                    this,
                    "Pedido registrado correctamente."
            );

            cargarPedidos();
            limpiarFormulario();

        } else {

            JOptionPane.showMessageDialog(
                    this,
                    "No se pudo registrar el pedido."
            );
        }
    }

    private void cargarSeleccion() {

        int fila =
                tablaPedidos.getSelectedRow();

        if (fila == -1) {
            return;
        }

        idSeleccionado =
                Integer.parseInt(
                        modeloTabla
                                .getValueAt(fila, 0)
                                .toString()
                );

        txtDireccion.setText(
                modeloTabla
                        .getValueAt(fila, 1)
                        .toString()
        );

        comboTipo.setSelectedItem(
                modeloTabla
                        .getValueAt(fila, 2)
                        .toString()
        );

        comboEstado.setSelectedItem(
                modeloTabla
                        .getValueAt(fila, 3)
                        .toString()
        );
    }

    private void editarPedido() {

        if (idSeleccionado == -1) {

            JOptionPane.showMessageDialog(
                    this,
                    "Selecciona un pedido."
            );

            return;
        }

        String direccion =
                txtDireccion.getText().trim();

        if (direccion.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Debes ingresar una dirección."
            );

            return;
        }

        String tipo =
                comboTipo
                        .getSelectedItem()
                        .toString();

        String estado =
                comboEstado
                        .getSelectedItem()
                        .toString();

        boolean actualizado =
                pedidoDAO.update(
                        idSeleccionado,
                        direccion,
                        tipo,
                        estado
                );

        if (actualizado) {

            JOptionPane.showMessageDialog(
                    this,
                    "Pedido actualizado correctamente."
            );

            cargarPedidos();
            limpiarFormulario();

        } else {

            JOptionPane.showMessageDialog(
                    this,
                    "No se pudo actualizar el pedido."
            );
        }
    }

    private void eliminarPedido() {

        if (idSeleccionado == -1) {

            JOptionPane.showMessageDialog(
                    this,
                    "Selecciona un pedido."
            );

            return;
        }

        int respuesta =
                JOptionPane.showConfirmDialog(
                        this,
                        "¿Deseas eliminar este pedido?",
                        "Confirmar eliminación",
                        JOptionPane.YES_NO_OPTION
                );

        if (respuesta !=
                JOptionPane.YES_OPTION) {

            return;
        }

        boolean eliminado =
                pedidoDAO.delete(
                        idSeleccionado
                );

        if (eliminado) {

            JOptionPane.showMessageDialog(
                    this,
                    "Pedido eliminado correctamente."
            );

            cargarPedidos();
            limpiarFormulario();

        } else {

            JOptionPane.showMessageDialog(
                    this,
                    "No se pudo eliminar el pedido.\n"
                            + "Verifica que no tenga entregas asociadas."
            );
        }
    }

    private void limpiarFormulario() {

        txtDireccion.setText("");

        comboTipo.setSelectedIndex(0);
        comboEstado.setSelectedIndex(0);

        idSeleccionado = -1;

        tablaPedidos.clearSelection();

        txtDireccion.requestFocus();
    }
}