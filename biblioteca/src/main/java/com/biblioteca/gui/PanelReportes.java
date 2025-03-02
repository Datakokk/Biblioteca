package com.biblioteca.gui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.SQLException;
import java.util.List;

import com.biblioteca.modelo.Reporte;
import com.biblioteca.servicio.ReporteServicio;

public class PanelReportes extends JPanel {
    private ReporteServicio reporteServicio;
    private JTable tablaReportes;
    private DefaultTableModel modeloTabla;
    private JButton btnPrestamosActivos, btnUsuariosFrecuentes, btnLibrosMasPrestados, btnRegresar;
    private MainFrame mainFrame;

    public PanelReportes(ReporteServicio reporteServicio, MainFrame mainFrame) {
        this.reporteServicio = reporteServicio;
        this.mainFrame = mainFrame;
        setLayout(new BorderLayout());

        // Tabla de Reportes
        modeloTabla = new DefaultTableModel(new String[]{"ID Reporte", "Descripción", "Fecha Generación"}, 0);
        tablaReportes = new JTable(modeloTabla);
        cargarReportes();
        add(new JScrollPane(tablaReportes), BorderLayout.CENTER);

        // Panel de Botones
        JPanel panelBotones = new JPanel();
        btnPrestamosActivos = new JButton("Reporte de Préstamos Activos");
        btnUsuariosFrecuentes = new JButton("Usuarios Frecuentes");
        btnLibrosMasPrestados = new JButton("Libros Más Prestados");
        btnRegresar = new JButton("Regresar");

        panelBotones.add(btnPrestamosActivos);
        panelBotones.add(btnUsuariosFrecuentes);
        panelBotones.add(btnLibrosMasPrestados);
        panelBotones.add(btnRegresar);
        add(panelBotones, BorderLayout.SOUTH);

        // Eventos de Botones
        btnPrestamosActivos.addActionListener(e -> mostrarPrestamosActivos());
        btnUsuariosFrecuentes.addActionListener(e -> mostrarUsuariosFrecuentes());
        btnLibrosMasPrestados.addActionListener(e -> mostrarLibrosMasPrestados());
        btnRegresar.addActionListener(e -> mainFrame.regresarMenuPrincipal());
    }

    private void cargarReportes() {
        modeloTabla.setRowCount(0);
        try {
            List<Reporte> reportes = reporteServicio.listarReportes();
            for (Reporte reporte : reportes) {
                modeloTabla.addRow(new Object[]{
                        reporte.getIdReporte(),  // Ahora se muestra el ID real
                        reporte.getDescripcion(),  // Ahora la descripción tiene el contenido correcto
                        reporte.getFechaGeneracion()
                });
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al cargar reportes", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void mostrarPrestamosActivos() {
        try {
            mostrarListaEnTabla("Préstamos Activos", reporteServicio.generarReportePrestamosActivos());
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al obtener préstamos activos", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void mostrarUsuariosFrecuentes() {
        try {
            mostrarListaEnTabla("Usuarios Frecuentes", reporteServicio.generarReporteUsuariosFrecuentes());
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al obtener usuarios frecuentes", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void mostrarLibrosMasPrestados() {
        try {
            mostrarListaEnTabla("Libros Más Prestados", reporteServicio.generarReporteLibrosMasPrestados());
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al obtener libros más prestados", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void mostrarListaEnTabla(String titulo, List<String> datos) {
        modeloTabla.setRowCount(0);
        int id = 1;
        for (String dato : datos) {
            modeloTabla.addRow(new Object[]{id++, dato, new java.util.Date()});
        }
        JOptionPane.showMessageDialog(this, "Mostrando " + titulo);
    }
}
