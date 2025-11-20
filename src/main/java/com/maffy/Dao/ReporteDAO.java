package com.maffy.Dao;

import com.maffy.database.Conexion;
import com.maffy.models.Pedido;
import java.sql.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ReporteDAO {
    private Connection conexion;
    
    public ReporteDAO() {
        this.conexion = Conexion.conectar();
    }
    
    public List<Pedido> obtenerReporteDiario(String fecha) {
        List<Pedido> pedidos = new ArrayList<>();
        String sql = "SELECT id_pedido, fecha, estado, total, id_cliente, id_usuario FROM pedido WHERE fecha = ? AND UPPER(TRIM(estado)) = 'COMPLETADO' ORDER BY id_pedido";
        
        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setString(1, fecha);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                Pedido pedido = new Pedido();
                pedido.setId(rs.getInt("id_pedido"));
                
                String fechaStr = rs.getString("fecha");
                LocalDate fechaLocal = LocalDate.parse(fechaStr);
                pedido.setFecha(fechaLocal);
                
                pedido.setEstado(rs.getString("estado"));
                
                double totalDouble = rs.getDouble("total");
                BigDecimal totalBigDecimal = BigDecimal.valueOf(totalDouble);
                pedido.setTotal(totalBigDecimal);
                
                pedido.setClienteId(rs.getInt("id_cliente"));
                pedido.setUsuarioId(rs.getInt("id_usuario"));
                
                pedidos.add(pedido);
            }
            
        } catch (SQLException e) {
            System.err.println("Error al obtener reporte diario: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("Error de conversión: " + e.getMessage());
            e.printStackTrace();
        }
        return pedidos;
    }
    
    public List<Pedido> obtenerReporteSemanal(String fechaInicio, String fechaFin) {
        List<Pedido> pedidos = new ArrayList<>();
        String sql = "SELECT id_pedido, fecha, estado, total, id_cliente, id_usuario FROM pedido WHERE fecha BETWEEN ? AND ? AND UPPER(TRIM(estado)) = 'COMPLETADO' ORDER BY fecha, id_pedido";
        
        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setString(1, fechaInicio);
            stmt.setString(2, fechaFin);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                Pedido pedido = new Pedido();
                pedido.setId(rs.getInt("id_pedido"));
                
                String fechaStr = rs.getString("fecha");
                LocalDate fechaLocal = LocalDate.parse(fechaStr);
                pedido.setFecha(fechaLocal);
                
                pedido.setEstado(rs.getString("estado"));
                
                double totalDouble = rs.getDouble("total");
                BigDecimal totalBigDecimal = BigDecimal.valueOf(totalDouble);
                pedido.setTotal(totalBigDecimal);
                
                pedido.setClienteId(rs.getInt("id_cliente"));
                pedido.setUsuarioId(rs.getInt("id_usuario"));
                
                pedidos.add(pedido);
            }
            
        } catch (SQLException e) {
            System.err.println("Error al obtener reporte semanal: " + e.getMessage());
            e.printStackTrace();
        }
        return pedidos;
    }
    
    public List<Pedido> obtenerReporteMensual(int anio, int mes) {
        List<Pedido> pedidos = new ArrayList<>();
        String sql = "SELECT id_pedido, fecha, estado, total, id_cliente, id_usuario FROM pedido WHERE EXTRACT(YEAR FROM fecha) = ? \r\n" + //
                        "  AND EXTRACT(MONTH FROM fecha) = ?\r\n" + //
                        "  AND UPPER(TRIM(estado)) = 'COMPLETADO' ORDER BY fecha, id_pedido";
        
        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setInt(1, anio);
            stmt.setInt(2, mes);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                Pedido pedido = new Pedido();
                pedido.setId(rs.getInt("id_pedido"));
                
                String fechaStr = rs.getString("fecha");
                LocalDate fechaLocal = LocalDate.parse(fechaStr);
                pedido.setFecha(fechaLocal);
                
                pedido.setEstado(rs.getString("estado"));
                
                double totalDouble = rs.getDouble("total");
                BigDecimal totalBigDecimal = BigDecimal.valueOf(totalDouble);
                pedido.setTotal(totalBigDecimal);
                
                pedido.setClienteId(rs.getInt("id_cliente"));
                pedido.setUsuarioId(rs.getInt("id_usuario"));
                
                pedidos.add(pedido);
            }
            
        } catch (SQLException e) {
            System.err.println("Error al obtener reporte mensual: " + e.getMessage());
            e.printStackTrace();
        }
        return pedidos;
    }
    
    public double obtenerTotalVentas(List<Pedido> pedidos) {
        if (pedidos == null || pedidos.isEmpty()) {
            return 0.0;
        }
        double total = 0.0;
        for (Pedido pedido : pedidos) {
            total += pedido.getTotal().doubleValue();
        }
        return total;
    }
    
    public void cerrarConexion() {
        try {
            if (conexion != null && !conexion.isClosed()) {
                conexion.close();
            }
        } catch (SQLException e) {
            System.err.println("Error al cerrar conexión: " + e.getMessage());
        }
    }
}