package com.biblioteca.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import com.biblioteca.modelo.Usuario;
import com.biblioteca.util.LoggerUtil;

public class UsuarioDAO {
    private Connection conexion;
    
    public UsuarioDAO(Connection conexion) {
        this.conexion = conexion;
    } 
    
    public void agregarUsuario(Usuario usuario) throws SQLException{
        String sql = "INSERT INTO usuarios (nombre, email, telefono, rol, password) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement ps = conexion.prepareStatement(sql)){
            ps.setString(1, usuario.getNombre());
            ps.setString(2, usuario.getEmail());
            ps.setString(3, usuario.getTelefono());
            ps.setString(4, usuario.getRol());
            ps.setString(5, usuario.getPassword()); // encriptamos la contraseña antes
            ps.executeUpdate();
            LoggerUtil.registrarEvento("Usuario agregado: " + usuario.getNombre() + " (" + usuario.getEmail() + ")");
        }
    }
    
    public Usuario obtenerUsuarioPorEmail(String email) throws SQLException {
        String sql = "SELECT * FROM usuarios WHERE email = ?";
        try(PreparedStatement ps = conexion.prepareStatement(sql)){
            ps.setString(1, email);
            
            try(ResultSet rs = ps.executeQuery()){
                if(rs.next()) {
                    Usuario usuario = new Usuario();
                    usuario.setIdUsuario(rs.getInt("id_usuario"));
                    usuario.setNombre(rs.getString("nombre"));
                    usuario.setEmail(rs.getString("email"));
                    usuario.setTelefono(rs.getString("telefono"));
                    usuario.setRol(rs.getString("rol"));
                    usuario.setPassword(rs.getString("password"));
                    LoggerUtil.registrarEvento("Usuario consultado: " + usuario.getEmail());
                    return usuario;
                }
            }
        }
        LoggerUtil.registrarEvento("Intento de consulta de usuario no encontrado: " + email);
        return null;
    }
    
    public List<Usuario> listarUsuarios() throws SQLException{
        List<Usuario> usuarios = new ArrayList<>();
        String sql = "SELECT * FROM usuarios";
        try(Statement st = conexion.createStatement();
            ResultSet rs = st.executeQuery(sql)){
            while(rs.next()) {
                Usuario usuario = new Usuario();
                usuario.setIdUsuario(rs.getInt("id_usuario"));
                usuario.setNombre(rs.getString("nombre"));
                usuario.setEmail(rs.getString("email"));
                usuario.setTelefono(rs.getString("telefono"));
                usuario.setRol(rs.getString("rol"));
                usuario.setPassword(rs.getString("password"));
                usuarios.add(usuario);
            }
        }
        LoggerUtil.registrarEvento("Listado de usuarios consultado. Total: " + usuarios.size());
        return usuarios;
    }
}
