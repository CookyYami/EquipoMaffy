package com.maffy.models;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Producto {
    private int id;
    private String nombre;
    private String descripcion;
    private BigDecimal precio;
    private int stock;
    private String categoria;
    private LocalDateTime fechaCreacion;
    
    public Producto() {}
    
    public Producto(String nombre, String descripcion, BigDecimal precio, int stock, String categoria) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
        this.stock = stock;
        this.categoria = categoria;
        this.fechaCreacion = LocalDateTime.now();
    }
    
    // Getters y Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    
    public BigDecimal getPrecio() { return precio; }
    public void setPrecio(BigDecimal precio) { this.precio = precio; }
    
    public int getStock() { return stock; }
    public void setStock(int stock) { this.stock = stock; }
    
    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }
    
    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(LocalDateTime fechaCreacion) { this.fechaCreacion = fechaCreacion; }
    
    @Override
    public String toString() {
        return String.format("Producto{id=%d, nombre='%s', precio=%.2f, stock=%d}", 
            id, nombre, precio, stock);
    }
}