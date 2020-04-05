package me.mreport.Managers;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import org.bukkit.Bukkit;

import me.mreport.Database;
import me.mreport.ChatR;

public class Messages {
	
	public static void addMessage(String server, UUID p, String message, String date, long timestamp, String type) {
		Bukkit.getScheduler().runTaskAsynchronously(ChatR.getInstance(),() -> {
			try {     
				PreparedStatement statement = Database.getConnection().prepareStatement("INSERT INTO chatLog(server, player, message, date, time, type) VALUES (?,?,?,?,?,?)");
				statement.setString(1, server);
				statement.setString(2, p.toString());
				statement.setString(3, message);
				statement.setString(4, date);
				statement.setLong(5, timestamp);
				statement.setString(6, type);
				statement.executeUpdate();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		});
	}
	
	public static int checkMessage(UUID p, Long time) {
		int count = 0;
	    try {     
	    	PreparedStatement statement = Database.getConnection().prepareStatement("SELECT COUNT(*) AS count FROM chatLog WHERE player = ? AND time <= ? AND time >= ?");
	    	statement.setString(1, p.toString());
	    	statement.setLong(2, System.currentTimeMillis());
	    	statement.setLong(3, time);
	    	ResultSet rs = statement.executeQuery();
	    	while (rs.next()) {
	    		count = rs.getInt("count");
	    	}
	    } catch (SQLException e) {
	    	e.printStackTrace();
	    }
	    return count;
	}

}
