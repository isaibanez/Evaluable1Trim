package vista;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import baseDatos.GestionBd;

import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.awt.event.ActionEvent;
import javax.swing.JTable;
import javax.swing.JComboBox;

public class VentanaAlumno extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTable tbNotasAlumno;
	private JComboBox cbModulosAlumno;
	private int idAlumno;
	private GestionBd gestionBd = new GestionBd();
	private DefaultTableModel modeloTabla = new DefaultTableModel();

	/**
	 * Create the frame.
	 */
	public VentanaAlumno(int idAlumno) {
		setTitle("Ventana de alumno");
		this.idAlumno = idAlumno;
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lbTituloAlumno = new JLabel("VENTANA DE ALUMNO");
		lbTituloAlumno.setFont(new Font("Tahoma", Font.BOLD, 16));
		lbTituloAlumno.setBounds(27, 26, 210, 14);
		contentPane.add(lbTituloAlumno);
		
		
		// Botón para cerrar la sesión del alumno – devuelve a la ventana de inicio:
		JButton btnCerrarSesionAlumno = new JButton("Cerrar sesión");
		btnCerrarSesionAlumno.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new VentanaInicio().setVisible(true);
				dispose();
			}
		});
		btnCerrarSesionAlumno.setBounds(276, 22, 122, 23);
		contentPane.add(btnCerrarSesionAlumno);
		
		// Tabla con notas:
		tbNotasAlumno = new JTable();
		tbNotasAlumno.setBounds(27, 111, 373, 110);
		modeloTabla.addColumn("MÓDULO");
		modeloTabla.addColumn("NOTA");
		tbNotasAlumno.setModel(modeloTabla);
		
		JScrollPane scroll = new JScrollPane(tbNotasAlumno);
		scroll.setBounds(27, 111, 373, 110);
		contentPane.add(scroll);
		
		JLabel lbModulosAlumno = new JLabel("Módulos");
		lbModulosAlumno.setBounds(27, 71, 92, 14);
		contentPane.add(lbModulosAlumno);
		
		// ↓ El ComboBox llama al método que devuelve las notas del alumno.
		cbModulosAlumno = new JComboBox();
		cbModulosAlumno.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cargarNotasAlumno();
			}
		});
		cbModulosAlumno.setBounds(129, 67, 271, 22);
		contentPane.add(cbModulosAlumno);
		
		// ↓ Llamada al método que devuelve los módulos del alumno.
		cargarModulosAlumno();
	}
	
	// ↓ Método que devuelve los módulos del alumno.
	private void cargarModulosAlumno() {
		ResultSet rs = gestionBd.obtenerModulosAlumno(idAlumno);
		
		try {
			cbModulosAlumno.addItem("Selecciona un módulo");
			
			if(rs != null) {
				while(rs.next()) {
					cbModulosAlumno.addItem(rs.getString("nombre"));
				}
			}
		} catch(SQLException e) {
			e.printStackTrace();
		}
	}
	
	// ↓ Método que devuelve las notas del alumno y las pinta en la tabla.
	private void cargarNotasAlumno() {
		modeloTabla.setRowCount(0);
		
		ResultSet rs;
		
		// ↓ Opción que devuelve TODAS las notas del alumno.
		if(cbModulosAlumno.getSelectedIndex() <= 0) {
			rs = gestionBd.obtenerNotasAlumno(idAlumno);
		} else {
			// ↓ Opción que devuelve solo la nota del módulo seleccionado en el ComboBox.
			String moduloAlumno = (String) cbModulosAlumno.getSelectedItem();
			rs = gestionBd.obtenerNotaAlumno(idAlumno, moduloAlumno);
		}
		
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
