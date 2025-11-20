package com.maffy.controllers;

import com.maffy.models.ItemPedido;
import com.maffy.database.Conexion;
import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.DefaultTableModel;

public class ItemPedidoController {
    
    // Agregar nuevo item pedido
    public boolean agregarItemPedido(int idPedido, int idProducto, int cantidad, BigDecimal subtotal) {
        String sql = "INSERT INTO itempedido (id_pedido, id_producto, cantidad, subtotal) VALUES (?, ?, ?, ?)";
        
        try (Connection con = Conexion.conectar();
             PreparedStatement pstmt = con.prepareStatement(sql)) {
            
            pstmt.setInt(1, idPedido);
            pstmt.setInt(2, idProducto);
            pstmt.setInt(3, cantidad);
            pstmt.setBigDecimal(4, subtotal);
            
            int filasAfectadas = pstmt.executeUpdate();
            return filasAfectadas > 0;
            
        } catch (SQLException e) {
            System.err.println("Error al agregar item pedido: " + e.getMessage());
            return false;
        }
    }
    
    // Actualizar item pedido
    public boolean actualizarItemPedido(int idItem, int idPedido, int idProducto, int cantidad, BigDecimal subtotal) {
        String sql = "UPDATE itempedido SET id_pedido = ?, id_producto = ?, cantidad = ?, subtotal = ? WHERE id_item = ?";
        
        try (Connection con = Conexion.conectar();
             PreparedStatement pstmt = con.prepareStatement(sql)) {
            
            pstmt.setInt(1, idPedido);
            pstmt.setInt(2, idProducto);
            pstmt.setInt(3, cantidad);
            pstmt.setBigDecimal(4, subtotal);
            pstmt.setInt(5, idItem);
            
            int filasAfectadas = pstmt.executeUpdate();
            return filasAfectadas > 0;
            
        } catch (SQLException e) {
            System.err.println("Error al actualizar item pedido: " + e.getMessage());
            return false;
        }
    }
    
    // Eliminar item pedido
    public boolean eliminarItemPedido(int idItem) {
        String sql = "DELETE FROM itempedido WHERE id_item = ?";
        
        try (Connection con = Conexion.conectar();
             PreparedStatement pstmt = con.prepareStatement(sql)) {
            
            pstmt.setInt(1, idItem);
            int filasAfectadas = pstmt.executeUpdate();
            return filasAfectadas > 0;
            
        } catch (SQLException e) {
            System.err.println("Error al eliminar item pedido: " + e.getMessage());
            return false;
        }
    }
    
    // Eliminar todos los items de un pedido
    public boolean eliminarItemsPorPedido(int idPedido) {
        String sql = "DELETE FROM itempedido WHERE id_pedido = ?";
        
        try (Connection con = Conexion.conectar();
             PreparedStatement pstmt = con.prepareStatement(sql)) {
            
            pstmt.setInt(1, idPedido);
            int filasAfectadas = pstmt.executeUpdate();
            return filasAfectadas > 0;
            
        } catch (SQLException e) {
            System.err.println("Error al eliminar items del pedido: " + e.getMessage());
            return false;
        }
    }
    
    // Obtener todos los items pedido
    public List<ItemPedido> obtenerTodosItemsPedido() {
        List<ItemPedido> items = new ArrayList<>();
        String sql = "SELECT * FROM itempedido ORDER BY id_item";
        
        try (Connection con = Conexion.conectar();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                ItemPedido item = new ItemPedido(
                    rs.getInt("id_item"),
                    rs.getInt("id_pedido"),
                    rs.getInt("id_producto"),
                    rs.getInt("cantidad"),
                    rs.getBigDecimal("subtotal")
                );
                items.add(item);
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener items pedido: " + e.getMessage());
        }
        return items;
    }
    
    // Obtener items por pedido
    public List<ItemPedido> obtenerItemsPorPedido(int idPedido) {
        List<ItemPedido> items = new ArrayList<>();
        String sql = "SELECT * FROM itempedido WHERE id_pedido = ? ORDER BY id_item";
        
        try (Connection con = Conexion.conectar();
             PreparedStatement pstmt = con.prepareStatement(sql)) {
            
            pstmt.setInt(1, idPedido);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                ItemPedido item = new ItemPedido(
                    rs.getInt("id_item"),
                    rs.getInt("id_pedido"),
                    rs.getInt("id_producto"),
                    rs.getInt("cantidad"),
                    rs.getBigDecimal("subtotal")
                );
                items.add(item);
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener items por pedido: " + e.getMessage());
        }
        return items;
    }
    
    // Obtener item pedido por ID
    public ItemPedido obtenerItemPedidoPorId(int idItem) {
        String sql = "SELECT * FROM itempedido WHERE id_item = ?";
        
        try (Connection con = Conexion.conectar();
             PreparedStatement pstmt = con.prepareStatement(sql)) {
            
            pstmt.setInt(1, idItem);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return new ItemPedido(
                    rs.getInt("id_item"),
                    rs.getInt("id_pedido"),
                    rs.getInt("id_producto"),
                    rs.getInt("cantidad"),
                    rs.getBigDecimal("subtotal")
                );
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener item pedido por ID: " + e.getMessage());
        }
        return null;
    }
    
    // Calcular total de un pedido
    public BigDecimal calcularTotalPedido(int idPedido) {
        String sql = "SELECT SUM(subtotal) as total FROM itempedido WHERE id_pedido = ?";
        
        try (Connection con = Conexion.conectar();
             PreparedStatement pstmt = con.prepareStatement(sql)) {
            
            pstmt.setInt(1, idPedido);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                BigDecimal total = rs.getBigDecimal("total");
                return total != null ? total : BigDecimal.ZERO;
            }
        } catch (SQLException e) {
            System.err.println("Error al calcular total pedido: " + e.getMessage());
        }
        return BigDecimal.ZERO;
    }
    
    // Cargar items pedido en tabla
    public boolean cargarItemsPedidoEnTabla(DefaultTableModel modeloTabla, int idPedido) {
        try {
            modeloTabla.setRowCount(0); // Limpiar tabla
            List<ItemPedido> items = obtenerItemsPorPedido(idPedido);
            
            for (ItemPedido item : items) {
                Object[] fila = {
                    item.getIdItem(),
                    item.getIdPedido(),
                    item.getIdProducto(),
                    item.getCantidad(),
                    item.getSubtotal()
                };
                modeloTabla.addRow(fila);
            }
            return true;
        } catch (Exception e) {
            System.err.println("Error al cargar items pedido en tabla: " + e.getMessage());
            return false;
        }
    }
    
    // Método para probar la funcionalidad
    public static void main(String[] args) {
        ItemPedidoController controller = new ItemPedidoController();
        
        // Probar conexión y obtener items
        List<ItemPedido> items = controller.obtenerTodosItemsPedido();
        System.out.println("Total de items en la base de datos: " + items.size());
        
        for (ItemPedido item : items) {
            System.out.println(item);
        }
    }
}