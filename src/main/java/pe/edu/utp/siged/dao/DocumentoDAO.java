package pe.edu.utp.siged.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import pe.edu.utp.siged.model.Documento;
import pe.edu.utp.siged.util.Conexion;

public class DocumentoDAO {

    private static final String FILTRO =
        " WHERE (? = '' OR estado = ?) AND (? = '' OR expediente LIKE ? OR asunto LIKE ? OR solicitante LIKE ?) ";

    /** Listado paginado y filtrado por estado y texto (RF07). */
    public List<Documento> listar(String estado, String texto, int desde, int cantidad) throws SQLException {
        List<Documento> lista = new ArrayList<>();
        String sql = "SELECT id_documento, expediente, fecha_ingreso, tipo, asunto, solicitante, folios, estado "
                   + "FROM documento" + FILTRO + "ORDER BY fecha_ingreso DESC, id_documento DESC LIMIT ? OFFSET ?";
        try (Connection cn = Conexion.obtener(); PreparedStatement ps = cn.prepareStatement(sql)) {
            int i = parametrosFiltro(ps, estado, texto);
            ps.setInt(i++, cantidad);
            ps.setInt(i, desde);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapear(rs));
                }
            }
        }
        return lista;
    }

    public int contar(String estado, String texto) throws SQLException {
        String sql = "SELECT COUNT(*) FROM documento" + FILTRO;
        try (Connection cn = Conexion.obtener(); PreparedStatement ps = cn.prepareStatement(sql)) {
            parametrosFiltro(ps, estado, texto);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? rs.getInt(1) : 0;
            }
        }
    }

    /** Cantidad de documentos por estado, para las tarjetas del panel. */
    public Map<String, Integer> contarPorEstado() throws SQLException {
        Map<String, Integer> r = new LinkedHashMap<>();
        for (String e : new String[]{"REGISTRADO", "DERIVADO", "EN_ATENCION", "ATENDIDO", "CERRADO"}) {
            r.put(e, 0);
        }
        String sql = "SELECT estado, COUNT(*) FROM documento GROUP BY estado";
        try (Connection cn = Conexion.obtener();
             Statement st = cn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                r.put(rs.getString(1), rs.getInt(2));
            }
        }
        return r;
    }

    public Documento buscarPorId(int id) throws SQLException {
        String sql = "SELECT id_documento, expediente, fecha_ingreso, tipo, asunto, solicitante, folios, estado "
                   + "FROM documento WHERE id_documento = ?";
        try (Connection cn = Conexion.obtener(); PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? mapear(rs) : null;
            }
        }
    }

    /**
     * Registra el documento, genera el expediente unico (RF05) y deja el primer evento
     * del historial (RF10). Todo en una sola transaccion.
     */
    public String registrar(Documento d, int idUsuario) throws SQLException {
        String insert = "INSERT INTO documento (expediente, fecha_ingreso, tipo, asunto, solicitante, folios, estado) "
                      + "VALUES (?, ?, ?, ?, ?, ?, 'REGISTRADO')";
        try (Connection cn = Conexion.obtener()) {
            cn.setAutoCommit(false);
            try {
                int id;
                try (PreparedStatement ps = cn.prepareStatement(insert, Statement.RETURN_GENERATED_KEYS)) {
                    ps.setString(1, "TMP-" + System.nanoTime());
                    ps.setDate(2, Date.valueOf(LocalDate.now()));
                    ps.setString(3, d.getTipo());
                    ps.setString(4, d.getAsunto());
                    ps.setString(5, d.getSolicitante());
                    ps.setInt(6, d.getFolios());
                    ps.executeUpdate();
                    try (ResultSet k = ps.getGeneratedKeys()) {
                        k.next();
                        id = k.getInt(1);
                    }
                }
                String expediente = String.format("EXP-%d-%04d", LocalDate.now().getYear(), id);
                try (PreparedStatement ps = cn.prepareStatement(
                        "UPDATE documento SET expediente = ? WHERE id_documento = ?")) {
                    ps.setString(1, expediente);
                    ps.setInt(2, id);
                    ps.executeUpdate();
                }
                HistorialDAO.registrar(cn, id, idUsuario, "Registro de documento", null, "REGISTRADO");
                cn.commit();
                return expediente;
            } catch (SQLException e) {
                cn.rollback();
                throw e;
            }
        }
    }

    private int parametrosFiltro(PreparedStatement ps, String estado, String texto) throws SQLException {
        String e = estado == null ? "" : estado;
        String t = texto == null ? "" : texto.trim();
        String like = "%" + t + "%";
        ps.setString(1, e);
        ps.setString(2, e);
        ps.setString(3, t);
        ps.setString(4, like);
        ps.setString(5, like);
        ps.setString(6, like);
        return 7;
    }

    private Documento mapear(ResultSet rs) throws SQLException {
        Documento d = new Documento();
        d.setId(rs.getInt("id_documento"));
        d.setExpediente(rs.getString("expediente"));
        d.setFechaIngreso(rs.getDate("fecha_ingreso").toLocalDate());
        d.setTipo(rs.getString("tipo"));
        d.setAsunto(rs.getString("asunto"));
        d.setSolicitante(rs.getString("solicitante"));
        d.setFolios(rs.getInt("folios"));
        d.setEstado(rs.getString("estado"));
        return d;
    }
}
