package pe.edu.utp.siged.util;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/** Fabrica de conexiones JDBC. Lee los datos de db.properties (no se escriben en el codigo). */
public final class Conexion {

    private static final Properties PROPS = new Properties();

    static {
        try (InputStream in = Conexion.class.getClassLoader().getResourceAsStream("db.properties")) {
            PROPS.load(in);
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (IOException | ClassNotFoundException | NullPointerException e) {
            throw new ExceptionInInitializerError("No se pudo cargar db.properties o el driver MySQL: " + e);
        }
    }

    private Conexion() { }

    public static Connection obtener() throws SQLException {
        return DriverManager.getConnection(
                PROPS.getProperty("db.url"),
                PROPS.getProperty("db.user"),
                PROPS.getProperty("db.password"));
    }
}
