package vista;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import baseDatos.GestionBd;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import java.awt.Font;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JTextField;

public class VentanaProfesor extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTable tbAlumnos;
	private JComboBox cbModulos;
	private int idProfesor;
	private GestionBd gestionBd = new GestionBd();
	private DefaultTableModel modeloTabla = new DefaultTableModel();
	private JTextField tfNota;
	private JLabel lbNuevaNota;

	/**
	 * Create the frame.
	 */
	public VentanaProfesor(int idProfesor) {
		setTitle("Ventana de profesor");
		this.idProfesor = idProfesor;
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		cbModulos = new JComboBox();
		cbModulos.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cargarAlumnosModulos();
			}
		});
		cbModulos.setBounds(94, 67, 306, 22);
		contentPane.add(cbModulos);
		
		JLabel lbModulosProfesor = new JLabel("Módulos");
		lbModulosProfesor.setBounds(27, 71, 46, 14);
		contentPane.add(lbModulosProfesor);
		
		// Tabla con alumnos:
		tbAlumnos = new JTable();
		modeloTabla.addColumn("ALUMNO");
		modeloTabla.addColumn("NOTA");
		tbAlumnos.setModel(modeloTabla);
		
		JScrollPane scroll = new JScrollPane(tbAlumnos);
		scroll.setBounds(27, 111, 373, 84);
		contentPane.add(scroll);
		
		JLabel lbTituloProfesor = new JLabel("VENTANA DE PROFESOR");
		lbTituloProfesor.setFont(new Font("Tahoma", Font.BOLD, 16));
		lbTituloProfesor.setBounds(27, 26, 210, 14);
		contentPane.add(lbTituloProfesor);
		
		
		// ↓ Botón para cerrar la sesión del profesor – devuelve a la ventana de inicio:
		JButton btnCerrarSesionProfesor = new JButton("Cerrar sesión");
		btnCerrarSesionProfesor.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new VentanaInicio().setVisible(true);
				dispose();
			}
		});
		btnCerrarSesionProfesor.setBounds(276, 22, 122, 23);
		contentPane.add(btnCerrarSesionProfesor);
		
		tfNota = new JTextField();
		tfNota.setBounds(150, 206, 67, 20);
		contentPane.add(tfNota);
		tfNota.setColumns(10);
		
		lbNuevaNota = new JLabel("Introduce una nota:");
		lbNuevaNota.setBounds(27, 209, 134, 14);
		contentPane.add(lbNuevaNota);
		
		JButton btnGuardarNota = new JButton("Guardar nota");
		btnGuardarNota.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				// ↓ Comprueba si hay alumno/fila + módulo seleccionados:
				int fila = tbAlumnos.getSelectedRow();
				if(cbModulos.getSelectedIndex() <= 0 || fila == -1) { // Índice menor que 0 || ninguna fila.
					JOptionPane.showMessageDialog(VentanaProfesor.this, "No has seleccionado ningún alumno/módulo");
					return;
				}
				
				// ↓ Comprueba si la nota es un número válido:
				double nota;
				try {
					nota = Double.parseDouble(tfNota.getText().replace(',', '.')); // Tiene que ser doble, y reemplaza comas por punto.
				} catch(NumberFormatException ex) {
					JOptionPane.showMessageDialog(VentanaProfesor.this, "El formato de la nota no es válido");
					return;
				}
				
				// ↓ Comprueba si la nota es de 0 a 10, y que no se pase de ese rango.
				if (nota < 0 || nota > 10) {
					JOptionPane.showMessageDialog(VentanaProfesor.this, "La nota tiene que ser entre 0 y 10");
					return;
				}
				
				String alumno = (String) modeloTabla.getValueAt(fila, 0); // Lee el nombre del alumno.
				String modulo = (String) cbModulos.getSelectedItem();
				
				// ↓ Comprueba que se ha guardado la nota/modificado esa fila correctamente. En caso afirmativo, muestra un mensaje tras actualizar la nota.
				int filaModificada = gestionBd.anadirNotaAlumno(nota, alumno, modulo, idProfesor);
				
				if(filaModificada > 0) {
					cargarAlumnosModulos();
					tfNota.setText("");
					JOptionPane.showMessageDialog(VentanaProfesor.this, "Nota guardada correctamente");
				}
				
				
			}
		});
		btnGuardarNota.setBounds(267, 205, 133, 23);
		contentPane.add(btnGuardarNota);
		
		// ↓ Llamada al método que devuelve los módulos del profesor.
		cargarModulos();
	}
	
	// ↓ Método que devuelve los módulos del profesor.
	private void cargarModulos() {
		ResultSet rs = gestionBd.obtenerModulosProfesor(idProfesor);
		
		try {
			cbModulos.addItem("Selecciona un módulo");
			
			if(rs != null) {
				while(rs.next()) {
					cbModulos.addItem(rs.getString("nombre"));
				}
			}
		} catch(SQLException e) {
			e.printStackTrace();
		}
	}
	
	// ↓ Método que devuelve los alumnos asignados al módulo seleccionado y los pinta en la tabla.
	private void cargarAlumnosModulos() {
		modeloTabla.setRowCount(0);
		
		// ↓ Si no hay seleccionado ningún módulo.
		if(cbModulos.getSelectedIndex() <= 0) {
			return;
		}
		
		String modulo = (String) cbModulos.getSelectedItem();
		ResultSet rs = gestionBd.obtenerAlumnosModulos(modulo, idProfesor);
		
		try {
			if(rs != null) {
				while(rs.next()) {
					modeloTabla.addRow(new Object[] {
							rs.getString("nombre"),
							// ↓ "getObject" para que no devuelva un 0 si la nota está vacía, sino un NULL.
							rs.getObject("nota")
					});
				}
 			}
		} catch(SQLException e) {
			e.printStackTrace();
		}
	}
}
