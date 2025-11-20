package Vista;

import com.maffy.models.Producto;
import com.maffy.services.ProductoService;
import java.math.BigDecimal;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class PanelInventario extends javax.swing.JPanel {

    private ProductoService productoService;
    private DefaultTableModel modeloTabla;

    public PanelInventario() {
        initComponents();
        productoService = new ProductoService();
        configurarTabla();
        configurarEventos();
        cargarDatos();
    }

    private void configurarTabla() {
        modeloTabla = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        modeloTabla.addColumn("ID");
        modeloTabla.addColumn("NOMBRE");
        modeloTabla.addColumn("PRECIO");
        modeloTabla.addColumn("STOCK");
        tablaProductos.setModel(modeloTabla);
    }

    private void configurarEventos() {
        tablaProductos.getSelectionModel().addListSelectionListener(e -> {
            if (e.getValueIsAdjusting()) return;
            int fila = tablaProductos.getSelectedRow();
            if (fila >= 0) {
                cargarProductoSeleccionado();
                btnEditar.setEnabled(true);
                btnEliminar.setEnabled(true);
            } else {
                // no selection: clear fields and disable edit/delete
                txtId.setText("");
                txtNombre.setText("");
                txtPrecio.setText("");
                txtStock.setText("");
                btnEditar.setEnabled(false);
                btnEliminar.setEnabled(false);
            }
        });
    }

    private void cargarDatos() {
        modeloTabla.setRowCount(0);
        List<Producto> productos = productoService.obtenerTodosProductos();
        for (Producto p : productos) {
            modeloTabla.addRow(new Object[]{p.getId(), p.getNombre(), p.getPrecio(), p.getStock()});
        }
    }

    private void limpiarCampos() {
        txtId.setText("");
        txtNombre.setText("");
        txtPrecio.setText("");
        txtStock.setText("");
        txtBuscar.setText("");
        tablaProductos.clearSelection();
        btnEditar.setEnabled(false);
        btnEliminar.setEnabled(false);
    }

    private void cargarProductoSeleccionado() {
        int fila = tablaProductos.getSelectedRow();
        if (fila < 0) return;
        txtId.setText(String.valueOf(modeloTabla.getValueAt(fila, 0)));
        txtNombre.setText(String.valueOf(modeloTabla.getValueAt(fila, 1)));
        txtPrecio.setText(String.valueOf(modeloTabla.getValueAt(fila, 2)));
        txtStock.setText(String.valueOf(modeloTabla.getValueAt(fila, 3)));
        // Categoria no está presente en la tabla de Supabase; campo removido en UI
    }

    private void guardarProducto() {
        try {
            String nombre = txtNombre.getText().trim();
            BigDecimal precio = new BigDecimal(txtPrecio.getText().trim());
            int stock = Integer.parseInt(txtStock.getText().trim());

            if (nombre.isEmpty()) { JOptionPane.showMessageDialog(this, "Nombre requerido"); return; }

            Producto p = new Producto();
            p.setNombre(nombre);
            p.setPrecio(precio);
            p.setStock(stock);
            if (productoService.agregarProducto(p)) {
                JOptionPane.showMessageDialog(this, "Producto creado");
                cargarDatos();
                limpiarCampos();
            } else {
                JOptionPane.showMessageDialog(this, "Error al crear producto");
            }
        } catch (NumberFormatException nfe) {
            JOptionPane.showMessageDialog(this, "Verifica precio y stock: " + nfe.getMessage());
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    private void editarProducto() {
        if (txtId.getText().isEmpty()) { JOptionPane.showMessageDialog(this, "Selecciona un producto para editar"); return; }
        try {
            int id = Integer.parseInt(txtId.getText());
            String nombre = txtNombre.getText().trim();
            BigDecimal precio = new BigDecimal(txtPrecio.getText().trim());
            int stock = Integer.parseInt(txtStock.getText().trim());

            Producto p = new Producto();
            p.setId(id);
            p.setNombre(nombre);
            p.setPrecio(precio);
            p.setStock(stock);
            

            if (productoService.actualizarProducto(p)) {
                JOptionPane.showMessageDialog(this, "Producto actualizado");
                cargarDatos();
                limpiarCampos();
            } else {
                JOptionPane.showMessageDialog(this, "Error al actualizar producto");
            }
        } catch (NumberFormatException nfe) {
            JOptionPane.showMessageDialog(this, "Verifica campos numéricos: " + nfe.getMessage());
        }
    }

    private void eliminarProducto() {
        if (txtId.getText().isEmpty()) { JOptionPane.showMessageDialog(this, "Selecciona un producto para eliminar"); return; }
        try {
            int id = Integer.parseInt(txtId.getText());
            int confirmar = JOptionPane.showConfirmDialog(this, "¿Eliminar producto id=" + id + "?", "Confirmar", JOptionPane.YES_NO_OPTION);
            if (confirmar == JOptionPane.YES_OPTION) {
                if (productoService.eliminarProducto(id)) {
                    JOptionPane.showMessageDialog(this, "Producto eliminado");
                    cargarDatos();
                    limpiarCampos();
                } else {
                    JOptionPane.showMessageDialog(this, "Error al eliminar producto");
                }
            }
        } catch (NumberFormatException nfe) {
            JOptionPane.showMessageDialog(this, "ID inválido");
        }
    }

    private void ajustarStock(boolean aumentar) {
        if (txtId.getText().isEmpty()) { JOptionPane.showMessageDialog(this, "Selecciona un producto para ajustar stock"); return; }
        try {
            int id = Integer.parseInt(txtId.getText());
            String input = JOptionPane.showInputDialog(this, (aumentar ? "Cantidad a aumentar:" : "Cantidad a disminuir:"));
            if (input == null) return; // cancel
            int delta = Integer.parseInt(input);
            if (!aumentar) delta = -Math.abs(delta);
            if (productoService.ajustarStock(id, delta)) {
                JOptionPane.showMessageDialog(this, "Stock ajustado");
                cargarDatos();
                limpiarCampos();
            } else {
                JOptionPane.showMessageDialog(this, "Error al ajustar stock");
            }
        } catch (NumberFormatException nfe) {
            JOptionPane.showMessageDialog(this, "Cantidad inválida");
        }
    }

    private void buscar() {
        String texto = txtBuscar.getText().trim().toLowerCase();
        if (texto.isEmpty()) { cargarDatos(); return; }
        modeloTabla.setRowCount(0);
        List<Producto> productos = productoService.obtenerTodosProductos();
        for (Producto p : productos) {
            if (String.valueOf(p.getId()).contains(texto)
                    || (p.getNombre() != null && p.getNombre().toLowerCase().contains(texto))) {
                modeloTabla.addRow(new Object[]{p.getId(), p.getNombre(), p.getPrecio(), p.getStock()});
            }
        }
    }

    // UI generated code (minimal) -------------------------------------------------
    private void initComponents() {
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        txtId = new javax.swing.JTextField();
        txtNombre = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        txtPrecio = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        txtStock = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tablaProductos = new javax.swing.JTable();
        btnGuardar = new javax.swing.JButton();
        btnLimpiar = new javax.swing.JButton();
        btnEditar = new javax.swing.JButton();
        btnEliminar = new javax.swing.JButton();
        btnAumentar = new javax.swing.JButton();
        btnDisminuir = new javax.swing.JButton();
        jLabel7 = new javax.swing.JLabel();
        txtBuscar = new javax.swing.JTextField();

        setPreferredSize(new java.awt.Dimension(940, 550));
        setBackground(new java.awt.Color(255, 254, 194));
        setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 18));
        jLabel1.setText("INVENTARIO");
        add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 10, -1, -1));

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 14));
        jLabel2.setText("ID:");
        add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 50, -1, -1));
        add(txtId, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 50, 140, -1));

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 14));
        jLabel3.setText("Nombre:");
        add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 90, -1, -1));
        add(txtNombre, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 90, 260, -1));

        // Precio label + field (moved up because Descripcion was removed)
        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 14));
        jLabel5.setText("Precio:");
        add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 130, -1, -1));
        add(txtPrecio, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 130, 120, -1));

        // Stock label + field (positioned to the right of Precio)
        jLabel6.setFont(new java.awt.Font("Segoe UI", 1, 14));
        jLabel6.setText("Stock:");
        add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 130, -1, -1));
        add(txtStock, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 130, 80, -1));

        // (Precio and Stock were added above; remove any duplicate code here)

        tablaProductos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {},
            new String [] {"ID","NOMBRE","PRECIO","STOCK"}
        ));
        tablaProductos.setRowHeight(24);
        tablaProductos.getTableHeader().setFont(new java.awt.Font("Segoe UI", 1, 12));
        jScrollPane1.setViewportView(tablaProductos);
        // moved table down to create space between buttons and table
        add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 260, 820, 280));

        btnGuardar.setBackground(new java.awt.Color(102, 204, 255));
        btnGuardar.setFont(new java.awt.Font("Segoe UI", 1, 14));
        btnGuardar.setText("Guardar");
        btnGuardar.addActionListener(evt -> guardarProducto());
        add(btnGuardar, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 200, 100, -1));

        btnLimpiar.setBackground(new java.awt.Color(255, 255, 153));
        btnLimpiar.setFont(new java.awt.Font("Segoe UI", 1, 14));
        btnLimpiar.setText("Limpiar");
        btnLimpiar.addActionListener(evt -> limpiarCampos());
        add(btnLimpiar, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 200, 100, -1));

        btnEditar.setBackground(new java.awt.Color(153, 255, 255));
        btnEditar.setFont(new java.awt.Font("Segoe UI", 1, 14));
        btnEditar.setText("Editar");
        btnEditar.setEnabled(false);
        btnEditar.addActionListener(evt -> editarProducto());
        add(btnEditar, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 200, 100, -1));

        btnEliminar.setBackground(new java.awt.Color(255, 153, 153));
        btnEliminar.setFont(new java.awt.Font("Segoe UI", 1, 14));
        btnEliminar.setText("Eliminar");
        btnEliminar.setEnabled(false);
        btnEliminar.addActionListener(evt -> eliminarProducto());
        add(btnEliminar, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 200, 100, -1));

        btnAumentar.setBackground(new java.awt.Color(204, 255, 204));
        btnAumentar.setFont(new java.awt.Font("Segoe UI", 1, 14));
        btnAumentar.setText("Aumentar");
        btnAumentar.addActionListener(evt -> ajustarStock(true));
        add(btnAumentar, new org.netbeans.lib.awtextra.AbsoluteConstraints(470, 200, 100, -1));

        btnDisminuir.setBackground(new java.awt.Color(255, 204, 204));
        btnDisminuir.setFont(new java.awt.Font("Segoe UI", 1, 14));
        btnDisminuir.setText("Disminuir");
        btnDisminuir.addActionListener(evt -> ajustarStock(false));
        add(btnDisminuir, new org.netbeans.lib.awtextra.AbsoluteConstraints(580, 200, 100, -1));

        jLabel7.setFont(new java.awt.Font("Segoe UI", 1, 14));
        jLabel7.setText("Buscar:");
        add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(680, 50, -1, -1));
        txtBuscar.addActionListener(evt -> buscar());
        add(txtBuscar, new org.netbeans.lib.awtextra.AbsoluteConstraints(730, 50, 150, -1));
    }

    // Variables
    private javax.swing.JButton btnAumentar;
    private javax.swing.JButton btnDisminuir;
    private javax.swing.JButton btnEditar;
    private javax.swing.JButton btnEliminar;
    private javax.swing.JButton btnGuardar;
    private javax.swing.JButton btnLimpiar;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tablaProductos;
    private javax.swing.JTextField txtBuscar;
    private javax.swing.JTextField txtId;
    private javax.swing.JTextField txtNombre;
    private javax.swing.JTextField txtPrecio;
    private javax.swing.JTextField txtStock;
}
