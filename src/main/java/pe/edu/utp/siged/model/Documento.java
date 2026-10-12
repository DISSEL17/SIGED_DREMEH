package pe.edu.utp.siged.model;

import java.io.Serializable;
import java.time.LocalDate;

public class Documento implements Serializable {
    private int id;
    private String expediente;
    private LocalDate fechaIngreso;
    private String tipo;
    private String asunto;
    private String solicitante;
    private int folios = 1;
    private String estado;

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getExpediente() { return expediente; }
    public void setExpediente(String expediente) { this.expediente = expediente; }
    public LocalDate getFechaIngreso() { return fechaIngreso; }
    public void setFechaIngreso(LocalDate fechaIngreso) { this.fechaIngreso = fechaIngreso; }
    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }
    public String getAsunto() { return asunto; }
    public void setAsunto(String asunto) { this.asunto = asunto; }
    public String getSolicitante() { return solicitante; }
    public void setSolicitante(String solicitante) { this.solicitante = solicitante; }
    public int getFolios() { return folios; }
    public void setFolios(int folios) { this.folios = folios; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public String getFechaTexto() {
        return fechaIngreso == null ? "" : fechaIngreso.format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    }

    /** Clase de Bootstrap para pintar la insignia del estado. */
    public String getClaseEstado() {
        switch (estado == null ? "" : estado) {
            case "REGISTRADO":  return "text-bg-secondary";
            case "DERIVADO":    return "text-bg-primary";
            case "EN_ATENCION": return "text-bg-warning";
            case "ATENDIDO":    return "text-bg-success";
            default:            return "text-bg-dark";
        }
    }
}
