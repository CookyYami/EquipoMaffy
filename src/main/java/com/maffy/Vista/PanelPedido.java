/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package com.maffy.Vista;

import com.maffy.Dao.PedidoDAO;
import com.maffy.Dao.ItemPedidoDAO;
import com.maffy.Dao.ClienteDAO;
import com.maffy.Dao.ProductoDAO;
import com.maffy.models.Pedido;
import com.maffy.models.ItemPedido;
import com.maffy.models.Producto;
import com.maffy.models.Cliente;
import com.maffy.utils.Logger;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import javax.swing.table.DefaultTableModel;
import javax.swing.JOptionPane;

/**
 *
 * @author HP
 */
public class PanelPedido extends javax.swing.JPanel {

    private java.util.Map<String, Integer> productoMap = new java.util.HashMap<>();

    /**
     * Creates new form FrmPedido
     */
    public PanelPedido() {
        initComponents();
        Logger.clearLog();
        Logger.info("=== INICIO DE PANELPEDIDO ===");
        Logger.debug("Testando obtenerTodosPedidos() directamente...");
        try {
            PedidoDAO testDao = new PedidoDAO();
            java.util.List<Pedido> testPedidos = testDao.obtenerTodosPedidos();
            Logger.info("obtenerTodosPedidos() devolvió: " + testPedidos.size() + " pedidos");
            for (Pedido p : testPedidos) {
                Logger.debug("  - Pedido id=" + p.getId() + ", fecha=" + p.getFecha() + ", estado=" + p.getEstado());
            }
        } catch (Exception ex) {
            Logger.exception("Excepción en test", ex);
            ex.printStackTrace();
        }
        Logger.debug("Llamando cargarCombos()...");
        // Cargar datos en combos y asignar acciones
        cargarCombos();
        Logger.debug("Llamando cargarTablaPedidos()...");
        cargarTablaPedidos();
        Logger.debug("Asignando listeners...");
        jButton1.addActionListener(evt -> guardarPedido());
        jComboBox2.addActionListener(evt -> mostrarProductoSeleccionado());
        // listeners para Limpiar, Editar y Eliminar
        jButton2.addActionListener(evt -> limpiarCampos());
        jButton3.addActionListener(evt -> editarPedido());
        jButton4.addActionListener(evt -> eliminarPedido());

        // selección en la tabla para cargar pedido en campos
        jTable1.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                cargarPedidoSeleccionado();
            }
        });
        jTable1.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (evt.getClickCount() == 1) {
                    cargarPedidoSeleccionado();
                }
            }
        });
        Logger.info("=== FIN INICIALIZACION PANELPEDIDO ===");
    }

  
    private void initComponents() {

        panelPedido = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jComboBox1 = new javax.swing.JComboBox<>();
        jComboBox2 = new javax.swing.JComboBox<>();
        jLabel5 = new javax.swing.JLabel();
        jTextField2 = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jTextField3 = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jLabel7 = new javax.swing.JLabel();
        jComboBox3 = new javax.swing.JComboBox<>();
        jLabel8 = new javax.swing.JLabel();
        jTextField4 = new javax.swing.JTextField();

        setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        panelPedido.setBackground(new java.awt.Color(189, 236, 182));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel1.setText("REGISTRAR PEDIDO");

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel2.setText("Código:");

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel3.setText("Seleccionar Cliente:");

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel4.setText("Seleccionar Producto:");

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jComboBox2.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel5.setText("Cantidad:");

        jLabel6.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel6.setText("Fecha:");

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "CODIGO", "CLIENTE", "PRODUCTO", "CANTIDAD", "FECHA", "ESTADO", "TOTAL"
            }
        ));
        jScrollPane1.setViewportView(jTable1);

        jButton1.setBackground(new java.awt.Color(102, 204, 255));
        jButton1.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jButton1.setText("Guardar");

        jButton2.setBackground(new java.awt.Color(255, 255, 153));
        jButton2.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jButton2.setText("Limpiar");

        jButton3.setBackground(new java.awt.Color(153, 255, 255));
        jButton3.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jButton3.setText("Editar");

        jButton4.setBackground(new java.awt.Color(255, 153, 153));
        jButton4.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jButton4.setText("Eliminar");

        jLabel7.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel7.setText("Estado:");

        jComboBox3.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jLabel8.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel8.setText("Buscar:");

        javax.swing.GroupLayout panelPedidoLayout = new javax.swing.GroupLayout(panelPedido);
        panelPedido.setLayout(panelPedidoLayout);
        panelPedidoLayout.setHorizontalGroup(
            panelPedidoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelPedidoLayout.createSequentialGroup()
                .addGroup(panelPedidoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelPedidoLayout.createSequentialGroup()
                        .addGap(104, 104, 104)
                        .addGroup(panelPedidoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panelPedidoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addGroup(panelPedidoLayout.createSequentialGroup()
                                    .addGroup(panelPedidoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(panelPedidoLayout.createSequentialGroup()
                                            .addComponent(jLabel3)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                            .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGroup(panelPedidoLayout.createSequentialGroup()
                                            .addComponent(jLabel2)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                            .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                    .addGap(89, 89, 89))
                                .addGroup(panelPedidoLayout.createSequentialGroup()
                                    .addGap(77, 77, 77)
                                    .addComponent(jButton1)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jButton2)
                                    .addGap(59, 59, 59)))
                            .addGroup(panelPedidoLayout.createSequentialGroup()
                                .addGroup(panelPedidoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(panelPedidoLayout.createSequentialGroup()
                                        .addGap(246, 246, 246)
                                        .addGroup(panelPedidoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(panelPedidoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                                .addComponent(jLabel1)
                                                .addGroup(panelPedidoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                    .addComponent(jLabel6)
                                                    .addComponent(jLabel5)
                                                    .addComponent(jLabel7)))
                                            .addGroup(panelPedidoLayout.createSequentialGroup()
                                                .addGap(105, 105, 105)
                                                .addComponent(jButton3))))
                                    .addGroup(panelPedidoLayout.createSequentialGroup()
                                        .addComponent(jLabel4)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(4, 4, 4)))
                        .addGroup(panelPedidoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panelPedidoLayout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addGroup(panelPedidoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, 164, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jComboBox3, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(panelPedidoLayout.createSequentialGroup()
                                .addGap(44, 44, 44)
                                .addComponent(jButton4))))
                    .addGroup(panelPedidoLayout.createSequentialGroup()
                        .addGap(46, 46, 46)
                        .addGroup(panelPedidoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 855, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(panelPedidoLayout.createSequentialGroup()
                                .addComponent(jLabel8)
                                .addGap(18, 18, 18)
                                .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, 249, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap(39, Short.MAX_VALUE))
        );
        panelPedidoLayout.setVerticalGroup(
            panelPedidoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelPedidoLayout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addComponent(jLabel1)
                .addGroup(panelPedidoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelPedidoLayout.createSequentialGroup()
                        .addGap(27, 27, 27)
                        .addGroup(panelPedidoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel2)
                            .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel5)
                            .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(panelPedidoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel3)
                            .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel6)
                            .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(panelPedidoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel7)
                            .addComponent(jLabel4)
                            .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jComboBox3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(65, 65, 65)
                        .addGroup(panelPedidoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel8))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 265, Short.MAX_VALUE)
                        .addGap(17, 17, 17))
                    .addGroup(panelPedidoLayout.createSequentialGroup()
                        .addGap(158, 158, 158)
                        .addGroup(panelPedidoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jButton1)
                            .addComponent(jButton2)
                            .addComponent(jButton3)
                            .addComponent(jButton4))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );

        add(panelPedido, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 940, 550));
    }// </editor-fold>//GEN-END:initComponents

    private void cargarCombos() {
        try {
            ClienteDAO cDao = new ClienteDAO();
            ProductoDAO pDao = new ProductoDAO();

            jComboBox1.removeAllItems();
            List<Cliente> clientes = cDao.obtenerTodosClientes();
            for (Cliente c : clientes) {
                jComboBox1.addItem(c.getId() + " - " + c.getNombre());
            }

            jComboBox2.removeAllItems();
            List<Producto> productos = pDao.obtenerTodosProductos();
            productoMap.clear();
            for (Producto p : productos) {
                String key = p.getId() + " - " + p.getNombre();
                productoMap.put(key, p.getId());
                jComboBox2.addItem(key);
            }

            // Estados posibles
            jComboBox3.removeAllItems();
            jComboBox3.addItem("PENDIENTE");
            jComboBox3.addItem("COMPLETADO");
            jComboBox3.addItem("CANCELADO");
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al cargar combos: " + ex.getMessage());
        }
    }

    private void guardarPedido() {
        try {
            // leer cliente
            String clienteSel = (String) jComboBox1.getSelectedItem();
            if (clienteSel == null) { JOptionPane.showMessageDialog(this, "Seleccione un cliente"); return; }
            int clienteId = Integer.parseInt(clienteSel.split(" - ")[0].trim());

            // leer producto
            String productoSel = (String) jComboBox2.getSelectedItem();
            if (productoSel == null) { JOptionPane.showMessageDialog(this, "Seleccione un producto"); return; }
            Integer productoIdObj = productoMap.get(productoSel);
            int productoId;
            if (productoIdObj != null) {
                productoId = productoIdObj;
            } else {
                // fallback por compatibilidad
                productoId = Integer.parseInt(productoSel.split(" - ")[0].trim());
            }

            int cantidad = Integer.parseInt(jTextField3.getText().trim());
            if (cantidad <= 0) { JOptionPane.showMessageDialog(this, "Cantidad inválida"); return; }

            ProductoDAO pDao = new ProductoDAO();
            Producto prod = pDao.obtenerProductoPorId(productoId);
            if (prod == null) { JOptionPane.showMessageDialog(this, "Producto no encontrado"); return; }

            BigDecimal subtotal = prod.getPrecio().multiply(new BigDecimal(cantidad));
            BigDecimal total = subtotal; // si quieres sumar múltiples items, sumar aquí

            // usuario fijo (cambiar si tienes sesión)
            int usuarioId = 1;

            Pedido pedido = new Pedido(clienteId, usuarioId, total);
            PedidoDAO pedidoDao = new PedidoDAO();
            int pedidoId = pedidoDao.insertarPedido(pedido);
            if (pedidoId <= 0) {
                JOptionPane.showMessageDialog(this, "Error al crear pedido");
                return;
            }

            ItemPedido item = new ItemPedido(pedidoId, productoId, cantidad, subtotal);
            ItemPedidoDAO itemDao = new ItemPedidoDAO();
            boolean okItem = itemDao.insertarItem(item);
            if (!okItem) {
                // intentar confirmar si el item realmente está en la BD
                try {
                    java.util.List<ItemPedido> itemsConfirm = itemDao.obtenerItemsPorPedido(pedidoId);
                    if (itemsConfirm != null && !itemsConfirm.isEmpty()) {
                        System.out.println("[WARN] insertarItem devolvió false, pero hay items en BD para pedido=" + pedidoId + ". Tratando como éxito.");
                        okItem = true;
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
            if (!okItem) {
                JOptionPane.showMessageDialog(this, "Pedido creado, pero error al insertar item");
            } else {
                JOptionPane.showMessageDialog(this, "Pedido creado correctamente (id=" + pedidoId + ")");
                // limpiar campos
                jTextField1.setText("");
                jTextField2.setText("");
                jTextField3.setText("");
                // recargar tabla
                cargarTablaPedidos();
            }

        } catch (NumberFormatException nfe) {
            JOptionPane.showMessageDialog(this, "Verifique los campos numéricos: " + nfe.getMessage());
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al guardar pedido: " + ex.getMessage());
        }
    }

    private void mostrarProductoSeleccionado() {
        try {
            String sel = (String) jComboBox2.getSelectedItem();
            if (sel == null) return;
            Integer id = productoMap.get(sel);
            if (id != null) {
                ProductoDAO pDao = new ProductoDAO();
                Producto p = pDao.obtenerProductoPorId(id);
                if (p != null) {
                    String msg = "Producto: " + p.getNombre() + "\nPrecio: " + p.getPrecio() + "\nStock: " + p.getStock();
                    // mostramos en un tooltip ligero (no molesto)
                    jComboBox2.setToolTipText(msg);
                }
            }
        } catch (Exception e) {
            // no bloquear UI por esto
            e.printStackTrace();
        }
    }

    private void cargarTablaPedidos() {
        Logger.debug("cargarTablaPedidos() iniciando...");
        try {
            PedidoDAO pedidoDao = new PedidoDAO();

            List<Pedido> pedidos = pedidoDao.obtenerTodosPedidos();
            Logger.debug("Se obtuvieron " + pedidos.size() + " pedidos de la base de datos");

            // Cambiar la estructura de la tabla para mostrar las mismas columnas que la tabla Pedido en Supabase
            String[] cols = new String[]{"id_pedido", "fecha", "estado", "id_cliente", "id_usuario", "total"};
            DefaultTableModel model = new DefaultTableModel(cols, 0) {
                @Override
                public boolean isCellEditable(int row, int column) { return false; }
            };

            for (Pedido p : pedidos) {
                Logger.debug("Añadiendo pedido: id=" + p.getId() + ", fecha=" + p.getFecha() + ", estado=" + p.getEstado() + ", total=" + p.getTotal());
                Object[] row = new Object[]{p.getId(), p.getFecha() != null ? p.getFecha().toString() : null, p.getEstado(), p.getClienteId(), p.getUsuarioId(), p.getTotal()};
                model.addRow(row);
            }
            jTable1.setModel(model);
            Logger.info("Tabla de pedidos cargada con " + pedidos.size() + " filas");

        } catch (Exception ex) {
            Logger.exception("Excepción en cargarTablaPedidos", ex);
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al cargar tabla de pedidos: " + ex.getMessage());
        }
    }

    private void limpiarCampos() {
        jTextField1.setText("");
        jTextField2.setText("");
        jTextField3.setText("");
        jTextField4.setText("");
        jTable1.clearSelection();
    }

    private void cargarPedidoSeleccionado() {
        try {
            int fila = jTable1.getSelectedRow();
            if (fila < 0) return;
            Object idObj = jTable1.getValueAt(fila, 0);
            if (idObj == null) return;
            int id = Integer.parseInt(idObj.toString());
            jTextField1.setText(String.valueOf(id));
            Object fechaObj = jTable1.getValueAt(fila, 1);
            jTextField2.setText(fechaObj != null ? fechaObj.toString() : "");
            Object estadoObj = jTable1.getValueAt(fila, 2);
            if (estadoObj != null) jComboBox3.setSelectedItem(estadoObj.toString());
            Object clienteObj = jTable1.getValueAt(fila, 3);
            if (clienteObj != null) {
                String clienteIdStr = clienteObj.toString();
                // buscar en combo cliente
                for (int i = 0; i < jComboBox1.getItemCount(); i++) {
                    String item = jComboBox1.getItemAt(i);
                    if (item != null && item.startsWith(clienteIdStr + " -")) {
                        jComboBox1.setSelectedIndex(i);
                        break;
                    }
                }
            }

            // cargar items para mostrar producto y cantidad si existen
            try {
                ItemPedidoDAO itDao = new ItemPedidoDAO();
                java.util.List<ItemPedido> items = itDao.obtenerItemsPorPedido(id);
                if (items != null && !items.isEmpty()) {
                    ItemPedido first = items.get(0);
                    // buscar producto key por id
                    for (Map.Entry<String, Integer> e : productoMap.entrySet()) {
                        if (e.getValue().equals(first.getProductoId())) {
                            jComboBox2.setSelectedItem(e.getKey());
                            break;
                        }
                    }
                    jTextField3.setText(String.valueOf(first.getCantidad()));
                } else {
                    jComboBox2.setSelectedIndex(jComboBox2.getItemCount() > 0 ? 0 : -1);
                    jTextField3.setText("");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void editarPedido() {
        if (jTextField1.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Selecciona un pedido de la tabla para editar");
            return;
        }

        try {
            int id = Integer.parseInt(jTextField1.getText());

            // leer cliente
            String clienteSel = (String) jComboBox1.getSelectedItem();
            if (clienteSel == null) { JOptionPane.showMessageDialog(this, "Seleccione un cliente"); return; }
            int clienteId = Integer.parseInt(clienteSel.split(" - ")[0].trim());

            // estado
            String estado = (String) jComboBox3.getSelectedItem();

            // fecha
            LocalDate fecha = null;
            String fechaTxt = jTextField2.getText().trim();
            if (!fechaTxt.isEmpty()) {
                try {
                    fecha = LocalDate.parse(fechaTxt);
                } catch (DateTimeParseException dtpe) {
                    fecha = LocalDate.now();
                }
            } else {
                fecha = LocalDate.now();
            }

            // obtener usuarioId existente si es posible
            PedidoDAO pedidoDao = new PedidoDAO();
            Pedido existing = pedidoDao.obtenerPedidoPorId(id);
            int usuarioId = existing != null ? existing.getUsuarioId() : 1;

            // calcular total si hay producto y cantidad
            BigDecimal total = existing != null ? existing.getTotal() : BigDecimal.ZERO;
            String productoSel = (String) jComboBox2.getSelectedItem();
            if (productoSel != null && !jTextField3.getText().trim().isEmpty()) {
                Integer productoIdObj = productoMap.get(productoSel);
                int productoId = productoIdObj != null ? productoIdObj : Integer.parseInt(productoSel.split(" - ")[0].trim());
                int cantidad = Integer.parseInt(jTextField3.getText().trim());
                ProductoDAO pDao = new ProductoDAO();
                Producto prod = pDao.obtenerProductoPorId(productoId);
                if (prod != null) {
                    total = prod.getPrecio().multiply(new BigDecimal(cantidad));
                }
            }

            Pedido p = new Pedido();
            p.setId(id);
            p.setClienteId(clienteId);
            p.setUsuarioId(usuarioId);
            p.setEstado(estado != null ? estado : "PENDIENTE");
            p.setFecha(fecha);
            p.setTotal(total);

            boolean ok = pedidoDao.actualizarPedido(p);
            if (!ok) {
                JOptionPane.showMessageDialog(this, "Error al actualizar el pedido");
                return;
            }

            // si se ha seleccionado producto/cantidad, reemplazar items
            try {
                ItemPedidoDAO itDao = new ItemPedidoDAO();
                itDao.eliminarItemsPorPedido(id);
                if (productoSel != null && !jTextField3.getText().trim().isEmpty()) {
                    Integer productoIdObj = productoMap.get(productoSel);
                    int productoId = productoIdObj != null ? productoIdObj : Integer.parseInt(productoSel.split(" - ")[0].trim());
                    int cantidad = Integer.parseInt(jTextField3.getText().trim());
                    ProductoDAO pDao = new ProductoDAO();
                    Producto prod = pDao.obtenerProductoPorId(productoId);
                    BigDecimal subtotal = prod != null ? prod.getPrecio().multiply(new BigDecimal(cantidad)) : BigDecimal.ZERO;
                    ItemPedido it = new ItemPedido(id, productoId, cantidad, subtotal);
                    itDao.insertarItem(it);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            JOptionPane.showMessageDialog(this, "Pedido actualizado correctamente");
            cargarTablaPedidos();
            limpiarCampos();

        } catch (NumberFormatException nfe) {
            JOptionPane.showMessageDialog(this, "Verifique los campos numéricos: " + nfe.getMessage());
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al editar pedido: " + ex.getMessage());
        }
    }

    private void eliminarPedido() {
        if (jTextField1.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Selecciona un pedido de la tabla para eliminar");
            return;
        }

        try {
            int id = Integer.parseInt(jTextField1.getText());
            int confirmacion = JOptionPane.showConfirmDialog(this,
                    "¿Estás seguro de eliminar el pedido id: " + id + "?",
                    "Confirmar Eliminación",
                    JOptionPane.YES_NO_OPTION);

            if (confirmacion == JOptionPane.YES_OPTION) {
                ItemPedidoDAO itDao = new ItemPedidoDAO();
                itDao.eliminarItemsPorPedido(id);
                PedidoDAO pedidoDao = new PedidoDAO();
                if (pedidoDao.eliminarPedido(id)) {
                    JOptionPane.showMessageDialog(this, "Pedido eliminado exitosamente");
                    cargarTablaPedidos();
                    limpiarCampos();
                } else {
                    JOptionPane.showMessageDialog(this, "Error al eliminar el pedido");
                }
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Código de pedido inválido");
        }
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JComboBox<String> jComboBox2;
    private javax.swing.JComboBox<String> jComboBox3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JTextField jTextField4;
    private javax.swing.JPanel panelPedido;
    // End of variables declaration//GEN-END:variables
}
