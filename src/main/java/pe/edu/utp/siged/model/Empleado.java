package pe.edu.utp.siged.model;

import java.io.Serializable;

public class Empleado implements Serializable {
    private int id;
    private String nombres;
    private String apellidos;
    private String cargo;
    private String area;

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getNombres() { return nombres; }
    public void setNombres(String nombres) { this.nombres = nombres; }
    public String getApellidos() { return apellidos; }
    public void setApellidos(String apellidos) { this.apellidos = apellidos; }
    public String getCargo() { return cargo; }
    public void setCargo(String cargo) { this.cargo = cargo; }
    public String getArea() { return area; }
    public void setArea(String area) { this.area = area; }

    /** Texto para listas desplegables: "Apellidos, Nombres - Area". */
    public String getEtiqueta() { return apellidos + ", " + nombres + " - " + area; }
}
