package model;

import java.time.LocalDateTime;

public class Compra {
    private int id;
    private Integer idCliente;
    LocalDateTime fechaCompra;
    private Double totalCompra;

    public Compra(int id, Integer idCliente, LocalDateTime fechaCompra, Double totalCompra) {
        this.id = id;
        this.idCliente = idCliente;
        this.fechaCompra = fechaCompra;
        this.totalCompra = totalCompra;
    }
    public Compra(Integer idCliente, LocalDateTime fechaCompra) {
        this.idCliente = idCliente;
        this.fechaCompra = fechaCompra;
    }

    public int getId() {
        return id;
    }

    public Integer getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(Integer idCliente) {
        this.idCliente = idCliente;
    }

    public LocalDateTime getFechaCompra() {
        return fechaCompra;
    }

    public void setFechaCompra(LocalDateTime fechaCompra) {
        this.fechaCompra = fechaCompra;
    }

    public Double getTotalCompra() {
        return totalCompra;
    }

    public void setTotalCompra(Double totalCompra) {
        this.totalCompra = totalCompra;
    }
}
