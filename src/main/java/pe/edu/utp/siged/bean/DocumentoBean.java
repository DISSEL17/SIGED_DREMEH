package pe.edu.utp.siged.bean;

import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import pe.edu.utp.siged.dao.DocumentoDAO;
import pe.edu.utp.siged.model.Documento;

/**
 * Listado con filtro y paginacion (h:dataTable) y registro de documentos (CU01).
 * Alcance: vista (conserva filtros y pagina mientras el usuario permanece en la pagina).
 */
@Named
@ViewScoped
public class DocumentoBean implements Serializable {

    private static final long serialVersionUID = 1L;
    private static final int TAMANO = 5;

    public static final String[] ESTADOS = {"REGISTRADO", "DERIVADO", "EN_ATENCION", "ATENDIDO", "CERRADO"};
    public static final String[] TIPOS = {"Solicitud", "Oficio", "Carta", "Informe", "Memorando"};

    @Inject
    private LoginBean login;

    private final DocumentoDAO dao = new DocumentoDAO();

    private String filtroEstado = "";
    private String filtroTexto = "";
    private int pagina;
    private int total;
    private List<Documento> lista = new ArrayList<>();
    private Documento nuevo = new Documento();

    @PostConstruct
    public void init() {
        cargar();
    }

    private void cargar() {
        try {
            total = dao.contar(filtroEstado, filtroTexto);
            if (pagina >= getTotalPaginas()) {
                pagina = Math.max(0, getTotalPaginas() - 1);
            }
            lista = dao.listar(filtroEstado, filtroTexto, pagina * TAMANO, TAMANO);
        } catch (SQLException e) {
            Mensajes.error("Error al consultar los documentos: " + e.getMessage());
        }
    }

    public void buscar() { pagina = 0; cargar(); }

    public void limpiar() { filtroEstado = ""; filtroTexto = ""; buscar(); }

    public void siguiente() { if (pagina + 1 < getTotalPaginas()) { pagina++; cargar(); } }

    public void anterior() { if (pagina > 0) { pagina--; cargar(); } }

    public String registrar() {
        try {
            String exp = dao.registrar(nuevo, login.getActual().getId());
            Mensajes.info("Documento registrado con el expediente " + exp + ".");
            Mensajes.conservar();
            return "lista?faces-redirect=true";          // navegacion implicita
        } catch (SQLException e) {
            Mensajes.error("No se pudo registrar el documento: " + e.getMessage());
            return null;                                // permanece en la misma vista
        }
    }

    public int getTotalPaginas() { return Math.max(1, (int) Math.ceil(total / (double) TAMANO)); }
    public boolean isHayAnterior() { return pagina > 0; }
    public boolean isHaySiguiente() { return pagina + 1 < getTotalPaginas(); }
    public int getPaginaVisible() { return pagina + 1; }
    public int getTotal() { return total; }
    public List<Documento> getLista() { return lista; }
    public Documento getNuevo() { return nuevo; }
    public String[] getEstados() { return ESTADOS; }
    public String[] getTipos() { return TIPOS; }
    public String getFiltroEstado() { return filtroEstado; }
    public void setFiltroEstado(String filtroEstado) { this.filtroEstado = filtroEstado; }
    public String getFiltroTexto() { return filtroTexto; }
    public void setFiltroTexto(String filtroTexto) { this.filtroTexto = filtroTexto; }
}
