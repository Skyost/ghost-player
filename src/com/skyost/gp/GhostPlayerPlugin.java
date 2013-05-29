package com.skyost.gp;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class GhostPlayerPlugin extends JavaPlugin {
	public GhostFactory ghostFactory;
	
	public void onEnable() {
		this.ghostFactory = new GhostFactory((Plugin) this);
		ghostFactory.create();
	}
	
	   public boolean onCommand(final CommandSender sender, Command cmd, String label, String[] args){
	        Player player = null;
	 
	        if (sender instanceof Player) {
	            player = (Player) sender;
	        }
	       
	        if(cmd.getName().equalsIgnoreCase("ghost")) {
	        	if (sender instanceof Player) {
                        if(player.hasPermission("ghostplayer.player.beghost")) {
                        	ghostFactory.addGhost(player);
                        	sender.sendMessage("You are a ghost now !");
                        }
                        else {
                        	sender.sendMessage(ChatColor.RED + "You don't have permision to do this !");
                        }
	        	}
	        	else {
	        		sender.sendMessage(ChatColor.RED + "[GhostPlayer] You can't do this from the console !");
	        	}
	        }
	        
	        if(cmd.getName().equalsIgnoreCase("clearsghost")) {
	        	if (sender instanceof Player) {
                        if(player.hasPermission("ghostplayer.admin.clearsghosts")) {
                        	ghostFactory.clearGhosts();
                        	sender.sendMessage("All ghosts cleared !");
                        }
                        else {
                        	sender.sendMessage(ChatColor.RED + "You don't have permision to do this !");
                        }
	        	}
	        	else {
                	ghostFactory.clearGhosts();
                	sender.sendMessage("[GhostPlayer] All ghosts cleared !");
	        	}
	        }
	        
	        if(cmd.getName().equalsIgnoreCase("human")) {
	        	if (sender instanceof Player) {
	        		if(player.hasPermission("ghostplayer.player.behuman")) {
	        			ghostFactory.removeGhost(player);
	        			sender.sendMessage("You are an human now !");
	        		}
	        		else {
	        			sender.sendMessage(ChatColor.RED + "You don't have permision to do this !");
	        		}
	        	}
	        	else {
	        		sender.sendMessage(ChatColor.RED + "[GhostPlayer] You can't do this from the console !");
	        	}
	        }
	        return false;
	    }

}
