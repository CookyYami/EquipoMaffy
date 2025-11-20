package com.maffy.controllers;

import com.maffy.models.Pedido;
import com.maffy.database.Conexion;
import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.DefaultTableModel;

public class PedidoController {
    
    // Agregar nuevo pedido
    public int agregarPedido(int idCliente, Date fecha, String estado, BigDecimal total) {
        String sql = "INSERT INTO pedido (id_cliente, fecha, estado, total) VALUES (?, ?, ?, ?)";
        
        try (Connection con = Conexion.conectar();
             PreparedStatement pstmt = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setInt(1, idCliente);
            pstmt.setDate(2, new java.sql.Date(fecha.getTime()));
            pstmt.setString(3, estado);
            pstmt.setBigDecimal(4, total);
            
            int filasAfectadas = pstmt.executeUpdate();
            
            if (filasAfectadas > 0) {
                // Obtener el ID generado
                ResultSet generatedKeys = pstmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                }
            }
            return -1;
            
        } catch (SQLException e) {
            System.err.println("Error al agregar pedido: " + e.getMessage());
            return -1;
        }
    }
    
    // Actualizar pedido
    public boolean actualizarPedido(int idPedido, int idCliente, Date fecha, String estado, BigDecimal total) {
        String sql = "UPDATE pedido SET id_cliente = ?, fecha = ?, estado = ?, total = ? WHERE id_pedido = ?";
        
        try (Connection con = Conexion.conectar();
             PreparedStatement pstmt = con.prepareStatement(sql)) {
            
            pstmt.setInt(1, idCliente);
            pstmt.setDate(2, new java.sql.Date(fecha.getTime()));
            pstmt.setString(3, estado);
            pstmt.setBigDecimal(4, total);
            pstmt.setInt(5, idPedido);
            
            int filasAfectadas = pstmt.executeUpdate();
            return filasAfectadas > 0;
            
        } catch (SQLException e) {
            System.err.println("Error al actualizar pedido: " + e.getMessage());
            return false;
        }
    }
    
    // Eliminar pedido
    public boolean eliminarPedido(int idPedido) {
        String sql = "DELETE FROM pedido WHERE id_pedido = ?";
        
        try (Connection con = Conexion.conectar();
             PreparedStatement pstmt = con.prepareStatement(sql)) {
            
            pstmt.setInt(1, idPedido);
            int filasAfectadas = pstmt.executeUpdate();
            return filasAfectadas > 0;
            
        } catch (SQLException e) {
            System.err.println("Error al eliminar pedido: " + e.getMessage());
            return false;
        }
    }
    
    // Obtener todos los pedidos
    public List<Pedido> obtenerTodosPedidos() {
        List<Pedido> pedidos = new ArrayList<>();
        String sql = "SELECT * FROM pedido ORDER BY id_pedido DESC";
        
        try (Connection con = Conexion.conectar();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Pedido pedido = new Pedido(
                    rs.getInt("id_pedido"),
                    rs.getInt("id_cliente"),
                    rs.getDate("fecha"),
                    rs.getString("estado"),
                    rs.getBigDecimal("total")
                );
                pedidos.add(pedido);
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener pedidos: " + e.getMessage());
        }
        return pedidos;
    }
    
    // Obtener pedido por ID
    public Pedido obtenerPedidoPorId(int idPedido) {
        String sql = "SELECT * FROM pedido WHERE id_pedido = ?";
        
        try (Connection con = Conexion.conectar();
             PreparedStatement pstmt = con.prepareStatement(sql)) {
            
            pstmt.setInt(1, idPedido);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return new Pedido(
                    rs.getInt("id_pedido"),
                    rs.getInt("id_cliente"),
                    rs.getDate("fecha"),
                    rs.getString("estado"),
                    rs.getBigDecimal("total")
                );
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener pedido por ID: " + e.getMessage());
        }
        return null;
    }
    
    // Cargar pedidos en tabla
    public boolean cargarPedidosEnTabla(DefaultTableModel modeloTabla) {
        try {
            modeloTabla.setRowCount(0); // Limpiar tabla
            List<Pedido> pedidos = obtenerTodosPedidos();
            
            for (Pedido pedido : pedidos) {
                Object[] fila = {
                    pedido.getIdPedido(),
                    pedido.getIdCliente(),
                    "Productos", // Aquí podrías mostrar los productos
                    pedido.getFecha(),
                    pedido.getEstado(),
                    pedido.getTotal()
                };
                modeloTabla.addRow(fila);
            }
            return true;
        } catch (Exception e) {
            System.err.println("Error al cargar pedidos en tabla: " + e.getMessage());
            return false;
        }
    }
    
    // Obtener pedidos por cliente
    public List<Pedido> obtenerPedidosPorCliente(int idCliente) {
        List<Pedido> pedidos = new ArrayList<>();
        String sql = "SELECT * FROM pedido WHERE id_cliente = ? ORDER BY fecha DESC";
        
        try (Connection con = Conexion.conectar();
             PreparedStatement pstmt = con.prepareStatement(sql)) {
            
            pstmt.setInt(1, idCliente);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                Pedido pedido = new Pedido(
                    rs.getInt("id_pedido"),
                    rs.getInt("id_cliente"),
                    rs.getDate("fecha"),
                    rs.getString("estado"),
                    rs.getBigDecimal("total")
                );
                pedidos.add(pedido);
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener pedidos por cliente: " + e.getMessage());
        }
        return pedidos;
    }
}