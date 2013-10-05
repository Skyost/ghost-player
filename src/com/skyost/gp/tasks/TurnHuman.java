package com.skyost.gp.tasks;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import com.skyost.gp.GhostPlayer;

public class TurnHuman extends BukkitRunnable { 	 
    private final String playername;
    private final Boolean isSilent;
    
    public TurnHuman(String playername, Boolean isSilent) {
        this.playername = playername;
        this.isSilent = isSilent;
    }
 
    public void run() {
    	Player player = Bukkit.getPlayer(playername);
    	if(player != null && player.isOnline()) {
	    	if(isSilent == true) {
	    		GhostPlayer.ghostFactory.setGhost(player, false);
	    		GhostPlayer.ghostFactory.removePlayer(player);
	    	}
	    	else {
	    		GhostPlayer.ghostFactory.setGhost(player, false);
	    		GhostPlayer.ghostFactory.removePlayer(player);
	    		player.sendMessage(GhostPlayer.messages.Message_11); // You are an human now !
	    	}
    	}
    }

}
