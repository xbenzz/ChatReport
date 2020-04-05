package me.mreport;

import java.util.Date;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.google.common.collect.Iterables;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;

import me.mreport.Command.ChatReportCommand;
import me.mreport.Command.ReportsCommand;
import me.mreport.Managers.ChatReport;
import me.mreport.User.UserManager;
import me.mreport.Utils.ChatListener;
import me.mreport.Utils.Cooldowns;
import me.mreport.Utils.Utils;

public class ChatR extends JavaPlugin {
	
	private static ChatR instance;
	   
	public void onEnable() {
	    getLogger().info("ChatReport by xBenz has been enabled!");
	    load();   
	    Bukkit.getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
	}
	   
	public void onDisable() {
		if (getConfig().getBoolean("Reset-Logs")) {
			new ChatReport().resetMsg();
		}
	    getLogger().info("ChatReport by xBenz has been disabled!");
	    Database.closeConnection();
	}
	   
	private void load() {
	    instance = this;
	    loadConfig();
	    Database.openConnection();
	    Database.createTables();
	    registerCommands();
	    registerEvents();
	    registerCooldowns();
	    runMonthlyCheck();
	}
	   
	public static void globalMessage(String player, String message) {
	      ByteArrayDataOutput out = ByteStreams.newDataOutput();
	      out.writeUTF("Message");
	      out.writeUTF(player);
	      out.writeUTF(message);
	        
	      Player p = Iterables.getFirst(Bukkit.getOnlinePlayers(), null);
	      p.sendPluginMessage(instance, "BungeeCord", out.toByteArray());
	}

	    
	private void registerCommands() {
	     getCommand("chatreport").setExecutor(new ChatReportCommand());
	     getCommand("reports").setExecutor(new ReportsCommand());
	}
	   
	private void registerEvents() {
	    PluginManager manager = Bukkit.getPluginManager();
	    manager.registerEvents(new ChatListener(), this);
	    manager.registerEvents(new UserManager(), this);
	}
	
	private void registerCooldowns() {
		Cooldowns.createCooldown("report_delay");
	}
	   
	private void loadConfig() {
		FileConfiguration cfg = getConfig();
		cfg.options().copyDefaults(true);
		saveDefaultConfig();
	}
	  
	public static ChatR getInstance() {
		 return instance;
	}
	
	public void runMonthlyCheck() {
		ChatReport r = new ChatReport();
		Bukkit.getScheduler().runTaskAsynchronously(this, () -> {
	        if (Utils.isFirstDayOfTheMonth(new Date())) {
	        	r.clearReports();
	        }
	    });
	}

}
