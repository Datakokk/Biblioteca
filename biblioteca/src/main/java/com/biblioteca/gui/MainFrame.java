package com.biblioteca.gui;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class MainFrame extends JFrame {
	public MainFrame() {
		setTitle("Gestión de Biblioteca");
		setSize(800, 900);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		iniciarComponentes();
	}
	
	private void iniciarComponentes() {
		JPanel panel = new JPanel();
		JLabel label = new JLabel("Bienvenido al Sistema de Biblioteca");
		panel.add(label);
		add(panel);
	}
	
	public static void Main(String[] args) {
		SwingUtilities.invokeLater(() -> {
			MainFrame frame = new MainFrame();
			frame.setVisible(true);
		});
	}
}
