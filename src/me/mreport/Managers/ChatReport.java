package me.mreport.Managers;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import me.mreport.ChatR;
import me.mreport.Database;
import me.mreport.User.User;
import me.mreport.Utils.Utils;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

public class ChatReport {
	
	final long ten = System.currentTimeMillis() - 600000;
	
	private int id;
	private UUID reporter;
	private UUID offender;
	private String reason;
	private String date;
	private String chatID;
	private String handler;
	private Status status;
	private String time;
	
	public ChatReport(UUID reporter, UUID offender, String reason, String date, String chatID) {
		this.reporter = reporter;
		this.offender = offender;
		this.reason = reason;
		this.date = date;
		this.chatID = chatID;
	}
	
	public ChatReport(int id, UUID reporter, UUID offender, String reason, String date, String chatID) {
		this.id = id;
		this.reporter = reporter;
		this.offender = offender;
		this.reason = reason;
		this.date = date;
		this.chatID = chatID;
	}
	
	public ChatReport(int id, UUID reporter, UUID offender, String reason, String date, String chatID, String handler, Status status, String time) {
		this.id = id;
		this.reporter = reporter;
		this.offender = offender;
		this.reason = reason;
		this.date = date;
		this.chatID = chatID;
		this.handler = handler;
		this.status = status;
		this.time = time;
	}
	
	public ChatReport() {
	}
	
