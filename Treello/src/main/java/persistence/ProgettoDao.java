package persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import FranV.Treello.App;
import model.Progetto;
import model.Utente;

public class ProgettoDao {

	Connection con=DbManager.con;
	
	public void save(String projectName) throws SQLException {
		if (con == null || con.isClosed())
			return;
		PreparedStatement stmt = con.prepareStatement("INSERT INTO Progetto(Nome, Utente) VALUES(?,?)");
		stmt.setString(1, projectName);// 1 specifies the first parameter in the query
		stmt.setString(2, App.getCurrentUser().getUsername());
		stmt.executeUpdate();
		stmt.close();
	}
	
	public List<Progetto> getUserProjects() throws SQLException {
		if (con == null || con.isClosed())
			return null;
		List<Progetto> projectsList = new ArrayList<Progetto>();
		PreparedStatement stmt = con.prepareStatement("SELECT * FROM Progetto WHERE Utente = ? ");
		stmt.setString(1, App.getCurrentUser().getUsername());
		ResultSet rs = stmt.executeQuery();
		while (rs.next()) {
			projectsList.add(new Progetto(rs.getInt("Id"),rs.getString("Nome"),App.getCurrentUser(),rs.getInt("Completato"),rs.getString("Sfondo")));
		}
		stmt.close();
		return projectsList;
	}
	
	public Progetto getProgettoFromId(int id) throws SQLException {
		if (con == null || con.isClosed())
			return null;
		PreparedStatement stmt = con.prepareStatement("SELECT * FROM Progetto WHERE Id = ? ");
		stmt.setInt(1, id);
		Progetto p;
		ResultSet rs = stmt.executeQuery();
		while (rs.next()) {
			p = new Progetto(rs.getInt("Id"),rs.getString("Nome"),App.getCurrentUser(),rs.getInt("Completato"),rs.getString("Sfondo"));
			App.setCurrentProgetto(p);
			stmt.close();
			return p;
		}
		stmt.close();
		return null;
	}

	public void update(String projectName) throws SQLException {
		if (con == null || con.isClosed())
			return;
		PreparedStatement stmt = con.prepareStatement("UPDATE Progetto SET Nome = ? WHERE Id = ?");
		stmt.setString(1, projectName);// 1 specifies the first parameter in the query
		stmt.setInt(2, App.getCurrentProgetto().getId());
		stmt.executeUpdate();
		stmt.close();
	}

	public void delete() throws SQLException {
		if (con == null || con.isClosed())
			return;
		PreparedStatement stmt = con.prepareStatement("DELETE FROM Progetto WHERE Id = ?");
		stmt.setInt(1, App.getCurrentProgetto().getId());
		stmt.executeUpdate();
		stmt.close();
	}
	
}
