package baseDatos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class GestionBd {
	private Connection connection;
	private PreparedStatement preparedStatement;
	
	// ↓ Obtener cargos:
	public ResultSet obtenerCargos() {
		connection = Conexion.getConnection();
		String query = "SELECT cargo FROM cargos";
		
		try {
			preparedStatement = connection.prepareStatement(query);
			
			return preparedStatement.executeQuery();
			
		} catch (SQLException e) {
			System.out.println("Error de SQL.");
			e.printStackTrace();
			
			return null;
		}
	}
	
	
	// ↓ Obtener usuario concreto (para el login en "VentanaInicio"):
	public ResultSet obtenerUsuario(String usuario, String contrasena, String cargo) {
		connection = Conexion.getConnection();
		String query = "SELECT u.id, u.nombre FROM usuarios u "
				+ "JOIN cargos c ON c.id = u.id_cargo "
				+ "WHERE u.usuario = ? AND u.contrasena = ? AND c.cargo = ?";
		
		try {
			preparedStatement = connection.prepareStatement(query);
			preparedStatement.setString(1, usuario);
			preparedStatement.setString(2,  contrasena);
			preparedStatement.setString(3,  cargo);
			
			return preparedStatement.executeQuery();
			
		} catch (SQLException e) {
			System.out.println("Error de SQL.");
			e.printStackTrace();
			
			return null;
		}
	}
	
	
	// ↓ Obtener módulos del profesor:
	public ResultSet obtenerModulosProfesor(int idProfesor) {
		connection = Conexion.getConnection();
		String query = "SELECT id, nombre FROM modulos WHERE id_profesor = ?";
		
		try {
			preparedStatement = connection.prepareStatement(query);
			preparedStatement.setInt(1,  idProfesor);
			
			return preparedStatement.executeQuery();
			
		} catch (SQLException e) {
			System.out.println("Error de SQL.");
			e.printStackTrace();
			
			return null;
		}
	}
	
	
	// ↓ Obtener alumnos matriculados en el módulo elegido por el profesor:
	public ResultSet obtenerAlumnosModulos(String nombreModulo, int idProfesor) {
		connection = Conexion.getConnection();
		String query = "SELECT u.nombre, n.nota FROM notas n "
				+ "JOIN usuarios u ON u.id = n.id_usuario "
				+ "JOIN modulos m ON m.id = n.id_modulo "
				+ "WHERE m.nombre = ? AND m.id_profesor = ?";
		
		try {
			preparedStatement = connection.prepareStatement(query);
			preparedStatement.setString(1,  nombreModulo);
			preparedStatement.setInt(2,  idProfesor);
			
			return preparedStatement.executeQuery();
				
		} catch(SQLException e) {
			System.out.println("Error de SQL.");
			e.printStackTrace();
			
			return null;
		}
	}
	
	
	// ↓ Obtener módulos del alumno:
		public ResultSet obtenerModulosAlumno(int idAlumno) {
			connection = Conexion.getConnection();
			String query = "SELECT m.nombre FROM modulos m "
					+ "JOIN notas n ON n.id_modulo = m.id "
					+ "WHERE n.id_usuario = ?";
			
			try {
				preparedStatement = connection.prepareStatement(query);
				preparedStatement.setInt(1,  idAlumno);
				
				return preparedStatement.executeQuery();
				
			} catch (SQLException e) {
				System.out.println("Error de SQL.");
				e.printStackTrace();
				
				return null;
			}
		}
		
		
		// ↓ Obtener TODAS las notas del alumno (todos los módulos matriculados):
		public ResultSet obtenerNotasAlumno(int idAlumno) {
			connection = Conexion.getConnection();
			String query = "SELECT m.nombre, n.nota FROM notas n "
					+ "JOIN modulos m ON m.id = n.id_modulo "
					+ "WHERE n.id_usuario = ?";
			
			try {
				preparedStatement = connection.prepareStatement(query);
				preparedStatement.setInt(1,  idAlumno);
				
				return preparedStatement.executeQuery();
				
			} catch (SQLException e) {
				System.out.println("Error de SQL.");
				e.printStackTrace();
				
				return null;
			}
		}
		
		// ↓ Obtener la nota de un alumno de un módulo concreto:
		public ResultSet obtenerNotaAlumno(int idAlumno, String nombreModulo) {
			connection = Conexion.getConnection();
			String query = "SELECT m.nombre, n.nota FROM notas n "
					+ "JOIN modulos m ON m.id = n.id_modulo "
					+ "WHERE n.id_usuario = ? and m.nombre = ?";
			
			try {
				preparedStatement = connection.prepareStatement(query);
				preparedStatement.setInt(1,  idAlumno);
				preparedStatement.setString(2, nombreModulo);
				
				return preparedStatement.executeQuery();
				
			} catch (SQLException e) {
				System.out.println("Error de SQL.");
				e.printStackTrace();
				
				return null;
			}
		}
	
	
		// ↓ Insertar la nota de un alumno:
		public int anadirNotaAlumno(double nota, String nombreAlumno, String nombreModulo, int idProfesor) {
			connection = Conexion.getConnection();
			String query = "UPDATE notas n "
					+ "JOIN usuarios u ON u.id = n.id_usuario "
					+ "JOIN modulos m ON m.id = n.id_modulo "
					+ "SET n.nota = ? "
					+ "WHERE u.nombre = ? AND m.nombre = ? AND m.id_profesor = ?";
			
			try {
				preparedStatement = connection.prepareStatement(query);
				preparedStatement.setDouble(1, nota);
				preparedStatement.setString(2,  nombreAlumno);
				preparedStatement.setString(3,  nombreModulo);
				preparedStatement.setInt(4,  idProfesor);
				
				return preparedStatement.executeUpdate();
				
			} catch (SQLException e) {
				System.out.println("Error de SQL.");
				e.printStackTrace();
				
				return 0;
			}
		}
	
	
}
