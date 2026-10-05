package cl.lema.vista;

import cl.lema.dao.EntregaDAO;
import cl.lema.dao.PedidoDAO;
import cl.lema.dao.RepartidorDAO;
import cl.lema.models.Entrega;
import cl.lema.models.Repartidor;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.List;

/**
 * Permite gestionar entregas mediante una interfaz gráfica Swing.
 * Relaciona pedidos y repartidores utilizando datos almacenados en MySQL.
 */

public class VentanaGestionEntregas extends JFrame {

    private JComboBox<String> comboPedidos;
    private JComboBox<Repartidor> comboRepartidores;

    private JTextField txtFecha;
    private JTextField txtHora;

    private JTable tablaEntregas;
    private DefaultTableModel modeloTabla;

    private final EntregaDAO entregaDAO =
            new EntregaDAO();

    private final PedidoDAO pedidoDAO =
            new PedidoDAO();

    private final RepartidorDAO repartidorDAO =
            new RepartidorDAO();

    private int idSeleccionado = -1;

    public VentanaGestionEntregas() {

        setTitle("Gestión de Entregas");
        setSize(850, 520);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        inicializarComponentes();

        cargarPedidos();
        cargarRepartidores();
        cargarEntregas();

        limpiarFormulario();
    }

    private void inicializarComponentes() {

        setLayout(
                new BorderLayout(10, 10)
        );

        JPanel panelFormulario =
                new JPanel(
                        new GridLayout(
                                4,
                                2,
                                10,
                                10
                        )
                );

        panelFormulario.setBorder(
                BorderFactory.createEmptyBorder(
                        10,
                        10,
                        10,
                        10
                )
        );

        panelFormulario.add(
                new JLabel("Pedido:")
        );

        comboPedidos =
                new JComboBox<>();

        panelFormulario.add(
                comboPedidos
        );

        panelFormulario.add(
                new JLabel("Repartidor:")
        );

        comboRepartidores =
                new JComboBox<>();

        panelFormulario.add(
                comboRepartidores
        );

        panelFormulario.add(
                new JLabel("Fecha (AAAA-MM-DD):")
        );

        txtFecha =
                new JTextField();

        panelFormulario.add(
                txtFecha
        );

        panelFormulario.add(
                new JLabel("Hora (HH:mm):")
        );

        txtHora =
                new JTextField();

        panelFormulario.add(
                txtHora
        );

        add(
                panelFormulario,
                BorderLayout.NORTH
        );

        modeloTabla =
                new DefaultTableModel(
                        new String[]{
                                "ID",
                                "Pedido",
                                "Repartidor",
                                "Fecha",
                                "Hora"
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

        tablaEntregas =
                new JTable(
                        modeloTabla
                );

        tablaEntregas.setSelectionMode(
                ListSelectionModel.SINGLE_SELECTION
        );

        add(
                new JScrollPane(
                        tablaEntregas
                ),
                BorderLayout.CENTER
        );

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

        panelBotones.add(
                btnGuardar
        );

        panelBotones.add(
                btnEditar
        );

        panelBotones.add(
                btnEliminar
        );

        panelBotones.add(
                btnLimpiar
        );

        add(
                panelBotones,
                BorderLayout.SOUTH
        );

        btnGuardar.addActionListener(
                e -> guardarEntrega()
        );

        btnEditar.addActionListener(
                e -> editarEntrega()
        );

        btnEliminar.addActionListener(
                e -> eliminarEntrega()
        );

        btnLimpiar.addActionListener(
                e -> limpiarFormulario()
        );

        tablaEntregas
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

        comboPedidos.removeAllItems();

        List<Object[]> pedidos =
                pedidoDAO.listarTodos();

        for (Object[] pedido :
                pedidos) {

            int id =
                    (int) pedido[0];

            String direccion =
                    pedido[1].toString();

            comboPedidos.addItem(
                    id
                            + " - "
                            + direccion
            );
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

    private void cargarEntregas() {

        modeloTabla.setRowCount(0);

        List<Entrega> entregas =
                entregaDAO.readAll();

        for (Entrega entrega :
                entregas) {

            modeloTabla.addRow(
                    new Object[]{
                            entrega.getId(),
                            entrega.getIdPedido()
                                    + " - "
                                    + buscarDireccionPedido(
                                    entrega.getIdPedido()
                            ),
                            entrega.getIdRepartidor()
                                    + " - "
                                    + buscarNombreRepartidor(
                                    entrega.getIdRepartidor()
                            ),
                            entrega.getFecha(),
                            entrega.getHora()
                    }
            );
        }
    }

    private void guardarEntrega() {

        Integer idPedido =
                obtenerIdPedidoSeleccionado();

        Repartidor repartidor =
                (Repartidor)
                        comboRepartidores
                                .getSelectedItem();

        if (idPedido == null
                || repartidor == null) {

            JOptionPane.showMessageDialog(
                    this,
                    "Debes seleccionar un pedido y un repartidor."
            );

            return;
        }

        try {

            LocalDate fecha =
                    LocalDate.parse(
                            txtFecha
                                    .getText()
                                    .trim()
                    );

            LocalTime hora =
                    LocalTime.parse(
                            txtHora
                                    .getText()
                                    .trim()
                    );

            Entrega entrega =
                    new Entrega(
                            idPedido,
                            repartidor.getId(),
                            fecha,
                            hora
                    );

            boolean guardada =
                    entregaDAO.create(entrega);

            if (guardada) {

                boolean estadoActualizado =
                        pedidoDAO.actualizarEstado(
                                idPedido,
                                "EN_REPARTO"
                        );

                if (!estadoActualizado) {

                    JOptionPane.showMessageDialog(
                            this,
                            "La entrega fue registrada, pero no se pudo actualizar el estado del pedido."
                    );

                    refrescarDatos();
                    limpiarFormulario();

                    return;
                }

                JOptionPane.showMessageDialog(
                        this,
                        "Entrega registrada correctamente."
                );

                refrescarDatos();
                limpiarFormulario();

            } else {

                JOptionPane.showMessageDialog(
                        this,
                        "No se pudo registrar la entrega."
                );
            }

        } catch (DateTimeParseException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Fecha u hora inválida.\n"
                            + "Fecha: AAAA-MM-DD\n"
                            + "Hora: HH:mm"
            );
        }
    }

    private void cargarSeleccion() {

        int fila =
                tablaEntregas
                        .getSelectedRow();

        if (fila == -1) {
            return;
        }

        idSeleccionado =
                Integer.parseInt(
                        modeloTabla
                                .getValueAt(
                                        fila,
                                        0
                                )
                                .toString()
                );

        String pedidoTexto =
                modeloTabla
                        .getValueAt(
                                fila,
                                1
                        )
                        .toString();

        int idPedido =
                Integer.parseInt(
                        pedidoTexto
                                .split(" - ")[0]
                );

        String repartidorTexto =
                modeloTabla
                        .getValueAt(
                                fila,
                                2
                        )
                        .toString();

        int idRepartidor =
                Integer.parseInt(
                        repartidorTexto
                                .split(" - ")[0]
                );

        seleccionarPedido(
                idPedido
        );

        seleccionarRepartidor(
                idRepartidor
        );

        txtFecha.setText(
                modeloTabla
                        .getValueAt(
                                fila,
                                3
                        )
                        .toString()
        );

        String hora =
                modeloTabla
                        .getValueAt(
                                fila,
                                4
                        )
                        .toString();

        if (hora.length() >= 5) {
            hora = hora.substring(
                    0,
                    5
            );
        }

        txtHora.setText(
                hora
        );
    }

    private void editarEntrega() {

        if (idSeleccionado == -1) {

            JOptionPane.showMessageDialog(
                    this,
                    "Selecciona una entrega."
            );

            return;
        }

        Integer idPedido =
                obtenerIdPedidoSeleccionado();

        Repartidor repartidor =
                (Repartidor)
                        comboRepartidores
                                .getSelectedItem();

        if (idPedido == null
                || repartidor == null) {

            JOptionPane.showMessageDialog(
                    this,
                    "Debes seleccionar un pedido y un repartidor."
            );

            return;
        }

        try {

            LocalDate fecha =
                    LocalDate.parse(
                            txtFecha
                                    .getText()
                                    .trim()
                    );

            LocalTime hora =
                    LocalTime.parse(
                            txtHora
                                    .getText()
                                    .trim()
                    );

            Entrega entrega =
                    new Entrega(
                            idSeleccionado,
                            idPedido,
                            repartidor.getId(),
                            fecha,
                            hora
                    );

            boolean actualizada =
                    entregaDAO.update(
                            entrega
                    );

            if (actualizada) {

                JOptionPane.showMessageDialog(
                        this,
                        "Entrega actualizada correctamente."
                );

                refrescarDatos();
                limpiarFormulario();

            } else {

                JOptionPane.showMessageDialog(
                        this,
                        "No se pudo actualizar la entrega."
                );
            }

        } catch (DateTimeParseException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Fecha u hora inválida.\n"
                            + "Fecha: AAAA-MM-DD\n"
                            + "Hora: HH:mm"
            );
        }
    }

    private void eliminarEntrega() {

        if (idSeleccionado == -1) {

            JOptionPane.showMessageDialog(
                    this,
                    "Selecciona una entrega."
            );

            return;
        }

        int respuesta =
                JOptionPane.showConfirmDialog(
                        this,
                        "¿Deseas eliminar esta entrega?",
                        "Confirmar eliminación",
                        JOptionPane.YES_NO_OPTION
                );

        if (respuesta !=
                JOptionPane.YES_OPTION) {

            return;
        }

        boolean eliminada =
                entregaDAO.delete(
                        idSeleccionado
                );

        if (eliminada) {

            JOptionPane.showMessageDialog(
                    this,
                    "Entrega eliminada correctamente."
            );

            refrescarDatos();
            limpiarFormulario();

        } else {

            JOptionPane.showMessageDialog(
                    this,
                    "No se pudo eliminar la entrega."
            );
        }
    }

    private Integer obtenerIdPedidoSeleccionado() {

        String seleccionado =
                (String)
                        comboPedidos
                                .getSelectedItem();

        if (seleccionado == null) {
            return null;
        }

        String idTexto =
                seleccionado
                        .split(" - ")[0];

        return Integer.parseInt(
                idTexto
        );
    }

    private void seleccionarPedido(
            int idPedido
    ) {

        for (
                int i = 0;
                i < comboPedidos.getItemCount();
                i++
        ) {

            String item =
                    comboPedidos.getItemAt(i);

            int id =
                    Integer.parseInt(
                            item
                                    .split(" - ")[0]
                    );

            if (id == idPedido) {

                comboPedidos.setSelectedIndex(
                        i
                );

                break;
            }
        }
    }

    private void seleccionarRepartidor(
            int idRepartidor
    ) {

        for (
                int i = 0;
                i < comboRepartidores.getItemCount();
                i++
        ) {

            Repartidor repartidor =
                    comboRepartidores
                            .getItemAt(i);

            if (repartidor.getId()
                    == idRepartidor) {

                comboRepartidores
                        .setSelectedIndex(i);

                break;
            }
        }
    }

    private String buscarDireccionPedido(
            int idPedido
    ) {

        List<Object[]> pedidos =
                pedidoDAO.listarTodos();

        for (Object[] pedido :
                pedidos) {

            int id =
                    (int) pedido[0];

            if (id == idPedido) {

                return pedido[1]
                        .toString();
            }
        }

        return "Pedido no encontrado";
    }

    private String buscarNombreRepartidor(
            int idRepartidor
    ) {

        List<Repartidor> repartidores =
                repartidorDAO.readAll();

        for (Repartidor repartidor :
                repartidores) {

            if (repartidor.getId()
                    == idRepartidor) {

                return repartidor
                        .getNombre();
            }
        }

        return "Repartidor no encontrado";
    }

    private void refrescarDatos() {

        cargarPedidos();
        cargarRepartidores();
        cargarEntregas();
    }

    private void limpiarFormulario() {

        idSeleccionado = -1;

        tablaEntregas
                .clearSelection();

        if (comboPedidos
                .getItemCount() > 0) {

            comboPedidos
                    .setSelectedIndex(0);
        }

        if (comboRepartidores
                .getItemCount() > 0) {

            comboRepartidores
                    .setSelectedIndex(0);
        }

        txtFecha.setText(
                LocalDate.now()
                        .toString()
        );

        String hora =
                LocalTime.now()
                        .withSecond(0)
                        .withNano(0)
                        .toString();

        txtHora.setText(
                hora
        );
    }
}