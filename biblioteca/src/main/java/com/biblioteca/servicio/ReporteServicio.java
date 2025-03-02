package com.biblioteca.servicio;

import com.biblioteca.dao.ReporteDAO;
import com.biblioteca.modelo.Reporte;
import java.sql.SQLException;
import java.util.List;

public class ReporteServicio {
    private ReporteDAO reporteDAO;

    public ReporteServicio(ReporteDAO reporteDAO) {
        this.reporteDAO = reporteDAO;
    }

    public List<Reporte> listarReportes() throws SQLException {
        return reporteDAO.listarReportes();
    }

    public void agregarReporte(String descripcion) throws SQLException {
        Reporte reporte = new Reporte();
        reporte.setDescripcion(descripcion);
        reporteDAO.agregarReporte(reporte);
    }

    public List<String> generarReportePrestamosActivos() throws SQLException {
        return reporteDAO.generarReportePrestamosActivos();
    }

    public List<String> generarReporteUsuariosFrecuentes() throws SQLException {
        return reporteDAO.generarReporteUsuariosFrecuentes();
    }

    public List<String> generarReporteLibrosMasPrestados() throws SQLException {
        return reporteDAO.generarReporteLibrosMasPrestados();
    }
}