package me.mreport.Command;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.mreport.ChatR;
import me.mreport.Managers.ChatReport;
import me.mreport.Managers.Messages;
import me.mreport.Utils.Cooldowns;
import me.mreport.Utils.Utils;

public class ChatReportCommand implements CommandExecutor {

	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		 if (!(sender instanceof Player)) {
		      sender.sendMessage(ChatColor.RED + "Players can only use this command.");
		      return false;
		 }
		    
		  Player player = (Player)sender;
		  
		  if (!player.hasPermission("Report.Use")) {
		      player.sendMessage(Utils.translate("&cYou don't have permission to perform this action."));
		      return false;
		  }
		  
		  if (args.length < 2) {
		      player.sendMessage(Utils.translate("&cIncorrect Arguments, /chatreport <player> <reason>"));
			  return false;
		  }
		  
         Player target = Bukkit.getPlayer(args[0]);
         
         if (target == null) {
		     player.sendMessage(Utils.translate("&cPlayer not online!"));
			 return false; 
         }
         
         if (target.getName().equalsIgnoreCase(player.getName())) {
		     player.sendMessage(Utils.translate("&cYou are unable to chat report yourself!"));
			 return false; 
         }
      	
      	 String reason = "";
    	 for (int i = 1; i < args.length; i++) {
    	      reason = reason + args[i] + " ";
    	 }
    	 final String res = reason.trim();
    	 
         if (Cooldowns.isOnCooldown("report_delay", player)) { 
         	player.sendMessage(Utils.translate(Utils.cooldown()));
            return false;
         }
    	 
		Date d = new Date();
		SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
 	    long tenMinutesAgo = System.currentTimeMillis() - 600000;
         
        int messages = Messages.checkMessage(target.getUniqueId(), tenMinutesAgo);
        String reportid = UUID.randomUUID().toString().replace("-", "");
       
        Bukkit.getScheduler().runTaskAsynchronously(ChatR.getInstance(), () -> {
	        ChatReport r = new ChatReport(player.getUniqueId(), target.getUniqueId(), res, date.format(d), reportid);
	        
	        if (r.hasReported()) {
			    player.sendMessage(Utils.translate(Utils.pastTen()));
	        	return;
	        }
	        
		    if (messages >= 1) { 
			    r.execute();
		        Cooldowns.addCooldown("report_delay", player, 60);
		    } else {
		    	player.sendMessage(Utils.translate(Utils.notType()));
		    }
        });
		  
		return false;
	}
}