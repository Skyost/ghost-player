package com.skyost.gp;

import java.io.IOException;
import java.io.PrintStream;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import com.skyost.gp.Metrics.Graph;

public class GhostPlayerPlugin extends JavaPlugin implements Listener {
	public GhostFactory ghostFactory;
	public GhostPlayerConfig config;
	public static boolean autoUpdate;
	public int totalGhosts;
	
	public void onEnable() {
		this.ghostFactory = new GhostFactory((Plugin) this);
		this.getServer().getPluginManager().registerEvents(this, this);
		loadConfig();
		update();
		startMetrics();
	}
	
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
    	if(ghostFactory.isGhost(event.getPlayer()) == true) {
    		ghostFactory.setGhost(event.getPlayer(), false);
    	}
    	else {
    		doNothing();
    	}
    }
	
	public void loadConfig() {
		try {
			System.setOut(new PrintStream(System.out, true, "cp850"));
			config = new GhostPlayerConfig(this);
			config.init();
			autoUpdate = config.AutoUpdateOnLoad;
			}
		catch(Exception ex) {
			getLogger().log(Level.SEVERE, "[Ghost Player] " + ex);
			getServer().getPluginManager().disablePlugin(this);
        return;
		}
	}
	
	public void startMetrics() {
		try {
		    Metrics metrics = new Metrics(this);
		    metrics.start();
		    Graph ghostsGraph = metrics.createGraph("Default");
		    ghostsGraph.addPlotter(new Metrics.Plotter("Total ghosts") {
		    @Override
		    public int getValue() {
		        return totalGhosts;	
		       }
		    });
		    
    		Graph updateGraph = metrics.createGraph("updateGraph");
    		updateGraph.addPlotter(new Metrics.Plotter("Checking for Updates") {	
    			@Override
    			public int getValue() {	
    				return 1;
    			}
    			
    			@Override
    			public String getColumnName() {
    				if(autoUpdate == true) {
    					return "Yes";
    				} else {
    					return "No";
    				}
    			}
    		});
		} catch (IOException ex) {
			getLogger().log(Level.SEVERE, "[Ghost Player] " + ex);
		}
	}
	
	public void update() {
		if(autoUpdate == true) {
			try {
				Updater updater = new Updater(this, "ghost-player", this.getFile(), Updater.UpdateType.DEFAULT, false);
				Updater.UpdateResult result = updater.getResult();
	        		switch(result) {
	            		case SUCCESS:
	            			System.out.println("[Ghost Player] Update found: The updater found an update, and has readied it to be loaded the next time the server restarts/reloads.");
	            			break;
	            		case NO_UPDATE:
	            			System.out.println("[Ghost Player] No Update: The updater did not find an update, and nothing was downloaded.");
	            			break;
	            		case FAIL_DOWNLOAD:
	            			System.out.println("[Ghost Player] Download Failed: The updater found an update, but was unable to download it.");
	            			break;
	            		case FAIL_DBO:
	            			System.out.println("[Ghost Player] dev.bukkit.org Failed: For some reason, the updater was unable to contact DBO to download the file.");
	            			break;
	            		case FAIL_NOVERSION:
	            			System.out.println("[Ghost Player] No version found: When running the version check, the file on DBO did not contain the a version in the format 'vVersion' such as 'v1.0'.");
	            			break;
	            		case FAIL_BADSLUG:
	            			System.out.println("[Ghost Player] Bad slug: The slug provided by the plugin running the updater was invalid and doesn't exist on DBO.");
	            			break;
	            		case UPDATE_AVAILABLE:
	            			System.out.println("[Ghost Player] Update found: There was an update found but not be downloaded !");
	            			break;
	        		}
				}	
			catch (Exception ex) {
				getLogger().log(Level.SEVERE, "[Ghost Player] " + ex);
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
	        
	        if(cmd.getName().equalsIgnoreCase("ghostview")) {
	        	if (sender instanceof Player) {
                        if(player.hasPermission("ghostplayer.player.ghostview")) {
                        	if(ghostFactory.isGhost(player) == true) {
                        		player.sendBlockChange(player.getTargetBlock(null, 100).getLocation(), Material.AIR, (byte) 0);
                        	}
                        	else {
                        		sender.sendMessage(ChatColor.RED + "An human can't do this !");
                        	}
                        }
                        else {
                        	sender.sendMessage(ChatColor.RED + "You don't have permision to do this !");
                        }
	        	}
	        	else {
	        		sender.sendMessage(ChatColor.RED + "[Ghost Player] You can't do this from the console !");
	        	}
	        }
	        
	        if(cmd.getName().equalsIgnoreCase("silentghost")) {
	        	if (sender instanceof Player) {
                        if(player.hasPermission("ghostplayer.player.beghost")) {
                        	if(ghostFactory.isGhost(player) == true) {
                        		sender.sendMessage(ChatColor.RED + "You are already a ghost !");
                        	}
                        	else {
                        		ghostFactory.setGhost(player, true);
                        		ghostFactory.addPlayer(player);
                        		totalGhosts = totalGhosts + 1;
                        	}
                        }
                        else {
                        	sender.sendMessage(ChatColor.RED + "You don't have permision to do this !");
                        }
	        	}
	        	else {
	        		sender.sendMessage(ChatColor.RED + "[Ghost Player] You can't do this from the console !");
	        	}
	        }
	       
	        if(cmd.getName().equalsIgnoreCase("ghost")) {
	        	if (sender instanceof Player) {
                        if(player.hasPermission("ghostplayer.player.beghost")) {
                        	if(ghostFactory.isGhost(player) == true) {
                        		sender.sendMessage(ChatColor.RED + "You are already a ghost !");
                        	}
                        	else {
                        		ghostFactory.setGhost(player, true);
                        		ghostFactory.addPlayer(player);
                        		sender.sendMessage("You are a ghost now !");
                        		totalGhosts = totalGhosts + 1;
                        	}
                        }
                        else {
                        	sender.sendMessage(ChatColor.RED + "You don't have permision to do this !");
                        }
	        	}
	        	else {
	        		sender.sendMessage(ChatColor.RED + "[Ghost Player] You can't do this from the console !");
	        	}
	        }
	        
	        if(cmd.getName().equalsIgnoreCase("removeghost") && args.length == 1) {
	        	if (sender instanceof Player) {
                        if(player.hasPermission("ghostplayer.admin.removeghost")) {
                        	try {
                        		if(ghostFactory.isGhost(player) == true) {
                        			ghostFactory.setGhost(Bukkit.getPlayer(args[0]), false);
                        			ghostFactory.removePlayer(Bukkit.getPlayer(args[0]));
                        			sender.sendMessage(Bukkit.getPlayer(args[0]).getName() + " has been removed from the ghosts !");
                        		}
                        		else {
                        			sender.sendMessage(ChatColor.RED + Bukkit.getPlayer(args[0]).getName() + " is already an human !");
                        		}
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
	        			if(ghostFactory.isGhost(player) == true) {
                			ghostFactory.setGhost(Bukkit.getPlayer(args[0]), false);
                			ghostFactory.removePlayer(Bukkit.getPlayer(args[0]));
	        				sender.sendMessage("[Ghost Player] " + Bukkit.getPlayer(args[0]).getName() + " has been removed from the ghosts !");
	        			}
	        			else {
	        				sender.sendMessage(ChatColor.RED + "[Ghost Player] " + Bukkit.getPlayer(args[0]).getName() + " is already an human !");
	        			}
	        		}
	        		catch(NullPointerException e) {
	        			sender.sendMessage(ChatColor.RED + "[Ghost Player] " + Bukkit.getPlayer(args[0]).getName() + " does not exist !");
	        		}
	        	}
	        }
	        
	        if(cmd.getName().equalsIgnoreCase("silenthuman")) {
	        	if (sender instanceof Player) {
	        		if(player.hasPermission("ghostplayer.player.behuman")) {
	        			if(ghostFactory.isGhost(player) == true) {
                				ghostFactory.setGhost(player, false);
                				ghostFactory.removePlayer(player);
	        			}
	        			else {
	        				sender.sendMessage(ChatColor.RED + "You are already an human !");
	        			}
	        		}
	        		else {
	        			sender.sendMessage(ChatColor.RED + "You don't have permision to do this !");
	        		}
	        	}
	        	else {
	        		sender.sendMessage(ChatColor.RED + "[Ghost Player] You can't do this from the console !");
	        	}
	        }
	        
	        if(cmd.getName().equalsIgnoreCase("human")) {
	        	if (sender instanceof Player) {
	        		if(player.hasPermission("ghostplayer.player.behuman")) {
	        			if(ghostFactory.isGhost(player) == true) {
		        				ghostFactory.setGhost(player, false);
		        				ghostFactory.removePlayer(player);
		        				sender.sendMessage("You are an human now !");
	        			}
	        			else {
	        				sender.sendMessage(ChatColor.RED + "You are already an human !");
	        			}
	        		}
	        		else {
	        			sender.sendMessage(ChatColor.RED + "You don't have permision to do this !");
	        		}
	        	}
	        	else {
	        		sender.sendMessage(ChatColor.RED + "[Ghost Player] You can't do this from the console !");
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
        			sender.sendMessage("[Ghost Player] All ghosts have been cleared !");
	        	}
	        }
	        
	        return false;
	    }

}
