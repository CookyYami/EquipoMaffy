package com.maffy.Dao;

import com.maffy.database.Conexion;
import com.maffy.models.Pedido;
import com.maffy.utils.Logger;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.math.BigDecimal;

/**
 * DAO para la tabla Pedido
 */
public class PedidoDAO {

    // cache del nombre de tabla detectado
    private String detectedTable = null;

    private String detectTableName(Connection conn) {
        if (detectedTable != null) return detectedTable;
        String[] candidates = {"\"Pedido\"", "pedido", "\"pedido\"", "pedidos", "\"Pedidos\""};
        for (String candidate : candidates) {
            String test = "SELECT 1 FROM " + candidate + " LIMIT 1";
            try (PreparedStatement pst = conn.prepareStatement(test); ResultSet rs = pst.executeQuery()) {
                detectedTable = candidate;
                Logger.info("Tabla de pedidos detectada: " + detectedTable);
                System.out.println("[INFO] Tabla de pedidos detectada: " + detectedTable);
                return detectedTable;
            } catch (SQLException ex) {
                // intentar siguiente candidato
            }
        }
        detectedTable = "\"Pedido\"";
        Logger.warn("No se pudo detectar tabla de pedidos; usando fallback: " + detectedTable);
        System.out.println("[WARN] No se pudo detectar tabla de pedidos; usando fallback: " + detectedTable);
        return detectedTable;
    }

    public List<Pedido> obtenerTodosPedidos() {
        List<Pedido> pedidos = new ArrayList<>();
        Logger.debug("obtenerTodosPedidos() iniciando...");
        System.out.println("[DEBUG-PEDIDO] obtenerTodosPedidos() iniciando...");
        try (Connection conn = Conexion.conectar()) {
            if (conn == null) {
                Logger.error("Conexion nula al obtener pedidos");
                System.err.println("[ERROR-PEDIDO] Conexion nula al obtener pedidos");
                return pedidos;
            }
            System.out.println("[DEBUG-PEDIDO] Conexión obtenida correctamente");
            String table = detectTableName(conn);
            System.out.println("[DEBUG-PEDIDO] Tabla detectada: " + table);
            String sql = "SELECT id_pedido, fecha, estado, id_cliente, id_usuario, total FROM " + table + " ORDER BY id_pedido DESC";
            Logger.debug("SQL: " + sql);
            System.out.println("[DEBUG-PEDIDO] SQL: " + sql);
            try (PreparedStatement pstmt = conn.prepareStatement(sql); ResultSet rs = pstmt.executeQuery()) {
                System.out.println("[DEBUG-PEDIDO] Query ejecutada, iterando resultados...");
                int count = 0;
                while (rs.next()) {
                    Pedido p = new Pedido();
                    p.setId(rs.getInt("id_pedido"));
                    Date d = rs.getDate("fecha");
                    if (d != null) p.setFecha(d.toLocalDate());
                    p.setEstado(rs.getString("estado"));
                    p.setClienteId(rs.getInt("id_cliente"));
                    p.setUsuarioId(rs.getInt("id_usuario"));
                    p.setTotal(rs.getBigDecimal("total"));
                    pedidos.add(p);
                    count++;
                    System.out.println("[DEBUG-PEDIDO]   Fila " + count + ": id=" + p.getId() + ", estado=" + p.getEstado());
                }
                Logger.info("" + pedidos.size() + " pedidos cargados desde tabla " + table);
                System.out.println("[OK-PEDIDO] " + pedidos.size() + " pedidos cargados desde tabla " + table);
            }
        } catch (SQLException e) {
            Logger.exception("Error al obtener pedidos", e);
            System.err.println("[ERROR-PEDIDO] Error al obtener pedidos: " + e.getMessage());
            System.err.println("[ERROR-PEDIDO] SQLState: " + e.getSQLState());
            System.err.println("[ERROR-PEDIDO] VendorCode: " + e.getErrorCode());
            e.printStackTrace();
        }
        return pedidos;
    }

