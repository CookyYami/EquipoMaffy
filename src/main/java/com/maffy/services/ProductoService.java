package com.maffy.services;

import com.maffy.Dao.ProductoDAO;
import com.maffy.models.Producto;
import java.util.List;

/**
 * Servicio ligero que delega en ProductoDAO para acceder a la tabla real en Supabase.
 * Esto evita duplicar SQL con nombres/columnas distintas y permite que la detección
 * de tabla/columnas del DAO se use de forma centralizada.
 */
public class ProductoService {

    private final ProductoDAO productoDAO = new ProductoDAO();

    public List<Producto> obtenerTodosProductos() {
        return productoDAO.obtenerTodosProductos();
    }

    public boolean agregarProducto(Producto p) {
        return productoDAO.insertarProducto(p);
    }

    public boolean actualizarProducto(Producto p) {
        return productoDAO.actualizarProducto(p);
    }

    public boolean eliminarProducto(int id) {
        return productoDAO.eliminarProducto(id);
    }

    public Producto obtenerProductoPorId(int id) {
        return productoDAO.obtenerProductoPorId(id);
    }

    public boolean ajustarStock(int id, int delta) {
        // DAO no tiene método explícito para ajustar stock, reuse actualizarProducto if needed.
        // For now implement a quick update via DAO existing methods: fetch, modify, update.
        Producto p = productoDAO.obtenerProductoPorId(id);
        if (p == null) return false;
        p.setStock(p.getStock() + delta);
        return productoDAO.actualizarProducto(p);
    }
}