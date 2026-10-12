package pe.edu.utp.siged.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import pe.edu.utp.siged.model.Empleado;
import pe.edu.utp.siged.util.Conexion;

public class EmpleadoDAO {

    /** Empleados activos que pueden recibir derivaciones. */
    public List<Empleado> listarActivos() throws SQLException {
        List<Empleado> lista = new ArrayList<>();
        String sql = "SELECT id_empleado, nombres, apellidos, cargo, area "
                   + "FROM empleado WHERE estado = 1 ORDER BY area, apellidos";
        try (Connection cn = Conexion.obtener();
             Statement st = cn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                Empleado e = new Empleado();
                e.setId(rs.getInt("id_empleado"));
                e.setNombres(rs.getString("nombres"));
                e.setApellidos(rs.getString("apellidos"));
                e.setCargo(rs.getString("cargo"));
                e.setArea(rs.getString("area"));
                lista.add(e);
            }
        }
        return lista;
    }
}
