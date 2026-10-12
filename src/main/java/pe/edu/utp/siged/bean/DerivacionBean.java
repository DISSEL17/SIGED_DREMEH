package pe.edu.utp.siged.bean;

import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import pe.edu.utp.siged.dao.DerivacionDAO;
import pe.edu.utp.siged.dao.DocumentoDAO;
import pe.edu.utp.siged.dao.EmpleadoDAO;
import pe.edu.utp.siged.model.Documento;
import pe.edu.utp.siged.model.Empleado;

/** Derivacion de un documento a un responsable (CU02). El id llega por f:viewParam. */
@Named
@ViewScoped
public class DerivacionBean implements Serializable {

    private static final long serialVersionUID = 1L;

    @Inject
    private LoginBean login;

    private Integer id;
    private Documento documento;
    private List<Empleado> empleados = new ArrayList<>();
    private Integer idEmpleado;
    private String observacion;

    /** Se ejecuta con f:viewAction despues de recibir el parametro id. */
    public void cargar() {
        try {
            documento = new DocumentoDAO().buscarPorId(id);
            empleados = new EmpleadoDAO().listarActivos();
        } catch (SQLException | NullPointerException e) {
            Mensajes.error("No se encontro el documento solicitado.");
        }
    }

    public String derivar() {
        try {
            Empleado elegido = empleados.stream()
                    .filter(e -> e.getId() == idEmpleado).findFirst().orElseThrow();
            new DerivacionDAO().derivar(documento.getId(), elegido.getId(), elegido.getEtiqueta(),
                    observacion, documento.getEstado(), login.getActual().getId());
            Mensajes.info("Documento " + documento.getExpediente() + " derivado a " + elegido.getEtiqueta() + ".");
            Mensajes.conservar();
            return "lista?faces-redirect=true";
        } catch (SQLException | RuntimeException e) {
            Mensajes.error("No se pudo derivar el documento.");
            return null;
        }
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public Documento getDocumento() { return documento; }
    public List<Empleado> getEmpleados() { return empleados; }
    public Integer getIdEmpleado() { return idEmpleado; }
    public void setIdEmpleado(Integer idEmpleado) { this.idEmpleado = idEmpleado; }
    public String getObservacion() { return observacion; }
    public void setObservacion(String observacion) { this.observacion = observacion; }
}
