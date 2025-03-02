package com.biblioteca.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import com.biblioteca.modelo.Libro;
import com.biblioteca.util.LoggerUtil;

public class LibroDAO {
    private Connection conexion;
    
    public LibroDAO(Connection conexion) {
        super();
        this.conexion = conexion;
    }

    // Agregamos un nuevo Libro
    public void agregarLibro(Libro libro) throws SQLException {
        String sql = "INSERT INTO libros (titulo, autor, genero, estado) VALUES (?, ?, ?, ?)";
        
        try(PreparedStatement ps = conexion.prepareStatement(sql)){
            ps.setString(1, libro.getTitulo());
            ps.setString(2, libro.getAutor());
            ps.setString(3, libro.getGenero());
            ps.setString(4, libro.getEstado());
            ps.executeUpdate();
            LoggerUtil.registrarEvento("Libro agregado: " + libro.getTitulo() + " por " + libro.getAutor());
        }
    }
    
    // Actualizamos un libro que existe en nuestra biblioteca
    public void actualizarLibro(Libro libro) throws SQLException {
        String sql = "UPDATE libros SET titulo = ?, autor = ?, genero = ?, estado = ? WHERE id_libro = ?";
        
        try(PreparedStatement ps = conexion.prepareStatement(sql)){
            ps.setString(1, libro.getTitulo());
            ps.setString(2, libro.getAutor());
            ps.setString(3, libro.getGenero());
            ps.setString(4, libro.getEstado());
            ps.setInt(5, libro.getIdLibro());
            ps.executeUpdate();
            LoggerUtil.registrarEvento("Libro actualizado: " + libro.getTitulo());
        }
    }
    
    // Eliminar un libro (verificando que no esta prestado)
    public void eliminarLibro(int idLibro) throws SQLException {
        String verificarSql = "SELECT * FROM libros WHERE id_libro = ? AND estado = 'disponible'";
        
        try (PreparedStatement psVerificar = conexion.prepareStatement(verificarSql)) {
            psVerificar.setInt(1, idLibro);
            try (ResultSet rs = psVerificar.executeQuery()) {
                if (!rs.next()) {
                    throw new SQLException("No se puede eliminar el libro, verifique que no esté prestado.");
                }
            }
        }

        String eliminarSql = "DELETE FROM libros WHERE id_libro = ?";
        
        try (PreparedStatement psEliminar = conexion.prepareStatement(eliminarSql)) {
            psEliminar.setInt(1, idLibro);
            int filasAfectadas = psEliminar.executeUpdate();
            
            if (filasAfectadas == 0) {
                throw new SQLException("No se encontró el libro para eliminar.");
            }
            LoggerUtil.registrarEvento("Libro eliminado: ID " + idLibro);
        }
    }

    // Buscar libro por ID
    public Libro obtenerLibroPorID(int idLibro) throws SQLException {
        String sql = "SELECT * FROM libros WHERE id_libro = ?";
        
        try(PreparedStatement ps = conexion.prepareStatement(sql)){
            ps.setInt(1, idLibro);
            
            try(ResultSet rs = ps.executeQuery()){
                if(rs.next()) {
                    Libro libro = new Libro();
                    libro.setIdLibro(rs.getInt("id_libro"));
                    libro.setTitulo(rs.getString("titulo"));
                    libro.setAutor(rs.getString("autor"));
                    libro.setGenero(rs.getString("genero"));
                    libro.setEstado(rs.getString("estado"));
                    LoggerUtil.registrarEvento("Libro consultado: " + libro.getTitulo());
                    return libro;
                }
            }
        }
        return null;
    }

    // Listar todos los libros
    public List<Libro> listarLibros() throws SQLException {
        List<Libro> libros = new ArrayList<>();
        String sql = "SELECT * FROM libros";
        
        try(Statement st = conexion.createStatement();
            ResultSet rs = st.executeQuery(sql)){
            while(rs.next()) {
                Libro libro = new Libro();
                libro.setIdLibro(rs.getInt("id_libro"));
                libro.setTitulo(rs.getString("titulo"));
                libro.setAutor(rs.getString("autor"));
                libro.setGenero(rs.getString("genero"));
                libro.setEstado(rs.getString("estado"));
                libros.add(libro);
            }
        }
        LoggerUtil.registrarEvento("Listado de libros consultado. Total: " + libros.size());
        return libros;
    }

    // Buscar libro por titulo, autor o genero
    public List<Libro> buscarLibros(String filtro, String valor) throws SQLException {
        List<Libro> libros = new ArrayList<>();
        String sql = "SELECT * FROM libros WHERE " + filtro + " LIKE ?";
        
        try(PreparedStatement ps = conexion.prepareStatement(sql)){
            ps.setString(1, "%" + valor + "%");
            try(ResultSet rs = ps.executeQuery()){
                while(rs.next()) {
                    Libro libro = new Libro();
                    libro.setIdLibro(rs.getInt("id_libro"));
                    libro.setTitulo(rs.getString("titulo"));
                    libro.setAutor(rs.getString("autor"));
                    libro.setGenero(rs.getString("genero"));
                    libro.setEstado(rs.getString("estado"));
                    libros.add(libro);
                }
            }
        }
        LoggerUtil.registrarEvento("Búsqueda de libros realizada con filtro: " + filtro + ", valor: " + valor + ". Total encontrados: " + libros.size());
        return libros;
    }
}
