package com.skyost.gp;

import java.io.IOException;
import java.io.PrintStream;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import com.skyost.gp.Metrics.Graph;

public class GhostPlayerPlugin extends JavaPlugin implements Listener {
	public GhostFactory ghostFactory;
	public GhostPlayerConfig config;
	public GhostPlayerMessages messages;
	public boolean autoUpdate;
	public boolean ghostOnDeath;
	public int totalGhosts = 0;
	public String u1;
	public String u2;
	public String u3;
	public String u4;
	public String u5;
	public String u6;
	public String u7;
	public String m1;
	public String m2;
	public String m3;
	public String m4;
	public String m5;
	public String m6;
	public String m7;
	public String m8;
	public String m9;
	public String m10;
	public String m11;
	public String ms1;
	public String ms2;
	
	public void onEnable() {
		this.ghostFactory = new GhostFactory((Plugin) this);
		this.getServer().getPluginManager().registerEvents(this, this);
		loadConfig();
		update();
		startMetrics();
	}
	
	public void onDisable() {
		try {
			config.save();
			messages.save();
			getServer().getPluginManager().disablePlugin(this);
		} 
		catch (InvalidConfigurationException ex) {
			getLogger().log(Level.SEVERE, "[Ghost Player] " + ex);
			getServer().getPluginManager().disablePlugin(this);
		}
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
    
    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
    	if(ghostOnDeath == true) {
    		if(ghostFactory.isGhost(event.getEntity()) == true) {
    			ghostFactory.setGhost(event.getEntity(), true);
    		}
    		else {
    			ghostFactory.setGhost(event.getEntity(), true);
    		}
    	}
    	else {
    		if(ghostFactory.isGhost(event.getEntity()) == true) {
    			ghostFactory.setGhost(event.getEntity(), false);
    		}
    		else {
    			ghostFactory.setGhost(event.getEntity(), false);
    		}
    	}
    }
	
