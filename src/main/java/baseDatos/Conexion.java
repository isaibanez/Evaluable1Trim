package baseDatos;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexion {
		
	private static Connection connection;
	
	public static Connection getConnection() {
		if(connection == null) {
			createNewConnection();
		}
		
		return connection;
	}

	private static void createNewConnection() {
		try {
			connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/universidad", "root", "root");
		} catch (SQLException e) {
			System.out.println("Error en la conexión a la base de datos.");
			e.printStackTrace();
		}
		
	}
	
}
