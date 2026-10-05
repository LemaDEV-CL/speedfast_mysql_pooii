package cl.lema.vista;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;
import cl.lema.dao.PedidoDAO;
import java.util.List;

/**
 * Muestra los pedidos almacenados en MySQL mediante una JTable.
 * Permite actualizar la información consultando nuevamente PedidoDAO.
 */
public class VentanaListaPedidos extends JFrame {

    private DefaultTableModel modeloTabla;
    private JTable tabla;
    private final PedidoDAO pedidoDAO = new PedidoDAO();

    public VentanaListaPedidos() {
        setTitle("Listado de pedidos");
        setSize(800, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        crearComponentes();
        cargarPedidos();

        setLocationRelativeTo(null);
    }

    private void crearComponentes() {
        setLayout(new BorderLayout(10, 10));

        modeloTabla = new DefaultTableModel(
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
            public boolean isCellEditable(int fila, int columna) {
                return false;
            }
        };

        tabla = new JTable(modeloTabla);

        JButton btnActualizar = new JButton("Actualizar");
        btnActualizar.addActionListener(e -> cargarPedidos());

        add(new JScrollPane(tabla), BorderLayout.CENTER);
        add(btnActualizar, BorderLayout.SOUTH);
    }

    private void cargarPedidos() {

        modeloTabla.setRowCount(0);

        List<Object[]> pedidos =
                pedidoDAO.listarTodos();

        for (Object[] pedido : pedidos) {

            String repartidor =
                    (String) pedido[4];

            if (repartidor == null) {
                repartidor = "Sin asignar";
            }

            modeloTabla.addRow(new Object[]{
                    pedido[0],
                    pedido[1],
                    pedido[2],
                    pedido[3],
                    repartidor
            });
        }
    }
}

