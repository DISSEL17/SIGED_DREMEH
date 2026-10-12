package pe.edu.utp.siged.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import pe.edu.utp.siged.util.Conexion;

public class DerivacionDAO {

    /**
     * Deriva el documento (RF08): inserta la derivacion, cambia el estado a DERIVADO y registra
     * el evento en el historial (RF10), todo en una transaccion.
     */
    public void derivar(int idDocumento, int idEmpleado, String nombreEmpleado, String observacion,
                        String estadoActual, int idUsuario) throws SQLException {
        try (Connection cn = Conexion.obtener()) {
            cn.setAutoCommit(false);
            try {
                try (PreparedStatement ps = cn.prepareStatement(
                        "INSERT INTO derivacion (id_documento, id_empleado, fecha, observacion) VALUES (?,?,?,?)")) {
                    ps.setInt(1, idDocumento);
                    ps.setInt(2, idEmpleado);
                    ps.setTimestamp(3, Timestamp.valueOf(LocalDateTime.now()));
                    ps.setString(4, observacion);
                    ps.executeUpdate();
                }
                try (PreparedStatement ps = cn.prepareStatement(
                        "UPDATE documento SET estado = 'DERIVADO' WHERE id_documento = ?")) {
                    ps.setInt(1, idDocumento);
                    ps.executeUpdate();
                }
                HistorialDAO.registrar(cn, idDocumento, idUsuario,
                        "Derivacion a " + nombreEmpleado, estadoActual, "DERIVADO");
                cn.commit();
            } catch (SQLException e) {
                cn.rollback();
                throw e;
            }
        }
    }
}
