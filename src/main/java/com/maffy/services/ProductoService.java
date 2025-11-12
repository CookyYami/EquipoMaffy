package com.maffy.services;

import com.maffy.database.SupabaseConnection;
import com.maffy.models.Producto;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductoService {
    
    public List<Producto> obtenerTodosProductos() {
        List<Producto> productos = new ArrayList<>();
        String sql = "SELECT * FROM productos ORDER BY id";
        
        try (Connection conn = SupabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            
            while (rs.next()) {
                Producto producto = new Producto();
                producto.setId(rs.getInt("id"));
                producto.setNombre(rs.getString("nombre"));
                producto.setDescripcion(rs.getString("descripcion"));
                producto.setPrecio(rs.getBigDecimal("precio"));
                producto.setStock(rs.getInt("stock"));
                producto.setCategoria(rs.getString("categoria"));
                productos.add(producto);
            }
            
        } catch (SQLException e) {
            System.out.println("❌ Error obteniendo productos: " + e.getMessage());
        }
        return productos;
    }
    
    public boolean agregarProducto(Producto producto) {
        String sql = "INSERT INTO productos (nombre, descripcion, precio, stock, categoria) VALUES (?, ?, ?, ?, ?)";
        
        try (Connection conn = SupabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, producto.getNombre());
            pstmt.setString(2, producto.getDescripcion());
            pstmt.setBigDecimal(3, producto.getPrecio());
            pstmt.setInt(4, producto.getStock());
            pstmt.setString(5, producto.getCategoria());
            
            int filasAfectadas = pstmt.executeUpdate();
            return filasAfectadas > 0;
            
        } catch (SQLException e) {
            System.out.println("❌ Error agregando producto: " + e.getMessage());
            return false;
        }
    }
}