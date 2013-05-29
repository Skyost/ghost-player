package com.skyost.gp;

import java.io.IOException;
import java.io.PrintStream;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class GhostPlayerPlugin extends JavaPlugin {
	public GhostFactory ghostFactory;
	public GhostPlayerConfig config;
	public static boolean autoUpdate;
	
	public void onEnable() {
		this.ghostFactory = new GhostFactory((Plugin) this);
		loadConfig();
		update();
		startMetricsLite();
	}
	
	public void loadConfig() {
		try {
			System.setOut(new PrintStream(System.out, true, "cp850"));
			config = new GhostPlayerConfig(this);
			config.init();
			autoUpdate = config.AutoUpdateOnLoad;
			}
		catch(Exception ex) {
			getLogger().log(Level.SEVERE, "[Magic Explosion] " + ex);
			getServer().getPluginManager().disablePlugin(this);
        return;
		}
	}
	
	public void startMetricsLite() {
		try {
				MetricsLite metrics = new MetricsLite(this);
				metrics.start();
			} 
		catch (IOException ex) {
			getLogger().log(Level.SEVERE, "[GhostPlayer] " + ex);
		}
	}
	
	public void update() {
		if(autoUpdate == true) {
			try {
				Updater updater = new Updater(this, "ghost-player", this.getFile(), Updater.UpdateType.DEFAULT, false);
				Updater.UpdateResult result = updater.getResult();
	        		switch(result) {
	            		case SUCCESS:
	            			System.out.println("[GhostPlayer] Update found: The updater found an update, and has readied it to be loaded the next time the server restarts/reloads.");
	            			break;
	            		case NO_UPDATE:
	            			System.out.println("[GhostPlayer] No Update: The updater did not find an update, and nothing was downloaded.");
	            			break;
	            		case FAIL_DOWNLOAD:
	            			System.out.println("[GhostPlayer] Download Failed: The updater found an update, but was unable to download it.");
	            			break;
	            		case FAIL_DBO:
	            			System.out.println("[GhostPlayer] dev.bukkit.org Failed: For some reason, the updater was unable to contact DBO to download the file.");
	            			break;
	            		case FAIL_NOVERSION:
	            			System.out.println("[GhostPlayer] No version found: When running the version check, the file on DBO did not contain the a version in the format 'vVersion' such as 'v1.0'.");
	            			break;
	            		case FAIL_BADSLUG:
	            			System.out.println("[GhostPlayer] Bad slug: The slug provided by the plugin running the updater was invalid and doesn't exist on DBO.");
	            			break;
	            		case UPDATE_AVAILABLE:
	            			System.out.println("[GhostPlayer] Update found: There was an update found but not be downloaded !");
	            			break;
	        		}
				}	
			catch (Exception ex) {
				getLogger().log(Level.SEVERE, "[GhostPlayer] " + ex);
			}
		}
		else {
			doNothing();
		}
	}
	
	public void doNothing() {
		
	}
	
	   public boolean onCommand(final CommandSender sender, Command cmd, String label, String[] args){
	        Player player = null;
	 
	        if (sender instanceof Player) {
	            player = (Player) sender;
	        }
	       
	        if(cmd.getName().equalsIgnoreCase("ghost")) {
	        	if (sender instanceof Player) {
                        if(player.hasPermission("ghostplayer.player.beghost")) {
                        	ghostFactory.setGhost(player, true);
                        	ghostFactory.addPlayer(player);
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
	        
	        if(cmd.getName().equalsIgnoreCase("removeghost") && args.length == 1) {
	        	if (sender instanceof Player) {
                        if(player.hasPermission("ghostplayer.admin.removeghost")) {
                        	try {
                        			ghostFactory.setGhost(Bukkit.getPlayer(args[0]), false);
                        			ghostFactory.removePlayer(Bukkit.getPlayer(args[0]));
                        			sender.sendMessage(Bukkit.getPlayer(args[0]).getName() + " has been removed from the ghosts !");
                        	}
        	        		catch(NullPointerException e) {
        	        			sender.sendMessage(ChatColor.RED + Bukkit.getPlayer(args[0]).getName() + " does not exist !");
        	        		}
                        }
                        else {
                        	sender.sendMessage(ChatColor.RED + "You don't have permision to do this !");
                        }
	        	}
	        	else {
	        		try {
                    	ghostFactory.setGhost(Bukkit.getPlayer(args[0]), false);
	        			ghostFactory.removePlayer(Bukkit.getPlayer(args[0]));
	        			sender.sendMessage("[GhostPlayer] " + Bukkit.getPlayer(args[0]).getName() + " has been removed from the ghosts !");
	        		}
	        		catch(NullPointerException e) {
	        			sender.sendMessage(ChatColor.RED + "[GhostPlayer] " + Bukkit.getPlayer(args[0]).getName() + " does not exist !");
	        		}
	        	}
	        }
	        
	        if(cmd.getName().equalsIgnoreCase("human")) {
	        	if (sender instanceof Player) {
	        		if(player.hasPermission("ghostplayer.player.behuman")) {
	        			ghostFactory.setGhost(player, false);
	        			ghostFactory.removePlayer(player);
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
	        
	        if(cmd.getName().equalsIgnoreCase("clearsghosts")) {
	        	if (sender instanceof Player) {
	        		if(player.hasPermission("ghostplayer.admin.clearsghosts")) {
	        			ghostFactory.clearMembers();
	        			sender.sendMessage("All ghosts have been cleared !");
	        		}
	        		else {
	        			sender.sendMessage(ChatColor.RED + "You don't have permision to do this !");
	        		}
	        	}
	        	else {
        			ghostFactory.clearMembers();
        			sender.sendMessage("[GhostPlayer] All ghosts have been cleared !");
	        	}
	        }
	        
	        return false;
	    }

}
