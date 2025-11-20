package com.maffy.Dao;

import com.maffy.database.Conexion;
import com.maffy.models.Producto;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO para la tabla Producto (segÃºn esquema de Supabase)
 */
public class ProductoDAO {

	// cache del nombre de tabla detectado
	private String detectedTable = null;

	private String detectTableName(Connection conn) {
		if (detectedTable != null) return detectedTable;
		String[] candidates = {"\"Producto\"", "producto", "\"producto\"", "productos", "\"Productos\""};
		for (String candidate : candidates) {
			String test = "SELECT 1 FROM " + candidate + " LIMIT 1";
			try (PreparedStatement pst = conn.prepareStatement(test); ResultSet rs = pst.executeQuery()) {
				// si no lanza excepción, asumimos que la tabla existe
				detectedTable = candidate;
				System.out.println("[INFO] Tabla de productos detectada: " + detectedTable);
				return detectedTable;
			} catch (SQLException ex) {
				// intentar siguiente candidato
			}
		}
		// fallback al nombre con comillas que venía usando
		detectedTable = "\"Producto\"";
		System.out.println("[WARN] No se pudo detectar tabla de productos; usando fallback: " + detectedTable);
		return detectedTable;
	}

	public List<Producto> obtenerTodosProductos() {
		List<Producto> productos = new ArrayList<>();
		try (Connection conn = Conexion.conectar()) {
			if (conn == null) {
				System.err.println("[ERROR] Conexion nula al obtener productos");
				return productos;
			}
			String table = detectTableName(conn);
			String sql = "SELECT * FROM " + table;
			try (PreparedStatement pstmt = conn.prepareStatement(sql); ResultSet rs = pstmt.executeQuery()) {
				while (rs.next()) {
					Producto producto = new Producto();
					// map common column names if present
					try { producto.setId(rs.getInt("id_producto")); } catch (SQLException ex) { }
					try { producto.setNombre(rs.getString("nombre_producto")); } catch (SQLException ex) { }
					// descripcion puede llamarse 'descripcion' o 'descripcion_producto'
					String desc = null;
					try { desc = rs.getString("descripcion"); } catch (SQLException ex) { }
					if (desc == null) {
						try { desc = rs.getString("descripcion_producto"); } catch (SQLException ex) { }
					}
					producto.setDescripcion(desc);
					try { producto.setPrecio(rs.getBigDecimal("precio")); } catch (SQLException ex) { }
					try { producto.setStock(rs.getInt("stock_actual")); } catch (SQLException ex) { }
					productos.add(producto);
				}
				System.out.println("[OK] " + productos.size() + " productos cargados desde tabla: " + table);
			}
		} catch (SQLException e) {
			System.err.println("[ERROR] Error al obtener productos: " + e.getMessage());
			e.printStackTrace();
		}
		return productos;
	}

	public boolean insertarProducto(Producto producto) {
		try (Connection conn = Conexion.conectar()) {
			if (conn == null) return false;
			String table = detectTableName(conn);
			String sql = "INSERT INTO " + table + " (nombre_producto, precio, stock_actual) VALUES (?, ?, ?)";
			try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
				pstmt.setString(1, producto.getNombre());
				pstmt.setBigDecimal(2, producto.getPrecio());
				pstmt.setInt(3, producto.getStock());
				int filas = pstmt.executeUpdate();
				return filas > 0;
			}
		} catch (SQLException e) {
			System.err.println("[ERROR] Error al insertar producto: " + e.getMessage());
			e.printStackTrace();
			return false;
		}
	}

	public boolean actualizarProducto(Producto producto) {
		try (Connection conn = Conexion.conectar()) {
			if (conn == null) return false;
			String table = detectTableName(conn);
			String sql = "UPDATE " + table + " SET nombre_producto = ?, precio = ?, stock_actual = ? WHERE id_producto = ?";
			try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
				pstmt.setString(1, producto.getNombre());
				pstmt.setBigDecimal(2, producto.getPrecio());
				pstmt.setInt(3, producto.getStock());
				pstmt.setInt(4, producto.getId());
				int filas = pstmt.executeUpdate();
				return filas > 0;
			}
		} catch (SQLException e) {
			System.err.println("[ERROR] Error al actualizar producto: " + e.getMessage());
			e.printStackTrace();
			return false;
		}
	}

	public boolean eliminarProducto(int id) {
		try (Connection conn = Conexion.conectar()) {
			if (conn == null) return false;
			String table = detectTableName(conn);
			String sql = "DELETE FROM " + table + " WHERE id_producto = ?";
			try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
				pstmt.setInt(1, id);
				int filas = pstmt.executeUpdate();
				return filas > 0;
			}
		} catch (SQLException e) {
			System.err.println("[ERROR] Error al eliminar producto: " + e.getMessage());
			e.printStackTrace();
			return false;
		}
	}

	public Producto obtenerProductoPorId(int id) {
		try (Connection conn = Conexion.conectar()) {
			if (conn == null) {
				System.err.println("[ERROR] Conexion nula al obtener producto por id");
				return null;
			}
			String table = detectTableName(conn);
				String sql = "SELECT * FROM " + table + " WHERE id_producto = ?";
				try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
					pstmt.setInt(1, id);
					try (ResultSet rs = pstmt.executeQuery()) {
						if (rs.next()) {
							Producto producto = new Producto();
							try { producto.setId(rs.getInt("id_producto")); } catch (SQLException ex) { }
							try { producto.setNombre(rs.getString("nombre_producto")); } catch (SQLException ex) { }
							String desc = null;
							try { desc = rs.getString("descripcion"); } catch (SQLException ex) { }
							if (desc == null) {
								try { desc = rs.getString("descripcion_producto"); } catch (SQLException ex) { }
							}
							producto.setDescripcion(desc);
							try { producto.setPrecio(rs.getBigDecimal("precio")); } catch (SQLException ex) { }
							try { producto.setStock(rs.getInt("stock_actual")); } catch (SQLException ex) { }
							return producto;
						}
					}
				}
		} catch (SQLException e) {
			System.err.println("[ERROR] Error al obtener producto por ID: " + e.getMessage());
			e.printStackTrace();
		}
		return null;
	}
}
