package com.maffy.models;

import java.math.BigDecimal;

public class ItemPedido {
    private int idItem;
    private int idPedido;
    private int idProducto;
    private int cantidad;
    private BigDecimal subtotal;
    
    // Constructores
    public ItemPedido() {}
    
    public ItemPedido(int idItem, int idPedido, int idProducto, int cantidad, BigDecimal subtotal) {
        this.idItem = idItem;
        this.idPedido = idPedido;
        this.idProducto = idProducto;
        this.cantidad = cantidad;
        this.subtotal = subtotal;
    }
    
    // Getters y Setters
    public int getIdItem() { return idItem; }
    public void setIdItem(int idItem) { this.idItem = idItem; }
    
    public int getIdPedido() { return idPedido; }
    public void setIdPedido(int idPedido) { this.idPedido = idPedido; }
    
    public int getIdProducto() { return idProducto; }
    public void setIdProducto(int idProducto) { this.idProducto = idProducto; }
    
    public int getCantidad() { return cantidad; }
    public void setCantidad(int cantidad) { this.cantidad = cantidad; }
    
    public BigDecimal getSubtotal() { return subtotal; }
    public void setSubtotal(BigDecimal subtotal) { this.subtotal = subtotal; }
    
    @Override
    public String toString() {
        return "ItemPedido{" +
                "idItem=" + idItem +
                ", idPedido=" + idPedido +
                ", idProducto=" + idProducto +
                ", cantidad=" + cantidad +
                ", subtotal=" + subtotal +
                '}';
    }
}