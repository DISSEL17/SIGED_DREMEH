package pe.edu.utp.siged.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import pe.edu.utp.siged.model.Usuario;
import pe.edu.utp.siged.util.Conexion;
import pe.edu.utp.siged.util.PasswordUtil;

public class UsuarioDAO {

    private static final String SQL =
        "SELECT u.id_usuario, u.nombre_usuario, u.hash_clave, u.rol, "
      + "       CONCAT(e.nombres,' ',e.apellidos) AS empleado "
      + "FROM usuario u LEFT JOIN empleado e ON e.id_empleado = u.id_empleado "
      + "WHERE u.nombre_usuario = ? AND u.estado = 1";

    /** Devuelve el usuario si las credenciales son validas; null en caso contrario. */
    public Usuario autenticar(String nombreUsuario, String clave) throws SQLException {
        try (Connection cn = Conexion.obtener();
             PreparedStatement ps = cn.prepareStatement(SQL)) {
            ps.setString(1, nombreUsuario);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next() && PasswordUtil.verificar(clave, rs.getString("hash_clave"))) {
                    Usuario u = new Usuario();
                    u.setId(rs.getInt("id_usuario"));
                    u.setNombreUsuario(rs.getString("nombre_usuario"));
                    u.setRol(rs.getString("rol"));
                    u.setNombreEmpleado(rs.getString("empleado"));
                    return u;
                }
            }
        }
        return null;
    }
}
