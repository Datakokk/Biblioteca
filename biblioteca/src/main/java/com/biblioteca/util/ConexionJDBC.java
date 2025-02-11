package com.biblioteca.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionJDBC {
	private static final String URL = "jdbc:mysql://db-biblioteca.cppn63j3jbw1.us-east-1.rds.amazonaws.com:3306/biblioteca";
	private static final String USUARIO = "admin";
    private static final String PASSWORD = "administrador";
    
    public static Connection obtenerConexion() throws SQLException {
    	return DriverManager.getConnection(URL, USUARIO, PASSWORD);
    }
}
