package com.biblioteca.servicio;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import com.biblioteca.dao.UsuarioDAO;
import com.biblioteca.modelo.Usuario;

public class UsuarioServicio {

	private UsuarioDAO usuarioDAO;
	
	public UsuarioServicio(Connection conexion) {
		this.usuarioDAO = new UsuarioDAO(conexion);
	}
	
	public void registrarUsuario(Usuario usuario) throws SQLException {
		if(usuarioDAO.obtenerUsuarioPorEmail(usuario.getEmail()) != null) {
			throw new SQLException("El email ya esta registrado");
		}
		usuarioDAO.agregarUsuario(usuario);
	}
	
	public Usuario obtenerUsuarioEmail(String email) throws SQLException {
		return usuarioDAO.obtenerUsuarioPorEmail(email);
	}
	
	public List<Usuario> listarUsuarios() throws SQLException{
		return usuarioDAO.listarUsuarios();
	}
}
