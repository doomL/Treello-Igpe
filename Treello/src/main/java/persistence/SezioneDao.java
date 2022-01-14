package persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import FranV.Treello.App;
import model.Progetto;
import model.Sezione;

public class SezioneDao {

	Connection con = DbManager.con;

	public List<Sezione> getSezioniProgetto(String id) throws SQLException {
		if (con == null || con.isClosed())
			return null;
		List<Sezione> SezioniList = new ArrayList<Sezione>();
		PreparedStatement stmt = con.prepareStatement("SELECT * FROM Sezione WHERE Progetto = ? ");
		stmt.setInt(1, Integer.parseInt(id));
		ProgettoDao pDao = new ProgettoDao();
		Progetto p = pDao.getProgettoFromId(Integer.parseInt(id));
		ResultSet rs = stmt.executeQuery();
		while (rs.next()) {
			SezioniList.add(new Sezione(rs.getInt("Id"), rs.getString("Nome"), p));
		}
		return SezioniList;
	}

	public void save(String name, int projectId) throws SQLException {
		if (con == null || con.isClosed())
			return;
		PreparedStatement stmt = con.prepareStatement("INSERT INTO Sezione(Nome, Progetto) VALUES(?,?)");
		stmt.setString(1, name);// 1 specifies the first parameter in the query
		stmt.setInt(2, projectId);
		stmt.executeUpdate();
		stmt.close();
	}

	public Sezione getSezioneFromId(int id) throws SQLException {
		if (con == null || con.isClosed())
			return null;
		PreparedStatement stmt = con.prepareStatement("SELECT * FROM Sezione WHERE Id = ? ");
		stmt.setInt(1, id);
		ResultSet rs = stmt.executeQuery();
		while (rs.next()) {
			return new Sezione(rs.getInt("Id"), rs.getString("Nome"),App.getCurrentProgetto());
		}
		return null;
	}

	public void delete(int id) throws SQLException {
		if (con == null || con.isClosed())
			return;
		PreparedStatement stmt = con.prepareStatement("DELETE FROM Sezione WHERE Id = ?");
		stmt.setInt(1, id);
		stmt.executeUpdate();
		stmt.close();
	}
}
