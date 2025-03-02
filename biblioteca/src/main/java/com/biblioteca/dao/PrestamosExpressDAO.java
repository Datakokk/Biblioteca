package com.biblioteca.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.biblioteca.modelo.PrestamosExpress;
import com.biblioteca.util.LoggerUtil;

public class PrestamosExpressDAO {

    private Connection conexion;
    
    public PrestamosExpressDAO(Connection conexion) {
        super();
        this.conexion = conexion;
    }
    
    // Agregar un nuevo prestamo express
    public void agregarPrestamoExpress(PrestamosExpress express) throws SQLException {
        String sql = "INSERT INTO prestamos_express (id_express, id_prestamo, dias_prestamos) VALUES (?, ?, ?)";
        try(PreparedStatement ps = conexion.prepareStatement(sql)){
            ps.setInt(1, express.getIdExpress());
            ps.setInt(2, express.getIdPrestamo());
            ps.setInt(3, express.getDiasPrestamos());
            ps.executeUpdate();
            LoggerUtil.registrarEvento("Préstamo express agregado: ID " + express.getIdExpress() + " para préstamo ID " + express.getIdPrestamo());
        }
    }
    
    // Listamos todos los prestamos express
    public List<PrestamosExpress> listarPrestamosExpress() throws SQLException {
        List<PrestamosExpress> prestamosExpress = new ArrayList<>();
        String sql = "SELECT * FROM prestamos_express";
        try(Statement st = conexion.createStatement();
            ResultSet rs = st.executeQuery(sql)){
            while(rs.next()) {
                PrestamosExpress express = new PrestamosExpress();
                express.setIdExpress(rs.getInt("id_express"));
                express.setIdPrestamo(rs.getInt("id_prestamos"));
                express.setDiasPrestamos(rs.getInt("dias_prestamos"));
                prestamosExpress.add(express);
            }
        }
        LoggerUtil.registrarEvento("Listado de préstamos express consultado. Total: " + prestamosExpress.size());
        return prestamosExpress;
    }
    
}
