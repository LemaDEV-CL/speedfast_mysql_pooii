package cl.lema.vista;

import cl.lema.models.*;
import cl.lema.servicio.ControladorDeEnvios;

import javax.swing.*;
import java.awt.*;

/**
 * Permite registrar pedidos utilizando el formulario desarrollado en semanas anteriores.
 * Conserva las validaciones y funcionalidades previas del proyecto SpeedFast.
 */
public class VentanaRegistroPedido extends JFrame {

    private JTextField txtCliente;
    private JTextField txtDireccion;
    private JTextField txtDistancia;

    private JComboBox<String> comboTipo;

    private JTextField txtRestaurante;
    private JTextField txtTiempoPreparacion;
    private JTextField txtPeso;
    private JTextField txtVolumen;
    private JTextField txtTienda;

    private JButton btnGuardar;
    private JPanel panelTipos;
    private CardLayout layoutTipos;

    private final ControladorDeEnvios controlador =
            new ControladorDeEnvios();

    public VentanaRegistroPedido() {
        setTitle("Registrar pedido");
        setSize(550, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        crearComponentes();

        setLocationRelativeTo(null);
    }

    private void crearComponentes() {
        setLayout(new BorderLayout(10, 10));

        txtCliente = new JTextField();
        txtDireccion = new JTextField();
        txtDistancia = new JTextField();

        txtRestaurante = new JTextField();
        txtTiempoPreparacion = new JTextField();
        txtPeso = new JTextField();
        txtVolumen = new JTextField();
        txtTienda = new JTextField();

        comboTipo = new JComboBox<>(
                new String[]{"Comida", "Encomienda", "Express"}
        );

        JPanel panelComun = new JPanel(
                new GridLayout(4, 2, 10, 10)
        );
                panelComun.add(new JLabel("Cliente:"));
        panelComun.add(txtCliente);

        panelComun.add(new JLabel("Dirección:"));
        panelComun.add(txtDireccion);

        panelComun.add(new JLabel("Distancia (km):"));
        panelComun.add(txtDistancia);

        panelComun.add(new JLabel("Tipo:"));
        panelComun.add(comboTipo);

        JPanel panelComida = new JPanel(
                new GridLayout(2, 2, 10, 10)
        );

        panelComida.add(new JLabel("Restaurante:"));
        panelComida.add(txtRestaurante);

        panelComida.add(new JLabel("Preparación (min):"));
        panelComida.add(txtTiempoPreparacion);

        JPanel panelEncomienda = new JPanel(
                new GridLayout(2, 2, 10, 10)
        );

        panelEncomienda.add(new JLabel("Peso:"));
        panelEncomienda.add(txtPeso);

        panelEncomienda.add(new JLabel("Volumen:"));
        panelEncomienda.add(txtVolumen);

        JPanel panelExpress = new JPanel(
                new GridLayout(1, 2, 10, 10)
        );

        panelExpress.add(new JLabel("Tienda:"));
        panelExpress.add(txtTienda);

        layoutTipos = new CardLayout();
        panelTipos = new JPanel(layoutTipos);

        panelTipos.add(panelComida, "Comida");
        panelTipos.add(panelEncomienda, "Encomienda");
        panelTipos.add(panelExpress, "Express");

        comboTipo.addActionListener(e -> {
            String tipoSeleccionado =
                    (String) comboTipo.getSelectedItem();

            layoutTipos.show(panelTipos, tipoSeleccionado);
        });

        layoutTipos.show(panelTipos, "Comida");

        btnGuardar = new JButton("Guardar");
        btnGuardar.addActionListener(e -> guardarPedido());

        add(panelComun, BorderLayout.NORTH);
        add(panelTipos, BorderLayout.CENTER);
        add(btnGuardar, BorderLayout.SOUTH);
    }

    private void guardarPedido() {
        String cliente = txtCliente.getText().trim();
        String direccion = txtDireccion.getText().trim();
        String distanciaTexto = txtDistancia.getText().trim();

        if (cliente.isEmpty() || direccion.isEmpty() || distanciaTexto.isEmpty()) {
            JOptionPane.showMessageDialog(
                    this,
                    "Completa cliente, dirección y distancia."
            );
            return;
        }

        try {
            double distancia = Double.parseDouble(distanciaTexto);

            if (distancia < 0) {
                JOptionPane.showMessageDialog(
                        this,
                        "La distancia debe ser un número mayor a cero."
                );
                return;
            }

            String tipo = (String) comboTipo.getSelectedItem();
            Pedido pedido;

            if ("Comida".equals(tipo)) {
                String restaurante =
                        txtRestaurante.getText().trim();

                String preparacionTexto =
                        txtTiempoPreparacion.getText().trim();

                if (restaurante.isEmpty()
                        || preparacionTexto.isEmpty()) {

                    JOptionPane.showMessageDialog(
                            this,
                            "Completa restaurante y tiempo de preparación."
                    );
                    return;
                }

                int tiempoPreparacion =
                        Integer.parseInt(preparacionTexto);

                if (tiempoPreparacion < 0) {
                    JOptionPane.showMessageDialog(
                            this,
                            "El tiempo de preparación no puede ser negativo."
                    );
                    return;
                }

                pedido = new PedidoComida(
                        cliente,
                        direccion,
                        distancia,
                        restaurante,
                        tiempoPreparacion
                );

            } else if ("Encomienda".equals(tipo)) {
                String pesoTexto = txtPeso.getText().trim();
                String volumenTexto = txtVolumen.getText().trim();

                if (pesoTexto.isEmpty() || volumenTexto.isEmpty()) {
                    JOptionPane.showMessageDialog(
                            this,
                            "Completa peso y volumen."
                    );
                    return;
                }

                int peso = Integer.parseInt(pesoTexto);
                int volumen = Integer.parseInt(volumenTexto);

                if (peso <= 0 || volumen <= 0) {
                    JOptionPane.showMessageDialog(
                            this,
                            "Peso y volumen deben ser enteros "
                                    + "mayores que cero."
                    );
                    return;
                }

                pedido = new PedidoEncomienda(
                        cliente,
                        direccion,
                        distancia,
                        peso,
                        volumen
                );

            } else if ("Express".equals(tipo)) {
                String tienda = txtTienda.getText().trim();

                if (tienda.isEmpty()) {
                    JOptionPane.showMessageDialog(
                            this,
                            "Completa el nombre de la tienda."
                    );
                    return;
                }

                pedido = new PedidoExpress(
                        cliente,
                        direccion,
                        distancia,
                        tienda
                );

            } else {
                JOptionPane.showMessageDialog(
                        this,
                        "Selecciona un tipo de pedido válido."
                );
                return;
            }

            boolean registrado = controlador.agregarPedido(pedido);

            if (!registrado) {
                JOptionPane.showMessageDialog(
                        this,
                        "No se pudo registrar el pedido."
                );
                return;
            }

            JOptionPane.showMessageDialog(
                    this,
                    "Pedido registrado correctamente con ID: "
                            + pedido.getIdPedido()
            );

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(
                    this,
                    "Revisa los campos numéricos del tipo seleccionado.\n"
            );
        }
    }
}