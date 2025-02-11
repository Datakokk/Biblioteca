package com.biblioteca.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import com.biblioteca.modelo.Reserva;

public class ReservaDAO {
    private Connection conexion;

    public ReservaDAO(Connection conexion) {
        this.conexion = conexion;
    }

    // 1. Registrar una reserva
    public void registrarReserva(Reserva reserva) throws SQLException {
        String sql = "INSERT INTO reservas (id_usuario, id_libro, fecha_reserva, estado_reserva) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = conexion.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, reserva.getIdUsuario());
            ps.setInt(2, reserva.getIdLibro());
            ps.setDate(3, new java.sql.Date(reserva.getFechaReserva().getTime()));
            ps.setString(4, reserva.getEstadoReserva());
            ps.executeUpdate();

            // Obtener el ID generado
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    reserva.setIdReserva(rs.getInt(1));
                }
            }
        }
    }

    // 2. Actualizar el estado de una reserva
    public void actualizarEstadoReserva(int idReserva, String nuevoEstado) throws SQLException {
        String sql = "UPDATE reservas SET estado_reserva = ? WHERE id_reserva = ?";
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setString(1, nuevoEstado);
            ps.setInt(2, idReserva);
            ps.executeUpdate();
        }
    }

    // 3. Obtener reservas por usuario
    public List<Reserva> obtenerReservasPorUsuario(int idUsuario) throws SQLException {
        List<Reserva> reservas = new ArrayList<>();
        String sql = "SELECT * FROM reservas WHERE id_usuario = ?";
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setInt(1, idUsuario);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Reserva reserva = new Reserva();
                    reserva.setIdReserva(rs.getInt("id_reserva"));
                    reserva.setIdUsuario(rs.getInt("id_usuario"));
                    reserva.setIdLibro(rs.getInt("id_libro"));
                    reserva.setFechaReserva(rs.getDate("fecha_reserva"));
                    reserva.setEstadoReserva(rs.getString("estado_reserva"));
                    reservas.add(reserva);
                }
            }
        }
        return reservas;
    }

    // 4. Listar todas las reservas
    public List<Reserva> listarReservas() throws SQLException {
        List<Reserva> reservas = new ArrayList<>();
        String sql = "SELECT * FROM reservas";
        try (Statement st = conexion.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                Reserva reserva = new Reserva();
                reserva.setIdReserva(rs.getInt("id_reserva"));
                reserva.setIdUsuario(rs.getInt("id_usuario"));
                reserva.setIdLibro(rs.getInt("id_libro"));
                reserva.setFechaReserva(rs.getDate("fecha_reserva"));
                reserva.setEstadoReserva(rs.getString("estado_reserva"));
                reservas.add(reserva);
            }
        }
        return reservas;
    }
}
