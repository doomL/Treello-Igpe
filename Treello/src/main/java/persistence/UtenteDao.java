package persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.security.crypto.bcrypt.BCrypt;

import model.Utente;

public class UtenteDao {
	
	Connection con=DbManager.con;
	
		public void save(Utente u) throws SQLException {
			if (con == null || con.isClosed())
				return;
			PreparedStatement stmt = con.prepareStatement("INSERT INTO Utente VALUES(?,?,?)");
			stmt.setString(1, u.getUsername());// 1 specifies the first parameter in the query
			stmt.setString(2, u.getEmail());
			stmt.setString(3, DbUtils.cryptPassword(u.getPassword()));
			stmt.executeUpdate();
			stmt.close();
		}
		
		public boolean login(Utente u) throws SQLException {
			// Statement stmt = con.createStatement();
			boolean matched=false;
			PreparedStatement stmt = con.prepareStatement("SELECT * FROM Utente WHERE Username =? ");
			stmt.setString(1, u.getUsername());// 1 specifies the first parameter in the query
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				matched = DbUtils.checkPassword(u.getPassword(), rs.getString("Password"));
			}
			return matched;
		}
}
