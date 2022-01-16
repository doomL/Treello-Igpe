package persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import FranV.Treello.App;
import model.Progetto;
import model.Sezione;
import model.Task;

public class TaskDao {

	Connection con=DbManager.con;

	public List<Task> getTaskSezioneFromId(String id) throws SQLException {
		if (con == null || con.isClosed())
			return null;
		List<Task> TaskList = new ArrayList<Task>();
		PreparedStatement stmt = con.prepareStatement("SELECT * FROM Task WHERE Sezione = ? ");
		stmt.setInt(1, Integer.parseInt(id));
		SezioneDao sDao = new SezioneDao();
		Sezione s = sDao.getSezioneFromId(Integer.parseInt(id));
		ResultSet rs = stmt.executeQuery();
		while (rs.next()) {
			TaskList.add(new Task(rs.getInt("Id"),rs.getString("Nome"), s,rs.getString("Data"),rs.getInt("Priorita"),rs.getInt("Completata")));
		}
		return TaskList;
	}

	public void save(String name, int sezioneId) throws SQLException {
		if (con == null || con.isClosed())
			return;
		PreparedStatement stmt = con.prepareStatement("INSERT INTO Task(Nome, Sezione, Data, Priorita, Completata) VALUES(?,?,?,0,0)");
		stmt.setString(1, name);// 1 specifies the first parameter in the query
		stmt.setInt(2, sezioneId);
		DateTimeFormatter dateF = DateTimeFormatter.ofPattern("dd/MM/yy HH:mm");
		LocalDateTime currentTime = LocalDateTime.now();
		stmt.setString(3, dateF.format(currentTime));
		stmt.executeUpdate();

		stmt.close();
	}

	public List<Task> getTaskSezione(Sezione s) throws SQLException {
		if (con == null || con.isClosed())
			return null;
		List<Task> TaskList = new ArrayList<Task>();
		PreparedStatement stmt = con.prepareStatement("SELECT * FROM Task WHERE Sezione = ? ");
		stmt.setInt(1, s.getId());
		ResultSet rs = stmt.executeQuery();
		while (rs.next()) {
			TaskList.add(new Task(rs.getInt("Id"),rs.getString("Nome"), s,rs.getString("Data"),rs.getInt("Priorita"),rs.getInt("Completata")));
		}
		return TaskList;
	}

	public void updatePriorita(int id, int getnPriorita) throws SQLException {
		if (con == null || con.isClosed())
			return;
		PreparedStatement stmt = con.prepareStatement("UPDATE Task SET Priorita = ? WHERE Id = ?");
		stmt.setInt(1, getnPriorita);// 1 specifies the first parameter in the query
		stmt.setInt(2, id);
		stmt.executeUpdate();
		stmt.close();
	}

	public void updateCompletata(int id, Boolean newValue) throws SQLException {
		System.out.println("non poetha");
		if (con == null || con.isClosed())
			return;
		PreparedStatement stmt = con.prepareStatement("UPDATE Task SET Completata = ? WHERE Id = ?");
		int value = newValue ? 1: 0;
		stmt.setInt(1, value);
		stmt.setInt(2, id);// 1 specifies the first parameter in the query
		stmt.executeUpdate();
		stmt.close();

	}
	public void delete(int id) throws SQLException {
		if (con == null || con.isClosed())
			return;
		PreparedStatement stmt = con.prepareStatement("DELETE FROM Task WHERE Id = ?");
		stmt.setInt(1, id);
		stmt.executeUpdate();
		stmt.close();
	}
	public void deleteFromSezione(int idSezione) throws SQLException {
		if (con == null || con.isClosed())
			return;
		PreparedStatement stmt = con.prepareStatement("DELETE FROM Task WHERE Sezione = ?");
		stmt.setInt(1, idSezione);
		stmt.executeUpdate();
		stmt.close();
	}
}
