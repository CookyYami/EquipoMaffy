package com.maffy.models;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Pedido {
    private int id;
    private LocalDate fecha;
    private String estado;
    private int clienteId;
    private int usuarioId;
    private BigDecimal total;

    public Pedido() {}

    public Pedido(int clienteId, int usuarioId, BigDecimal total) {
        this.clienteId = clienteId;
        this.usuarioId = usuarioId;
        this.total = total;
        this.fecha = LocalDate.now();
        this.estado = "PENDIENTE";
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public LocalDate getFecha() { return fecha; }
    public void setFecha(LocalDate fecha) { this.fecha = fecha; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public int getClienteId() { return clienteId; }
    public void setClienteId(int clienteId) { this.clienteId = clienteId; }

    public int getUsuarioId() { return usuarioId; }
    public void setUsuarioId(int usuarioId) { this.usuarioId = usuarioId; }

    public BigDecimal getTotal() { return total; }
    public void setTotal(BigDecimal total) { this.total = total; }

    @Override
    public String toString() {
        return String.format("Pedido{id=%d, cliente=%d, total=%s}", id, clienteId, total);
    }
}
