package com.skyost.gp;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Arrays;
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
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import com.skyost.gp.Metrics.Graph;

public class GhostPlayerPlugin extends JavaPlugin implements Listener {
	
	public static GhostFactory ghostFactory;
	public GhostPlayerConfig config;
	public GhostPlayerMessages messages;
	public int totalGhosts;
	
	@SuppressWarnings("static-access")
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
    	if(ghostFactory.hasPlayer(event.getPlayer()) == true) {
    		ghostFactory.removePlayer(event.getPlayer());
    	}
    }
    
    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
    	if(config.WorldsDisabled.toUpperCase().indexOf(event.getEntity().getWorld().getName().toUpperCase()) == -1) {
	    	if(config.TurnIntoGhostOnDeath == true) {
	    		if(ghostFactory.hasPlayer(event.getEntity()) == true) {
	    			ghostFactory.addPlayer(event.getEntity());
	    			ghostFactory.setGhost(event.getEntity(), true);
	    		}
	    	}
	    	else {
	    		if(ghostFactory.hasPlayer(event.getEntity()) == true) {
	    			ghostFactory.removePlayer(event.getEntity());
	    			ghostFactory.setGhost(event.getEntity(), false);
	    		}
	    	}
    	}
    }
    
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent e) {
    	if(config.WorldsDisabled.toUpperCase().indexOf(e.getPlayer().getWorld().getName().toUpperCase()) == -1) {
	    	if(config.GhostsCanInteract == false) {
		    	Player player = e.getPlayer();
		    	if(ghostFactory.isGhost(player) == true) {
		    		player.sendMessage(ChatColor.RED + messages.Message_31);
		    		e.setCancelled(true);
		    	}
	    	}
    	}
    }
    
    @SuppressWarnings("unused")
	@EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
    	Player player = event.getPlayer();
    	if(config.WorldsDisabled.toUpperCase().indexOf(player.getWorld().getName().toUpperCase()) == -1) {
    		if(config.TurnedIntoOnJoin.equalsIgnoreCase("SILENT HUMAN")) {
    			if(player.hasPermission("ghostplayer.player.behuman")) {
    				if(ghostFactory.isGhost(player) == true) {
        				ghostFactory.setGhost(player, false);
        				ghostFactory.removePlayer(player);
    				}
    			}
    		}
    		else if(config.TurnedIntoOnJoin.equalsIgnoreCase("HUMAN")) {
    			if(player.hasPermission("ghostplayer.player.behuman")) {
    				if(ghostFactory.isGhost(player) == true) {
        				ghostFactory.setGhost(player, false);
        				ghostFactory.removePlayer(player);
    				}
    				player.sendMessage(messages.Message_11); // You are an human now !
    			}
    		}
    		else if(config.TurnedIntoOnJoin.equalsIgnoreCase("SILENT GHOST")) {
    			if(player.hasPermission("ghostplayer.player.beghost")) {
	    			if(ghostFactory.isGhost(player) == false) {
	        			if(!(config.GhostTime.equals(-1))) {
	        				try {
	                			ghostFactory.setGhost(player, true);
	                			ghostFactory.addPlayer(player);
	                			totalGhosts = totalGhosts + 1;
	        					BukkitTask task = new TurnHuman(player, true).runTaskLaterAsynchronously(this, config.GhostTime);
	        				}
	        				catch(Exception e) {
	        					try {
	        						player.sendMessage(ChatColor.RED + messages.Message_17); // Invalid number in config for 'GhostTime'. It will be set to 'FOREVER'.
	            					config.GhostTime = -1;
									config.save();
								}
	        					catch (InvalidConfigurationException ex) {
									ex.printStackTrace();
								}
	        				}
	        			}
	        			else {
	        				ghostFactory.setGhost(player, true);
	            			ghostFactory.addPlayer(player);
	            			totalGhosts = totalGhosts + 1;
	        			}
	    			}
        		}	
			}
    		else if(config.TurnedIntoOnJoin.equalsIgnoreCase("GHOST")) {
    			if(player.hasPermission("ghostplayer.player.beghost")) {
	    			if(ghostFactory.isGhost(player) == false) {
	        			if(!(config.GhostTime.equals(-1))) {
	        				try {
	                			ghostFactory.setGhost(player, true);
	                			ghostFactory.addPlayer(player);
	                			player.sendMessage(messages.Message_3); // You are a ghost now !
	                			totalGhosts = totalGhosts + 1;
	        					BukkitTask task = new TurnHuman(player, true).runTaskLaterAsynchronously(this, config.GhostTime);
	        				}
	        				catch(Exception e) {
	        					try {
	        						player.sendMessage(ChatColor.RED + messages.Message_17); // Invalid number in config for 'GhostTime'. It will be set to 'FOREVER'.
	            					config.GhostTime = -1;
									config.save();
								}
	        					catch (InvalidConfigurationException ex) {
									ex.printStackTrace();
								}
	        				}
	        			}
	        			else {
	        				ghostFactory.setGhost(player, true);
	            			ghostFactory.addPlayer(player);
	            			player.sendMessage(messages.Message_3); // You are a ghost now !
	            			totalGhosts = totalGhosts + 1;
	        			}
	    			}
        		}
			}
    		else if(config.TurnedIntoOnJoin.equalsIgnoreCase("SILENT GHOST HUNTER") || config.TurnedIntoOnJoin.equalsIgnoreCase("SILENT GHOST-HUNTER") || config.TurnedIntoOnJoin.equalsIgnoreCase("SILENT GHOSTHUNTER")) {
    			if(player.hasPermission("ghostplayer.player.beghosthunter")) {
                	if(ghostFactory.isGhost(player) == false || ghostFactory.hasPlayer(player) == false) {
                		ghostFactory.addPlayer(player);
                	}
                }
			}
    		else if(config.TurnedIntoOnJoin.equalsIgnoreCase("GHOST HUNTER") || config.TurnedIntoOnJoin.equalsIgnoreCase("GHOST-HUNTER") || config.TurnedIntoOnJoin.equalsIgnoreCase("GHOSTHUNTER")) {
    			if(player.hasPermission("ghostplayer.player.beghosthunter")) {
                	if(ghostFactory.isGhost(player) == false || ghostFactory.hasPlayer(player) == false) {
                		ghostFactory.addPlayer(player);
                		player.sendMessage(messages.Message_20); // You are a ghost hunter now !
                	}
                }
			}
    		else {
    			try {
	    			player.sendMessage(ChatColor.RED + messages.Message_26); // Invalid data in config for 'TurnedIntoOnJoin'. It will be set to 'HUMAN'.
	    			config.TurnedIntoOnJoin = "HUMAN";
	    			config.save();
	    			if(player.hasPermission("ghostplayer.player.behuman")) {
	    				if(ghostFactory.isGhost(player) == true) {
	        				ghostFactory.setGhost(player, false);
	        				ghostFactory.removePlayer(player);
	        				player.sendMessage(messages.Message_11); // You are an human now !
	    				}
	    			}
    			}
    			catch(Exception ex) {
    				ex.printStackTrace();
    			}
    		}
    	}
    }
    
    class TurnHuman extends BukkitRunnable { 	 
        private final Player player;
        private final Boolean isSilent;
        
        public TurnHuman(Player player, Boolean isSilent) {
            this.player = player;
            this.isSilent = isSilent;
        }
     
        public void run() {
        	if(isSilent == true) {
        		ghostFactory.setGhost(player, false);
				ghostFactory.removePlayer(player);
        	}
        	else {
        		ghostFactory.setGhost(player, false);
				ghostFactory.removePlayer(player);
        		player.sendMessage(messages.Message_11); // You are an human now !
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
    				if(config.AutoUpdateOnLoad == true) {
    					return "Yes";
    				}
    				else if(config.AutoUpdateOnLoad == false) {
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
	
	public void deleteFile(String file) {
		File filename = new File(file);
		filename.delete();
	}
	
	public void update() {
		if(config.AutoUpdateOnLoad == true) {
			try {
				Updater updater = new Updater(this, "ghost-player", this.getFile(), Updater.UpdateType.DEFAULT, true);
				Updater.UpdateResult result = updater.getResult();
	        		switch(result) {
	            		case SUCCESS:
		            		System.out.println("[Ghost Player] " + messages.Update_SUCCESS);
		            		getServer().getPluginManager().disablePlugin(this);
	            			break;
	            		case NO_UPDATE:
	            			System.out.println("[Ghost Player] " + messages.Update_NOUPDATE);
	            			break;
	            		case FAIL_DOWNLOAD:
	            			System.out.println("[Ghost Player] " + messages.Update_FAILDOWNLOAD);
	            			break;
	            		case FAIL_DBO:
	            			System.out.println("[Ghost Player] " + messages.Update_FAILDBO);
	            			break;
	            		case FAIL_NOVERSION:
	            			System.out.println("[Ghost Player] " + messages.Update_FAILNOVERSION);
	            			break;
	            		case FAIL_BADSLUG:
	            			System.out.println("[Ghost Player] " + messages.Update_FAILBADSLUG);
	            			break;
	            		case UPDATE_AVAILABLE:
	            			System.out.println("[Ghost Player] " + messages.Update_UPDATEAVAILABLE);
	            			break;
	        		}
				}	
			catch (Exception ex) {
				getLogger().log(Level.SEVERE, "[Ghost Player] " + ex);
			}
		}
	}
	
	@SuppressWarnings("unused")
	public boolean onCommand(final CommandSender sender, Command cmd, String label, String[] args){
	        Player player = null;
	 
	        if(sender instanceof Player) {
	            player = (Player) sender;
	        }
	        
	        if(cmd.getName().equalsIgnoreCase("ghostview")) {
	        	if(sender instanceof Player) {
                        if(player.hasPermission("ghostplayer.player.ghostview")) {
                        	if(config.WorldsDisabled.toUpperCase().indexOf(player.getWorld().getName().toUpperCase()) != -1) {
                        		sender.sendMessage(ChatColor.RED + messages.Message_S3); // This plugin is disabled in this world !
                        	}
                        	else {
                        		if(ghostFactory.hasPlayer(player) == true) {
                        			player.sendBlockChange(player.getTargetBlock(null, 100).getLocation(), Material.AIR, (byte) 0);
                        		}
                        		else {
                        			sender.sendMessage(ChatColor.RED + messages.Message_1); // An human can't do this !
                        		}
                        	}
                        }
                        else {
                        	sender.sendMessage(ChatColor.RED + messages.Message_S1); // You don't have permission to do this !
                        }
	        	}
	        	else {
	        		sender.sendMessage(ChatColor.RED + "[Ghost Player] " + messages.Message_S2); // You can't do this from the console !
	        	}
	        }
	        
	        if(cmd.getName().equalsIgnoreCase("ghosthunter")) {
	        	if(sender instanceof Player) {
                        if(player.hasPermission("ghostplayer.player.beghosthunter")) {
                        	if(config.WorldsDisabled.toUpperCase().indexOf(player.getWorld().getName().toUpperCase()) != -1) {
                        		sender.sendMessage(ChatColor.RED + messages.Message_S3); // This plugin is disabled in this world !
                        	}
                        	else {
                        		if(ghostFactory.isGhost(player) == true) {
                        			sender.sendMessage(ChatColor.RED + messages.Message_18); // You are already a ghost so you don't need to be a ghost hunter to see your friends !
                        		}
                        		else if(ghostFactory.hasPlayer(player) == true) {
                        			sender.sendMessage(ChatColor.RED + messages.Message_19); // You are already a ghost hunter !
                        		}
                        		else {
                        			ghostFactory.addPlayer(player);
                        			sender.sendMessage(messages.Message_20); // You are a ghost hunter now !
                        		}
                        	}
                        }
                        else {
                        	sender.sendMessage(ChatColor.RED + messages.Message_S1); // You don't have permission to do this !
                        }
	        	}
	        	else {
	        		if(args.length == 1) {
		        		if(Bukkit.getOfflinePlayer(args[0]).isOnline()) {
		        			player = Bukkit.getPlayer(args[0]);
                        	if(config.WorldsDisabled.toUpperCase().indexOf(player.getWorld().getName().toUpperCase()) != -1) {
                        		sender.sendMessage(ChatColor.RED + "[Ghost Player] " + messages.Message_S3); // This plugin is disabled in this world !
                        	}
                        	else {
			        			if(ghostFactory.isGhost(player) == true) {
		                			sender.sendMessage(ChatColor.RED + "[Ghost Player] " + messages.Message_27); // This player is already a ghost so he don't need to be a ghost hunter !
		                		}
		                		else if(ghostFactory.hasPlayer(player) == true) {
		                			sender.sendMessage(ChatColor.RED + "[Ghost Player] " + messages.Message_28); // This player is already a ghost hunter !
		                		}
		                		else {
		                			ghostFactory.addPlayer(player);
		                			player.sendMessage(messages.Message_20); // You are a ghost hunter now !
		                		}
                        	}
		        		}
		        		else {
		        			sender.sendMessage(ChatColor.RED + "[Ghost Player] " + messages.Message_S4); // This player is offline !
		        		}
	        		}
	        		else {
	        			sender.sendMessage(ChatColor.RED + "[Ghost Player] " + messages.Message_S5); // You must have at least one argument !
	        		}
	        	}
	        }
	        
	        if(cmd.getName().equalsIgnoreCase("silentghosthunter")) {
	        	if(sender instanceof Player) {
	        		if(player.hasPermission("ghostplayer.player.beghosthunter")) {
	        			if(config.WorldsDisabled.toUpperCase().indexOf(player.getWorld().getName().toUpperCase()) != -1) {
	        				sender.sendMessage(ChatColor.RED + messages.Message_S3); // This plugin is disabled in this world !
                    	}
                       	else {
                        	if(ghostFactory.isGhost(player) == true) {
                        		sender.sendMessage(ChatColor.RED + messages.Message_18); // You are already a ghost so you don't need to be a ghost hunter to see your friends !
                        	}
                        	else if(ghostFactory.hasPlayer(player) == true) {
                        		sender.sendMessage(ChatColor.RED + messages.Message_19); // You are already a ghost hunter !
                        	}
                        	else {
                        		ghostFactory.addPlayer(player);
                        	}
                        }
	        		}
                    else {
                        sender.sendMessage(ChatColor.RED + messages.Message_S1); // You don't have permission to do this !
                    }
	        	}
	        	else {
	        		if(args.length == 1) {
		        		if(Bukkit.getOfflinePlayer(args[0]).isOnline()) {
		        			player = Bukkit.getPlayer(args[0]);
                        	if(config.WorldsDisabled.toUpperCase().indexOf(player.getWorld().getName().toUpperCase()) != -1) {
                        		sender.sendMessage(ChatColor.RED + "[Ghost Player] " + messages.Message_S3); // This plugin is disabled in this world !
                        	}
                        	else {
	                        	if(ghostFactory.isGhost(player) == true) {
		                			sender.sendMessage(ChatColor.RED + "[Ghost Player] " + messages.Message_27); // This player is already a ghost so he don't need to be a ghost hunter !
		                		}
		                		else if(ghostFactory.hasPlayer(player) == true) {
		                			sender.sendMessage(ChatColor.RED + "[Ghost Player] " + messages.Message_28); // This player is already a ghost hunter !
		                		}
		                		else {
		                			ghostFactory.addPlayer(player);
		                		}
                        	}
		        		}
		        		else {
		        			sender.sendMessage(ChatColor.RED + "[Ghost Player] " + messages.Message_S4); // This player is offline !
		        		}
	        		}
	        		else {
	        			sender.sendMessage(ChatColor.RED + "[Ghost Player] " + messages.Message_S5); // You must have at least one argument !
	        		}
	        	}
	        }
	        
	        if(cmd.getName().equalsIgnoreCase("humanworld")) {
	        	if(args.length >= 1) {
	        		if(sender instanceof Player) {
                        if(player.hasPermission("ghostplayer.admin.sethumanworld")) {
                        	try {
                        		String worldName = null;
                        		worldName = Arrays.toString(args).substring(1,  Arrays.toString(args).length() - 1);
                        		worldName = worldName.replaceAll(",", "");
                        		if(config.WorldsDisabled.equals("")) {
                        			config.WorldsDisabled = config.WorldsDisabled + worldName;
                        		}
                        		else {
                        			config.WorldsDisabled = config.WorldsDisabled + " " + worldName;
                        		}
								config.save();
								String message = messages.Message_13.replaceAll("/world/", worldName);
	                        	sender.sendMessage(message); // /world/ has been added to the list !
							} 
                        	catch (InvalidConfigurationException ex) {
								getLogger().log(Level.SEVERE, "[Ghost Player] " + ex);
							}
                        }
                        else {
                        	sender.sendMessage(ChatColor.RED + messages.Message_S1); // You don't have permission to do this !
                        }
	        		}
	        		else {
	        			try {
	        				String worldName = null;
                    		worldName = Arrays.toString(args).substring(1,  Arrays.toString(args).length() - 1);
                    		worldName = worldName.replaceAll(",", "");
	        				if(config.WorldsDisabled.equals("")) {
                    			config.WorldsDisabled = config.WorldsDisabled + worldName;
                    		}
                    		else {
                    			config.WorldsDisabled = config.WorldsDisabled + " " + worldName;
                    		}
							config.save();
							String message = messages.Message_13.replaceAll("/world/", "" + worldName);
                        	sender.sendMessage("[Ghost Player] " + message); // /world/ has been added to the list !
						} 
                    	catch (InvalidConfigurationException ex) {
							getLogger().log(Level.SEVERE, "[Ghost Player] " + ex);
						}
	        		}
	        	}
	        	else {
	        		if(sender instanceof Player) {
	        			sender.sendMessage(ChatColor.RED + messages.Message_14); // Simply type /humanworld <world> or /hw <world> to add a world where the plugin is disabled !
	        		}
	        		else {
	        			sender.sendMessage(ChatColor.RED + "[Ghost Player] " + messages.Message_14); // Simply type /humanworld <world> or /hw <world> to add a world where the plugin is disabled !
	        		}
	        	}
	        }
	        
	        if(cmd.getName().equalsIgnoreCase("ghostworld")) {
	        	if(args.length >= 1) {
	        		if(sender instanceof Player) {
                        if(player.hasPermission("ghostplayer.admin.setghostworld")) {
                        	try {
                        		String worldName = null;
                        		worldName = Arrays.toString(args).substring(1,  Arrays.toString(args).length() - 1);
                        		worldName = worldName.replaceAll(",", "");
                        		config.WorldsDisabled = config.WorldsDisabled.replaceAll(worldName, "");
								config.save();
								String message = messages.Message_15.replaceAll("/world/", worldName);
	                        	sender.sendMessage(message); // /world/ has been removed to the list !
							} 
                        	catch (InvalidConfigurationException ex) {
								getLogger().log(Level.SEVERE, "[Ghost Player] " + ex);
							}
                        }
                        else {
                        	sender.sendMessage(ChatColor.RED + messages.Message_S1); // You don't have permission to do this !
                        }
	        		}
	        		else {
	        			try {
	        				String worldName = null;
                    		worldName = Arrays.toString(args).substring(1,  Arrays.toString(args).length() - 1);
                    		worldName = worldName.replaceAll(",", "");
                    		config.WorldsDisabled = config.WorldsDisabled.replaceAll(worldName, "");
							config.save();
							String message = messages.Message_15.replaceAll("/world/", worldName);
                        	sender.sendMessage("[Ghost Player] " + message); // /world/ has been removed to the list !
						} 
                    	catch (InvalidConfigurationException ex) {
							getLogger().log(Level.SEVERE, "[Ghost Player] " + ex);
						}
	        		}
	        	}
	        	else {
	        		if(sender instanceof Player) {
	        			sender.sendMessage(ChatColor.RED + messages.Message_16); // Simply type /humanworld <world> or /hw <world> to add a world where the plugin is disabled !
	        		}
	        		else {
	        			sender.sendMessage(ChatColor.RED + "[Ghost Player] " + messages.Message_16); // Simply type /humanworld <world> or /hw <world> to add a world where the plugin is disabled !
	        		}
	        	}
	        }
	        
	        if(cmd.getName().equalsIgnoreCase("silentghost")) {
	        	if(sender instanceof Player) {
                        if(player.hasPermission("ghostplayer.player.beghost")) {
                        	if(config.WorldsDisabled.toUpperCase().indexOf(player.getWorld().getName().toUpperCase()) != -1) {
                        		sender.sendMessage(ChatColor.RED + messages.Message_S3); // This plugin is disabled in this world !
                        	}
                        	else {
                        		if(ghostFactory.isGhost(player) == true) {
                        			sender.sendMessage(ChatColor.RED + messages.Message_2); // You are already a ghost !
                        		}
                        		else {
                        			if(!(config.GhostTime.equals(-1))) {
                        				try {
                                			ghostFactory.setGhost(player, true);
                                			ghostFactory.addPlayer(player);
                                			totalGhosts = totalGhosts + 1;
                        					BukkitTask task = new TurnHuman(player, true).runTaskLaterAsynchronously(this, config.GhostTime);
                        				}
                        				catch(Exception e) {
                        					try {
                        						sender.sendMessage(ChatColor.RED + messages.Message_17); // Invalid number in config for 'GhostTime'. It will be set to 'FOREVER'.
                            					config.GhostTime = -1;
												config.save();
											}
                        					catch (InvalidConfigurationException ex) {
												ex.printStackTrace();
											}
                        				}
                        			}
                        			else {
                        				ghostFactory.setGhost(player, true);
                            			ghostFactory.addPlayer(player);
                            			totalGhosts = totalGhosts + 1;
                        			}
                        		}
                        	}
                        }
                        else {
                        	sender.sendMessage(ChatColor.RED + messages.Message_S1); // You don't have permission to do this !
                        }
	        	}
	        	else {
	        		if(args.length == 1) {
		        		if(Bukkit.getOfflinePlayer(args[0]).isOnline()) {
		        			player = Bukkit.getPlayer(args[0]);
		        			if(config.WorldsDisabled.toUpperCase().indexOf(player.getWorld().getName().toUpperCase()) != -1) {
                        		sender.sendMessage(ChatColor.RED + "[Ghost Player] " + messages.Message_S3); // This plugin is disabled in this world !
                        	}
                        	else {
                        		if(ghostFactory.isGhost(player) == true) {
                        			sender.sendMessage(ChatColor.RED + "[Ghost Player] " + messages.Message_29); // This player is already a ghost !
                        		}
                        		else {
                        			if(!(config.GhostTime.equals(-1))) {
                        				try {
                                			ghostFactory.setGhost(player, true);
                                			ghostFactory.addPlayer(player);
                                			totalGhosts = totalGhosts + 1;
                        					BukkitTask task = new TurnHuman(player, true).runTaskLaterAsynchronously(this, config.GhostTime);
                        				}
                        				catch(Exception e) {
                        					try {
                        						sender.sendMessage(ChatColor.RED + "[Ghost Player] " + messages.Message_17); // Invalid number in config for 'GhostTime'. It will be set to 'FOREVER'.
                            					config.GhostTime = -1;
												config.save();
											}
                        					catch (InvalidConfigurationException ex) {
												ex.printStackTrace();
											}
                        				}
                        			}
                        			else {
                        				ghostFactory.setGhost(player, true);
                            			ghostFactory.addPlayer(player);
                            			totalGhosts = totalGhosts + 1;
                        			}
                        		}
                        	}
		        		}
		        		else {
		        			sender.sendMessage(ChatColor.RED + "[Ghost Player] " + messages.Message_S4); // This player is offline !
		        		}
	        		}
	        		else {
	        			sender.sendMessage(ChatColor.RED + "[Ghost Player] " + messages.Message_S5); // You must have at least one argument !
	        		}
	        	}
	        }
	       
	        if(cmd.getName().equalsIgnoreCase("ghost")) {
	        	if(sender instanceof Player) {
                        if(player.hasPermission("ghostplayer.player.beghost")) {
                        	if(config.WorldsDisabled.toUpperCase().indexOf(player.getWorld().getName().toUpperCase()) != -1) {
                        		sender.sendMessage(ChatColor.RED + messages.Message_S3); // This plugin is disabled in this world !
                        	}
                        	else {
                        		if(ghostFactory.isGhost(player) == true) {
                        			sender.sendMessage(ChatColor.RED + messages.Message_2); // You are already a ghost !
                        		}
                        		else {
                        			if(!(config.GhostTime.equals(-1))) {
                        				try {
                        					ghostFactory.setGhost(player, true);
                        					ghostFactory.addPlayer(player);
                        					sender.sendMessage(messages.Message_3); // You are a ghost now !
                        					totalGhosts = totalGhosts + 1;
                        					BukkitTask task = new TurnHuman(player, false).runTaskLaterAsynchronously(this, config.GhostTime);
                        				}
                        				catch(Exception e) {
                        					try {
                        						sender.sendMessage(ChatColor.RED + messages.Message_17); // Invalid number in config for 'GhostTime'. It will be set to 'FOREVER'.
                            					config.GhostTime = -1;
												config.save();
											}
                        					catch (InvalidConfigurationException ex) {
												ex.printStackTrace();
                        					}
                        				}
                        			}
                        			else {
                        				ghostFactory.setGhost(player, true);
                    					ghostFactory.addPlayer(player);
                    					sender.sendMessage(messages.Message_3); // You are a ghost now !
                    					totalGhosts = totalGhosts + 1;
                        			}
                        		}
                        	}
                        }
                        else {
                        	sender.sendMessage(ChatColor.RED + messages.Message_S1); // You don't have permission to do this !
                        }
	        	}
	        	else {
	        		if(args.length == 1) {
		        		if(Bukkit.getOfflinePlayer(args[0]).isOnline()) {
		        			player = Bukkit.getPlayer(args[0]);
		        			if(config.WorldsDisabled.toUpperCase().indexOf(player.getWorld().getName().toUpperCase()) != -1) {
                        		sender.sendMessage(ChatColor.RED + "[Ghost Player] " + messages.Message_S3); // This plugin is disabled in this world !
                        	}
                        	else {
                        		if(ghostFactory.isGhost(player) == true) {
                        			sender.sendMessage(ChatColor.RED + "[Ghost Player] " + messages.Message_29); // This player is already a ghost !
                        		}
                        		else {
                        			if(!(config.GhostTime.equals(-1))) {
                        				try {
                                			ghostFactory.setGhost(player, true);
                                			ghostFactory.addPlayer(player);
                                			player.sendMessage(messages.Message_3); // You are a ghost now !
                                			totalGhosts = totalGhosts + 1;
                        					BukkitTask task = new TurnHuman(player, false).runTaskLaterAsynchronously(this, config.GhostTime);
                        				}
                        				catch(Exception e) {
                        					try {
                        						sender.sendMessage(ChatColor.RED + "[Ghost Player] " + messages.Message_17); // Invalid number in config for 'GhostTime'. It will be set to 'FOREVER'.
                            					config.GhostTime = -1;
												config.save();
											}
                        					catch (InvalidConfigurationException ex) {
												ex.printStackTrace();
											}
                        				}
                        			}
                        			else {
                        				ghostFactory.setGhost(player, true);
                            			ghostFactory.addPlayer(player);
                            			player.sendMessage(messages.Message_3); // You are a ghost now !
                            			totalGhosts = totalGhosts + 1;
                        			}
                        		}
                        	}
		        		}
		        		else {
		        			sender.sendMessage(ChatColor.RED + "[Ghost Player] " + messages.Message_S4); // This player is offline !
		        		}
	        		}
	        		else {
	        			sender.sendMessage(ChatColor.RED + "[Ghost Player] " + messages.Message_S5); // You must have at least one argument !
	        		}
	        	}
	        }
	        
	        if(cmd.getName().equalsIgnoreCase("removeghost")) {
	        	if(args.length == 1) {
	        		if(sender instanceof Player) {
                        if(player.hasPermission("ghostplayer.admin.removeghost")) {
                        	if(config.WorldsDisabled.toUpperCase().indexOf(player.getWorld().getName().toUpperCase()) != -1) {
                        		sender.sendMessage(ChatColor.RED + messages.Message_S3); // This plugin is disabled in this world !
                        	}
                        	else {
                        		try {
                        			if(ghostFactory.isGhost(Bukkit.getPlayer(args[0])) == true) {
                        				String message1 = messages.Message_4.replaceAll("/target/", Bukkit.getPlayer(args[0]).getName());
                        				String message2 = messages.Message_5.replaceAll("/sender/", player.getName());
                        				ghostFactory.setGhost(Bukkit.getPlayer(args[0]), false);
                        				ghostFactory.removePlayer(Bukkit.getPlayer(args[0]));
                        				sender.sendMessage(message1); // Has been removed from the ghosts !
                        				Bukkit.getPlayer(args[0]).sendMessage(message2); // Has removed you from the ghosts !
                        			}
                        			else {
                        				messages.Message_6 = messages.Message_6.replaceAll("/target/", Bukkit.getPlayer(args[0]).getName());
        	        					sender.sendMessage(ChatColor.RED + messages.Message_6); // Is already an human !
        	        					}
                        			}
                        			catch(NullPointerException e) {
                        				messages.Message_7 = messages.Message_7.replaceAll("/target/", args[0]);
                        				sender.sendMessage(ChatColor.RED + messages.Message_7); // Does not exist or not connected !
                        			}
                        		}
                        	}
                        else {
                        	sender.sendMessage(ChatColor.RED + messages.Message_S1); // You don't have permission to do this !
                        }
	        		}
	        		else {
	        			try {
	        				if(ghostFactory.isGhost(Bukkit.getPlayer(args[0])) == true) {
	        					String message = messages.Message_4.replaceAll("/target/", Bukkit.getPlayer(args[0]).getName());
	        					ghostFactory.setGhost(Bukkit.getPlayer(args[0]), false);
	        					ghostFactory.removePlayer(Bukkit.getPlayer(args[0]));
	        					sender.sendMessage("[Ghost Player] " + message); // Has been removed from the ghosts !
	        					Bukkit.getPlayer(args[0]).sendMessage(messages.Message_8); // You have been removed from the ghosts !
	        				}
	        				else {
	        					messages.Message_6 = messages.Message_6.replaceAll("/target/", Bukkit.getPlayer(args[0]).getName());
	        					sender.sendMessage(ChatColor.RED + "[Ghost Player] " + messages.Message_6); // Is already an human !
	        				}
	        			}
	        			catch(NullPointerException e) {
	        				String message = messages.Message_7.replaceAll("/target/", args[0]);
	        				sender.sendMessage(ChatColor.RED + "[Ghost Player] " + message); // Does not exist or not connected !
	        			}
	        		}
	        	}
	        	else {
	        		if(sender instanceof Player) {
	        			sender.sendMessage(ChatColor.RED + messages.Message_12); // Simply type /removeghost <player> or /rg <player> to remove <player> from the ghosts !
	        		}
	        		else {
	        			sender.sendMessage(ChatColor.RED + "[Ghost Player] " + messages.Message_12); // Simply type /removeghost <player> or /rg <player> to remove <player> from the ghosts !
	        		}
	        	}
	        }
	        
	        if(cmd.getName().equalsIgnoreCase("removeghosthunter")) {
	        	if(args.length == 1) {
	        		if(sender instanceof Player) {
                        if(player.hasPermission("ghostplayer.admin.removeghosthunter")) {
                        	if(config.WorldsDisabled.toUpperCase().indexOf(player.getWorld().getName().toUpperCase()) != -1) {
                        		sender.sendMessage(ChatColor.RED + messages.Message_S3); // This plugin is disabled in this world !
                        	}
                        	else {
                        		try {
                        			if(ghostFactory.isGhost(Bukkit.getPlayer(args[0])) == true) {
                        				String message = messages.Message_21.replaceAll("/target/", Bukkit.getPlayer(args[0]).getName());                  				
                        				sender.sendMessage(ChatColor.RED + message); // /target/ is not a ghost hunter !
                        			}
                        			else {
                        				if(ghostFactory.hasPlayer(Bukkit.getPlayer(args[0])) == true) {
                            				ghostFactory.removePlayer(Bukkit.getPlayer(args[0]));
                            				String message1 = messages.Message_22.replaceAll("/target/", Bukkit.getPlayer(args[0]).getName());
                            				String message2 = messages.Message_24.replaceAll("/sender/", player.getName());
                            				sender.sendMessage(message1); // /target/ has been removed from the ghosts hunters !
                            				Bukkit.getPlayer(args[0]).sendMessage(message2); // /sender/ been removed from the ghosts hunters !
                            			}
                        				else {
                        					messages.Message_6 = messages.Message_6.replaceAll("/target/", Bukkit.getPlayer(args[0]).getName());
            	        					sender.sendMessage(ChatColor.RED + messages.Message_6); // Is already an human !
                        				}
        	        					}
                        			}
                        			catch(Exception e) {
                        				messages.Message_7 = messages.Message_7.replaceAll("/target/", args[0]);
                        				sender.sendMessage(ChatColor.RED + messages.Message_7); // Does not exist or not connected !
                        			}
                        		}
                        	}
                        else {
                        	sender.sendMessage(ChatColor.RED + messages.Message_S1); // You don't have permission to do this !
                        }
	        		}
	        		else {
	        			try {
                			if(ghostFactory.isGhost(Bukkit.getPlayer(args[0])) == true) {
                				String message = messages.Message_21.replaceAll("/target/", Bukkit.getPlayer(args[0]).getName());                  				
                				sender.sendMessage(ChatColor.RED + "[Ghost Player] " + message); // /target/ is not a ghost hunter !
                			}
                			else {
                				if(ghostFactory.hasPlayer(Bukkit.getPlayer(args[0])) == true) {
                    				ghostFactory.removePlayer(Bukkit.getPlayer(args[0]));
                    				String message = messages.Message_22.replaceAll("/target/", Bukkit.getPlayer(args[0]).getName());                  				
                    				sender.sendMessage("[Ghost Player] " + message); // /target/ has been removed from the ghosts hunters !
                    				Bukkit.getPlayer(args[0]).sendMessage(messages.Message_23); // You have been removed from the ghosts hunters !
                    			}
                				else {
                					messages.Message_6 = messages.Message_6.replaceAll("/target/", Bukkit.getPlayer(args[0]).getName());
    	        					sender.sendMessage(ChatColor.RED + "[Ghost Player] " + messages.Message_6); // Is already an human !
                				}
	        					}
                			}
                			catch(Exception e) {
                				String message = messages.Message_7.replaceAll("/target/", args[0]);
                				sender.sendMessage(ChatColor.RED + "[Ghost Player] " + message); // Does not exist or not connected !
                			}
	        		}
	        	}
	        	else {
	        		if(sender instanceof Player) {
	        			sender.sendMessage(ChatColor.RED + messages.Message_25); // Simply type /removeghosthunter <player> or /rgh <player> to remove <player> from the ghosts hunters !
	        		}
	        		else {
	        			sender.sendMessage(ChatColor.RED + "[Ghost Player] " + messages.Message_25); // Simply type /removeghosthunter <player> or /rgh <player> to remove <player> from the ghosts hunters !
	        		}
	        	}
	        }
	        
	        if(cmd.getName().equalsIgnoreCase("silenthuman")) {
	        	if(sender instanceof Player) {
	        		if(player.hasPermission("ghostplayer.player.behuman")) {
                    	if(config.WorldsDisabled.toUpperCase().indexOf(player.getWorld().getName().toUpperCase()) != -1) {
                    		sender.sendMessage(ChatColor.RED + messages.Message_S3); // This plugin is disabled in this world !
                    	}
                    	else {
                    		if(ghostFactory.isGhost(player) == true) {
                				ghostFactory.setGhost(player, false);
                				ghostFactory.removePlayer(player);
	        				}
                    		else {
                    			sender.sendMessage(ChatColor.RED + messages.Message_9); // You are already an human !
                    		}
                    	}
	        		}
	        		else {
	        			sender.sendMessage(ChatColor.RED + messages.Message_S1); // You don't have permission to do this !
	        		}
	        	}
	        	else {
	        		if(args.length == 1) {
		        		if(Bukkit.getOfflinePlayer(args[0]).isOnline()) {
		        			player = Bukkit.getPlayer(args[0]);
		        			if(config.WorldsDisabled.toUpperCase().indexOf(player.getWorld().getName().toUpperCase()) != -1) {
	                    		sender.sendMessage(ChatColor.RED + "[Ghost Player] " + messages.Message_S3); // This plugin is disabled in this world !
	                    	}
	                    	else {
	                    		if(ghostFactory.isGhost(player) == true) {
	                				ghostFactory.setGhost(player, false);
	                				ghostFactory.removePlayer(player);
		        				}
	                    		else {
	                    			sender.sendMessage(ChatColor.RED + "[Ghost Player] " + messages.Message_30); // This player is already an human !
	                    		}
                        	}
		        		}
		        		else {
		        			sender.sendMessage(ChatColor.RED + "[Ghost Player] " + messages.Message_S4); // This player is offline !
		        		}
	        		}
	        		else {
	        			sender.sendMessage(ChatColor.RED + "[Ghost Player] " + messages.Message_S5); // You must have at least one argument !
	        		}
	        	}
	        }
	        
	        if(cmd.getName().equalsIgnoreCase("human")) {
	        	if(sender instanceof Player) {
	        		if(player.hasPermission("ghostplayer.player.behuman")) {
                    	if(config.WorldsDisabled.toUpperCase().indexOf(player.getWorld().getName().toUpperCase()) != -1) {
                    		sender.sendMessage(ChatColor.RED + messages.Message_S3); // This plugin is disabled in this world !
                    	}
                    	else {
                    		if(ghostFactory.hasPlayer(player) == true) {
		        				ghostFactory.setGhost(player, false);
		        				ghostFactory.removePlayer(player);
		        				sender.sendMessage(messages.Message_11); // You are an human now !
                    		}
                    		else {
                    			sender.sendMessage(ChatColor.RED + messages.Message_9); // You are already an human !
                    		}
                    	}
	        		}
	        		else {
	        			sender.sendMessage(ChatColor.RED + messages.Message_S1); // You don't have permission to do this !
	        		}
	        	}
	        	else {
	        		if(args.length == 1) {
		        		if(Bukkit.getOfflinePlayer(args[0]).isOnline()) {
		        			player = Bukkit.getPlayer(args[0]);
		        			if(config.WorldsDisabled.toUpperCase().indexOf(player.getWorld().getName().toUpperCase()) != -1) {
	                    		sender.sendMessage(ChatColor.RED + "[Ghost Player] " + messages.Message_S3); // This plugin is disabled in this world !
	                    	}
	                    	else {
	                    		if(ghostFactory.isGhost(player) == true) {
	                				ghostFactory.setGhost(player, false);
	                				ghostFactory.removePlayer(player);
	                				player.sendMessage(messages.Message_11); // You are an human now !
		        				}
	                    		else {
	                    			sender.sendMessage(ChatColor.RED + "[Ghost Player] " + messages.Message_30); // This player is already an human !
	                    		}
                        	}
		        		}
		        		else {
		        			sender.sendMessage(ChatColor.RED + "[Ghost Player] " + messages.Message_S4); // This player is offline !
		        		}
	        		}
	        		else {
	        			sender.sendMessage(ChatColor.RED + "[Ghost Player] " + messages.Message_S5); // You must have at least one argument !
	        		}
	        	}
	        }
	        
	        if(cmd.getName().equalsIgnoreCase("clearsghosts")) {
	        	if(sender instanceof Player) {
	        		if(player.hasPermission("ghostplayer.admin.clearsghosts")) {
                    	if(config.WorldsDisabled.toUpperCase().indexOf(player.getWorld().getName().toUpperCase()) != -1) {
                    		sender.sendMessage(ChatColor.RED + messages.Message_S3); // This plugin is disabled in this world !
                    	}
                    	else {
                    		ghostFactory.clearMembers();
                    		sender.sendMessage(messages.Message_10); // All ghosts have been cleared !
                    	}
	        		}
	        		else {
	        			sender.sendMessage(ChatColor.RED + messages.Message_S1); // You don't have permission to do this !
	        		}
	        	}
	        	else {
        			ghostFactory.clearMembers();
        			sender.sendMessage("[Ghost Player] " + messages.Message_10); // All ghosts have been cleared !
	        	}
	        }
	        return true;
	    }

}
