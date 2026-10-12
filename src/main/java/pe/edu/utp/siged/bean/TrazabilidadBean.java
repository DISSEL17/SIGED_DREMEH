package pe.edu.utp.siged.bean;

import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import pe.edu.utp.siged.dao.DocumentoDAO;
import pe.edu.utp.siged.dao.HistorialDAO;
import pe.edu.utp.siged.model.Documento;
import pe.edu.utp.siged.model.Historial;

/** Linea de tiempo de un expediente (CU04). */
@Named
@ViewScoped
public class TrazabilidadBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;
    private Documento documento;
    private List<Historial> eventos = new ArrayList<>();

    public void cargar() {
        try {
            documento = new DocumentoDAO().buscarPorId(id);
            eventos = new HistorialDAO().listarPorDocumento(id);
        } catch (SQLException | NullPointerException e) {
            Mensajes.error("No se encontro el expediente solicitado.");
        }
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public Documento getDocumento() { return documento; }
    public List<Historial> getEventos() { return eventos; }
}
