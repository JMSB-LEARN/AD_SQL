package model;

public class Producto {

    private int id;
    private String nombre;
    private double precio;
    private int idFabricante;
    private String nombreFabricante;

    public Producto(int id, String nombre, double precio) {
        this.id = id;
        this.nombre = nombre;
        this.precio = precio;
    }

    public Producto(int id, String nombre, double precio, int idFabricante, String nombreFabricante) {
        this.id = id;
        this.nombre = nombre;
        this.precio = precio;
        this.idFabricante = idFabricante;
        this.nombreFabricante = nombreFabricante;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public int getIdFabricante() {
        return idFabricante;
    }

    public void setIdFabricante(int idFabricante) {
        this.idFabricante = idFabricante;
    }

    public String getNombreFabricante() {
        return nombreFabricante;
    }

    public void setNombreFabricante(String nombreFabricante) {
        this.nombreFabricante = nombreFabricante;
    }

    @Override
    public String toString() {
        return "Producto{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", precio=" + precio +
                ", idFabricante=" + idFabricante +
                ", nombreFabricante='" + nombreFabricante + '\'' +
                '}';
    }
}
