package com.biblioteca.main;

import com.biblioteca.dao.ReporteDAO;
import com.biblioteca.modelo.*;
import com.biblioteca.servicio.*;
import com.biblioteca.util.ConexionJDBC;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        System.out.println("📚 Sistema de Biblioteca Iniciado ✅");

        try (Connection conexion = ConexionJDBC.obtenerConexion();
             Scanner scanner = new Scanner(System.in)) {

            UsuarioServicio usuarioServicio = new UsuarioServicio(conexion);
            LibroServicio libroServicio = new LibroServicio(conexion);
            PrestamoServicio prestamoServicio = new PrestamoServicio(new com.biblioteca.dao.PrestamoDAO(conexion));
            ReservaServicio reservaServicio = new ReservaServicio(new com.biblioteca.dao.ReservaDAO(conexion));
            ReporteDAO reporteDAO = new ReporteDAO(conexion);

            // 1️⃣ INICIO DE SESIÓN
            System.out.print("Ingrese su email: ");
            String email = scanner.nextLine();
            System.out.print("Ingrese su contraseña: ");
            String password = scanner.nextLine();

            Usuario usuario = usuarioServicio.autenticarUsuario(email, password);

            if (usuario == null) {
                System.out.println("❌ Credenciales incorrectas. Saliendo...");
                return;
            }

            String rolUsuario = usuario.getRol(); // Obtenemos el rol del usuario
            System.out.println("✅ Bienvenido, " + usuario.getNombre() + " | Rol: " + rolUsuario);

            // 2️⃣ MENÚ SEGÚN EL ROL
            while (true) {
                if (rolUsuario.equalsIgnoreCase("administrador")) {
                    System.out.println("\n🔹 Menú Administrador:");
                    System.out.println("1. Agregar usuario");
                    System.out.println("2. Agregar libro");
                    System.out.println("3. Registrar préstamo");
                    System.out.println("4. Registrar devolución");
                    System.out.println("5. Listar libros");
                    System.out.println("6. Actualizar libro");
                    System.out.println("7. Eliminar libro");
                    System.out.println("8. Reporte de Préstamos Activos");
                    System.out.println("9. Reporte de Usuarios con Más Préstamos");
                    System.out.println("10. Reporte de Libros Más Prestados");
                    System.out.println("11. Salir");
                } else {
                    System.out.println("\n🔹 Menú Usuario:");
                    System.out.println("1. Listar libros disponibles");
                    System.out.println("2. Registrar préstamo");
                    System.out.println("3. Registrar devolución");
                    System.out.println("4. Ver mis préstamos activos");
                    System.out.println("5. Salir");
                }

                System.out.print("Seleccione una opción: ");
                int opcion = scanner.nextInt();
                scanner.nextLine(); // Consumir la nueva línea

                if (rolUsuario.equalsIgnoreCase("administrador")) {
                    switch (opcion) {
                        case 1:
                            System.out.print("Ingrese nombre: ");
                            String nombre = scanner.nextLine();
                            System.out.print("Ingrese email: ");
                            String nuevoEmail = scanner.nextLine();
                            System.out.print("Ingrese teléfono: ");
                            String telefono = scanner.nextLine();
                            System.out.print("Ingrese rol (administrador/usuario): ");
                            String nuevoRol = scanner.nextLine();
                            System.out.print("Ingrese contraseña: ");
                            String nuevaPassword = scanner.nextLine();

                            Usuario nuevoUsuario = new Usuario();
                            nuevoUsuario.setNombre(nombre);
                            nuevoUsuario.setEmail(nuevoEmail);
                            nuevoUsuario.setTelefono(telefono);
                            nuevoUsuario.setRol(nuevoRol);
                            nuevoUsuario.setPassword(nuevaPassword);

                            usuarioServicio.registrarUsuario(nuevoUsuario);
                            System.out.println("✅ Usuario registrado con éxito!");
                            break;

                        case 2:
                            System.out.print("Ingrese título: ");
                            String titulo = scanner.nextLine();
                            System.out.print("Ingrese autor: ");
                            String autor = scanner.nextLine();
                            System.out.print("Ingrese género: ");
                            String genero = scanner.nextLine();
                            System.out.print("Ingrese estado (disponible/prestado): ");
                            String estado = scanner.nextLine();

                            Libro libro = new Libro();
                            libro.setTitulo(titulo);
                            libro.setAutor(autor);
                            libro.setGenero(genero);
                            libro.setEstado(estado);

                            libroServicio.agregarLibro(libro);
                            System.out.println("✅ Libro agregado con éxito!");
                            break;

                        case 3:
                            System.out.print("Ingrese ID del usuario: ");
                            int idUsuario = scanner.nextInt();
                            System.out.print("Ingrese ID del libro: ");
                            int idLibro = scanner.nextInt();
                            scanner.nextLine();

                            Prestamo prestamo = new Prestamo();
                            prestamo.setIdUsuario(idUsuario);
                            prestamo.setIdLibro(idLibro);
                            prestamo.setFechaPrestamo(new java.util.Date());

                            prestamoServicio.registrarPrestamo(prestamo);
                            System.out.println("✅ Préstamo registrado con éxito!");
                            break;

                        case 8:
                            System.out.println("\n📌 Reporte de Préstamos Activos:");
                            List<String> prestamosActivos = reporteDAO.generarReportePrestamosActivos();
                            prestamosActivos.forEach(System.out::println);
                            break;

                        case 9:
                            System.out.println("\n📌 Reporte de Usuarios con Más Préstamos:");
                            List<String> usuariosFrecuentes = reporteDAO.generarReporteUsuariosFrecuentes();
                            usuariosFrecuentes.forEach(System.out::println);
                            break;

                        case 10:
                            System.out.println("\n📌 Reporte de Libros Más Prestados:");
                            List<String> librosPopulares = reporteDAO.generarReporteLibrosMasPrestados();
                            librosPopulares.forEach(System.out::println);
                            break;

                        case 11:
                            System.out.println("👋 Saliendo del sistema...");
                            return;

                        default:
                            System.out.println("⚠ Opción inválida. Intente nuevamente.");
                    }
                } else {
                    switch (opcion) {
                        case 1:
                            List<Libro> libros = libroServicio.listarLibros();
                            System.out.println("📚 Libros disponibles:");
                            for (Libro l : libros) {
                                if (l.getEstado().equalsIgnoreCase("disponible")) {
                                    System.out.println(l.getIdLibro() + " - " + l.getTitulo() + " de " + l.getAutor());
                                }
                            }
                            break;

                        case 5:
                            System.out.println("👋 Saliendo del sistema...");
                            return;

                        default:
                            System.out.println("⚠ Opción inválida. Intente nuevamente.");
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("❌ Error en la conexión con la base de datos");
        }
    }
}
