package pe.edu.utp.siged.model;

import java.io.Serializable;

/** DTO de usuario autenticado. */
public class Usuario implements Serializable {
    private int id;
    private String nombreUsuario;
    private String rol;
    private String nombreEmpleado;

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getNombreUsuario() { return nombreUsuario; }
    public void setNombreUsuario(String nombreUsuario) { this.nombreUsuario = nombreUsuario; }
    public String getRol() { return rol; }
    public void setRol(String rol) { this.rol = rol; }
    public String getNombreEmpleado() { return nombreEmpleado; }
    public void setNombreEmpleado(String nombreEmpleado) { this.nombreEmpleado = nombreEmpleado; }
}
