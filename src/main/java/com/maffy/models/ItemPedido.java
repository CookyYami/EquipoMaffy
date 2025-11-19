package com.maffy.models;

import java.math.BigDecimal;

public class ItemPedido {
    private int id;
    private int pedidoId;
    private int productoId;
    private int cantidad;
    private BigDecimal subtotal;

    public ItemPedido() {}

    public ItemPedido(int pedidoId, int productoId, int cantidad, BigDecimal subtotal) {
        this.pedidoId = pedidoId;
        this.productoId = productoId;
        this.cantidad = cantidad;
        this.subtotal = subtotal;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getPedidoId() { return pedidoId; }
    public void setPedidoId(int pedidoId) { this.pedidoId = pedidoId; }

    public int getProductoId() { return productoId; }
    public void setProductoId(int productoId) { this.productoId = productoId; }

    public int getCantidad() { return cantidad; }
    public void setCantidad(int cantidad) { this.cantidad = cantidad; }

    public BigDecimal getSubtotal() { return subtotal; }
    public void setSubtotal(BigDecimal subtotal) { this.subtotal = subtotal; }

    @Override
    public String toString() {
        return String.format("ItemPedido{id=%d, pedido=%d, producto=%d, cantidad=%d, subtotal=%s}", id, pedidoId, productoId, cantidad, subtotal);
    }
}
