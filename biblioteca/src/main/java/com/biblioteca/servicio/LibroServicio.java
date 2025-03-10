package com.biblioteca.servicio;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import com.biblioteca.dao.LibroDAO;
import com.biblioteca.modelo.Libro;

public class LibroServicio {
	
	private LibroDAO libroDAO;
	
	public LibroServicio(Connection conexion) {
		this.libroDAO = new LibroDAO(conexion);
	}
	
	public void agregarLibro(Libro libro) throws SQLException {
		libroDAO.agregarLibro(libro);
	}
	
	public void actualizarLibro(Libro libro) throws SQLException {
		libroDAO.actualizarLibro(libro);
	}
	
	public Libro obtenerLibroPorID(int idLibro) throws SQLException {
		return libroDAO.obtenerLibroPorID(idLibro);
	}
	
	public void eliminarLibro(int idLibro) throws SQLException {
		libroDAO.eliminarLibro(idLibro);
	}
	
	public List<Libro> listarLibros() throws SQLException{
		return libroDAO.listarLibros();
	}
	
	public List<Libro> buscarLibros(String filtro, String valor) throws SQLException{
		return libroDAO.buscarLibros(filtro, valor);
	}
}
