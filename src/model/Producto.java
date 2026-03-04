package model;

public class Producto {

    private Integer  id;
    private String nombre;
    private Double precio;
    private Integer  idFabricante;

    public Producto(Integer id, String nombre, Double precio) {
        this.id = id;
        this.nombre = nombre;
        this.precio = precio;
    }

    public Producto(Integer id, String nombre, Double precio, Integer idFabricante){
        this.nombre = nombre;
        this.precio = precio;
        this.idFabricante = idFabricante;
    }
    public Producto(String nombre, Double precio, Integer idFabricante) {
        this.nombre = nombre;
        this.precio = precio;
        this.idFabricante = idFabricante;
    }

    public Integer  getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Double getPrecio() {
        return precio;
    }

    public void setPrecio(Double precio) {
        this.precio = precio;
    }

    public Integer getIdFabricante() {
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
