package persistence;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DbManager {

	public static Connection con = null;

	public void init() throws SQLException {
		String url = "jdbc:sqlite:treello.db";
		con = DriverManager.getConnection(url);
		if (con != null && !con.isClosed())
			System.out.println("Db Connected!");
	}

	
	
}
