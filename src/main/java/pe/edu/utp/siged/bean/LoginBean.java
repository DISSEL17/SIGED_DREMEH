package pe.edu.utp.siged.bean;

import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;
import java.io.Serializable;
import java.sql.SQLException;
import pe.edu.utp.siged.dao.UsuarioDAO;
import pe.edu.utp.siged.model.Usuario;

/** Autenticacion y datos de la sesion (RF01, RF02). Alcance: sesion. */
@Named
@SessionScoped
public class LoginBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private String usuario;
    private String clave;
    private Usuario actual;

    /** Accion del formulario de login. Devuelve el resultado que usa faces-config.xml. */
    public String iniciar() {
        try {
            Usuario u = new UsuarioDAO().autenticar(usuario, clave);
            if (u == null) {
                Mensajes.error("Usuario o clave incorrectos.");
                return "fallo";
            }
            actual = u;
            FacesContext.getCurrentInstance().getExternalContext()
                    .getSessionMap().put("usuario", u);   // lo lee AuthFilter
            clave = null;
            return "exito";
        } catch (SQLException e) {
            Mensajes.error("No se pudo conectar con la base de datos.");
            return "fallo";
        }
    }

    public String cerrar() {
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        return "salir";
    }

    public boolean isLogueado() { return actual != null; }

    /** Solo administrador y mesa de partes registran y derivan documentos. */
    public boolean isPuedeRegistrar() {
        return actual != null
            && ("ADMINISTRADOR".equals(actual.getRol()) || "MESA_PARTES".equals(actual.getRol()));
    }

    public String getUsuario() { return usuario; }
    public void setUsuario(String usuario) { this.usuario = usuario; }
    public String getClave() { return clave; }
    public void setClave(String clave) { this.clave = clave; }
    public Usuario getActual() { return actual; }
}
