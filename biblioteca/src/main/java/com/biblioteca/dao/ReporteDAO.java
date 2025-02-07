package com.biblioteca.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.biblioteca.modelo.Reporte;

public class ReporteDAO {
	private Connection conexion;
	
	public ReporteDAO(Connection conexion) {
		super();
		this.conexion = conexion;
	}

	// Agregar un nuevo reporte
	public void agregarReporte(Reporte reporte) throws SQLException {
		String sql = "INSERT INTO reportes (descripcion, fecha_generacion) VALUES (?, ?)";
		try(PreparedStatement ps = conexion.prepareStatement(sql)){
			ps.setString(1, "descripcion");
			ps.setTimestamp(2, new Timestamp(reporte.getFechaGeneracion().getTime()));
			ps.executeUpdate();
		}
	}
	
	// Listar todos los reportes
	public List<Reporte> listarReportes() throws SQLException {
		List<Reporte> reportes = new ArrayList<>();
		String sql = "SELECT * FROM reservas";
		try(Statement st = conexion.createStatement();
			ResultSet rs = st.executeQuery(sql)){
			while(rs.next()) {
				Reporte reporte = new Reporte();
				reporte.setDescripcion(rs.getString("descripcion"));
				reporte.setFechaGeneracion(rs.getTimestamp("fecha_generacion"));
				reportes.add(reporte);
			}
		}
		return reportes;
	}
}
