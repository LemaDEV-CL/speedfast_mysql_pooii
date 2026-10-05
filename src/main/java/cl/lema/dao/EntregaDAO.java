package cl.lema.dao;

import cl.lema.conexion.ConexionBD;
import cl.lema.models.Entrega;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Gestiona la persistencia de entregas en MySQL mediante JDBC.
 * Implementa las operaciones CRUD y relaciona pedidos con repartidores.
 */

public class EntregaDAO {

    public boolean create(Entrega entrega) {

        String sql = """
                INSERT INTO entrega
                (id_pedido, id_repartidor, fecha, hora)
                VALUES (?, ?, ?, ?)
                """;

        try (
                Connection conexion = ConexionBD.obtenerConexion();
                PreparedStatement statement = conexion.prepareStatement(
                        sql,
                        Statement.RETURN_GENERATED_KEYS
                )
        ) {

            statement.setInt(1, entrega.getIdPedido());
            statement.setInt(2, entrega.getIdRepartidor());
            statement.setDate(
                    3,
                    Date.valueOf(entrega.getFecha())
            );
            statement.setTime(
                    4,
                    Time.valueOf(entrega.getHora())
            );

            int filasAfectadas = statement.executeUpdate();

            if (filasAfectadas > 0) {

                try (ResultSet claves = statement.getGeneratedKeys()) {

                    if (claves.next()) {
                        entrega.setId(claves.getInt(1));
                    }
                }

                return true;
            }

        } catch (SQLException e) {

            System.out.println(
                    "Error al crear entrega: "
                            + e.getMessage()
            );
        }

        return false;
    }

    public List<Entrega> readAll() {

        List<Entrega> entregas =
                new ArrayList<>();

        String sql = """
                SELECT id,
                       id_pedido,
                       id_repartidor,
                       fecha,
                       hora
                FROM entrega
                ORDER BY id
                """;

        try (
                Connection conexion = ConexionBD.obtenerConexion();
                PreparedStatement statement =
                        conexion.prepareStatement(sql);
                ResultSet resultado =
                        statement.executeQuery()
        ) {

            while (resultado.next()) {

                Entrega entrega =
                        new Entrega(
                                resultado.getInt("id"),
                                resultado.getInt("id_pedido"),
                                resultado.getInt("id_repartidor"),
                                resultado.getDate("fecha").toLocalDate(),
                                resultado.getTime("hora").toLocalTime()
                        );

                entregas.add(entrega);
            }

        } catch (SQLException e) {

            System.out.println(
                    "Error al listar entregas: "
                            + e.getMessage()
            );
        }

        return entregas;
    }

    public boolean update(Entrega entrega) {

        String sql = """
                UPDATE entrega
                SET id_pedido = ?,
                    id_repartidor = ?,
                    fecha = ?,
                    hora = ?
                WHERE id = ?
                """;

        try (
                Connection conexion = ConexionBD.obtenerConexion();
                PreparedStatement statement =
                        conexion.prepareStatement(sql)
        ) {

            statement.setInt(1, entrega.getIdPedido());
            statement.setInt(2, entrega.getIdRepartidor());
            statement.setDate(
                    3,
                    Date.valueOf(entrega.getFecha())
            );
            statement.setTime(
                    4,
                    Time.valueOf(entrega.getHora())
            );
            statement.setInt(5, entrega.getId());

            int filasAfectadas =
                    statement.executeUpdate();

            return filasAfectadas > 0;

        } catch (SQLException e) {

            System.out.println(
                    "Error al actualizar entrega: "
                            + e.getMessage()
            );

            return false;
        }
    }

    public boolean delete(int id) {

        String sql = """
                DELETE FROM entrega
                WHERE id = ?
                """;

        try (
                Connection conexion = ConexionBD.obtenerConexion();
                PreparedStatement statement =
                        conexion.prepareStatement(sql)
        ) {

            statement.setInt(1, id);

            int filasAfectadas =
                    statement.executeUpdate();

            return filasAfectadas > 0;

        } catch (SQLException e) {

            System.out.println(
                    "Error al eliminar entrega: "
                            + e.getMessage()
            );

            return false;
        }
    }
}