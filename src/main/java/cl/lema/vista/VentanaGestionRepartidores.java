package cl.lema.vista;

import cl.lema.dao.RepartidorDAO;
import cl.lema.models.Repartidor;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * Permite gestionar repartidores mediante una interfaz gráfica Swing.
 * Integra las operaciones CRUD con RepartidorDAO y MySQL.
 */

public class VentanaGestionRepartidores extends JFrame {

    private JTextField txtNombre;

    private JButton btnGuardar;
    private JButton btnEditar;
    private JButton btnEliminar;
    private JButton btnLimpiar;

    private JTable tablaRepartidores;
    private DefaultTableModel modeloTabla;

    private final RepartidorDAO repartidorDAO =
            new RepartidorDAO();

    private int idSeleccionado = -1;

    public VentanaGestionRepartidores() {

        setTitle("Gestión de Repartidores");
        setSize(600, 450);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        inicializarComponentes();
        cargarRepartidores();
    }

    private void inicializarComponentes() {

        setLayout(new BorderLayout(10, 10));

        JPanel panelFormulario =
                new JPanel(new GridLayout(2, 2, 10, 10));

        panelFormulario.setBorder(
                BorderFactory.createEmptyBorder(
                        10, 10, 10, 10
                )
        );

        panelFormulario.add(
                new JLabel("Nombre:")
        );

        txtNombre = new JTextField();
        panelFormulario.add(txtNombre);

        add(
                panelFormulario,
                BorderLayout.NORTH
        );

        modeloTabla = new DefaultTableModel(
                new String[]{
                        "ID",
                        "Nombre"
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

        tablaRepartidores =
                new JTable(modeloTabla);

        add(
                new JScrollPane(tablaRepartidores),
                BorderLayout.CENTER
        );

        JPanel panelBotones =
                new JPanel();

        btnGuardar =
                new JButton("Guardar");

        btnEditar =
                new JButton("Editar");

        btnEliminar =
                new JButton("Eliminar");

        btnLimpiar =
                new JButton("Limpiar");

        panelBotones.add(btnGuardar);
        panelBotones.add(btnEditar);
        panelBotones.add(btnEliminar);
        panelBotones.add(btnLimpiar);

        add(
                panelBotones,
                BorderLayout.SOUTH
        );

        btnGuardar.addActionListener(
                e -> guardarRepartidor()
        );

        btnEditar.addActionListener(
                e -> editarRepartidor()
        );

        btnEliminar.addActionListener(
                e -> eliminarRepartidor()
        );

        btnLimpiar.addActionListener(
                e -> limpiarFormulario()
        );

        tablaRepartidores
                .getSelectionModel()
                .addListSelectionListener(
                        e -> cargarSeleccion()
                );
    }

    private void cargarRepartidores() {

        modeloTabla.setRowCount(0);

        List<Repartidor> repartidores =
                repartidorDAO.readAll();

        for (Repartidor repartidor :
                repartidores) {

            modeloTabla.addRow(
                    new Object[]{
                            repartidor.getId(),
                            repartidor.getNombre()
                    }
            );
        }
    }

    private void guardarRepartidor() {

        String nombre =
                txtNombre.getText().trim();

        if (nombre.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Debes ingresar un nombre."
            );

            return;
        }

        Repartidor repartidor =
                new Repartidor(nombre);

        boolean guardado =
                repartidorDAO.create(
                        repartidor
                );

        if (guardado) {

            JOptionPane.showMessageDialog(
                    this,
                    "Repartidor registrado correctamente."
            );

            cargarRepartidores();
            limpiarFormulario();

        } else {

            JOptionPane.showMessageDialog(
                    this,
                    "No se pudo registrar el repartidor."
            );
        }
    }

    private void cargarSeleccion() {

        int fila =
                tablaRepartidores
                        .getSelectedRow();

        if (fila == -1) {
            return;
        }

        idSeleccionado =
                (int) modeloTabla
                        .getValueAt(
                                fila,
                                0
                        );

        String nombre =
                modeloTabla
                        .getValueAt(
                                fila,
                                1
                        )
                        .toString();

        txtNombre.setText(nombre);
    }

    private void editarRepartidor() {

        if (idSeleccionado == -1) {

            JOptionPane.showMessageDialog(
                    this,
                    "Selecciona un repartidor."
            );

            return;
        }

        String nombre =
                txtNombre.getText().trim();

        if (nombre.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Debes ingresar un nombre."
            );

            return;
        }

        Repartidor repartidor =
                new Repartidor(
                        idSeleccionado,
                        nombre
                );

        boolean actualizado =
                repartidorDAO.update(
                        repartidor
                );

        if (actualizado) {

            JOptionPane.showMessageDialog(
                    this,
                    "Repartidor actualizado correctamente."
            );

            cargarRepartidores();
            limpiarFormulario();

        } else {

            JOptionPane.showMessageDialog(
                    this,
                    "No se pudo actualizar el repartidor."
            );
        }
    }

    private void eliminarRepartidor() {

        if (idSeleccionado == -1) {

            JOptionPane.showMessageDialog(
                    this,
                    "Selecciona un repartidor."
            );

            return;
        }

        int opcion =
                JOptionPane.showConfirmDialog(
                        this,
                        "¿Deseas eliminar este repartidor?",
                        "Confirmar eliminación",
                        JOptionPane.YES_NO_OPTION
                );

        if (opcion !=
                JOptionPane.YES_OPTION) {

            return;
        }

        boolean eliminado =
                repartidorDAO.delete(
                        idSeleccionado
                );

        if (eliminado) {

            JOptionPane.showMessageDialog(
                    this,
                    "Repartidor eliminado correctamente."
            );

            cargarRepartidores();
            limpiarFormulario();

        } else {

            JOptionPane.showMessageDialog(
                    this,
                    "No se pudo eliminar el repartidor."
            );
        }
    }

    private void limpiarFormulario() {

        txtNombre.setText("");

        idSeleccionado = -1;

        tablaRepartidores
                .clearSelection();

        txtNombre.requestFocus();
    }
}