	public void execute() {
		Player p = Bukkit.getPlayer(reporter);
		Player to = Bukkit.getPlayer(offender);
		
		Bukkit.getScheduler().runTaskAsynchronously(ChatR.getInstance(),() -> {
			try {     				
				PreparedStatement statement = Database.getConnection().prepareStatement("INSERT INTO reports(offender, reporter, reason, date, chatID, staff, status, time) VALUES (?,?,?,?,?,?,?,?)");
				statement.setString(1, String.valueOf(offender));
				statement.setString(2, String.valueOf(reporter));
				statement.setString(3, reason);
				statement.setString(4, date);
				statement.setString(5, chatID);
				statement.setString(6, null);
				statement.setString(7, Status.OPEN.toString());
				statement.setString(8, null);
				statement.executeUpdate();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		});
		
		Bukkit.getScheduler().runTaskAsynchronously(ChatR.getInstance(), () -> {
			try {     
				PreparedStatement statement = Database.getConnection().prepareStatement("SELECT * FROM chatLog WHERE player = ? AND time <= ? AND time >= ?");
				statement.setString(1, to.getUniqueId().toString());
		    	statement.setLong(2, System.currentTimeMillis());
				statement.setLong(3, ten);
				ResultSet rs = statement.executeQuery();
				while (rs.next()) {
						try {     
							PreparedStatement statement2 = Database.getConnection().prepareStatement("INSERT INTO reportLog(server, player, message, date, time, type, handle, reportid) VALUES (?,?,?,?,?,?,?,?)");
							statement2.setString(1, rs.getString("server"));
							statement2.setString(2, to.getUniqueId().toString()); 
							statement2.setString(3, rs.getString("message"));
							statement2.setString(4, rs.getString("date"));
							statement2.setLong(5, rs.getLong("time"));
							statement2.setString(6, rs.getString("type"));
							statement2.setInt(7, 0);
							statement2.setString(8, chatID);
							statement2.executeUpdate();
						} catch (SQLException e) {
							e.printStackTrace();
						}
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		});
	   
		p.sendMessage(Utils.translate(Utils.reportSent(to.getName())));
	}
	
	public void listReports(UUID handler, int page, List<ChatReport> repo) {
		Player handle = Bukkit.getPlayer(handler);
		ArrayList<TextComponent> rp = new ArrayList<TextComponent>();
		
		if (page <= 0) {
        	handle.sendMessage(Utils.translate("&cPlease enter a valid page number!"));
			return;
		}
		
		for (ChatReport report : repo) {
			OfflinePlayer offender = Bukkit.getOfflinePlayer(report.getOffender());
			OfflinePlayer reporter = Bukkit.getOfflinePlayer(report.getReporter());
			
			TextComponent message = new TextComponent("[Accept] ");
			message.setColor(ChatColor.GREEN);
			message.setClickEvent( new ClickEvent( ClickEvent.Action.RUN_COMMAND, "/reports close ACCEPTED " + report.getID()) );
				
			TextComponent message2 = new TextComponent("[Deny] ");
		    message2.setColor(ChatColor.RED);
	   	    message2.setClickEvent( new ClickEvent( ClickEvent.Action.RUN_COMMAND, "/reports close DENIED " + report.getID()) );
			    
		    TextComponent message3 = new TextComponent("---> ");
		    message3.setColor(ChatColor.WHITE);
		    message3.setBold(true);
			    
		    TextComponent chat = new TextComponent("[Click to View] ");
		    chat.setColor(ChatColor.AQUA);
		    chat.setClickEvent( new ClickEvent( ClickEvent.Action.OPEN_URL, ChatR.getInstance().getConfig().getString("ChatReport-Link") + report.getChatID() ) );
		    chat.setHoverEvent( new HoverEvent( HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(Utils.translate(
		    		"&6Reported By: &e" + reporter.getName() + 
		    		"\n&6Reason: &e" + report.getReason() + 
		    		"\n&6ID: &e" + report.getID() 
		    		)).create()));
			    
		    TextComponent name = new TextComponent(offender.getName());
		    name.setColor(ChatColor.YELLOW);
		    name.setBold(true);
			    
		    message.addExtra(message2);
		    message.addExtra(message3);
		    message.addExtra(chat);
		    message.addExtra(name);
		    rp.add(message);
		} 
			
		if (rp.isEmpty()) {
			handle.sendMessage(" ");
        	String header = Utils.translate("&d&lChat Reports &a&l(&e&lPage 0 &e&lof 0&a&l)");
        	handle.sendMessage(header);
    		handle.sendMessage(" ");
		} else {
			Utils.paginate(handle, rp, page, rp.size());
		}
	}
	
	public void searchReports(UUID handler, int page, List<ChatReport> repo) {
		Player handle = Bukkit.getPlayer(handler);
		ArrayList<TextComponent> rp = new ArrayList<TextComponent>();
		
		if (page <= 0) {
        	handle.sendMessage(Utils.translate("&cPlease enter a valid page number!"));
			return;
		}
		
		for (ChatReport report : repo) {
			OfflinePlayer offender = Bukkit.getOfflinePlayer(report.getOffender());
			OfflinePlayer reporter = Bukkit.getOfflinePlayer(report.getReporter());
			OfflinePlayer staff = Bukkit.getOfflinePlayer(UUID.fromString(report.getHandler()));
		    
		    TextComponent message = new TextComponent(Utils.translate("&c" + offender.getName() + " &eBy &a" + reporter.getName()));
		    
		    TextComponent message2 = new TextComponent(" ---> ");
		    message2.setColor(ChatColor.WHITE);
		    message2.setBold(true);
			    
		    TextComponent chat = new TextComponent("[Click to View] ");
		    chat.setColor(ChatColor.AQUA);
		    chat.setClickEvent( new ClickEvent( ClickEvent.Action.OPEN_URL, ChatR.getInstance().getConfig().getString("ChatReport-Link") + report.getChatID() ) );
		    chat.setHoverEvent( new HoverEvent( HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(Utils.translate(
		    		"&6Reason: &e" + report.getReason() + 
		    		"\n&6Status: &e" + report.getStatus() + 
		    		"\n&6Staff: &e" + staff.getName() +
		    		"\n&6Time: &e" + report.getTime() + 
		    		"\n&6ID: &e" + report.getID()
		    		)).create()));
			    
		    message.addExtra(message2);
		    message.addExtra(chat);
		    rp.add(message);
		} 
			
		if (rp.isEmpty()) {
			handle.sendMessage(" ");
        	String header = Utils.translate("&d&lSearch Results &a&l(&e&lPage 0 &e&lof 0&a&l)");
        	handle.sendMessage(header);
    		handle.sendMessage(" ");
		} else {
			Utils.paginateSearch(handle, rp, page, rp.size());
		}
	}
	
	public void closeReport(int reportID, ChatReport report, UUID handler, String result) {
		Player handle = Bukkit.getPlayer(handler);
		if (report == null) {
			handle.sendMessage(Utils.translate("&cError: Invalid Report!"));
			return;
		}
		
		Date d = new Date();
		SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		Player reporter = Bukkit.getPlayer(report.getReporter());
		OfflinePlayer reporterOff = Bukkit.getOfflinePlayer(report.getReporter());
		OfflinePlayer offender = Bukkit.getOfflinePlayer(report.getOffender());
		
		User sender = new User(reporterOff.getUniqueId());
		User staffer = new User(handle.getUniqueId());
		
		handle.sendMessage(Utils.translate("&cYou have removed report ID: " + reportID));
	    
		String reportMessage;
	    if (result.equalsIgnoreCase("ACCEPTED")) {
	    	reportMessage = Utils.reportAccepted(offender.getName());
			Bukkit.getScheduler().runTaskAsynchronously(ChatR.getInstance(), () -> {
				try {   
					PreparedStatement statement = Database.getConnection().prepareStatement("UPDATE reports SET staff = ?, status = ?, time = ? WHERE ID = ?");
					statement.setString(1, handler.toString());
					statement.setString(2, Status.ACCEPTED.toString());
					statement.setString(3, date.format(d));
					statement.setInt(4, reportID);
					statement.executeUpdate();
				} catch (SQLException e) {
					e.printStackTrace();
				} 
			});
	    } else {
	    	reportMessage = Utils.reportDenied(offender.getName());
			Bukkit.getScheduler().runTaskAsynchronously(ChatR.getInstance(), () -> {
				try {   
					PreparedStatement statement = Database.getConnection().prepareStatement("UPDATE reports SET staff = ?, status = ?, time = ? WHERE ID = ?");
					statement.setString(1, handler.toString());
					statement.setString(2, Status.DENIED.toString());
					statement.setString(3, date.format(d));
					statement.setInt(4, reportID);
					statement.executeUpdate();
				} catch (SQLException e) {
					e.printStackTrace();
				} 
			});
	    }
		
	    if (reporter != null) {
	    	reporter.sendMessage(Utils.translate(reportMessage));
	    } else {
	    	ChatR.globalMessage(reporterOff.getName(), Utils.translate(reportMessage));
	    }
	    
	    if (result.equalsIgnoreCase("ACCEPTED")) {
		    staffer.setStaffAccepted(staffer.getStaffAccepted() + 1);
		    staffer.setMonthlyStaffAccepted(staffer.getMonthlyStaffAccepted() + 1);
		    
		    sender.setSentAccepted(sender.getSentAccepted() + 1);
		    sender.setMonthlySentAccepted(sender.getMonthlySentAccepted() + 1);
	    } else {
		    staffer.setStaffDenied(staffer.getStaffDenied() + 1);
		    staffer.setMonthlyStaffDenied(staffer.getMonthlyStaffDenied() + 1);
		    
		    sender.setSentDenied(sender.getSentDenied() + 1);
		    sender.setMonthlySentDenied(sender.getMonthlySentDenied() + 1);
	    }
	}
	
	public List<ChatReport> getReports() {
		List<ChatReport> reports = new ArrayList<ChatReport>();
	    try {     				
	    	PreparedStatement statement = Database.getConnection().prepareStatement("SELECT * FROM reports WHERE status = ? ORDER BY id DESC");
	    	statement.setString(1, Status.OPEN.toString());
	    	ResultSet rs = statement.executeQuery();
	    	while (rs.next()) {
	    		reports.add(new ChatReport(rs.getInt("ID"), UUID.fromString(rs.getString("reporter")), UUID.fromString(rs.getString("offender")), rs.getString("reason"), rs.getString("date"), rs.getString("chatID")));
	    	}
	    } catch (SQLException e) {
	    	e.printStackTrace();
	    }
	    return reports;
	}
	
	public List<ChatReport> getReports(UUID uuid) {
		List<ChatReport> reports = new ArrayList<ChatReport>();
	    try {     				
	    	PreparedStatement statement = Database.getConnection().prepareStatement("SELECT * FROM reports WHERE NOT status = ? AND offender = ? ORDER BY id DESC");
	    	statement.setString(1, Status.OPEN.toString());
	    	statement.setString(2, uuid.toString());
	    	ResultSet rs = statement.executeQuery();
	    	while (rs.next()) {
	    		reports.add(new ChatReport(rs.getInt("ID"), UUID.fromString(rs.getString("reporter")), UUID.fromString(rs.getString("offender")), rs.getString("reason"), rs.getString("date"), rs.getString("chatID"), rs.getString("staff"), Status.valueOf(rs.getString("status")), rs.getString("time")));
	    	}
	    } catch (SQLException e) {
	    	e.printStackTrace();
	    }
	    return reports;
	}
	
	public ChatReport getReport(int ID) {
		ChatReport r = null;
		try {
			PreparedStatement ps = Database.getConnection().prepareStatement("SELECT * FROM reports WHERE ID = ?");
			ps.setInt(1, ID);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				r = new ChatReport(rs.getInt("ID"), UUID.fromString(rs.getString("reporter")), UUID.fromString(rs.getString("offender")), rs.getString("reason"), rs.getString("date"), rs.getString("chatID"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return r;
	}
	
	public boolean hasReported() {
		try {
			PreparedStatement ps = Database.getConnection().prepareStatement("SELECT * FROM reports WHERE offender = ? AND status = ?");
			ps.setString(1, offender.toString());
	    	ps.setString(2, Status.OPEN.toString());
			ResultSet rs = ps.executeQuery();
			return rs.next();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public void clearReports() {
		Bukkit.getScheduler().runTaskAsynchronously(ChatR.getInstance(), () -> {
			try {   
				PreparedStatement statement = Database.getConnection().prepareStatement("UPDATE players SET acceptedStaffMont = 0, deniedStaffMont = 0, acceptedSentMont = 0, deniedSentMont = 0");
				statement.executeUpdate();	   
			} catch (SQLException e) {
				e.printStackTrace();
			}
		});
	}
	
	public void resetMsg() {
	    try {     
	    	PreparedStatement statement = Database.getConnection().prepareStatement("DELETE FROM chatLog");
		    statement.executeUpdate();
	    } catch (SQLException e) {
	    	e.printStackTrace();
	    }
	}
	
	@Override
	public String toString() {
		return "Report(id=" + getID() + ", reporter=" + getReporter() + ", offender=" + getOffender() + ", reason=" + getReason() + ", date=" + getDate() + ")";
	}

	public int getID() {
		return id;
	}
	
	public UUID getReporter() {
		return reporter;
	}

	public UUID getOffender() {
		return offender;
	}

	public String getReason() {
		return reason;
	}
	
	public String getDate() {
		return date;
	}
	
	public String getChatID() {
		return chatID;
	}
	
	public String getHandler() {
		return handler;
	}
	
	public Status getStatus() {
		return status;
	}
	
	public String getTime() {
		return time;
	}

 }