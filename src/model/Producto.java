package model;

public class Producto {

    private int id;
    private String nombre;
    private double precio;
    private int idFabricante;

    public Producto(int id, String nombre, double precio) {
        this.id = id;
        this.nombre = nombre;
        this.precio = precio;
    }

    public Producto(int id, String nombre, double precio, int idFabricante){
        this.nombre = nombre;
        this.precio = precio;
        this.idFabricante = idFabricante;
    }
    public Producto(String nombre, double precio, int idFabricante) {
        this.id = id;
        this.nombre = nombre;
        this.precio = precio;
        this.idFabricante = idFabricante;
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

    @Override
    public String toString() {
        return "Producto{" +
                "id=" + id +
                ", nombre='" + nombre +
                ", precio=" + precio +
                ", idFabricante=" + idFabricante +
                '}';
    }
}
