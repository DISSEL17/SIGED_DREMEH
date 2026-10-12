package pe.edu.utp.siged.bean;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Named;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import pe.edu.utp.siged.dao.DocumentoDAO;

/** Cifras del panel principal. Alcance: peticion (se recalcula en cada visita). */
@Named
@RequestScoped
public class DashboardBean {

    private Map<String, Integer> conteos;

    public Map<String, Integer> getConteos() {
        if (conteos == null) {
            try {
                conteos = new DocumentoDAO().contarPorEstado();
            } catch (SQLException e) {
                Mensajes.error("No se pudieron cargar los indicadores.");
                conteos = new java.util.LinkedHashMap<>();
            }
        }
        return conteos;
    }

    /** Tarjeta de Bootstrap con la cantidad de documentos de un estado. */
    public static class Tarjeta {
        private final String estado;
        private final int cantidad;
        private final String clase;
        Tarjeta(String estado, int cantidad, String clase) {
            this.estado = estado; this.cantidad = cantidad; this.clase = clase;
        }
        public String getEstado() { return estado; }
        public int getCantidad() { return cantidad; }
        public String getClase() { return clase; }
    }

    public List<Tarjeta> getTarjetas() {
        String[] clases = {"text-bg-secondary", "text-bg-primary", "text-bg-warning", "text-bg-success", "text-bg-dark"};
        List<Tarjeta> r = new ArrayList<>();
        int i = 0;
        for (Map.Entry<String, Integer> e : getConteos().entrySet()) {
            r.add(new Tarjeta(e.getKey().replace('_', ' '), e.getValue(), clases[i++ % clases.length]));
        }
        return r;
    }

    public int getTotal() {
        return getConteos().values().stream().mapToInt(Integer::intValue).sum();
    }
}
