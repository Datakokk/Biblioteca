package com.biblioteca.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.biblioteca.modelo.Reporte;
import com.biblioteca.util.LoggerUtil;

public class ReporteDAO {
    private Connection conexion;
    
    public ReporteDAO(Connection conexion) {
        super();
        this.conexion = conexion;
    }

    // Agregar un nuevo reporte
    public void agregarReporte(Reporte reporte) throws SQLException {
        String sql = "INSERT INTO reportes (descripcion, fecha_generacion) VALUES (?, ?)";
        try(PreparedStatement ps = conexion.prepareStatement(sql)){
            ps.setString(1, reporte.getDescripcion());
            ps.setTimestamp(2, new Timestamp(reporte.getFechaGeneracion().getTime()));
            ps.executeUpdate();
            LoggerUtil.registrarEvento("Reporte agregado: " + reporte.getDescripcion());
        }
    }
    
    // Listar todos los reportes
    public List<Reporte> listarReportes() throws SQLException {
        List<Reporte> reportes = new ArrayList<>();
        String sql = "SELECT * FROM reportes";
        try(Statement st = conexion.createStatement();
            ResultSet rs = st.executeQuery(sql)){
            while(rs.next()) {
                Reporte reporte = new Reporte();
                reporte.setDescripcion(rs.getString("descripcion"));
                reporte.setFechaGeneracion(rs.getTimestamp("fecha_generacion"));
                reportes.add(reporte);
            }
        }
        LoggerUtil.registrarEvento("Listado de reportes consultado. Total: " + reportes.size());
        return reportes;
    }
    
    // Muestra los libros que aun no han sido devueltos
    public List<String> generarReportePrestamosActivos() throws SQLException {
        List<String> reportes = new ArrayList<>();
        String sql = "SELECT u.nombre AS usuario, l.titulo AS libro, p.fecha_prestamo " +
                     "FROM prestamos p " +
                     "JOIN usuarios u ON p.id_usuario = u.id_usuario " +
                     "JOIN libros l ON p.id_libro = l.id_libro " +
                     "WHERE p.fecha_devolucion IS NULL";
        
        try (Statement st = conexion.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                String reporte = "Usuario: " + rs.getString("usuario") +
                                 " | Libro: " + rs.getString("libro") +
                                 " | Fecha Préstamo: " + rs.getDate("fecha_prestamo");
                reportes.add(reporte);
            }
        }
        LoggerUtil.registrarEvento("Reporte de préstamos activos generado. Total: " + reportes.size());
        return reportes;
    }
    
    // Muestra los usuarios que han realizado más préstamos.
    public List<String> generarReporteUsuariosFrecuentes() throws SQLException {
        List<String> reportes = new ArrayList<>();
        String sql = "SELECT u.nombre, COUNT(p.id_prestamo) AS total_prestamos " +
                     "FROM prestamos p " +
                     "JOIN usuarios u ON p.id_usuario = u.id_usuario " +
                     "GROUP BY u.nombre " +
                     "ORDER BY total_prestamos DESC " +
                     "LIMIT 5"; // Solo los 5 usuarios más frecuentes
        
        try (Statement st = conexion.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                String reporte = "Usuario: " + rs.getString("nombre") +
                                 " | Total Préstamos: " + rs.getInt("total_prestamos");
                reportes.add(reporte);
            }
        }
        LoggerUtil.registrarEvento("Reporte de usuarios frecuentes generado. Total: " + reportes.size());
        return reportes;
    }
    
    // Muestra los libros que más han sido prestados
    public List<String> generarReporteLibrosMasPrestados() throws SQLException {
        List<String> reportes = new ArrayList<>();
        String sql = "SELECT l.titulo, COUNT(p.id_prestamo) AS total_prestamos " +
                     "FROM prestamos p " +
                     "JOIN libros l ON p.id_libro = l.id_libro " +
                     "GROUP BY l.titulo " +
                     "ORDER BY total_prestamos DESC " +
                     "LIMIT 5"; // Solo los 5 libros más populares
        
        try (Statement st = conexion.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                String reporte = "Libro: " + rs.getString("titulo") +
                                 " | Total Veces Prestado: " + rs.getInt("total_prestamos");
                reportes.add(reporte);
            }
        }
        LoggerUtil.registrarEvento("Reporte de libros más prestados generado. Total: " + reportes.size());
        return reportes;
    }
}
