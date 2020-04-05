package me.mreport.Utils;

import java.util.Date;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import me.mreport.Managers.Messages;

public class ChatListener implements Listener {
	
	  @EventHandler(priority=EventPriority.NORMAL)
	  public void onAsyncPlayerChat(AsyncPlayerChatEvent e) {
	 	if (e.isCancelled()) {
	 		return;
	    }
	    
	    UUID uuid = e.getPlayer().getUniqueId();
        Date now = new Date();
	    String server = Bukkit.getServerName();
	    Messages.addMessage(server, uuid, e.getMessage(), now.toString(), System.currentTimeMillis(), "CHAT");
	  }
	  
	  @EventHandler(priority=EventPriority.NORMAL)
	  public void onPlayerCommand(PlayerCommandPreprocessEvent e) {
	    if (e.isCancelled()) {
		   return;
		}
	    
	    UUID uuid = e.getPlayer().getUniqueId();    
	    Date now = new Date();
		String server = Bukkit.getServerName();
		Messages.addMessage(server, uuid, e.getMessage(), now.toString(), System.currentTimeMillis(), "CMD");
	 }

}
