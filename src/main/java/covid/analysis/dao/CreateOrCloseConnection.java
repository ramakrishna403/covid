package covid.analysis.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class CreateOrCloseConnection {

	
	public static Connection getConnection() {

		Connection con = null;
		String URL = "jdbc:mysql://localhost:3306/covid_analysis";
		String username = "root";
		String password = "root";
		try {
			con = DriverManager.getConnection(URL, username, password);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return con;
	}

	
	public static void closeConnection(Connection con) throws SQLException {
		con.close();
	}

}
