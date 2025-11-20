package com.maffy.models;

import java.math.BigDecimal;
import java.util.Date;

public class Pedido {
    private int idPedido;
    private int idCliente;
    private Date fecha;
    private String estado;
    private BigDecimal total;
    
    // Constructores
    public Pedido() {}
    
    public Pedido(int idPedido, int idCliente, Date fecha, String estado, BigDecimal total) {
        this.idPedido = idPedido;
        this.idCliente = idCliente;
        this.fecha = fecha;
        this.estado = estado;
        this.total = total;
    }
    
    // Getters y Setters
    public int getIdPedido() { return idPedido; }
    public void setIdPedido(int idPedido) { this.idPedido = idPedido; }
    
    public int getIdCliente() { return idCliente; }
    public void setIdCliente(int idCliente) { this.idCliente = idCliente; }
    
    public Date getFecha() { return fecha; }
    public void setFecha(Date fecha) { this.fecha = fecha; }
    
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    
    public BigDecimal getTotal() { return total; }
    public void setTotal(BigDecimal total) { this.total = total; }
    
    @Override
    public String toString() {
        return "Pedido{" +
                "idPedido=" + idPedido +
                ", idCliente=" + idCliente +
                ", fecha=" + fecha +
                ", estado='" + estado + '\'' +
                ", total=" + total +
                '}';
    }
}