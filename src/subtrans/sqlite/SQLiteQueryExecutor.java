package subtrans.sqlite;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

//script for translations.db
//CREATE TABLE files(name varchar(2048));
//CREATE TABLE translations(sequence varchar(2048), translation varchar(2048), occurences int);
//CREATE INDEX file_ind on files(name);
//CREATE INDEX trans_ind on translations(sequence);

/**
 * 
 * @author kamilburczyk
 * 
 *         Class executes queries to SQLite database which has table eng_pol(definition, translation) where both columns are text.
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
		sequence = removeApostrophe(sequence);
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

	private String removeApostrophe(String s) {
		int pos = s.indexOf("'");
		if (pos > 0) {
			return s.substring(0, pos);
		}
		return s.replaceAll("'", ""); // there is also possibility like: 'cause
	}

	public boolean translationExist(String sequence, String translation) {
		sequence = sequence.replaceAll("\\'", "\\'\\'");
		translation = translation.replaceAll("\\'", "\\'\\'");
		try {
			Statement statement = connection.createStatement();
			ResultSet rs = statement.executeQuery("select * from translations where sequence='" + sequence + "' and translation='" + translation + "';");
			if (rs.next()) {
				return true;
			}
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	public void addTranslation(String sequence, String translation, int occurences) {
		sequence = sequence.replaceAll("\\'", "\\'\\'");
		translation = translation.replaceAll("\\'", "\\'\\'");
		if (translationExist(sequence, translation)) {
			try {
				Statement statement = connection.createStatement();
				ResultSet rs = statement.executeQuery("select occurences from translations where sequence='" + sequence + "' and translation='" + translation + "';");
				if (rs.next()) {
					int currentOccurences = rs.getInt("occurences");
					statement.executeUpdate("update translations set occurences=" + Integer.valueOf(currentOccurences + occurences) + " where sequence='" + sequence + "' and translation='" + translation + "';");
				}
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} else {
			try {
				Statement statement = connection.createStatement();
				statement.executeUpdate("insert into translations values('" + sequence + "','" + translation + "'," + Integer.valueOf(occurences) + ")");
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public boolean fileExist(String fileName) {
		try {
			Statement statement = connection.createStatement();
			ResultSet rs = statement.executeQuery("select * from files where name='" + fileName + "';");
			if (rs.next()) {
				return true;
			}
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	public void addFile(String fileName) {
		if (!fileExist(fileName)) {
			try {
				Statement statement = connection.createStatement();
				statement.executeUpdate("insert into files values('" + fileName + "')");
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public String getBestTranslation(String sequence) {
		sequence = sequence.replaceAll("\\'", "\\'\\'");
		try {
			Statement statement = connection.createStatement();
			ResultSet rs = statement.executeQuery("select translation from translations where sequence='" + sequence + "' order by occurences desc;");
			if (rs.next()) {
				return rs.getString("translation");
			}
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
}
