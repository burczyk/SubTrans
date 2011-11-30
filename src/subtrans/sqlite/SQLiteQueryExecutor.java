package subtrans.sqlite;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class SQLiteQueryExecutor {
	private Connection connection;

	public SQLiteQueryExecutor(String filename) {
		try {
			Class.forName("org.sqlite.JDBC");
			connection = DriverManager.getConnection("jdbc:sqlite:" + filename);
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	public String translate(String sequence) {
		try {
			Statement statement = connection.createStatement();
			ResultSet rs = statement.executeQuery("select * from eng_pol where definition='" + sequence + "';");
			while (rs.next()) {
				return rs.getString("translation");
			}
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public void close() {
		try {
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