    public int insertarPedido(Pedido pedido) {
        try (Connection conn = Conexion.conectar()) {
            if (conn == null) {
                System.err.println("[ERROR] Conexion nula al insertar pedido");
                return -1;
            }
            String table = detectTableName(conn);
            String sql = "INSERT INTO " + table + " (fecha, estado, id_cliente, id_usuario, total) VALUES (?, ?, ?, ?, ?) RETURNING id_pedido";
            LocalDate fecha = pedido.getFecha() != null ? pedido.getFecha() : LocalDate.now();
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setDate(1, Date.valueOf(fecha));
                pstmt.setString(2, pedido.getEstado());
                pstmt.setInt(3, pedido.getClienteId());
                pstmt.setInt(4, pedido.getUsuarioId());
                pstmt.setBigDecimal(5, pedido.getTotal() != null ? pedido.getTotal() : BigDecimal.ZERO);

                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                        int id = rs.getInt(1);
                        System.out.println("[OK] Pedido insertado con id=" + id + " en tabla " + table);
                        return id;
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("[ERROR] Error al insertar pedido: " + e.getMessage());
            e.printStackTrace();
        }
        return -1;
    }

    public Pedido obtenerPedidoPorId(int id) {
        try (Connection conn = Conexion.conectar()) {
            if (conn == null) return null;
            String table = detectTableName(conn);
            String sql = "SELECT id_pedido, fecha, estado, id_cliente, id_usuario, total FROM " + table + " WHERE id_pedido = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setInt(1, id);
                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                        Pedido p = new Pedido();
                        p.setId(rs.getInt("id_pedido"));
                        Date d = rs.getDate("fecha");
                        if (d != null) p.setFecha(d.toLocalDate());
                        p.setEstado(rs.getString("estado"));
                        p.setClienteId(rs.getInt("id_cliente"));
                        p.setUsuarioId(rs.getInt("id_usuario"));
                        p.setTotal(rs.getBigDecimal("total"));
                        return p;
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("[ERROR] Error al obtener pedido por id: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    public boolean eliminarPedido(int id) {
        try (Connection conn = Conexion.conectar()) {
            if (conn == null) return false;
            String table = detectTableName(conn);
            String sql = "DELETE FROM " + table + " WHERE id_pedido = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setInt(1, id);
                int filas = pstmt.executeUpdate();
                System.out.println("[OK] Pedido eliminado: " + (filas > 0));
                return filas > 0;
            }
        } catch (SQLException e) {
            System.err.println("[ERROR] Error al eliminar pedido: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public boolean actualizarPedido(Pedido pedido) {
        try (Connection conn = Conexion.conectar()) {
            if (conn == null) {
                System.err.println("[ERROR] Conexion nula al actualizar pedido");
                return false;
            }
            String table = detectTableName(conn);
            String sql = "UPDATE " + table + " SET fecha = ?, estado = ?, id_cliente = ?, id_usuario = ?, total = ? WHERE id_pedido = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                java.sql.Date fechaSql = java.sql.Date.valueOf(pedido.getFecha() != null ? pedido.getFecha() : java.time.LocalDate.now());
                pstmt.setDate(1, fechaSql);
                pstmt.setString(2, pedido.getEstado());
                pstmt.setInt(3, pedido.getClienteId());
                pstmt.setInt(4, pedido.getUsuarioId());
                pstmt.setBigDecimal(5, pedido.getTotal() != null ? pedido.getTotal() : java.math.BigDecimal.ZERO);
                pstmt.setInt(6, pedido.getId());
                int filas = pstmt.executeUpdate();
                System.out.println("[OK] Pedido actualizado: " + (filas > 0));
                return filas > 0;
            }
        } catch (SQLException e) {
            System.err.println("[ERROR] Error al actualizar pedido: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}
