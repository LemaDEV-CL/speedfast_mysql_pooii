package cl.lema.dao;

import cl.lema.conexion.ConexionBD;
import cl.lema.models.Pedido;
import cl.lema.models.PedidoComida;
import cl.lema.models.PedidoEncomienda;
import cl.lema.models.PedidoExpress;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Gestiona la persistencia de pedidos en MySQL mediante JDBC.
 * Implementa las operaciones CRUD y consultas auxiliares utilizadas por la aplicación.
 */

public class PedidoDAO {

    public boolean guardar(Pedido pedido) {

        String sql = """
                INSERT INTO pedido (direccion, tipo, estado)
                VALUES (?, ?, ?)
                """;

        try (
                Connection conexion = ConexionBD.obtenerConexion();
                PreparedStatement statement = conexion.prepareStatement(
                        sql,
                        Statement.RETURN_GENERATED_KEYS
                )
        ) {

            statement.setString(
                    1,
                    pedido.getDireccion()
            );

            statement.setString(
                    2,
                    obtenerTipoPedido(pedido)
            );

            statement.setString(
                    3,
                    pedido.getEstado().name()
            );

            int filasAfectadas = statement.executeUpdate();

            if (filasAfectadas > 0) {

                try (ResultSet claves = statement.getGeneratedKeys()) {

                    if (claves.next()) {

                        int idGenerado = claves.getInt(1);
                        pedido.setIdPedido(idGenerado);

                        return true;
                    }
                }
            }

        } catch (SQLException e) {

            System.out.println(
                    "Error al guardar pedido: "
                            + e.getMessage()
            );
        }

        return false;
    }

    public boolean create(
            String direccion,
            String tipo,
            String estado
    ) {

        String sql = """
                INSERT INTO pedido (direccion, tipo, estado)
                VALUES (?, ?, ?)
                """;

        try (
                Connection conexion = ConexionBD.obtenerConexion();
                PreparedStatement statement =
                        conexion.prepareStatement(sql)
        ) {

            statement.setString(1, direccion);
            statement.setString(2, tipo);
            statement.setString(3, estado);

            int filasAfectadas =
                    statement.executeUpdate();

            return filasAfectadas > 0;

        } catch (SQLException e) {

            System.out.println(
                    "Error al crear pedido: "
                            + e.getMessage()
            );

            return false;
        }
    }

    private String obtenerTipoPedido(Pedido pedido) {

        if (pedido instanceof PedidoComida) {
            return "COMIDA";
        }

        if (pedido instanceof PedidoEncomienda) {
            return "ENCOMIENDA";
        }

        if (pedido instanceof PedidoExpress) {
            return "EXPRESS";
        }

        throw new IllegalArgumentException(
                "Tipo de pedido no reconocido."
        );
    }

    public boolean actualizarEstado(
            int idPedido,
            String nuevoEstado
    ) {

        String sql = """
                UPDATE pedido
                SET estado = ?
                WHERE id = ?
                """;

        try (
                Connection conexion =
                        ConexionBD.obtenerConexion();

                PreparedStatement statement =
                        conexion.prepareStatement(sql)
        ) {

            statement.setString(
                    1,
                    nuevoEstado
            );

            statement.setInt(
                    2,
                    idPedido
            );

            int filasAfectadas =
                    statement.executeUpdate();

            return filasAfectadas > 0;

        } catch (SQLException e) {

            System.out.println(
                    "Error al actualizar estado del pedido: "
                            + e.getMessage()
            );

            return false;
        }
    }

    public List<Object[]> listarTodos() {

        List<Object[]> pedidos =
                new ArrayList<>();

        String sql = """
                SELECT
                    p.id,
                    p.direccion,
                    p.tipo,
                    p.estado,
                    r.nombre AS repartidor
                FROM pedido p
                LEFT JOIN entrega e
                    ON p.id = e.id_pedido
                LEFT JOIN repartidor r
                    ON e.id_repartidor = r.id
                ORDER BY p.id
                """;

        try (
                Connection conexion =
                        ConexionBD.obtenerConexion();

                PreparedStatement statement =
                        conexion.prepareStatement(sql);

                ResultSet resultado =
                        statement.executeQuery()
        ) {

            while (resultado.next()) {

                pedidos.add(
                        new Object[]{
                                resultado.getInt("id"),
                                resultado.getString("direccion"),
                                resultado.getString("tipo"),
                                resultado.getString("estado"),
                                resultado.getString("repartidor")
                        }
                );
            }

        } catch (SQLException e) {

            System.out.println(
                    "Error al listar pedidos: "
                            + e.getMessage()
            );
        }

        return pedidos;
    }

    public List<Object[]> readAll() {
        return listarTodos();
    }

    public List<Integer> listarPendientes() {

        List<Integer> pedidosPendientes =
                new ArrayList<>();

        String sql = """
                SELECT id
                FROM pedido
                WHERE estado = ?
                ORDER BY id
                """;

        try (
                Connection conexion =
                        ConexionBD.obtenerConexion();

                PreparedStatement statement =
                        conexion.prepareStatement(sql)
        ) {

            statement.setString(
                    1,
                    "PENDIENTE"
            );

            try (
                    ResultSet resultado =
                            statement.executeQuery()
            ) {

                while (resultado.next()) {

                    pedidosPendientes.add(
                            resultado.getInt("id")
                    );
                }
            }

        } catch (SQLException e) {

            System.out.println(
                    "Error al listar pedidos pendientes: "
                            + e.getMessage()
            );
        }

        return pedidosPendientes;
    }

    public boolean update(
            int id,
            String direccion,
            String tipo,
            String estado
    ) {

        String sql = """
                UPDATE pedido
                SET direccion = ?,
                    tipo = ?,
                    estado = ?
                WHERE id = ?
                """;

        try (
                Connection conexion =
                        ConexionBD.obtenerConexion();

                PreparedStatement statement =
                        conexion.prepareStatement(sql)
        ) {

            statement.setString(1, direccion);
            statement.setString(2, tipo);
            statement.setString(3, estado);
            statement.setInt(4, id);

            int filasAfectadas =
                    statement.executeUpdate();

            return filasAfectadas > 0;

        } catch (SQLException e) {

            System.out.println(
                    "Error al actualizar pedido: "
                            + e.getMessage()
            );

            return false;
        }
    }

    public boolean delete(int id) {

        String sql = """
                DELETE FROM pedido
                WHERE id = ?
                """;

        try (
                Connection conexion =
                        ConexionBD.obtenerConexion();

                PreparedStatement statement =
                        conexion.prepareStatement(sql)
        ) {

            statement.setInt(1, id);

            int filasAfectadas =
                    statement.executeUpdate();

            return filasAfectadas > 0;

        } catch (SQLException e) {

            System.out.println(
                    "Error al eliminar pedido: "
                            + e.getMessage()
            );

            return false;
        }
    }
}