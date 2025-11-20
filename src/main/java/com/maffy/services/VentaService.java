package com.maffy.services;

import com.maffy.database.Conexion;
import com.maffy.models.Venta;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class VentaService {
    
public boolean registrarVenta(Venta venta) {
    String sql = "INSERT INTO pedido (id_cliente, total, fecha, estado) VALUES (?, ?, ?, ?)";
    
    try (Connection conn = Conexion.conectar();
         PreparedStatement pstmt = conn.prepareStatement(sql)) {

        pstmt.setInt(1, venta.getClienteId());
        pstmt.setBigDecimal(2, venta.getTotal());
        pstmt.setDate(3, java.sql.Date.valueOf(venta.getFechaVenta().toLocalDate())); 
        pstmt.setString(4, venta.getEstado());

        return pstmt.executeUpdate() > 0;

    } catch (SQLException e) {
        System.out.println("❌ Error registrando venta: " + e.getMessage());
        return false;
    }
}

    
  public List<Venta> obtenerVentasDelDia() {
    List<Venta> ventas = new ArrayList<>();

    String sql =
        "SELECT id_pedido, fecha, id_cliente, total, estado " +
        "FROM pedido " +
        "WHERE fecha = CURRENT_DATE " +
        "AND UPPER(estado) LIKE 'COMPLET%' " +
        "ORDER BY id_pedido";

    try (Connection conn = Conexion.conectar();
         PreparedStatement pstmt = conn.prepareStatement(sql);
         ResultSet rs = pstmt.executeQuery()) {

        while (rs.next()) {
            Venta venta = new Venta();

            venta.setId(rs.getInt("id_pedido"));
            venta.setClienteId(rs.getInt("id_cliente"));
            venta.setTotal(rs.getBigDecimal("total"));
            venta.setFechaVenta(rs.getDate("fecha").toLocalDate().atStartOfDay());
            venta.setEstado(rs.getString("estado"));

            ventas.add(venta);
        }

    } catch (SQLException e) {
        System.out.println("❌ Error obteniendo ventas del día: " + e.getMessage());
    }

    return ventas;
}


    /**
     * Obtiene ventas completadas entre dos fechas (inclusive). Las fechas son LocalDate (sin hora).
     */
public List<Venta> obtenerVentasPorRango(LocalDate desde, LocalDate hasta) {
    List<Venta> ventas = new ArrayList<>();

    String sql =
        "SELECT id_pedido, fecha, id_cliente, total, estado " +
        "FROM pedido " +
        "WHERE fecha BETWEEN ? AND ? " +
        "AND UPPER(estado) LIKE 'COMPLET%' " +
        "ORDER BY fecha";

    try (Connection conn = Conexion.conectar();
         PreparedStatement pstmt = conn.prepareStatement(sql)) {

        pstmt.setDate(1, java.sql.Date.valueOf(desde));
        pstmt.setDate(2, java.sql.Date.valueOf(hasta));

        try (ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {

                Venta venta = new Venta();

                venta.setId(rs.getInt("id_pedido"));
                venta.setClienteId(rs.getInt("id_cliente"));
                venta.setTotal(rs.getBigDecimal("total"));

                // fecha = DATE (sin hora) → convertir a LocalDateTime
                venta.setFechaVenta(rs.getDate("fecha").toLocalDate().atStartOfDay());

                venta.setEstado(rs.getString("estado"));

                ventas.add(venta);
            }
        }

    } catch (SQLException e) {
        System.out.println("❌ Error obteniendo ventas por rango: " + e.getMessage());
    }

    return ventas;
}
}