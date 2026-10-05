package cl.lema.app;

import cl.lema.models.*;
import cl.lema.vista.VentanaPrincipal;

import javax.swing.*;

/**
 * Punto de entrada de la aplicación SpeedFast.
 * Inicia la interfaz gráfica y abre la ventana principal.
 */
public class Main {

    public static void main(String[] args)  {
        SwingUtilities.invokeLater(() -> {
            VentanaPrincipal ventana = new VentanaPrincipal();
            ventana.setVisible(true);
        });
    }
}
