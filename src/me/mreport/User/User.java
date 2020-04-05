package me.mreport.User;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import org.bukkit.Bukkit;

import me.mreport.ChatR;
import me.mreport.Database;

public class User {
	
	private UUID uuid;
	private String username;
	
	public User(UUID uuid) {
		this.uuid = uuid;
	}
	
	public User(UUID uuid, String username) {
		this.uuid = uuid;
		this.username = username;
	}
		
	public boolean isCreated() {	   
		try {
			ResultSet results = Database.getStatement().executeQuery("SELECT * FROM players WHERE user='" + this.uuid + "'");
			if (results.next()) {
				return true;
			}   
		} catch (SQLException e) {
	      e.printStackTrace();
	    }
		return false;
	}
	
   
	public void createStaff() {
		Bukkit.getScheduler().runTaskAsynchronously(ChatR.getInstance(), () -> {
			try { 
				PreparedStatement statement = Database.getConnection().prepareStatement("INSERT INTO players(user, name, acceptedSent, deniedSent, acceptedStaff, deniedStaff, acceptedSentMont, deniedSentMont, acceptedStaffMont, deniedStaffMont) VALUES (?,?,?,?,?,?,?,?,?,?)");
				statement.setString(1, String.valueOf(this.uuid));
				statement.setString(2, username);
				statement.setInt(3, 0);
				statement.setInt(4, 0);
				statement.setInt(5, 0);
				statement.setInt(6, 0);
				statement.setInt(7, 0);
				statement.setInt(8, 0);
				statement.setInt(9, 0);
				statement.setInt(10, 0);
				statement.executeUpdate();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		});
   }
	
	public void updateName(String name) {
		Bukkit.getScheduler().runTaskAsynchronously(ChatR.getInstance(), () -> {
			try {
				PreparedStatement staff = Database.getConnection().prepareStatement("UPDATE players SET name = ? WHERE user = ?");
				staff.setString(1, name);
				staff.setString(2, this.uuid.toString());
				staff.executeUpdate();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		});
   }
   
	public void setMonthlyStaffAccepted(int amount) {
		Bukkit.getScheduler().runTaskAsynchronously(ChatR.getInstance(), () -> {
			try {
				PreparedStatement staff = Database.getConnection().prepareStatement("UPDATE players SET acceptedStaffMont = ? WHERE user = ?");
				staff.setInt(1, amount);
				staff.setString(2, this.uuid.toString());
				staff.executeUpdate();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		});
   }
   
	public void setStaffAccepted(int amount) {
		Bukkit.getScheduler().runTaskAsynchronously(ChatR.getInstance(), () -> {
			try {
				PreparedStatement staff = Database.getConnection().prepareStatement("UPDATE players SET acceptedStaff = ? WHERE user = ?");
				staff.setInt(1, amount);
				staff.setString(2, this.uuid.toString());
				staff.executeUpdate();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		});
   }
   
	public void setMonthlyStaffDenied(int amount) {
		Bukkit.getScheduler().runTaskAsynchronously(ChatR.getInstance(), () -> {
			try {
				PreparedStatement staff = Database.getConnection().prepareStatement("UPDATE players SET deniedStaffMont = ? WHERE user = ?");
				staff.setInt(1, amount);
				staff.setString(2, this.uuid.toString());
				staff.executeUpdate();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		});
   }
   
	public void setStaffDenied(int amount) {
		Bukkit.getScheduler().runTaskAsynchronously(ChatR.getInstance(), () -> {
			try {
				PreparedStatement staff = Database.getConnection().prepareStatement("UPDATE players SET deniedStaff = ? WHERE user = ?");
				staff.setInt(1, amount);
				staff.setString(2, this.uuid.toString());
				staff.executeUpdate();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		});
   }
   
	public void setMonthlySentAccepted(int amount) {
		Bukkit.getScheduler().runTaskAsynchronously(ChatR.getInstance(), () -> {
			try {
				PreparedStatement staff = Database.getConnection().prepareStatement("UPDATE players SET acceptedSentMont = ? WHERE user = ?");
				staff.setInt(1, amount);
				staff.setString(2, this.uuid.toString());
				staff.executeUpdate();
		 	} catch (SQLException e) {
		 		e.printStackTrace();
		 	}
		});
   }
   
	public void setSentAccepted(int amount) {
		Bukkit.getScheduler().runTaskAsynchronously(ChatR.getInstance(), () -> {
			try {
				PreparedStatement staff = Database.getConnection().prepareStatement("UPDATE players SET acceptedSent = ? WHERE user = ?");
				staff.setInt(1, amount);
				staff.setString(2, this.uuid.toString());
				staff.executeUpdate();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		});
   }
   
	public void setMonthlySentDenied(int amount) {
		Bukkit.getScheduler().runTaskAsynchronously(ChatR.getInstance(), () -> {
			try {
				PreparedStatement staff = Database.getConnection().prepareStatement("UPDATE players SET deniedSentMont = ? WHERE user = ?");
				staff.setInt(1, amount);
				staff.setString(2, this.uuid.toString());
				staff.executeUpdate();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		});
   }
   
	public void setSentDenied(int amount) {
		Bukkit.getScheduler().runTaskAsynchronously(ChatR.getInstance(), () -> {
			try {
				PreparedStatement staff = Database.getConnection().prepareStatement("UPDATE players SET deniedSent = ? WHERE user = ?");
				staff.setInt(1, amount);
				staff.setString(2, this.uuid.toString());
				staff.executeUpdate();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		});
   }
	
	public String getName() {
		try {
			PreparedStatement statement = Database.getConnection().prepareStatement("SELECT * FROM players WHERE user = ?");
			statement.setString(1, this.uuid.toString());
			ResultSet results = statement.executeQuery();
			if (results.next())
				return results.getString("name");
	     } catch (SQLException e) {
	    	 return null;
	     }
		return null;
	}
   
	public int getMonthlyStaffAccepted() {
		try {
			PreparedStatement statement = Database.getConnection().prepareStatement("SELECT * FROM players WHERE user=?");
			statement.setString(1, this.uuid.toString());
			ResultSet results = statement.executeQuery();
			if (results.next())
				return results.getInt("acceptedStaffMont");
	     } catch (SQLException e) {
	    	 return 0;
	     }
		return 0;
	}
   
   public int getStaffAccepted() {
	   try {
		   PreparedStatement statement = Database.getConnection().prepareStatement("SELECT * FROM players WHERE user=?");
		   statement.setString(1, this.uuid.toString());
		   ResultSet results = statement.executeQuery();
		   results.next();
		   return results.getInt("acceptedStaff");
	   } catch (SQLException e) {
    	  return 0;
	   }
   }
   
	public int getMonthlySentAccepted() {
	   try {
		   	PreparedStatement statement = Database.getConnection().prepareStatement("SELECT * FROM players WHERE user=?");
		   	statement.setString(1, this.uuid.toString());
		   	ResultSet results = statement.executeQuery();
		   	results.next();
		   	return results.getInt("acceptedSentMont");
	   } catch (SQLException e) {
	       return 0;
	   }
	}
   
	public int getSentAccepted() {
		try {
			PreparedStatement statement = Database.getConnection().prepareStatement("SELECT * FROM players WHERE user=?");
			statement.setString(1, this.uuid.toString());
			ResultSet results = statement.executeQuery();
			results.next();
			return results.getInt("acceptedSent");
	     } catch (SQLException e) {
	    	 return 0;
	     }
	}
   
   public int getMonthlyStaffDenied() {
	   
	     try {
	       PreparedStatement statement = Database.getConnection().prepareStatement("SELECT * FROM players WHERE user=?");
	       statement.setString(1, this.uuid.toString());
	       ResultSet results = statement.executeQuery();
	       results.next();
	       return results.getInt("deniedStaffMont");
	     } catch (SQLException e) {
	       return 0;
	     }
   }
   
   public int getStaffDenied() {
	   
	     try {
	       PreparedStatement statement = Database.getConnection().prepareStatement("SELECT * FROM players WHERE user=?");
	       statement.setString(1, this.uuid.toString());
	       ResultSet results = statement.executeQuery();
	       results.next();
	       return results.getInt("deniedStaff");
	     } catch (SQLException e) {
	       return 0;
	     }
   }
   
   public int getMonthlySentDenied() {
	     try {
	       PreparedStatement statement = Database.getConnection().prepareStatement("SELECT * FROM players WHERE user=?");
	       statement.setString(1, this.uuid.toString());
	       ResultSet results = statement.executeQuery();
	       results.next();
	       return results.getInt("deniedSentMont");
	     } catch (SQLException e) {
	       return 0;
	     }
   }
   
   public int getSentDenied() {
	   
	     try {
	       PreparedStatement statement = Database.getConnection().prepareStatement("SELECT * FROM players WHERE user=?");
	       statement.setString(1, this.uuid.toString());
	       ResultSet results = statement.executeQuery();
	       results.next();
	       return results.getInt("deniedSent");
	     } catch (SQLException e) {
	       return 0;
	     }
   }
   
}
