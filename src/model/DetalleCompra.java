package model;

public class DetalleCompra {
    private int id;
    private Integer idCompra,idProducto,cantidad;
    private double precioUnitario;

    public DetalleCompra(int id, Integer idCompra, Integer idProducto, Integer cantidad, double precioUnitario) {
        this.id = id;
        this.idCompra = idCompra;
        this.idProducto = idProducto;
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
    }
    public DetalleCompra(Integer idCompra, Integer idProducto, Integer cantidad, double precioUnitario) {
        this.idCompra = idCompra;
        this.idProducto = idProducto;
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
    }
}
