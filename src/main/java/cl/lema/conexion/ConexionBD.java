package cl.lema.conexion;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Conecta la aplicación con MySQL mediante JDBC.
 * Crea la base de datos y las tablas si no existen y carga los repartidores iniciales.
 */
public class ConexionBD {

    private static final String URL = "jdbc:mysql://localhost:3306/speedfast?createDatabaseIfNotExist=true";
    private static final String USER = "speedadmin";
    private static final String PASSWORD = "admin1234";

    static {
        inicializarTabla();
    }

    public static Connection obtenerConexion() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    private static void inicializarTabla() {

        String sqlRepartidor = """
            CREATE TABLE IF NOT EXISTS repartidor (
                id INT AUTO_INCREMENT PRIMARY KEY,
                nombre VARCHAR(100) NOT NULL UNIQUE
            )
            """;

        String sqlPedido = """
            CREATE TABLE IF NOT EXISTS pedido (
                id INT AUTO_INCREMENT PRIMARY KEY,
                direccion VARCHAR(150) NOT NULL,
                tipo VARCHAR(30) NOT NULL,
                estado VARCHAR(20) NOT NULL
            )
            """;

        String sqlEntrega = """
            CREATE TABLE IF NOT EXISTS entrega (
                id INT AUTO_INCREMENT PRIMARY KEY,
                id_pedido INT NOT NULL,
                id_repartidor INT NOT NULL,
                fecha DATE NOT NULL,
                hora TIME NOT NULL,

                CONSTRAINT fk_entrega_pedido
                    FOREIGN KEY (id_pedido)
                    REFERENCES pedido(id),

                CONSTRAINT fk_entrega_repartidor
                    FOREIGN KEY (id_repartidor)
                    REFERENCES repartidor(id)
            )
            """;

        try (Connection conexion = obtenerConexion();
             Statement statement = conexion.createStatement()) {

            statement.executeUpdate(sqlRepartidor);
            statement.executeUpdate(sqlPedido);
            statement.executeUpdate(sqlEntrega);

            String sqlRepartidoresIniciales = """
                INSERT IGNORE INTO repartidor (nombre)
                VALUES
                    ('Iván'),
                    ('Luis'),
                    ('Pedro')
                """;

            statement.executeUpdate(sqlRepartidoresIniciales);

            System.out.println("Base de datos inicializada correctamente.");

        } catch (Exception e) {
            System.out.println("Error al crear las tablas: " + e.getMessage());
        }
    }
}
