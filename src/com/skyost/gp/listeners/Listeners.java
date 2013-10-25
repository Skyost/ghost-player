package com.skyost.gp.listeners;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import com.skyost.gp.GhostPlayer;
import com.skyost.gp.tasks.TurnHuman;

public class Listeners implements Listener {
	
	@EventHandler
    private static final void onPlayerQuit(PlayerQuitEvent event) {
    	if(GhostPlayer.ghostFactory.isGhost(event.getPlayer())) {
    		GhostPlayer.ghostFactory.setGhost(event.getPlayer(), false);
    	}
    	if(GhostPlayer.ghostFactory.hasPlayer(event.getPlayer())) {
    		GhostPlayer.ghostFactory.removePlayer(event.getPlayer());
    	}
    }
    
    @EventHandler
    private static final void onPlayerDeath(PlayerDeathEvent event) {
    	Player entity = event.getEntity();
    	if(GhostPlayer.config.HumanWorlds.contains(entity.getWorld().getName())) {
	    	if(GhostPlayer.ghostFactory.hasPlayer(entity)) {
	    		GhostPlayer.ghostFactory.addPlayer(event.getEntity());
	    	   	if(GhostPlayer.config.TurnIntoGhostOnDeath) {
	    	   		GhostPlayer.ghostFactory.setGhost(event.getEntity(), true);
	    	   	}
	    	   	else {
	    	    	GhostPlayer.ghostFactory.setGhost(event.getEntity(), false);
	    	    }
	    	}
    	}
    }
    
    @EventHandler
    private static final void onPlayerInteract(PlayerInteractEvent event) {
    	Player player = event.getPlayer();
    	if(GhostPlayer.config.HumanWorlds.contains(player.getWorld().getName())) {
	    	if(!GhostPlayer.config.GhostsCanInteract) {
		    	if(GhostPlayer.ghostFactory.isGhost(player)) {
		    		player.sendMessage(ChatColor.RED + GhostPlayer.messages.Message_31);
		    		event.setCancelled(true);
		    	}
	    	}
    	}
    }
    
	@EventHandler
    private static final void onPlayerJoin(PlayerJoinEvent event) {
    	Player player = event.getPlayer();
    	if(GhostPlayer.config.HumanWorlds.contains(player.getWorld().getName())) {
    		if(GhostPlayer.config.TurnedIntoOnJoin.equalsIgnoreCase("SILENT HUMAN")) {
    			if(player.hasPermission("ghostplayer.player.behuman")) {
    				if(GhostPlayer.ghostFactory.isGhost(player)) {
        				GhostPlayer.ghostFactory.setGhost(player, false);
        				GhostPlayer.ghostFactory.removePlayer(player);
    				}
    			}
    		}
    		else if(GhostPlayer.config.TurnedIntoOnJoin.equalsIgnoreCase("HUMAN")) {
    			if(player.hasPermission("ghostplayer.player.behuman")) {
    				if(GhostPlayer.ghostFactory.isGhost(player)) {
        				GhostPlayer.ghostFactory.setGhost(player, false);
        				GhostPlayer.ghostFactory.removePlayer(player);
    				}
    				player.sendMessage(GhostPlayer.messages.Message_11); // You are an human now !
    			}
    		}
    		else if(GhostPlayer.config.TurnedIntoOnJoin.equalsIgnoreCase("SILENT GHOST")) {
    			if(player.hasPermission("ghostplayer.player.beghost")) {
	    			if(!GhostPlayer.ghostFactory.isGhost(player)) {
	        			if(!(GhostPlayer.config.GhostTime.equals(-1))) {
	        				try {
                        		GhostPlayer.ghostFactory.setGhost(player, true);
                        		GhostPlayer.ghostFactory.addPlayer(player);
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
			}
    		else if(GhostPlayer.config.TurnedIntoOnJoin.equalsIgnoreCase("GHOST")) {
    			if(player.hasPermission("ghostplayer.player.beghost")) {
	    			if(!GhostPlayer.ghostFactory.isGhost(player)) {
	        			try {
                			GhostPlayer.ghostFactory.setGhost(player, true);
                			GhostPlayer.ghostFactory.addPlayer(player);
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
			}
    		else if(GhostPlayer.config.TurnedIntoOnJoin.equalsIgnoreCase("SILENT GHOST HUNTER") || GhostPlayer.config.TurnedIntoOnJoin.equalsIgnoreCase("SILENT GHOST-HUNTER") || GhostPlayer.config.TurnedIntoOnJoin.equalsIgnoreCase("SILENT GHOSTHUNTER")) {
    			if(player.hasPermission("ghostplayer.player.beghosthunter")) {
                	if(!GhostPlayer.ghostFactory.isGhost(player) || !GhostPlayer.ghostFactory.hasPlayer(player)) {
                		GhostPlayer.ghostFactory.addPlayer(player);
                	}
                }
			}
    		else if(GhostPlayer.config.TurnedIntoOnJoin.equalsIgnoreCase("GHOST HUNTER") || GhostPlayer.config.TurnedIntoOnJoin.equalsIgnoreCase("GHOST-HUNTER") || GhostPlayer.config.TurnedIntoOnJoin.equalsIgnoreCase("GHOSTHUNTER")) {
    			if(player.hasPermission("ghostplayer.player.beghosthunter")) {
                	if(!GhostPlayer.ghostFactory.isGhost(player) || !GhostPlayer.ghostFactory.hasPlayer(player)) {
                		GhostPlayer.ghostFactory.addPlayer(player);
                		player.sendMessage(GhostPlayer.messages.Message_20); // You are a ghost hunter now !
                	}
                }
			}
    		else {
    			try {
	    			player.sendMessage(ChatColor.RED + GhostPlayer.messages.Message_26); // Invalid data in config for 'TurnedIntoOnJoin'. It will be set to 'HUMAN'.
	    			GhostPlayer.config.TurnedIntoOnJoin = "HUMAN";
	    			GhostPlayer.config.save();
	    			if(player.hasPermission("ghostplayer.player.behuman")) {
	    				if(GhostPlayer.ghostFactory.isGhost(player)) {
	        				GhostPlayer.ghostFactory.setGhost(player, false);
	        				GhostPlayer.ghostFactory.removePlayer(player);
	        				player.sendMessage(GhostPlayer.messages.Message_11); // You are an human now !
	    				}
	    			}
    			}
    			catch(Exception ex) {
    				ex.printStackTrace();
    			}
    		}
    	}
    }

}
