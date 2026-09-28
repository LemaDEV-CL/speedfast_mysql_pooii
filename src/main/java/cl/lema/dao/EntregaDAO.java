package cl.lema.dao;

import cl.lema.conexion.ConexionBD;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Guarda en MySQL la asignación de un pedido a un repartidor, con fecha y hora.
 */
public class EntregaDAO {

    public boolean guardar(
            int idPedido,
            int idRepartidor,
            LocalDate fecha,
            LocalTime hora
    ) {

        String sql = """
                INSERT INTO entrega
                (id_pedido, id_repartidor, fecha, hora)
                VALUES (?, ?, ?, ?)
                """;

        try (
                Connection conexion = ConexionBD.obtenerConexion();
                PreparedStatement statement =
                        conexion.prepareStatement(sql)
        ) {

            statement.setInt(1, idPedido);
            statement.setInt(2, idRepartidor);
            statement.setDate(
                    3,
                    java.sql.Date.valueOf(fecha)
            );
            statement.setTime(
                    4,
                    java.sql.Time.valueOf(hora)
            );

            int filasAfectadas =
                    statement.executeUpdate();

            return filasAfectadas > 0;

        } catch (SQLException e) {

            System.out.println(
                    "Error al guardar entrega: "
                            + e.getMessage()
            );

            return false;
        }
    }
}