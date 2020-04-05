package me.mreport.User;

import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class UserManager implements Listener {
	
	@EventHandler(ignoreCancelled=true, priority=EventPriority.NORMAL)
	public void onPlayerJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
	    UUID uuid = player.getUniqueId();
	    User profile = new User(uuid, player.getName());
	    if (!profile.isCreated()) {
	    	profile.createStaff();
	    }
	    
	    if (!player.getName().equalsIgnoreCase(profile.getName())) {
	    	profile.updateName(player.getName());
	    }
	}

}
