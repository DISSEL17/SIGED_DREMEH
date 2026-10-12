package pe.edu.utp.siged.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import pe.edu.utp.siged.model.Historial;
import pe.edu.utp.siged.util.Conexion;

public class HistorialDAO {

    /** Inserta un evento usando la conexion (y transaccion) del llamador. */
    static void registrar(Connection cn, int idDocumento, int idUsuario, String accion,
                          String anterior, String nuevo) throws SQLException {
        String sql = "INSERT INTO historial (id_documento, id_usuario, fecha, accion, estado_anterior, estado_nuevo) "
                   + "VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setInt(1, idDocumento);
            ps.setInt(2, idUsuario);
            ps.setTimestamp(3, Timestamp.valueOf(LocalDateTime.now()));
            ps.setString(4, accion);
            ps.setString(5, anterior);
            ps.setString(6, nuevo);
            ps.executeUpdate();
        }
    }

    /** Linea de tiempo del expediente en orden cronologico (CU04). */
    public List<Historial> listarPorDocumento(int idDocumento) throws SQLException {
        List<Historial> lista = new ArrayList<>();
        String sql = "SELECT h.fecha, u.nombre_usuario, h.accion, h.estado_anterior, h.estado_nuevo "
                   + "FROM historial h JOIN usuario u ON u.id_usuario = h.id_usuario "
                   + "WHERE h.id_documento = ? ORDER BY h.fecha, h.id_evento";
        try (Connection cn = Conexion.obtener(); PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setInt(1, idDocumento);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Historial h = new Historial();
                    h.setFecha(rs.getTimestamp(1).toLocalDateTime());
                    h.setUsuario(rs.getString(2));
                    h.setAccion(rs.getString(3));
                    h.setEstadoAnterior(rs.getString(4));
                    h.setEstadoNuevo(rs.getString(5));
                    lista.add(h);
                }
            }
        }
        return lista;
    }
}
