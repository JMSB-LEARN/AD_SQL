package model;

import java.time.LocalDateTime;

public class Compra {
    private int id;
    private Integer idCliente;
    LocalDateTime fechaCompra;
    private Double totalComra;

    public Compra(int id, Integer idCliente, LocalDateTime fechaCompra, Double totalComra) {
        this.id = id;
        this.idCliente = idCliente;
        this.fechaCompra = fechaCompra;
        this.totalComra = totalComra;
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

    public Double getTotalComra() {
        return totalComra;
    }

    public void setTotalComra(Double totalComra) {
        this.totalComra = totalComra;
    }
}
