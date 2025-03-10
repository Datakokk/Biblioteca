package com.biblioteca.dao;

import com.biblioteca.modelo.Prestamo;
import com.biblioteca.util.LoggerUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PrestamoDAO {
    private Connection conexion;

    public PrestamoDAO(Connection conexion) {
        this.conexion = conexion;
    }

    // 1. Registrar un préstamo
    public void registrarPrestamo(Prestamo prestamo) throws SQLException {
        String sql = "INSERT INTO prestamos (id_usuario, id_libro, fecha_prestamo) VALUES (?, ?, ?)";
        try (PreparedStatement ps = conexion.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, prestamo.getIdUsuario());
            ps.setInt(2, prestamo.getIdLibro());
            ps.setDate(3, new java.sql.Date(prestamo.getFechaPrestamo().getTime()));
            ps.executeUpdate();
            
            // Obtener el ID generado
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    prestamo.setIdPrestamo(rs.getInt(1));
                }
            }
            LoggerUtil.registrarEvento("Préstamo registrado: Libro ID " + prestamo.getIdLibro() + " a Usuario ID " + prestamo.getIdUsuario());
        }

        // Actualizar el estado del libro a "prestado"
        String actualizarLibro = "UPDATE libros SET estado = 'prestado' WHERE id_libro = ?";
        try (PreparedStatement ps = conexion.prepareStatement(actualizarLibro)) {
            ps.setInt(1, prestamo.getIdLibro());
            ps.executeUpdate();
        }
    }

    // 2. Registrar devolución de un libro
    public void devolverLibro(int idPrestamo) throws SQLException {
        // Verificar si el préstamo ya fue devuelto
        String verificarDevolucion = "SELECT fecha_devolucion, id_libro FROM prestamos WHERE id_prestamo = ?";
        int idLibro = -1;
        Date fechaDevolucion = null;
        
        try (PreparedStatement ps = conexion.prepareStatement(verificarDevolucion)) {
            ps.setInt(1, idPrestamo);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    fechaDevolucion = rs.getDate("fecha_devolucion");
                    idLibro = rs.getInt("id_libro");
                }
            }
        }

        if (fechaDevolucion != null) {
            throw new SQLException("Este préstamo ya ha sido devuelto anteriormente.");
        }

        // Actualizar la fecha de devolución
        String sql = "UPDATE prestamos SET fecha_devolucion = NOW() WHERE id_prestamo = ?";
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setInt(1, idPrestamo);
            ps.executeUpdate();
        }

        // Actualizar el estado del libro a 'disponible'
        String actualizarEstado = "UPDATE libros SET estado = 'disponible' WHERE id_libro = ?";
        try (PreparedStatement ps = conexion.prepareStatement(actualizarEstado)) {
            ps.setInt(1, idLibro);
            ps.executeUpdate();
        }
        LoggerUtil.registrarEvento("Libro devuelto: ID Prestamo " + idPrestamo + " - Libro ID " + idLibro);
    }
    
    // 3. Listar todos los préstamos
    public List<Prestamo> listarPrestamos() throws SQLException {
        List<Prestamo> prestamos = new ArrayList<>();
        String sql = "SELECT * FROM prestamos";
        try (Statement st = conexion.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                Prestamo prestamo = new Prestamo();
                prestamo.setIdPrestamo(rs.getInt("id_prestamo"));
                prestamo.setIdUsuario(rs.getInt("id_usuario"));
                prestamo.setIdLibro(rs.getInt("id_libro"));
                prestamo.setFechaPrestamo(rs.getDate("fecha_prestamo"));
                prestamo.setFechaDevolucion(rs.getDate("fecha_devolucion"));
                prestamos.add(prestamo);
            }
        }
        LoggerUtil.registrarEvento("Listado de préstamos consultado. Total: " + prestamos.size());
        return prestamos;
    }

    // 4. Obtener préstamos activos de un usuario
    public List<Prestamo> obtenerPrestamosActivosPorUsuario(int idUsuario) throws SQLException {
        List<Prestamo> prestamos = new ArrayList<>();
        String sql = "SELECT * FROM prestamos WHERE id_usuario = ? AND fecha_devolucion IS NULL";
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setInt(1, idUsuario);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Prestamo prestamo = new Prestamo();
                    prestamo.setIdPrestamo(rs.getInt("id_prestamo"));
                    prestamo.setIdUsuario(rs.getInt("id_usuario"));
                    prestamo.setIdLibro(rs.getInt("id_libro"));
                    prestamo.setFechaPrestamo(rs.getDate("fecha_prestamo"));
                    prestamo.setFechaDevolucion(rs.getDate("fecha_devolucion"));
                    prestamos.add(prestamo);
                }
            }
        }
        LoggerUtil.registrarEvento("Préstamos activos consultados para Usuario ID: " + idUsuario + ". Total: " + prestamos.size());
        return prestamos;
    }
}
