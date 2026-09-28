package cl.lema.app;

import cl.lema.models.*;
import cl.lema.vista.VentanaPrincipal;

import javax.swing.*;

/**
 * Inicia SpeedFast y abre la ventana principal con Swing.
 */
public class Main {

    public static void main(String[] args)  {
        SwingUtilities.invokeLater(() -> {
            VentanaPrincipal ventana = new VentanaPrincipal();
            ventana.setVisible(true);
        });
    }
}
