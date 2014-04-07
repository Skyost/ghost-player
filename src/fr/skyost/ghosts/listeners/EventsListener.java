package fr.skyost.ghosts.listeners;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import fr.skyost.ghosts.GhostPlayer;
import fr.skyost.ghosts.tasks.TurnHuman;

public class EventsListener implements Listener {
	
	@EventHandler
    private final void onPlayerQuit(PlayerQuitEvent event) {
    	if(GhostPlayer.ghostManager.isGhost(event.getPlayer())) {
    		GhostPlayer.ghostManager.setGhost(event.getPlayer(), false);
    	}
    	if(GhostPlayer.ghostManager.hasPlayer(event.getPlayer())) {
    		GhostPlayer.ghostManager.removePlayer(event.getPlayer());
    	}
    }
    
    @EventHandler
    private final void onPlayerDeath(PlayerDeathEvent event) {
    	Player entity = event.getEntity();
    	if(!GhostPlayer.config.HumanWorlds.contains(entity.getWorld().getName())) {
	    	if(GhostPlayer.ghostManager.hasPlayer(entity)) {
	    		GhostPlayer.ghostManager.addPlayer(event.getEntity());
	    	   	if(GhostPlayer.config.TurnIntoGhostOnDeath) {
	    	   		GhostPlayer.ghostManager.setGhost(event.getEntity(), true);
	    	   	}
	    	   	else {
	    	    	GhostPlayer.ghostManager.setGhost(event.getEntity(), false);
	    	    }
	    	}
    	}
    }
    
    @EventHandler
    private final void onPlayerInteract(PlayerInteractEvent event) {
    	Player player = event.getPlayer();
    	if(!GhostPlayer.config.HumanWorlds.contains(player.getWorld().getName())) {
	    	if(!GhostPlayer.config.GhostsCanInteract) {
		    	if(GhostPlayer.ghostManager.isGhost(player)) {
		    		player.sendMessage(ChatColor.RED + GhostPlayer.messages.Message_31);
		    		event.setCancelled(true);
		    	}
	    	}
    	}
    }
    
    @EventHandler
    private final void onPlayerDropItem(PlayerDropItemEvent event) {
    	Player player = event.getPlayer();
    	if(!GhostPlayer.config.HumanWorlds.contains(player.getWorld().getName())) {
	    	if(!GhostPlayer.config.GhostsCanInteract) {
		    	if(GhostPlayer.ghostManager.isGhost(player)) {
		    		player.sendMessage(ChatColor.RED + GhostPlayer.messages.Message_31);
		    		event.setCancelled(true);
		    	}
	    	}
    	}
    }
    
    @EventHandler
    private final void onEntityDamage(EntityDamageEvent event) {
    	Entity entity = event.getEntity();
	    if(entity instanceof Player) {
	    	Player player = (Player)entity;
	       	if(!GhostPlayer.config.HumanWorlds.contains(player.getWorld().getName())) {
	    	   	if(!GhostPlayer.config.GhostsCanInteract) {
	    	    	if(GhostPlayer.ghostManager.isGhost(player)) {
	    	    		event.setCancelled(true);
	    		   	}
	    	    }
	        }
    	}
    }
    
	@EventHandler
    private final void onPlayerJoin(PlayerJoinEvent event) {
    	Player player = event.getPlayer();
    	if(!GhostPlayer.config.HumanWorlds.contains(player.getWorld().getName())) {
    		if(player.hasPermission("ghostplayer.login.silenthuman")) {
    			if(GhostPlayer.ghostManager.isGhost(player)) {
    				GhostPlayer.ghostManager.setGhost(player, false);
    				GhostPlayer.ghostManager.removePlayer(player);
				}
    		}
    		else if(player.hasPermission("ghostplayer.login.human")) {
    			if(GhostPlayer.ghostManager.isGhost(player)) {
    				GhostPlayer.ghostManager.setGhost(player, false);
    				GhostPlayer.ghostManager.removePlayer(player);
				}
				player.sendMessage(GhostPlayer.messages.Message_11); // You are an human now !
    		}
    		else if(player.hasPermission("ghostplayer.login.silentghost")) {
    			if(!GhostPlayer.ghostManager.isGhost(player)) {
        			if(!(GhostPlayer.config.GhostTime.equals(-1))) {
        				try {
                    		GhostPlayer.ghostManager.setGhost(player, true);
                    		GhostPlayer.ghostManager.addPlayer(player);
                    		GhostPlayer.totalGhosts++;
                    		if(GhostPlayer.config.GhostTime != -1) {
                    			new TurnHuman(player.getName(), true).runTaskLaterAsynchronously(Bukkit.getPluginManager().getPlugin("Ghost Player"), GhostPlayer.config.GhostTime);
                    		}
            			}
            			catch(Exception e) {
            				try {
            					player.sendMessage(ChatColor.RED + GhostPlayer.messages.Message_17); // Invalid number in config for 'GhostTime'. It will be set to 'FOREVER'.
                				GhostPlayer.config.GhostTime = -1;
								GhostPlayer.config.save();
							}
            				catch(Exception ex) {
								ex.printStackTrace();
							}
            			}
        			}
    			}
    		}
    		else if(player.hasPermission("ghostplayer.login.ghost")) {
    			if(!GhostPlayer.ghostManager.isGhost(player)) {
        			try {
            			GhostPlayer.ghostManager.setGhost(player, true);
            			GhostPlayer.ghostManager.addPlayer(player);
            			player.sendMessage(GhostPlayer.messages.Message_3); // You are a ghost now !
            			GhostPlayer.totalGhosts++;
                    	if(GhostPlayer.config.GhostTime != -1) {
                    		new TurnHuman(player.getName(), false).runTaskLaterAsynchronously(Bukkit.getPluginManager().getPlugin("Ghost Player"), GhostPlayer.config.GhostTime);
                    	}
            		}
            		catch(Exception e) {
            			try {
            				player.sendMessage(ChatColor.RED + GhostPlayer.messages.Message_17); // Invalid number in config for 'GhostTime'. It will be set to 'FOREVER'.
                			GhostPlayer.config.GhostTime = -1;
							GhostPlayer.config.save();
						}
            			catch(Exception ex) {
							ex.printStackTrace();
            			}
            		}
    			}
    		}
    		else if(player.hasPermission("ghostplayer.login.silentghosthunter")) {
    			if(!GhostPlayer.ghostManager.isGhost(player) || !GhostPlayer.ghostManager.hasPlayer(player)) {
            		GhostPlayer.ghostManager.addPlayer(player);
            	}
    		}
    		else if(player.hasPermission("ghostplayer.login.ghosthunter")) {
            	if(!GhostPlayer.ghostManager.isGhost(player) || !GhostPlayer.ghostManager.hasPlayer(player)) {
            		GhostPlayer.ghostManager.addPlayer(player);
            		player.sendMessage(GhostPlayer.messages.Message_20); // You are a ghost hunter now !
            	}
    		}
    	}
    }

}
