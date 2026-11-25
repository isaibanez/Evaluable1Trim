package vista;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import baseDatos.GestionBd;

import java.awt.GridLayout;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.awt.FlowLayout;
import java.awt.BorderLayout;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JComboBox;
import javax.swing.JButton;
import javax.swing.ImageIcon;
import javax.swing.JPasswordField;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class VentanaInicio extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField tfUsuario;
	private JPasswordField pfContrasena;
	private JComboBox cbCargo;
	private GestionBd gestionBd = new GestionBd();
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					VentanaInicio frame = new VentanaInicio();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public VentanaInicio() {
		setTitle("Ventana de inicio");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lbUsuario = new JLabel("Usuario");
		lbUsuario.setBounds(26, 46, 46, 14);
		contentPane.add(lbUsuario);
		
		JLabel lbContrasena = new JLabel("Contraseña");
		lbContrasena.setBounds(26, 92, 73, 14);
		contentPane.add(lbContrasena);
		
		JLabel lbCargo = new JLabel("Cargo");
		lbCargo.setBounds(26, 138, 46, 14);
		contentPane.add(lbCargo);
		
		tfUsuario = new JTextField();
		tfUsuario.setBounds(99, 45, 118, 20);
		contentPane.add(tfUsuario);
		tfUsuario.setColumns(10);
		
		cbCargo = new JComboBox();
		cbCargo.setBounds(99, 134, 181, 22);
		contentPane.add(cbCargo);
		
		// Botón "Limpiar":
		JButton btnLimpiar = new JButton("Limpiar");
		btnLimpiar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				tfUsuario.setText("");
				pfContrasena.setText("");
			}
		});
		btnLimpiar.setBounds(290, 134, 98, 23);
		contentPane.add(btnLimpiar);
		
		
		// Botón "Entrar":
		JButton btnEntrar = new JButton("Entrar");
		btnEntrar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				// ↓ Obtiene el texto introducido/seleccionado en los campos de usuario, contraseña y cargo.
				String usuario = tfUsuario.getText();
				String contrasena = new String(pfContrasena.getPassword());
				String cargo = (String) cbCargo.getSelectedItem();
				
				// ↓ Si el campo de usuario o contraseña están vacíos, muestra un mensaje.
				if(usuario.isEmpty() || contrasena.isEmpty()) {
					JOptionPane.showMessageDialog(VentanaInicio.this, "Debes rellenar todos los campos");
					return;
				}
				
				ResultSet rs = gestionBd.obtenerUsuario(usuario, contrasena, cargo);
				
				try {
					if(rs != null && rs.next()) {
						// ↓ Obtenemos el id del usuario (lo necesitaremos más adelante para las ventanas de profesor y de alumno) y el nombre.
						int id = rs.getInt("id");
						String nombre = rs.getString("nombre");
						
						JOptionPane.showMessageDialog(VentanaInicio.this, "Bienvenid@, " + nombre);
						
						// ↓ Si el cargo seleccionado es "Profesor", mostrará la ventana de profesor (del usuario profesor que ha iniciado sesión).
						if (cargo.equals("Profesor")) {
							new VentanaProfesor(id).setVisible(true);
							dispose();
							// ↓ Si el cargo seleccionado es "Alumno", mostrará la ventana de alumno (del usuario alumno que ha iniciado sesión).
						} else if(cargo.equals("Alumno")) {
							new VentanaAlumno(id).setVisible(true);
							dispose();
						}
						// ↓ Si los datos introducidos no son válidos, muestra un mensaje.
					} else {
						JOptionPane.showMessageDialog(VentanaInicio.this, "Datos incorrectos");
					}
				} catch(SQLException ex){
					ex.printStackTrace();
					}
				}
		});
		btnEntrar.setBounds(99, 196, 119, 23);
		contentPane.add(btnEntrar);
		
		
		// Botón "Salir":
		JButton btnSalir = new JButton("Salir");
		btnSalir.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		btnSalir.setBounds(234, 196, 119, 23);
		contentPane.add(btnSalir);
		
		JLabel lblNewLabel = new JLabel("");
		lblNewLabel.setIcon(new ImageIcon(VentanaInicio.class.getResource("/imagenes/Universidad Europea.png")));
		lblNewLabel.setBounds(234, 44, 181, 68);
		contentPane.add(lblNewLabel);
		
		pfContrasena = new JPasswordField();
		pfContrasena.setBounds(99, 89, 118, 20);
		contentPane.add(pfContrasena);
		
		// ↓ Llamada al método que devuelve los cargos.
		cargarCargos();
	}
	
	// ↓ Método que devuelve los cargos.
	private void cargarCargos() {
		ResultSet rs = gestionBd.obtenerCargos();
		
		try {
			cbCargo.addItem("Selecciona una opción");
			
			if(rs != null) {
				while(rs.next()) {
					cbCargo.addItem(rs.getString("cargo"));
				}
			}
		} catch(SQLException e) {
			e.printStackTrace();
		}
		
	}
}
