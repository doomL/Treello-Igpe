package persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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
			TaskList.add(new Task(rs.getInt("Id"),rs.getString("Nome"), s));
		}
		return TaskList;
	}

	public void save(String name, int sezioneId) throws SQLException {
		if (con == null || con.isClosed())
			return;
		PreparedStatement stmt = con.prepareStatement("INSERT INTO Task(Nome, Sezione) VALUES(?,?)");
		stmt.setString(1, name);// 1 specifies the first parameter in the query
		stmt.setInt(2, sezioneId);
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
			TaskList.add(new Task(rs.getInt("Id"),rs.getString("Nome"), s));
		}
		return TaskList;
	}
}
