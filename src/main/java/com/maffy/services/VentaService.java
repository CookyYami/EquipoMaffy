package com.maffy.services;

import com.maffy.database.Conexion;
import com.maffy.models.Venta;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VentaService {
    
    public boolean registrarVenta(Venta venta) {
        String sql = "INSERT INTO ventas (cliente_id, total, estado) VALUES (?, ?, ?)";
        
        try (Connection conn = Conexion.conectar();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, venta.getClienteId());
            pstmt.setBigDecimal(2, venta.getTotal());
            pstmt.setString(3, venta.getEstado());
            
            int filasAfectadas = pstmt.executeUpdate();
            return filasAfectadas > 0;
            
        } catch (SQLException e) {
            System.out.println("❌ Error registrando venta: " + e.getMessage());
            return false;
        }
    }
    
    public List<Venta> obtenerVentasDelDia() {
        List<Venta> ventas = new ArrayList<>();
        String sql = "SELECT * FROM ventas WHERE DATE(fecha_venta) = CURRENT_DATE ORDER BY id";
        
        try (Connection conn = Conexion.conectar();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            
            while (rs.next()) {
                Venta venta = new Venta();
                venta.setId(rs.getInt("id"));
                venta.setClienteId(rs.getInt("cliente_id"));
                venta.setTotal(rs.getBigDecimal("total"));
                venta.setFechaVenta(rs.getTimestamp("fecha_venta").toLocalDateTime());
                venta.setEstado(rs.getString("estado"));
                ventas.add(venta);
            }
            
        } catch (SQLException e) {
            System.out.println("❌ Error obteniendo ventas: " + e.getMessage());
        }
        return ventas;
    }
}