/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.maffy.Dao;

import com.maffy.database.Conexion;
import com.maffy.models.Cliente;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author HP
 */
public class ClienteDAO {

    public List<Cliente> obtenerTodosClientes() {
        List<Cliente> clientes = new ArrayList<>();
        // CAMBIO: "Cliente" por "cliente" (minúscula)
        String sql = "SELECT id_cliente, nombre_cliente, telefono, direccion FROM cliente";

        try (Connection conn = Conexion.conectar(); PreparedStatement pstmt = conn.prepareStatement(sql); ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                Cliente cliente = new Cliente();
                cliente.setId(rs.getInt("id_cliente"));
                cliente.setNombre(rs.getString("nombre_cliente"));
                cliente.setTelefono(rs.getString("telefono"));
                cliente.setDireccion(rs.getString("direccion"));
                clientes.add(cliente);
            }

            System.out.println("[OK] " + clientes.size() + " clientes cargados desde Supabase");

        } catch (SQLException e) {
            System.err.println("[ERROR] Error al obtener clientes: " + e.getMessage());
            e.printStackTrace();
        }
        return clientes;
    }

    public boolean insertarCliente(Cliente cliente) {
        // CAMBIO: "Cliente" por "cliente" (minúscula)
        String sql = "INSERT INTO cliente (nombre_cliente, telefono, direccion) VALUES (?, ?, ?)";

        try (Connection conn = Conexion.conectar(); PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, cliente.getNombre());
            pstmt.setString(2, cliente.getTelefono());
            pstmt.setString(3, cliente.getDireccion());

            int filasAfectadas = pstmt.executeUpdate();
            System.out.println("[OK] Cliente insertado: " + (filasAfectadas > 0));
            return filasAfectadas > 0;

        } catch (SQLException e) {
            System.err.println("[ERROR] Error al insertar cliente: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public boolean actualizarCliente(Cliente cliente) {
        // CAMBIO: "Cliente" por "cliente" (minúscula)
        String sql = "UPDATE cliente SET nombre_cliente = ?, telefono = ?, direccion = ? WHERE id_cliente = ?";

        try (Connection conn = Conexion.conectar(); PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, cliente.getNombre());
            pstmt.setString(2, cliente.getTelefono());
            pstmt.setString(3, cliente.getDireccion());
            pstmt.setInt(4, cliente.getId());

            int filasAfectadas = pstmt.executeUpdate();
            System.out.println("[OK] Cliente actualizado: " + (filasAfectadas > 0));
            return filasAfectadas > 0;

        } catch (SQLException e) {
            System.err.println("[ERROR] Error al actualizar cliente: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public boolean eliminarCliente(int id) {
        // CAMBIO: "Cliente" por "cliente" (minúscula)
        String sql = "DELETE FROM cliente WHERE id_cliente = ?";

        try (Connection conn = Conexion.conectar(); PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            int filasAfectadas = pstmt.executeUpdate();
            System.out.println("[OK] Cliente eliminado: " + (filasAfectadas > 0));
            return filasAfectadas > 0;

        } catch (SQLException e) {
            System.err.println("[ERROR] Error al eliminar cliente: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}
