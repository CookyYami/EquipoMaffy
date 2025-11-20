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
        modeloTabla.addColumn("DESCRIPCION");
        modeloTabla.addColumn("PRECIO");
        modeloTabla.addColumn("STOCK");
        modeloTabla.addColumn("CATEGORIA");
        tablaProductos.setModel(modeloTabla);
    }

    private void configurarEventos() {
        tablaProductos.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) cargarProductoSeleccionado();
        });
    }

    private void cargarDatos() {
        modeloTabla.setRowCount(0);
        List<Producto> productos = productoService.obtenerTodosProductos();
        for (Producto p : productos) {
            modeloTabla.addRow(new Object[]{p.getId(), p.getNombre(), p.getDescripcion(), p.getPrecio(), p.getStock(), p.getCategoria()});
        }
    }

    private void limpiarCampos() {
        txtId.setText("");
        txtNombre.setText("");
        txtDescripcion.setText("");
        txtPrecio.setText("");
        txtStock.setText("");
        txtCategoria.setText("");
        txtBuscar.setText("");
        tablaProductos.clearSelection();
    }

    private void cargarProductoSeleccionado() {
        int fila = tablaProductos.getSelectedRow();
        if (fila < 0) return;
        txtId.setText(String.valueOf(modeloTabla.getValueAt(fila, 0)));
        txtNombre.setText(String.valueOf(modeloTabla.getValueAt(fila, 1)));
        txtDescripcion.setText(String.valueOf(modeloTabla.getValueAt(fila, 2)));
        txtPrecio.setText(String.valueOf(modeloTabla.getValueAt(fila, 3)));
        txtStock.setText(String.valueOf(modeloTabla.getValueAt(fila, 4)));
        txtCategoria.setText(String.valueOf(modeloTabla.getValueAt(fila, 5)));
    }

    private void guardarProducto() {
        try {
            String nombre = txtNombre.getText().trim();
            String descripcion = txtDescripcion.getText().trim();
            BigDecimal precio = new BigDecimal(txtPrecio.getText().trim());
            int stock = Integer.parseInt(txtStock.getText().trim());
            String categoria = txtCategoria.getText().trim();

            if (nombre.isEmpty()) { JOptionPane.showMessageDialog(this, "Nombre requerido"); return; }

            Producto p = new Producto(nombre, descripcion, precio, stock, categoria);
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
            String descripcion = txtDescripcion.getText().trim();
            BigDecimal precio = new BigDecimal(txtPrecio.getText().trim());
            int stock = Integer.parseInt(txtStock.getText().trim());
            String categoria = txtCategoria.getText().trim();

            Producto p = new Producto();
            p.setId(id);
            p.setNombre(nombre);
            p.setDescripcion(descripcion);
            p.setPrecio(precio);
            p.setStock(stock);
            p.setCategoria(categoria);

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
                    || (p.getNombre() != null && p.getNombre().toLowerCase().contains(texto))
                    || (p.getDescripcion() != null && p.getDescripcion().toLowerCase().contains(texto))
                    || (p.getCategoria() != null && p.getCategoria().toLowerCase().contains(texto))) {
                modeloTabla.addRow(new Object[]{p.getId(), p.getNombre(), p.getDescripcion(), p.getPrecio(), p.getStock(), p.getCategoria()});
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
        txtDescripcion = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        txtPrecio = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        txtStock = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        txtCategoria = new javax.swing.JTextField();
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

        setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 18));
        jLabel1.setText("INVENTARIO");
        add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 10, -1, -1));

        jLabel2.setText("ID:");
        add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 50, -1, -1));
        add(txtId, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 50, 120, -1));

        jLabel3.setText("Nombre:");
        add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 90, -1, -1));
        add(txtNombre, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 90, 220, -1));

        jLabel4.setText("Descripcion:");
        add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 130, -1, -1));
        add(txtDescripcion, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 130, 300, -1));

        jLabel5.setText("Precio:");
        add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 170, -1, -1));
        add(txtPrecio, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 170, 120, -1));

        jLabel6.setText("Stock:");
        add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 170, -1, -1));
        add(txtStock, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 170, 80, -1));

        jLabel8 = new javax.swing.JLabel();
        jLabel8.setText("Categoria:");
        add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 50, -1, -1));
        add(txtCategoria, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 50, 140, -1));

        tablaProductos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {},
            new String [] {"ID","NOMBRE","DESCRIPCION","PRECIO","STOCK","CATEGORIA"}
        ));
        jScrollPane1.setViewportView(tablaProductos);
        add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 220, 820, 320));

        btnGuardar.setText("Guardar");
        btnGuardar.addActionListener(evt -> guardarProducto());
        add(btnGuardar, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 200, 100, -1));

        btnLimpiar.setText("Limpiar");
        btnLimpiar.addActionListener(evt -> limpiarCampos());
        add(btnLimpiar, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 200, 100, -1));

        btnEditar.setText("Editar");
        btnEditar.addActionListener(evt -> editarProducto());
        add(btnEditar, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 200, 100, -1));

        btnEliminar.setText("Eliminar");
        btnEliminar.addActionListener(evt -> eliminarProducto());
        add(btnEliminar, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 200, 100, -1));

        btnAumentar.setText("Aumentar");
        btnAumentar.addActionListener(evt -> ajustarStock(true));
        add(btnAumentar, new org.netbeans.lib.awtextra.AbsoluteConstraints(470, 200, 100, -1));

        btnDisminuir.setText("Disminuir");
        btnDisminuir.addActionListener(evt -> ajustarStock(false));
        add(btnDisminuir, new org.netbeans.lib.awtextra.AbsoluteConstraints(580, 200, 100, -1));

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
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tablaProductos;
    private javax.swing.JTextField txtBuscar;
    private javax.swing.JTextField txtCategoria;
    private javax.swing.JTextField txtDescripcion;
    private javax.swing.JTextField txtId;
    private javax.swing.JTextField txtNombre;
    private javax.swing.JTextField txtPrecio;
    private javax.swing.JTextField txtStock;
}
