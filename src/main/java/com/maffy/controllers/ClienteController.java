/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.maffy.controllers;

import com.maffy.Dao.ClienteDAO;
import com.maffy.models.Cliente;
import java.util.List;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author HP
 */
public class ClienteController {
    private ClienteDAO clienteDAO;
    
    public ClienteController() {
        this.clienteDAO = new ClienteDAO();
    }
    
    public boolean cargarClientesEnTabla(DefaultTableModel modeloTabla) {
        try {
            // Limpiar tabla existente
            modeloTabla.setRowCount(0);
            
            // Obtener clientes de Supabase
            List<Cliente> clientes = clienteDAO.obtenerTodosClientes();
            
            // Llenar la tabla con las columnas correctas
            for (Cliente cliente : clientes) {
                Object[] fila = {
                    cliente.getId(),           // id_cliente
                    cliente.getNombre(),       // nombre_cliente  
                    cliente.getTelefono(),     // telefono
                    cliente.getDireccion()// direccion
                };
                modeloTabla.addRow(fila);
            }
            
            System.out.println("[OK] Tabla actualizada con " + clientes.size() + " clientes");
            return true;
            
        } catch (Exception e) {
            System.err.println("[ERROR] Error al cargar clientes en tabla: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    public List<Cliente> obtenerTodosClientes() {
        return clienteDAO.obtenerTodosClientes();
    }
    
    // Métodos actualizados para usar direccion en lugar de email
    public boolean agregarCliente(String nombre, String telefono, String direccion) {
        Cliente cliente = new Cliente(nombre, telefono, direccion);
        return clienteDAO.insertarCliente(cliente);
    }
    
    public boolean actualizarCliente(int id, String nombre, String telefono, String direccion) {
        Cliente cliente = new Cliente(nombre, telefono, direccion);
        cliente.setId(id);
        return clienteDAO.actualizarCliente(cliente);
    }
    
    public boolean eliminarCliente(int id) {
        return clienteDAO.eliminarCliente(id);
    }
}
