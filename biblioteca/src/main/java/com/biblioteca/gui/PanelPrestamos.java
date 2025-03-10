package com.biblioteca.gui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.List;
import com.biblioteca.servicio.PrestamoServicio;
import com.biblioteca.modelo.Prestamo;

public class PanelPrestamos extends JPanel {
    private PrestamoServicio prestamoServicio;
    private JTable tablaPrestamos;
    private DefaultTableModel modeloTabla;
    private JTextField txtIdUsuario, txtIdLibro;
    private JButton btnRegistrar, btnDevolver, btnRegresar;
    private MainFrame mainFrame;

    public PanelPrestamos(PrestamoServicio prestamoServicio, MainFrame mainFrame) {
        this.prestamoServicio = prestamoServicio;
        this.mainFrame = mainFrame;
        setLayout(new BorderLayout());

        // Tabla de Préstamos
        modeloTabla = new DefaultTableModel(new String[]{"ID Préstamo", "ID Usuario", "ID Libro", "Fecha Préstamo", "Fecha Devolución"}, 0);
        tablaPrestamos = new JTable(modeloTabla);
        cargarPrestamos();
        add(new JScrollPane(tablaPrestamos), BorderLayout.CENTER);

        // Panel de Formularios
        JPanel panelFormulario = new JPanel(new GridLayout(3, 2, 5, 5));
        panelFormulario.setBorder(BorderFactory.createTitledBorder("Registrar Préstamo"));
        txtIdUsuario = new JTextField();
        txtIdLibro = new JTextField();
        
        panelFormulario.add(new JLabel("ID Usuario:"));
        panelFormulario.add(txtIdUsuario);
        panelFormulario.add(new JLabel("ID Libro:"));
        panelFormulario.add(txtIdLibro);
        
        add(panelFormulario, BorderLayout.NORTH);

        // Panel de Botones
        JPanel panelBotones = new JPanel();
        btnRegistrar = new JButton("Registrar Préstamo");
        btnDevolver = new JButton("Devolver Libro");
        btnRegresar = new JButton("Regresar");
        panelBotones.add(btnRegistrar);
        panelBotones.add(btnDevolver);
        panelBotones.add(btnRegresar);
        add(panelBotones, BorderLayout.SOUTH);

        // Eventos de Botones
        btnRegistrar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                registrarPrestamo();
            }
        });

        btnDevolver.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                devolverPrestamo();
            }
        });
        
        btnRegresar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainFrame.regresarMenuPrincipal();
            }
        });
    }

    private void cargarPrestamos() {
        modeloTabla.setRowCount(0);
        try {
            List<Prestamo> prestamos = prestamoServicio.listarPrestamos();
            for (Prestamo prestamo : prestamos) {
                modeloTabla.addRow(new Object[]{
                        prestamo.getIdPrestamo(),
                        prestamo.getIdUsuario(),
                        prestamo.getIdLibro(),
                        prestamo.getFechaPrestamo(),
                        prestamo.getFechaDevolucion()});
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al cargar préstamos", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void registrarPrestamo() {
        try {
            Prestamo prestamo = new Prestamo();
            prestamo.setIdUsuario(Integer.parseInt(txtIdUsuario.getText()));
            prestamo.setIdLibro(Integer.parseInt(txtIdLibro.getText()));
            prestamo.setFechaPrestamo(new java.sql.Date(System.currentTimeMillis()));

            prestamoServicio.registrarPrestamo(prestamo);
            cargarPrestamos();
            JOptionPane.showMessageDialog(this, "Préstamo registrado con éxito!");
        } catch (SQLException | NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Error al registrar préstamo", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void devolverPrestamo() {
        int filaSeleccionada = tablaPrestamos.getSelectedRow();
        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un préstamo para devolver");
            return;
        }

        try {
            int idPrestamo = (int) modeloTabla.getValueAt(filaSeleccionada, 0);
            prestamoServicio.devolverLibro(idPrestamo);
            cargarPrestamos();
            JOptionPane.showMessageDialog(this, "Libro devuelto con éxito!");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al devolver libro", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}