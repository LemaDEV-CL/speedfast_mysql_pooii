package cl.lema.dao;

import cl.lema.conexion.ConexionBD;
import cl.lema.models.Repartidor;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Gestiona la persistencia de repartidores en MySQL mediante JDBC.
 * Implementa las operaciones CRUD para registrar, listar, actualizar y eliminar repartidores.
 */

public class RepartidorDAO {

    public boolean create(Repartidor repartidor) {

        String sql = """
                INSERT INTO repartidor (nombre)
                VALUES (?)
                """;

        try (
                Connection conexion = ConexionBD.obtenerConexion();
                PreparedStatement statement =
                        conexion.prepareStatement(
                                sql,
                                Statement.RETURN_GENERATED_KEYS
                        )
        ) {

            statement.setString(
                    1,
                    repartidor.getNombre()
            );

            int filasAfectadas =
                    statement.executeUpdate();

            if (filasAfectadas > 0) {

                try (ResultSet claves =
                             statement.getGeneratedKeys()) {

                    if (claves.next()) {
                        repartidor.setId(
                                claves.getInt(1)
                        );
                    }
                }

                return true;
            }

        } catch (SQLException e) {

            System.out.println(
                    "Error al crear repartidor: "
                            + e.getMessage()
            );
        }

        return false;
    }

    public List<Repartidor> readAll() {

        List<Repartidor> repartidores =
                new ArrayList<>();

        String sql = """
                SELECT id, nombre
                FROM repartidor
                ORDER BY id
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

                Repartidor repartidor =
                        new Repartidor(
                                resultado.getInt("id"),
                                resultado.getString("nombre")
                        );

                repartidores.add(repartidor);
            }

        } catch (SQLException e) {

            System.out.println(
                    "Error al listar repartidores: "
                            + e.getMessage()
            );
        }

        return repartidores;
    }

    public boolean update(Repartidor repartidor) {

        String sql = """
                UPDATE repartidor
                SET nombre = ?
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
                    repartidor.getNombre()
            );

            statement.setInt(
                    2,
                    repartidor.getId()
            );

            int filasAfectadas =
                    statement.executeUpdate();

            return filasAfectadas > 0;

        } catch (SQLException e) {

            System.out.println(
                    "Error al actualizar repartidor: "
                            + e.getMessage()
            );

            return false;
        }
    }

    public boolean delete(int id) {

        String sql = """
                DELETE FROM repartidor
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
                    "Error al eliminar repartidor: "
                            + e.getMessage()
            );

            return false;
        }
    }
}