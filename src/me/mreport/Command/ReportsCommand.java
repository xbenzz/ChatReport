package me.mreport.Command;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.mreport.ChatR;
import me.mreport.Managers.ChatReport;
import me.mreport.User.User;
import me.mreport.Utils.Utils;

public class ReportsCommand implements CommandExecutor {
	
	public boolean isStringInt(String s) {
	    try {
	        Integer.parseInt(s);
	        return true;
	    } catch (NumberFormatException ex) {
	        return false;
	    }
	}

	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (!(sender instanceof Player)) {
			sender.sendMessage(ChatColor.RED + "Players can only use this command.");
		    return false;
		}
		    
		Player player = (Player)sender;
		if (!player.hasPermission("Report.Handle")) {
		    player.sendMessage(Utils.translate("&cYou don't have permission to perform this action."));
		    return false;
		}
		  
		  
		ChatReport r = new ChatReport();
		  
		if (args.length == 0) {
			Bukkit.getScheduler().runTaskAsynchronously(ChatR.getInstance(), () -> {
				  List<ChatReport> repo = r.getReports();
				  r.listReports(player.getUniqueId(), 1, repo);
			});
		  } else if (args[0].equalsIgnoreCase("close")) {  
			  	if (player.hasPermission("Report.Handle")) {
			  		
			  		if (args[1] == null) {
			  			Bukkit.getScheduler().runTaskAsynchronously(ChatR.getInstance(), () -> {
							List<ChatReport> repo = r.getReports();
							r.listReports(player.getUniqueId(), 1, repo);
			  			});
			  			return false;
			  		}
			  		
			  		if (args.length < 3) {
			  			Bukkit.getScheduler().runTaskAsynchronously(ChatR.getInstance(), () -> {
							List<ChatReport> repo = r.getReports();
							r.listReports(player.getUniqueId(), 1, repo);
			  			});
			  			return false;
			  		}
			  		
			  		String status = args[1];
			  		int ID = Integer.valueOf(args[2]);
			  		
		  			Bukkit.getScheduler().runTaskAsynchronously(ChatR.getInstance(), () -> {
						ChatReport s = r.getReport(ID);
				  		r.closeReport(ID, s, player.getUniqueId(), status);
		  			});
			  		return true; 
			  	} 
		  } else if (args[0].equalsIgnoreCase("search")) {
			  	if (player.hasPermission("Report.Search")) {
			  		if (args.length < 2) {
		  				player.sendMessage(Utils.translate("&cIncorrect Arguments, /reports search <user>"));
			  			return false;
			  		}
			  		
			  		OfflinePlayer target = Bukkit.getOfflinePlayer(args[1]);
			  		if (args.length < 3) {
						Bukkit.getScheduler().runTaskAsynchronously(ChatR.getInstance(), () -> {
							List<ChatReport> repo = r.getReports(target.getUniqueId());
							r.searchReports(player.getUniqueId(), 1, repo);
						});
			  			return false;
			  		}
			  		
			  		int page = Integer.valueOf(args[2]);
			  		
					Bukkit.getScheduler().runTaskAsynchronously(ChatR.getInstance(), () -> {
						List<ChatReport> repo = r.getReports(target.getUniqueId());
						r.searchReports(player.getUniqueId(), page, repo);
					});
			  	}
		  } else if (!isStringInt(args[0])) {
			  OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);
			  
	  		  Bukkit.getScheduler().runTaskAsynchronously(ChatR.getInstance(), () -> {
	  			  User user = new User(target.getUniqueId());
			  
	  			  if (!user.isCreated()) {
	  				  player.sendMessage(Utils.translate("&cThat player does not exist!"));
	  				  return;
	  			  }
			  
	  			  int acceptedSt = user.getStaffAccepted();
	  			  int deniedSt = user.getStaffDenied();
			  
				  int acceptedS = user.getSentAccepted();
				  int deniedS = user.getSentDenied();
				  player.sendMessage(" ");
				  player.sendMessage(Utils.translate("&d&l" + target.getName() + " Chat Report Statistics"));
				  player.sendMessage(Utils.translate("&aReports Submitted (Accepted): &b" + acceptedS + " (" + user.getMonthlySentAccepted() + " this month)"));
				  player.sendMessage(Utils.translate("&aReports Submitted (Denied): &b" + deniedS + " (" + user.getMonthlySentDenied() + " this month)"));
				  player.sendMessage(Utils.translate("&eReports Attended (Accepted): &6" + acceptedSt + " (" + user.getMonthlyStaffAccepted() + " this month)"));
				  player.sendMessage(Utils.translate("&eReports Attended (Denied): &6" + deniedSt + " (" + user.getMonthlyStaffDenied() + " this month)"));
				  player.sendMessage(" ");
	  		});
		 } else {
			Bukkit.getScheduler().runTaskAsynchronously(ChatR.getInstance(), () -> {
				List<ChatReport> repo = r.getReports();
				r.listReports(player.getUniqueId(), Integer.valueOf(args[0]), repo);
			});
		 }
		  
		return false;
	}
}