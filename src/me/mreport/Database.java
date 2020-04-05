package me.mreport;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Database {
	
	private static Connection connection;
	private static PreparedStatement statement;
	private static String host;
	private static String database;
	private static String username;
  	private static String password;
  
	public static void openConnection() {
		host = ChatR.getInstance().getConfig().getString("MySQL-Host");
		database = ChatR.getInstance().getConfig().getString("MySQL-Database");
		username = ChatR.getInstance().getConfig().getString("MySQL-User");
		password = ChatR.getInstance().getConfig().getString("MySQL-Password");
		
		try {
			if ((connection != null) && (!connection.isClosed())) {
				return;
			}
			Class.forName("com.mysql.jdbc.Driver");
			connection = DriverManager.getConnection("jdbc:mysql://" + host + ":3306/" + database + "?autoReconnect=true", username, password);
			System.out.println("[!] Database has been connected successfully.");
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
  
	public static void closeConnection() {
		try {
			if ((connection == null) && (connection.isClosed())) {
				return;
			}
			connection.close();
			System.out.println("[!] Database has been disconnected successfully.");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
  
	public static void createTables() {
		try { 
			String sql = "CREATE TABLE IF NOT EXISTS reports(ID INT(255) NOT NULL AUTO_INCREMENT PRIMARY KEY, offender VARCHAR(64), reporter VARCHAR(64), reason LONGTEXT, date VARCHAR(36), chatID TEXT, staff VARCHAR(64), status VARCHAR(36), time VARCHAR(36))";
			String sql2 = "CREATE TABLE IF NOT EXISTS players(ID INT(255) NOT NULL AUTO_INCREMENT PRIMARY KEY, user varchar(64), name VARCHAR(64), acceptedSent INT(16), deniedSent INT(16), acceptedStaff INT(16), deniedStaff INT(16), acceptedSentMont INT(16), deniedSentMont INT(16), acceptedStaffMont INT(16), deniedStaffMont INT(16))";
			String sql3 = "CREATE TABLE IF NOT EXISTS chatLog(ID INT(255) NOT NULL AUTO_INCREMENT PRIMARY KEY, server VARCHAR(100), player VARCHAR(100), message VARCHAR(400), date VARCHAR(36), time VARCHAR(50), type VARCHAR(50))";
			String sql4 = "CREATE TABLE IF NOT EXISTS reportLog(ID INT(255) NOT NULL AUTO_INCREMENT PRIMARY KEY, server VARCHAR(100), player VARCHAR(100), message VARCHAR(400), date VARCHAR(36), time VARCHAR(50), type VARCHAR(50), handle INT(16), reportid TEXT)";
			statement = connection.prepareStatement(sql);
			statement.executeUpdate();
			statement = connection.prepareStatement(sql2);
			statement.executeUpdate();
			statement = connection.prepareStatement(sql3);
			statement.executeUpdate();
			statement = connection.prepareStatement(sql4);
			statement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
  
	public static Connection getConnection() {
		return connection;
	}
  
	public static PreparedStatement getStatement() {
		return statement;
	}
}
