package subtrans.sqlite;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author kamilburczyk
 * 
 *         Class executes queries to SQLite database which has table
 *         eng_pol(definition, translation) where both columns are text.
 * 
 */
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

	/**
	 * 
	 * @param sequence
	 *            Sequence to translate.
	 * @return List of all potential translations which match given definition.
	 */
	public List<String> translate(String sequence) {
		List<String> translations = new ArrayList<String>();
		try {
			Statement statement = connection.createStatement();
			ResultSet rs = statement.executeQuery("select * from eng_pol where definition='" + sequence + "';");
			while (rs.next()) {
				translations.add(rs.getString("translation"));
			}
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return translations;
	}

	/**
	 * Closes connection to database.
	 */
	public void close() {
		try {
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
