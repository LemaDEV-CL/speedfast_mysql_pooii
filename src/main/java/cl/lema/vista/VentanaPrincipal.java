package cl.lema.vista;

import javax.swing.*;
import java.awt.*;

/**
 * Muestra el menú de SpeedFast con Swing.
 * Abre las ventanas de registro, listado y asignación conectadas con MySQL.
 */
public class VentanaPrincipal extends javax.swing.JFrame {

    public VentanaPrincipal() {
        setTitle("SpeedFast - Gestor de pedidos");
        setSize(550, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        setLayout(new GridLayout(3, 1, 10, 10));

        JButton btnRegistrar = new JButton("Registrar pedido");
        JButton btnListar = new JButton("Listar pedidos");
        JButton btnIniciarEntrega = new JButton("Asignar repartidor / Iniciar entrega");

        add(btnRegistrar);
        add(btnListar);

        btnListar.addActionListener(e -> {
            VentanaListaPedidos ventana = new VentanaListaPedidos();
            ventana.setVisible(true);
        });

        add(btnIniciarEntrega);

        btnIniciarEntrega.addActionListener(e -> {
            VentanaAsignarRepartidor ventana =
                    new VentanaAsignarRepartidor();

            ventana.setVisible(true);
        });

        btnRegistrar.addActionListener(e -> {
            VentanaRegistroPedido ventana = new VentanaRegistroPedido();
            ventana.setVisible(true);
        });
    }

}
