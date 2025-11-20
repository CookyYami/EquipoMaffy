package com.maffy.controllers;

import com.maffy.Dao.ReporteDAO;
import com.maffy.models.Pedido;

import java.time.LocalDate;
import java.util.List;

public class ReporteController {
    private ReporteDAO reporteDAO;
    
    public ReporteController() {
        this.reporteDAO = new ReporteDAO();
    }
    
    public List<Pedido> generarReporteDiario(String fecha) {
        return reporteDAO.obtenerReporteDiario(fecha);
    }
    
public List<Pedido> generarReporteSemanal(String fecha) {
    LocalDate fechaBase = LocalDate.parse(fecha);

    LocalDate lunes = fechaBase.minusDays(fechaBase.getDayOfWeek().getValue() - 1);
    LocalDate domingo = lunes.plusDays(6);

    return reporteDAO.obtenerReporteSemanal(lunes.toString(), domingo.toString());
}
    
    public List<Pedido> generarReporteMensual(int anio, int mes) {
        return reporteDAO.obtenerReporteMensual(anio, mes);
    }
    
    public double calcularTotalVentas(List<Pedido> pedidos) {
        return reporteDAO.obtenerTotalVentas(pedidos);
    }
    
    public int calcularCantidadPedidos(List<Pedido> pedidos) {
        return pedidos.size();
    }
    
    public void cerrar() {
        if (reporteDAO != null) {
            reporteDAO.cerrarConexion();
        }
    }
}