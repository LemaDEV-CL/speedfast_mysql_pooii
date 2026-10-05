package cl.lema.vista;

import javax.swing.*;
import java.awt.*;

/**
 * Muestra el menú principal de SpeedFast.
 * Permite acceder a la gestión de repartidores, pedidos y entregas.
 */
public class VentanaPrincipal extends javax.swing.JFrame {

    public VentanaPrincipal() {
        setTitle("SpeedFast - Gestor de pedidos");
        setSize(550, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        setLayout(new GridLayout(3, 1, 10, 10));

        JButton btnGestionRepartidores = new JButton("Gestión de Repartidores");
        JButton btnGestionEntregas = new JButton("Gestión de entregas");
        JButton btnGestionPedidos = new JButton("Gestión de pedidos");

        btnGestionRepartidores.addActionListener(e -> {
            VentanaGestionRepartidores ventana = new VentanaGestionRepartidores();
            ventana.setVisible(true);
        });

        btnGestionEntregas.addActionListener(
                e -> new VentanaGestionEntregas()
                        .setVisible(true)
        );

        btnGestionPedidos.addActionListener(
                e -> new VentanaGestionPedidos()
                        .setVisible(true)
        );

        add(btnGestionRepartidores);
        add(btnGestionPedidos);
        add(btnGestionEntregas);




    }



}
