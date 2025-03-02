package com.biblioteca.servicio;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import at.favre.lib.crypto.bcrypt.BCrypt; // Nueva importación

import com.biblioteca.dao.UsuarioDAO;
import com.biblioteca.modelo.Usuario;

public class UsuarioServicio {

    private UsuarioDAO usuarioDAO;

    public UsuarioServicio(Connection conexion) {
        this.usuarioDAO = new UsuarioDAO(conexion);
    }

    public Usuario obtenerUsuarioPorEmail(String email) throws SQLException {
        return usuarioDAO.obtenerUsuarioPorEmail(email);
    }

    public void registrarUsuario(Usuario usuario) throws SQLException {
        if (usuarioDAO.obtenerUsuarioPorEmail(usuario.getEmail()) != null) {
            throw new SQLException("El email ya está registrado");
        }

        // 🔐 Generamos el hash de la contraseña usando bcrypt 0.9.0
        String hashedPassword = BCrypt.withDefaults().hashToString(12, usuario.getPassword().toCharArray());
        usuario.setPassword(hashedPassword);

        usuarioDAO.agregarUsuario(usuario);
    }

    public Usuario autenticarUsuario(String email, String password) throws SQLException {
        Usuario usuario = usuarioDAO.obtenerUsuarioPorEmail(email);

        if (usuario != null) {

            // 🔎 Verificar la contraseña con bcrypt 0.9.0
            BCrypt.Result result = BCrypt.verifyer().verify(password.toCharArray(), usuario.getPassword().toCharArray());
            boolean match = result.verified;

            if (match) {
                System.out.println("✅ Contraseña correcta");
                return usuario;
            } else {
                System.out.println("❌ Contraseña incorrecta");
            }
        } else {
            System.out.println("⚠️ Usuario no encontrado");
        }
        return null;
    }

    public List<Usuario> listarUsuarios() throws SQLException {
        return usuarioDAO.listarUsuarios();
    }
}
