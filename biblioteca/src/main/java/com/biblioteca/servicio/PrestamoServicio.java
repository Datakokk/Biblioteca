package com.biblioteca.servicio;

import java.sql.SQLException;
import java.util.List;

import com.biblioteca.dao.PrestamoDAO;
import com.biblioteca.modelo.Prestamo;

public class PrestamoServicio {
	private PrestamoDAO prestamoDAO;	
	
	public PrestamoServicio(PrestamoDAO prestamosDAO) {
		this.prestamoDAO = prestamosDAO;
	}
	
	public void registrarPrestamo(Prestamo prestamo) throws SQLException {
		// Validamos si el usuario tiene 5 prestamos activos
		List<Prestamo> prestamosActivos = prestamoDAO.obtenerPrestamosActivosPorUsuario(prestamo.getIdUsuario());
		if(prestamosActivos.size() > 5) {
			throw new SQLException("El usuario ha alcanzado el límite de 5 préstamos.");
		}
		
		prestamoDAO.registrarPrestamo(prestamo);
	}
	
	public void devolverLibro(int idPrestamo) throws SQLException {
		prestamoDAO.devolverLibro(idPrestamo);
	}
	
	public List<Prestamo> listarPrestamos() throws SQLException{
		return prestamoDAO.listarPrestamos();
	}
	
	public List<Prestamo> obtenerPrestamosActivosPorUsuario(int idUsuario) throws SQLException{
		return prestamoDAO.obtenerPrestamosActivosPorUsuario(idUsuario);
	}
}
