package integration;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseUtils {
	
	private static final String cPOSTGRESQL_DRIVER = "org.postgresql.Driver";
	private static final String cCONNECTION_URL = "jdbc:postgresql://localhost:5432/vet?user=vet&password=vet";
	public static Connection iDatabaseConnection;

	public static Connection getConnection() throws Exception {
		Connection mNewConnection = null;
		
		try {
			Class.forName(DatabaseUtils.cPOSTGRESQL_DRIVER);
		} catch (ClassNotFoundException e1) {
			throw new ClassNotFoundException("Test: no se encontró la clase del driver de la base de datos", e1);
		}

		try {
			mNewConnection = DriverManager.getConnection(DatabaseUtils.cCONNECTION_URL);
		} catch (SQLException e) {
			throw new SQLException("Test: no se pudo crear la conexión a la base.", e);
		}
		
		return mNewConnection;
	}
	
	public static void closeConnection(Connection pConnection) throws Exception {
		try {
			pConnection.close();
		} catch (SQLException e) {
			throw new SQLException("Test: no se pudo cerrar la conexión de la base de datos", e);
		}
	}
	
	public static void start() throws Exception {
		DatabaseUtils.iDatabaseConnection = DatabaseUtils.getConnection();
	}
	
	public static void shutdown() throws Exception {
		DatabaseUtils.closeConnection(DatabaseUtils.iDatabaseConnection);
	}
	
	public static ResultSet executeQuery(String pQuery) throws Exception {
		Statement mStatement = DatabaseUtils.iDatabaseConnection.createStatement();
		return mStatement.executeQuery(pQuery);
	}
}
