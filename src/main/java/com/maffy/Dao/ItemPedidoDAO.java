package com.maffy.Dao;

import com.maffy.database.Conexion;
import com.maffy.models.ItemPedido;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.math.BigDecimal;

public class ItemPedidoDAO {

    private String detectedTable = null;

    private String detectTableName(Connection conn) {
        if (detectedTable != null) return detectedTable;
        String[] candidates = {"\"ItemPedido\"", "item_pedido", "\"item_pedido\"", "itempedido", "\"ItemPedidos\""};
        for (String candidate : candidates) {
            String test = "SELECT 1 FROM " + candidate + " LIMIT 1";
            try (PreparedStatement pst = conn.prepareStatement(test); ResultSet rs = pst.executeQuery()) {
                detectedTable = candidate;
                System.out.println("[INFO] Tabla de items de pedido detectada: " + detectedTable);
                return detectedTable;
            } catch (SQLException ex) {
                // seguir con siguiente candidato
            }
        }
        detectedTable = "\"ItemPedido\"";
        System.out.println("[WARN] No se pudo detectar tabla de items; usando fallback: " + detectedTable);
        return detectedTable;
    }

    public List<ItemPedido> obtenerItemsPorPedido(int pedidoId) {
        List<ItemPedido> items = new ArrayList<>();
        try (Connection conn = Conexion.conectar()) {
            if (conn == null) {
                System.err.println("[ERROR] Conexion nula al obtener items por pedido");
                return items;
            }
            String table = detectTableName(conn);
            String sql = "SELECT id_item, id_pedido, id_producto, cantidad, subtotal FROM " + table + " WHERE id_pedido = ?";
            System.out.println("[DEBUG] SQL obtenerItemsPorPedido: " + sql + " (pedidoId=" + pedidoId + ")");
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setInt(1, pedidoId);
                try (ResultSet rs = pstmt.executeQuery()) {
                    while (rs.next()) {
                        ItemPedido it = new ItemPedido();
                        it.setId(rs.getInt("id_item"));
                        it.setPedidoId(rs.getInt("id_pedido"));
                        it.setProductoId(rs.getInt("id_producto"));
                        it.setCantidad(rs.getInt("cantidad"));
                        it.setSubtotal(rs.getBigDecimal("subtotal"));
                        items.add(it);
                    }
                    System.out.println("[OK] " + items.size() + " items cargados para pedido " + pedidoId + " desde tabla " + table);
                }
            }
        } catch (SQLException e) {
            System.err.println("[ERROR] Error al obtener items por pedido: " + e.getMessage());
            e.printStackTrace();
        }
        return items;
    }

    public boolean insertarItem(ItemPedido item) {
        try (Connection conn = Conexion.conectar()) {
            if (conn == null) {
                System.err.println("[ERROR] Conexion nula al insertar item pedido");
                return false;
            }
            String table = detectTableName(conn);
            String sql = "INSERT INTO " + table + " (id_pedido, id_producto, cantidad, subtotal) VALUES (?, ?, ?, ?)";
            System.out.println("[DEBUG] SQL insertarItem: " + sql);
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setInt(1, item.getPedidoId());
                pstmt.setInt(2, item.getProductoId());
                pstmt.setInt(3, item.getCantidad());
                pstmt.setBigDecimal(4, item.getSubtotal() != null ? item.getSubtotal() : BigDecimal.ZERO);
                int filas = pstmt.executeUpdate();
                boolean ok = filas > 0;
                System.out.println("[OK] ItemPedido insertado: " + ok + " (pedido=" + item.getPedidoId() + ", producto=" + item.getProductoId() + ", filas=" + filas + ")");
                return ok;
            }
        } catch (SQLException e) {
            System.err.println("[ERROR] Error al insertar item pedido: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public boolean eliminarItemsPorPedido(int pedidoId) {
        try (Connection conn = Conexion.conectar()) {
            if (conn == null) {
                System.err.println("[ERROR] Conexion nula al eliminar items por pedido");
                return false;
            }
            String table = detectTableName(conn);
            String sql = "DELETE FROM " + table + " WHERE id_pedido = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setInt(1, pedidoId);
                int filas = pstmt.executeUpdate();
                System.out.println("[OK] Items eliminados para pedido " + pedidoId + ": filas=" + filas);
                return true;
            }
        } catch (SQLException e) {
            System.err.println("[ERROR] Error al eliminar items por pedido: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}
