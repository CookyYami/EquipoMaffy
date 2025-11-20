package com.maffy.Vista;

import com.maffy.controllers.ReporteController;
import com.maffy.models.Pedido;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PanelReporte extends JPanel {
    private JComboBox<String> cmbTipoReporte;
    private JTextField txtFecha;
    private JComboBox<Integer> cmbAnio, cmbMes;
    private JButton btnGenerar, btnLimpiar;
    private JTable tablaReportes;
    private DefaultTableModel modeloTabla;
    private JLabel lblEstadisticas;
    private ReporteController reporteController;
    
    public PanelReporte() {
        this.reporteController = new ReporteController();
        initComponents();
    }
    
    private void initComponents() {
        setLayout(new BorderLayout());
        setBackground(new Color(178, 255, 255));
        
        JPanel panelFiltros = new JPanel(new GridLayout(3, 2, 10, 10));
        panelFiltros.setBackground(new Color(204, 255, 255));
        panelFiltros.setBorder(BorderFactory.createTitledBorder("Filtros de Reporte"));
        panelFiltros.setPreferredSize(new Dimension(940, 150));
        
        panelFiltros.add(new JLabel("Tipo de Reporte:"));
        cmbTipoReporte = new JComboBox<>(new String[]{"DIARIO", "SEMANAL", "MENSUAL"});
        cmbTipoReporte.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                actualizarCamposFecha();
            }
        });
        panelFiltros.add(cmbTipoReporte);
        
        panelFiltros.add(new JLabel("Fecha (yyyy-mm-dd):"));
        txtFecha = new JTextField(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
        panelFiltros.add(txtFecha);
        
        panelFiltros.add(new JLabel("Mes/Año:"));
        JPanel panelMesAnio = new JPanel(new FlowLayout());
        
        cmbMes = new JComboBox<>(new Integer[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12});
        cmbAnio = new JComboBox<>(new Integer[]{2024, 2025, 2026});
        
        Date fechaActual = new Date();
        cmbMes.setSelectedItem(Integer.parseInt(new SimpleDateFormat("MM").format(fechaActual)));
        cmbAnio.setSelectedItem(Integer.parseInt(new SimpleDateFormat("yyyy").format(fechaActual)));
        
        panelMesAnio.add(new JLabel("Mes:"));
        panelMesAnio.add(cmbMes);
        panelMesAnio.add(new JLabel("Año:"));
        panelMesAnio.add(cmbAnio);
        
        panelFiltros.add(panelMesAnio);
        
        add(panelFiltros, BorderLayout.NORTH);
        
        modeloTabla = new DefaultTableModel(
            new Object[]{"ID Pedido", "Fecha", "Total"}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        tablaReportes = new JTable(modeloTabla);
        JScrollPane scrollPane = new JScrollPane(tablaReportes);
        add(scrollPane, BorderLayout.CENTER);
        
        JPanel panelInferior = new JPanel(new BorderLayout());
        
        JPanel panelBotones = new JPanel(new FlowLayout());
        panelBotones.setBackground(new Color(204, 255, 255));
        
        btnGenerar = new JButton("Generar Reporte");
        btnLimpiar = new JButton("Limpiar");
        
        btnGenerar.setBackground(new Color(204, 255, 255));
        btnGenerar.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnLimpiar.setBackground(new Color(204, 255, 255));
        btnLimpiar.setFont(new Font("Segoe UI", Font.BOLD, 12));
        
        btnGenerar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                generarReporte();
            }
        });
        
        btnLimpiar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                limpiarFiltros();
            }
        });
        
        panelBotones.add(btnGenerar);
        panelBotones.add(btnLimpiar);
        panelInferior.add(panelBotones, BorderLayout.NORTH);
        
        JPanel panelStats = new JPanel(new FlowLayout());
        panelStats.setBackground(new Color(225, 255, 255));
        panelStats.setBorder(BorderFactory.createTitledBorder("Estadísticas"));
        
        lblEstadisticas = new JLabel("Seleccione un tipo de reporte y haga clic en Generar");
        panelStats.add(lblEstadisticas);
        
        panelInferior.add(panelStats, BorderLayout.SOUTH);
        add(panelInferior, BorderLayout.SOUTH);
        
        actualizarCamposFecha();
    }
    
    private void actualizarCamposFecha() {
        String tipoReporte = (String) cmbTipoReporte.getSelectedItem();
        boolean esMensual = "MENSUAL".equals(tipoReporte);
        
        Component[] componentes = ((JPanel)getComponent(0)).getComponents();
        componentes[2].setVisible(!esMensual);
        componentes[3].setVisible(!esMensual);
        componentes[4].setVisible(esMensual);
        componentes[5].setVisible(esMensual);
        
        revalidate();
        repaint();
    }
    
    private void generarReporte() {
        String tipoReporte = (String) cmbTipoReporte.getSelectedItem();
        
        modeloTabla.setRowCount(0);
        
        try {
            List<Pedido> pedidos;
            switch (tipoReporte) {
                case "DIARIO":
                    pedidos = reporteController.generarReporteDiario(txtFecha.getText());
                    break;
                case "SEMANAL":
                    pedidos = reporteController.generarReporteSemanal(txtFecha.getText());
                    break;
                case "MENSUAL":
                    int anio = (Integer) cmbAnio.getSelectedItem();
                    int mes = (Integer) cmbMes.getSelectedItem();
                    pedidos = reporteController.generarReporteMensual(anio, mes);
                    break;
                default:
                    pedidos = List.of();
            }
            
            List<Pedido> pedidosCompletados = new ArrayList<>();
            for (Pedido pedido : pedidos) {
                if ("COMPLETADO".equals(pedido.getEstado())) {
                    pedidosCompletados.add(pedido);
                }
            }
            
            for (Pedido pedido : pedidosCompletados) {
                modeloTabla.addRow(new Object[]{
                    pedido.getId(),
                    pedido.getFecha().toString(),
                    String.format("%.2f", pedido.getTotal().doubleValue())
                });
            }
            
            actualizarEstadisticas(pedidosCompletados, tipoReporte);
            
            JOptionPane.showMessageDialog(this, 
                "Reporte " + tipoReporte + " generado\nPedidos COMPLETADOS: " + pedidosCompletados.size(),
                "Reporte Generado", 
                JOptionPane.INFORMATION_MESSAGE);
                
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "Error al generar reporte: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
    
    private void actualizarEstadisticas(List<Pedido> pedidos, String tipoReporte) {
        if (pedidos.isEmpty()) {
            lblEstadisticas.setText("No hay pedidos COMPLETADOS para " + tipoReporte);
            return;
        }
        
        double totalVentas = reporteController.calcularTotalVentas(pedidos);
        int cantidadPedidos = reporteController.calcularCantidadPedidos(pedidos);
        
        String stats = String.format(
            "%s | Pedidos COMPLETADOS: %d | Total: %.2f",
            tipoReporte, cantidadPedidos, totalVentas
        );
        
        lblEstadisticas.setText(stats);
    }
    
    private void limpiarFiltros() {
        cmbTipoReporte.setSelectedIndex(0);
        txtFecha.setText(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
        
        Date fechaActual = new Date();
        cmbMes.setSelectedItem(Integer.parseInt(new SimpleDateFormat("MM").format(fechaActual)));
        cmbAnio.setSelectedItem(Integer.parseInt(new SimpleDateFormat("yyyy").format(fechaActual)));
        
        modeloTabla.setRowCount(0);
        lblEstadisticas.setText("Seleccione un tipo de reporte y haga clic en Generar");
        actualizarCamposFecha();
    }
}