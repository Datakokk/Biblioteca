package com.biblioteca.servicio;

import java.sql.Date;
import java.sql.SQLException;
import java.util.List;

import com.biblioteca.dao.ReservaDAO;
import com.biblioteca.modelo.Reserva;

public class ReservaServicio {
	private ReservaDAO reservaDAO;
	
	public ReservaServicio(ReservaDAO reservaDAO) {
		super();
		this.reservaDAO = reservaDAO;
	}

	public void registrarReserva(Reserva reserva) throws SQLException {
		//Validamos que el usuario no tenga reservas para el mismo libro.
		List<Reserva> reservas = reservaDAO.obtenerReservasPorUsuario(reserva.getIdUsuario());
		for(Reserva r : reservas) {
			if(r.getIdLibro() == reserva.getIdLibro() && r.getEstadoReserva().equals("pendiente")) {
				throw new SQLException("Ya existe una reserva pendiente para este libro.");
			}
		}
		
		//Registrams la reserva
		reserva.setFechaReserva(new Date(System.currentTimeMillis()));
		reserva.setEstadoReserva("pendiente");
		reservaDAO.registrarReserva(reserva);
	}
	
	public void actualizarEstadoReserva(int idReserva, String nuevoEstado) throws SQLException {
		reservaDAO.actualizarEstadoReserva(idReserva, nuevoEstado);
	}
	
	public List<Reserva> obtenerReservasPorUsuario(int idUsuario) throws SQLException{
		return reservaDAO.obtenerReservasPorUsuario(idUsuario);
	}
	
	public List<Reserva> listarReservas() throws SQLException {
		return reservaDAO.listarReservas();
	}
}