	public void loadConfig() {
		try {
			System.setOut(new PrintStream(System.out, true, "cp850"));
			config = new GhostPlayerConfig(this);
			config.init();
			messages = new GhostPlayerMessages(this);
			messages.init();
			autoUpdate = config.AutoUpdateOnLoad;
			ghostOnDeath = config.TurnIntoGhostOnDeath;
			u1 = messages.Update_SUCCESS;
			u2 = messages.Update_NOUPDATE;
			u3 = messages.Update_FAILDOWNLOAD;
			u4 = messages.Update_FAILDBO;
			u5 = messages.Update_FAILNOVERSION;
			u6 = messages.Update_FAILBADSLUG;
			u7 = messages.Update_UPDATEAVAILABLE;
			m1 = messages.Message_1;
			m2 = messages.Message_2;
			m3 = messages.Message_3;
			m4 = messages.Message_4;
			m5 = messages.Message_5;
			m6 = messages.Message_6;
			m7 = messages.Message_7;
			m8 = messages.Message_8;
			m9 = messages.Message_9;
			m10 = messages.Message_10;
			m11 = messages.Message_11;
			ms1 = messages.Message_S1;
			ms2 = messages.Message_S2;
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
		    Graph ghostsGraph = metrics.createGraph("Ghosts Graph");
		    ghostsGraph.addPlotter(new Metrics.Plotter("Total ghosts") {
		    @Override
		    public int getValue() {
		        return totalGhosts;	
		       }
		    });
		    
    		Graph updateGraph = metrics.createGraph("Update Graph");
    		updateGraph.addPlotter(new Metrics.Plotter("Checking for Updates") {	
    			@Override
    			public int getValue() {	
    				return 1;
    			}
    			
    			@Override
    			public String getColumnName() {
    				if(autoUpdate == true) {
    					return "Yes";
    				}
    				else if(autoUpdate == false) {
    					return "No";
    				}
    				else {
    					return "Maybe";
    				}
    			}
    		});
		    metrics.start();
		}
		catch (IOException ex) {
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
	            			System.out.println("[Ghost Player] " + u1);
	            			break;
	            		case NO_UPDATE:
	            			System.out.println("[Ghost Player] " + u2);
	            			break;
	            		case FAIL_DOWNLOAD:
	            			System.out.println("[Ghost Player] " + u3);
	            			break;
	            		case FAIL_DBO:
	            			System.out.println("[Ghost Player] " + u4);
	            			break;
	            		case FAIL_NOVERSION:
	            			System.out.println("[Ghost Player] " + u5);
	            			break;
	            		case FAIL_BADSLUG:
	            			System.out.println("[Ghost Player] " + u6);
	            			break;
	            		case UPDATE_AVAILABLE:
	            			System.out.println("[Ghost Player] " + u7);
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
                        		sender.sendMessage(ChatColor.RED + m1); // An human can't do this !
                        	}
                        }
                        else {
                        	sender.sendMessage(ChatColor.RED + ms1); // You don't have permission to do this !
                        }
	        	}
	        	else {
	        		sender.sendMessage(ChatColor.RED + "[Ghost Player] " + ms2); // You can't do this from the console !
	        	}
	        }
	        
	        if(cmd.getName().equalsIgnoreCase("silentghost")) {
	        	if (sender instanceof Player) {
                        if(player.hasPermission("ghostplayer.player.beghost")) {
                        	if(ghostFactory.isGhost(player) == true) {
                        		sender.sendMessage(ChatColor.RED + m2); // You are already a ghost !
                        	}
                        	else {
                        		ghostFactory.setGhost(player, true);
                        		ghostFactory.addPlayer(player);
                        		totalGhosts = totalGhosts + 1;
                        	}
                        }
                        else {
                        	sender.sendMessage(ChatColor.RED + ms1); // You don't have permission to do this !
                        }
	        	}
	        	else {
	        		sender.sendMessage(ChatColor.RED + "[Ghost Player] " + ms2); // You can't do this from the console !
	        	}
	        }
	       
	        if(cmd.getName().equalsIgnoreCase("ghost")) {
	        	if (sender instanceof Player) {
                        if(player.hasPermission("ghostplayer.player.beghost")) {
                        	if(ghostFactory.isGhost(player) == true) {
                        		sender.sendMessage(ChatColor.RED + m2); // You are already a ghost !
                        	}
                        	else {
                        		ghostFactory.setGhost(player, true);
                        		ghostFactory.addPlayer(player);
                        		sender.sendMessage(m3); // You are a ghost now !
                        		totalGhosts = totalGhosts + 1;
                        	}
                        }
                        else {
                        	sender.sendMessage(ChatColor.RED + ms1); // You don't have permission to do this !
                        }
	        	}
	        	else {
	        		sender.sendMessage(ChatColor.RED + "[Ghost Player] " + ms2); // You can't do this from the console !
	        	}
	        }
	        
	        if(cmd.getName().equalsIgnoreCase("removeghost") && args.length == 1) {
	        	if (sender instanceof Player) {
                        if(player.hasPermission("ghostplayer.admin.removeghost")) {
                        	try {
                        		if(ghostFactory.isGhost(Bukkit.getPlayer(args[0])) == true) {
                        			ghostFactory.setGhost(Bukkit.getPlayer(args[0]), false);
                        			ghostFactory.removePlayer(Bukkit.getPlayer(args[0]));
                        			sender.sendMessage(Bukkit.getPlayer(args[0]).getName() + " " + m4); // Has been removed from the ghosts !
                        			Bukkit.getPlayer(args[0]).sendMessage(player.getName() + " " + m5); // Has removed you from the ghosts !
                        		}
                        		else {
                        			sender.sendMessage(ChatColor.RED + Bukkit.getPlayer(args[0]).getName() + " " + m6); // Is already an human !
                        		}
                        	}
        	        		catch(NullPointerException e) {
        	        			sender.sendMessage(ChatColor.RED + args[0] + " " + m7); // Does not exist or not connected !
        	        		}
                        }
                        else {
                        	sender.sendMessage(ChatColor.RED + ms1); // You don't have permission to do this !
                        }
	        	}
	        	else {
	        		try {
	        			if(ghostFactory.isGhost(Bukkit.getPlayer(args[0])) == true) {
                			ghostFactory.setGhost(Bukkit.getPlayer(args[0]), false);
                			ghostFactory.removePlayer(Bukkit.getPlayer(args[0]));
	        				sender.sendMessage("[Ghost Player] " + Bukkit.getPlayer(args[0]).getName() + " " + m4); // Has been removed from the ghosts !
                			Bukkit.getPlayer(args[0]).sendMessage(m8); // You have been removed from the ghosts !
	        			}
	        			else {
	        				sender.sendMessage(ChatColor.RED + "[Ghost Player] " + Bukkit.getPlayer(args[0]).getName() + " " + m6); // Is already an human !
	        			}
	        		}
	        		catch(NullPointerException e) {
	        			sender.sendMessage(ChatColor.RED + "[Ghost Player] " + args[0] + " " + m7); // Does not exist or not connected !
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
	        				sender.sendMessage(ChatColor.RED + m9); // You are already an human !
	        			}
	        		}
	        		else {
	        			sender.sendMessage(ChatColor.RED + ms1); // You don't have permission to do this !
	        		}
	        	}
	        	else {
	        		sender.sendMessage(ChatColor.RED + "[Ghost Player] " + ms2); // You can't do this from the console !
	        	}
	        }
	        
	        if(cmd.getName().equalsIgnoreCase("human")) {
	        	if (sender instanceof Player) {
	        		if(player.hasPermission("ghostplayer.player.behuman")) {
	        			if(ghostFactory.isGhost(player) == true) {
		        				ghostFactory.setGhost(player, false);
		        				ghostFactory.removePlayer(player);
		        				sender.sendMessage(m11); // You are an human now !
	        			}
	        			else {
	        				sender.sendMessage(ChatColor.RED + m9); // You are already an human !
	        			}
	        		}
	        		else {
	        			sender.sendMessage(ChatColor.RED + ms1); // You don't have permission to do this !
	        		}
	        	}
	        	else {
	        		sender.sendMessage(ChatColor.RED + "[Ghost Player] " + ms2); // You can't do this from the console !
	        	}
	        }
	        
	        if(cmd.getName().equalsIgnoreCase("clearsghosts")) {
	        	if (sender instanceof Player) {
	        		if(player.hasPermission("ghostplayer.admin.clearsghosts")) {
	        			ghostFactory.clearMembers();
	        			sender.sendMessage(m10); // All ghosts have been cleared !
	        		}
	        		else {
	        			sender.sendMessage(ChatColor.RED + ms1); // You don't have permission to do this !
	        		}
	        	}
	        	else {
        			ghostFactory.clearMembers();
        			sender.sendMessage("[Ghost Player] " + m10); // All ghosts have been cleared !
	        	}
	        }
	        
	        return false;
	    }

}
