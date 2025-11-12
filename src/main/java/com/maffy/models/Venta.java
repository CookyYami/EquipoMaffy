package com.maffy.models;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Venta {
    private int id;
    private int clienteId;
    private BigDecimal total;
    private LocalDateTime fechaVenta;
    private String estado;
    
    public Venta() {}
    
    public Venta(int clienteId, BigDecimal total) {
        this.clienteId = clienteId;
        this.total = total;
        this.fechaVenta = LocalDateTime.now();
        this.estado = "COMPLETADA";
    }
    
    // Getters y Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public int getClienteId() { return clienteId; }
    public void setClienteId(int clienteId) { this.clienteId = clienteId; }
    
    public BigDecimal getTotal() { return total; }
    public void setTotal(BigDecimal total) { this.total = total; }
    
    public LocalDateTime getFechaVenta() { return fechaVenta; }
    public void setFechaVenta(LocalDateTime fechaVenta) { this.fechaVenta = fechaVenta; }
    
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    
    @Override
    public String toString() {
        return String.format("Venta{id=%d, total=%.2f, fecha=%s}", 
            id, total, fechaVenta);
    }
}