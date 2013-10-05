package com.skyost.gp.listeners;

import java.util.Arrays;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import com.skyost.gp.GhostPlayer;
import com.skyost.gp.tasks.TurnHuman;

public class Commands implements CommandExecutor {

	@SuppressWarnings({ "unused", "deprecation" })
	public boolean onCommand(final CommandSender sender, Command cmd, String label, String[] args){
        Player player = null;
 
        if(sender instanceof Player) {
            player = (Player) sender;
        }
        
        if(cmd.getName().equalsIgnoreCase("ghostview")) {
        	if(sender instanceof Player) {
                    if(player.hasPermission("ghostplayer.player.ghostview")) {
                    	if(GhostPlayer.config.WorldsDisabled.toUpperCase().indexOf(player.getWorld().getName().toUpperCase()) != -1) {
                    		sender.sendMessage(ChatColor.RED + GhostPlayer.messages.Message_S3); // This plugin is disabled in this world !
                    	}
                    	else {
                    		if(GhostPlayer.ghostFactory.hasPlayer(player) == true) {
                    			player.sendBlockChange(player.getTargetBlock(null, 10).getLocation(), Material.AIR, (byte) 0);
                    		}
                    		else {
                    			sender.sendMessage(ChatColor.RED + GhostPlayer.messages.Message_1); // An human can't do this !
                    		}
                    	}
                    }
                    else {
                    	sender.sendMessage(ChatColor.RED + GhostPlayer.messages.Message_S1); // You don't have permission to do this !
                    }
        	}
        	else {
        		sender.sendMessage(ChatColor.RED + "[Ghost Player] " + GhostPlayer.messages.Message_S2); // You can't do this from the console !
        	}
        }
        
        if(cmd.getName().equalsIgnoreCase("ghosthunter")) {
        	if(sender instanceof Player) {
                    if(player.hasPermission("ghostplayer.player.beghosthunter")) {
                    	if(GhostPlayer.config.WorldsDisabled.toUpperCase().indexOf(player.getWorld().getName().toUpperCase()) != -1) {
                    		sender.sendMessage(ChatColor.RED + GhostPlayer.messages.Message_S3); // This plugin is disabled in this world !
                    	}
                    	else {
                    		if(GhostPlayer.ghostFactory.isGhost(player) == true) {
                    			sender.sendMessage(ChatColor.RED + GhostPlayer.messages.Message_18); // You are already a ghost so you don't need to be a ghost hunter to see your friends !
                    		}
                    		else if(GhostPlayer.ghostFactory.hasPlayer(player) == true) {
                    			sender.sendMessage(ChatColor.RED + GhostPlayer.messages.Message_19); // You are already a ghost hunter !
                    		}
                    		else {
                    			GhostPlayer.ghostFactory.addPlayer(player);
                    			sender.sendMessage(GhostPlayer.messages.Message_20); // You are a ghost hunter now !
                    		}
                    	}
                    }
                    else {
                    	sender.sendMessage(ChatColor.RED + GhostPlayer.messages.Message_S1); // You don't have permission to do this !
                    }
        	}
        	else {
        		if(args.length == 1) {
	        		if(Bukkit.getOfflinePlayer(args[0]).isOnline()) {
	        			player = Bukkit.getPlayer(args[0]);
                    	if(GhostPlayer.config.WorldsDisabled.toUpperCase().indexOf(player.getWorld().getName().toUpperCase()) != -1) {
                    		sender.sendMessage(ChatColor.RED + "[Ghost Player] " + GhostPlayer.messages.Message_S3); // This plugin is disabled in this world !
                    	}
                    	else {
		        			if(GhostPlayer.ghostFactory.isGhost(player) == true) {
	                			sender.sendMessage(ChatColor.RED + "[Ghost Player] " + GhostPlayer.messages.Message_27); // This player is already a ghost so he don't need to be a ghost hunter !
	                		}
	                		else if(GhostPlayer.ghostFactory.hasPlayer(player) == true) {
	                			sender.sendMessage(ChatColor.RED + "[Ghost Player] " + GhostPlayer.messages.Message_28); // This player is already a ghost hunter !
	                		}
	                		else {
	                			GhostPlayer.ghostFactory.addPlayer(player);
	                			player.sendMessage(GhostPlayer.messages.Message_20); // You are a ghost hunter now !
	                		}
                    	}
	        		}
	        		else {
	        			sender.sendMessage(ChatColor.RED + "[Ghost Player] " + GhostPlayer.messages.Message_S4); // This player is offline !
	        		}
        		}
        		else {
        			sender.sendMessage(ChatColor.RED + "[Ghost Player] " + GhostPlayer.messages.Message_S5); // You must have at least one argument !
        		}
        	}
        }
        
        if(cmd.getName().equalsIgnoreCase("silentghosthunter")) {
        	if(sender instanceof Player) {
        		if(player.hasPermission("ghostplayer.player.beghosthunter")) {
        			if(GhostPlayer.config.WorldsDisabled.toUpperCase().indexOf(player.getWorld().getName().toUpperCase()) != -1) {
        				sender.sendMessage(ChatColor.RED + GhostPlayer.messages.Message_S3); // This plugin is disabled in this world !
                	}
                   	else {
                    	if(GhostPlayer.ghostFactory.isGhost(player) == true) {
                    		sender.sendMessage(ChatColor.RED + GhostPlayer.messages.Message_18); // You are already a ghost so you don't need to be a ghost hunter to see your friends !
                    	}
                    	else if(GhostPlayer.ghostFactory.hasPlayer(player) == true) {
                    		sender.sendMessage(ChatColor.RED + GhostPlayer.messages.Message_19); // You are already a ghost hunter !
                    	}
                    	else {
                    		GhostPlayer.ghostFactory.addPlayer(player);
                    	}
                    }
        		}
                else {
                    sender.sendMessage(ChatColor.RED + GhostPlayer.messages.Message_S1); // You don't have permission to do this !
                }
        	}
        	else {
        		if(args.length == 1) {
	        		if(Bukkit.getOfflinePlayer(args[0]).isOnline()) {
	        			player = Bukkit.getPlayer(args[0]);
                    	if(GhostPlayer.config.WorldsDisabled.toUpperCase().indexOf(player.getWorld().getName().toUpperCase()) != -1) {
                    		sender.sendMessage(ChatColor.RED + "[Ghost Player] " + GhostPlayer.messages.Message_S3); // This plugin is disabled in this world !
                    	}
                    	else {
                        	if(GhostPlayer.ghostFactory.isGhost(player) == true) {
	                			sender.sendMessage(ChatColor.RED + "[Ghost Player] " + GhostPlayer.messages.Message_27); // This player is already a ghost so he don't need to be a ghost hunter !
	                		}
	                		else if(GhostPlayer.ghostFactory.hasPlayer(player) == true) {
	                			sender.sendMessage(ChatColor.RED + "[Ghost Player] " + GhostPlayer.messages.Message_28); // This player is already a ghost hunter !
	                		}
	                		else {
	                			GhostPlayer.ghostFactory.addPlayer(player);
	                		}
                    	}
	        		}
	        		else {
	        			sender.sendMessage(ChatColor.RED + "[Ghost Player] " + GhostPlayer.messages.Message_S4); // This player is offline !
	        		}
        		}
        		else {
        			sender.sendMessage(ChatColor.RED + "[Ghost Player] " + GhostPlayer.messages.Message_S5); // You must have at least one argument !
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
                    		if(GhostPlayer.config.WorldsDisabled.equals("")) {
                    			GhostPlayer.config.WorldsDisabled = GhostPlayer.config.WorldsDisabled + worldName;
                    		}
                    		else {
                    			GhostPlayer.config.WorldsDisabled = GhostPlayer.config.WorldsDisabled + " " + worldName;
                    		}
							GhostPlayer.config.save();
							String message = GhostPlayer.messages.Message_13.replaceAll("/world/", worldName);
                        	sender.sendMessage(message); // /world/ has been added to the list !
						} 
                    	catch (InvalidConfigurationException ex) {
							ex.printStackTrace();
						}
                    }
                    else {
                    	sender.sendMessage(ChatColor.RED + GhostPlayer.messages.Message_S1); // You don't have permission to do this !
                    }
        		}
        		else {
        			try {
        				String worldName = null;
                		worldName = Arrays.toString(args).substring(1,  Arrays.toString(args).length() - 1);
                		worldName = worldName.replaceAll(",", "");
        				if(GhostPlayer.config.WorldsDisabled.equals("")) {
                			GhostPlayer.config.WorldsDisabled = GhostPlayer.config.WorldsDisabled + worldName;
                		}
                		else {
                			GhostPlayer.config.WorldsDisabled = GhostPlayer.config.WorldsDisabled + " " + worldName;
                		}
						GhostPlayer.config.save();
						String message = GhostPlayer.messages.Message_13.replaceAll("/world/", "" + worldName);
                    	sender.sendMessage("[Ghost Player] " + message); // /world/ has been added to the list !
					} 
                	catch (InvalidConfigurationException ex) {
						ex.printStackTrace();
					}
        		}
        	}
        	else {
        		if(sender instanceof Player) {
        			sender.sendMessage(ChatColor.RED + GhostPlayer.messages.Message_14); // Simply type /humanworld <world> or /hw <world> to add a world where the plugin is disabled !
        		}
        		else {
        			sender.sendMessage(ChatColor.RED + "[Ghost Player] " + GhostPlayer.messages.Message_14); // Simply type /humanworld <world> or /hw <world> to add a world where the plugin is disabled !
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
                    		GhostPlayer.config.WorldsDisabled = GhostPlayer.config.WorldsDisabled.replaceAll(worldName, "");
							GhostPlayer.config.save();
							String message = GhostPlayer.messages.Message_15.replaceAll("/world/", worldName);
                        	sender.sendMessage(message); // /world/ has been removed to the list !
						} 
                    	catch (InvalidConfigurationException ex) {
							ex.printStackTrace();
						}
                    }
                    else {
                    	sender.sendMessage(ChatColor.RED + GhostPlayer.messages.Message_S1); // You don't have permission to do this !
                    }
        		}
        		else {
        			try {
        				String worldName = null;
                		worldName = Arrays.toString(args).substring(1,  Arrays.toString(args).length() - 1);
                		worldName = worldName.replaceAll(",", "");
                		GhostPlayer.config.WorldsDisabled = GhostPlayer.config.WorldsDisabled.replaceAll(worldName, "");
						GhostPlayer.config.save();
						String message = GhostPlayer.messages.Message_15.replaceAll("/world/", worldName);
                    	sender.sendMessage("[Ghost Player] " + message); // /world/ has been removed to the list !
					} 
                	catch(InvalidConfigurationException ex) {
						ex.printStackTrace();
					}
        		}
        	}
        	else {
        		if(sender instanceof Player) {
        			sender.sendMessage(ChatColor.RED + GhostPlayer.messages.Message_16); // Simply type /humanworld <world> or /hw <world> to add a world where the plugin is disabled !
        		}
        		else {
        			sender.sendMessage(ChatColor.RED + "[Ghost Player] " + GhostPlayer.messages.Message_16); // Simply type /humanworld <world> or /hw <world> to add a world where the plugin is disabled !
        		}
        	}
        }
        
        if(cmd.getName().equalsIgnoreCase("silentghost")) {
        	if(sender instanceof Player) {
                    if(player.hasPermission("ghostplayer.player.beghost")) {
                    	if(GhostPlayer.config.WorldsDisabled.toUpperCase().indexOf(player.getWorld().getName().toUpperCase()) != -1) {
                    		sender.sendMessage(ChatColor.RED + GhostPlayer.messages.Message_S3); // This plugin is disabled in this world !
                    	}
                    	else {
                    		if(GhostPlayer.ghostFactory.isGhost(player) == true) {
                    			sender.sendMessage(ChatColor.RED + GhostPlayer.messages.Message_2); // You are already a ghost !
                    		}
                    		else {
                    			if(!(GhostPlayer.config.GhostTime.equals(-1))) {
                    				try {
                            			GhostPlayer.ghostFactory.setGhost(player, true);
                            			GhostPlayer.ghostFactory.addPlayer(player);
                            			GhostPlayer.totalGhosts++;
                    					BukkitTask task = new TurnHuman(player.getName(), true).runTaskLaterAsynchronously(Bukkit.getPluginManager().getPlugin("Ghost Player"), GhostPlayer.config.GhostTime);
                    				}
                    				catch(Exception e) {
                    					try {
                    						sender.sendMessage(ChatColor.RED + GhostPlayer.messages.Message_17); // Invalid number in config for 'GhostTime'. It will be set to 'FOREVER'.
                        					GhostPlayer.config.GhostTime = -1;
											GhostPlayer.config.save();
										}
                    					catch (InvalidConfigurationException ex) {
											ex.printStackTrace();
										}
                    				}
                    			}
                    			else {
                    				GhostPlayer.ghostFactory.setGhost(player, true);
                        			GhostPlayer.ghostFactory.addPlayer(player);
                        			GhostPlayer.totalGhosts++;
                    			}
                    		}
                    	}
                    }
                    else {
                    	sender.sendMessage(ChatColor.RED + GhostPlayer.messages.Message_S1); // You don't have permission to do this !
                    }
        	}
        	else {
        		if(args.length == 1) {
	        		if(Bukkit.getOfflinePlayer(args[0]).isOnline()) {
	        			player = Bukkit.getPlayer(args[0]);
	        			if(GhostPlayer.config.WorldsDisabled.toUpperCase().indexOf(player.getWorld().getName().toUpperCase()) != -1) {
                    		sender.sendMessage(ChatColor.RED + "[Ghost Player] " + GhostPlayer.messages.Message_S3); // This plugin is disabled in this world !
                    	}
                    	else {
                    		if(GhostPlayer.ghostFactory.isGhost(player) == true) {
                    			sender.sendMessage(ChatColor.RED + "[Ghost Player] " + GhostPlayer.messages.Message_29); // This player is already a ghost !
                    		}
                    		else {
                    			if(!(GhostPlayer.config.GhostTime.equals(-1))) {
                    				try {
                            			GhostPlayer.ghostFactory.setGhost(player, true);
                            			GhostPlayer.ghostFactory.addPlayer(player);
                            			GhostPlayer.totalGhosts++;
                    					BukkitTask task = new TurnHuman(player.getName(), true).runTaskLaterAsynchronously(Bukkit.getPluginManager().getPlugin("Ghost Player"), GhostPlayer.config.GhostTime);
                    				}
                    				catch(Exception e) {
                    					try {
                    						sender.sendMessage(ChatColor.RED + "[Ghost Player] " + GhostPlayer.messages.Message_17); // Invalid number in config for 'GhostTime'. It will be set to 'FOREVER'.
                        					GhostPlayer.config.GhostTime = -1;
											GhostPlayer.config.save();
										}
                    					catch (InvalidConfigurationException ex) {
											ex.printStackTrace();
										}
                    				}
                    			}
                    			else {
                    				GhostPlayer.ghostFactory.setGhost(player, true);
                        			GhostPlayer.ghostFactory.addPlayer(player);
                        			GhostPlayer.totalGhosts++;
                    			}
                    		}
                    	}
	        		}
	        		else {
	        			sender.sendMessage(ChatColor.RED + "[Ghost Player] " + GhostPlayer.messages.Message_S4); // This player is offline !
	        		}
        		}
        		else {
        			sender.sendMessage(ChatColor.RED + "[Ghost Player] " + GhostPlayer.messages.Message_S5); // You must have at least one argument !
        		}
        	}
        }
       
        if(cmd.getName().equalsIgnoreCase("ghost")) {
        	if(sender instanceof Player) {
                    if(player.hasPermission("ghostplayer.player.beghost")) {
                    	if(GhostPlayer.config.WorldsDisabled.toUpperCase().indexOf(player.getWorld().getName().toUpperCase()) != -1) {
                    		sender.sendMessage(ChatColor.RED + GhostPlayer.messages.Message_S3); // This plugin is disabled in this world !
                    	}
                    	else {
                    		if(GhostPlayer.ghostFactory.isGhost(player) == true) {
                    			sender.sendMessage(ChatColor.RED + GhostPlayer.messages.Message_2); // You are already a ghost !
                    		}
                    		else {
                    			if(!(GhostPlayer.config.GhostTime.equals(-1))) {
                    				try {
                    					GhostPlayer.ghostFactory.setGhost(player, true);
                    					GhostPlayer.ghostFactory.addPlayer(player);
                    					sender.sendMessage(GhostPlayer.messages.Message_3); // You are a ghost now !
                    					GhostPlayer.totalGhosts++;
                    					BukkitTask task = new TurnHuman(player.getName(), false).runTaskLaterAsynchronously(Bukkit.getPluginManager().getPlugin("Ghost Player"), GhostPlayer.config.GhostTime);
                    				}
                    				catch(Exception e) {
                    					try {
                    						sender.sendMessage(ChatColor.RED + GhostPlayer.messages.Message_17); // Invalid number in config for 'GhostTime'. It will be set to 'FOREVER'.
                        					GhostPlayer.config.GhostTime = -1;
											GhostPlayer.config.save();
										}
                    					catch (InvalidConfigurationException ex) {
											ex.printStackTrace();
                    					}
                    				}
                    			}
                    			else {
                    				GhostPlayer.ghostFactory.setGhost(player, true);
                					GhostPlayer.ghostFactory.addPlayer(player);
                					sender.sendMessage(GhostPlayer.messages.Message_3); // You are a ghost now !
                					GhostPlayer.totalGhosts++;
                    			}
                    		}
                    	}
                    }
                    else {
                    	sender.sendMessage(ChatColor.RED + GhostPlayer.messages.Message_S1); // You don't have permission to do this !
                    }
        	}
        	else {
        		if(args.length == 1) {
	        		if(Bukkit.getOfflinePlayer(args[0]).isOnline()) {
	        			player = Bukkit.getPlayer(args[0]);
	        			if(GhostPlayer.config.WorldsDisabled.toUpperCase().indexOf(player.getWorld().getName().toUpperCase()) != -1) {
                    		sender.sendMessage(ChatColor.RED + "[Ghost Player] " + GhostPlayer.messages.Message_S3); // This plugin is disabled in this world !
                    	}
                    	else {
                    		if(GhostPlayer.ghostFactory.isGhost(player) == true) {
                    			sender.sendMessage(ChatColor.RED + "[Ghost Player] " + GhostPlayer.messages.Message_29); // This player is already a ghost !
                    		}
                    		else {
                    			if(!(GhostPlayer.config.GhostTime.equals(-1))) {
                    				try {
                            			GhostPlayer.ghostFactory.setGhost(player, true);
                            			GhostPlayer.ghostFactory.addPlayer(player);
                            			player.sendMessage(GhostPlayer.messages.Message_3); // You are a ghost now !
                            			GhostPlayer.totalGhosts++;
                    					BukkitTask task = new TurnHuman(player.getName(), false).runTaskLaterAsynchronously(Bukkit.getPluginManager().getPlugin("Ghost Player"), GhostPlayer.config.GhostTime);
                    				}
                    				catch(Exception e) {
                    					try {
                    						sender.sendMessage(ChatColor.RED + "[Ghost Player] " + GhostPlayer.messages.Message_17); // Invalid number in config for 'GhostTime'. It will be set to 'FOREVER'.
                        					GhostPlayer.config.GhostTime = -1;
											GhostPlayer.config.save();
										}
                    					catch (InvalidConfigurationException ex) {
											ex.printStackTrace();
										}
                    				}
                    			}
                    			else {
                    				GhostPlayer.ghostFactory.setGhost(player, true);
                        			GhostPlayer.ghostFactory.addPlayer(player);
                        			player.sendMessage(GhostPlayer.messages.Message_3); // You are a ghost now !
                        			GhostPlayer.totalGhosts++;
                    			}
                    		}
                    	}
	        		}
	        		else {
	        			sender.sendMessage(ChatColor.RED + "[Ghost Player] " + GhostPlayer.messages.Message_S4); // This player is offline !
	        		}
        		}
        		else {
        			sender.sendMessage(ChatColor.RED + "[Ghost Player] " + GhostPlayer.messages.Message_S5); // You must have at least one argument !
        		}
        	}
        }
        
        if(cmd.getName().equalsIgnoreCase("removeghost")) {
        	if(args.length == 1) {
        		if(sender instanceof Player) {
                    if(player.hasPermission("ghostplayer.admin.removeghost")) {
                    	if(GhostPlayer.config.WorldsDisabled.toUpperCase().indexOf(player.getWorld().getName().toUpperCase()) != -1) {
                    		sender.sendMessage(ChatColor.RED + GhostPlayer.messages.Message_S3); // This plugin is disabled in this world !
                    	}
                    	else {
                    		try {
                    			if(GhostPlayer.ghostFactory.isGhost(Bukkit.getPlayer(args[0])) == true) {
                    				String message1 = GhostPlayer.messages.Message_4.replaceAll("/target/", Bukkit.getPlayer(args[0]).getName());
                    				String message2 = GhostPlayer.messages.Message_5.replaceAll("/sender/", player.getName());
                    				GhostPlayer.ghostFactory.setGhost(Bukkit.getPlayer(args[0]), false);
                    				GhostPlayer.ghostFactory.removePlayer(Bukkit.getPlayer(args[0]));
                    				sender.sendMessage(message1); // Has been removed from the ghosts !
                    				Bukkit.getPlayer(args[0]).sendMessage(message2); // Has removed you from the ghosts !
                    			}
                    			else {
                    				GhostPlayer.messages.Message_6 = GhostPlayer.messages.Message_6.replaceAll("/target/", Bukkit.getPlayer(args[0]).getName());
    	        					sender.sendMessage(ChatColor.RED + GhostPlayer.messages.Message_6); // Is already an human !
    	        					}
                    			}
                    			catch(NullPointerException e) {
                    				GhostPlayer.messages.Message_7 = GhostPlayer.messages.Message_7.replaceAll("/target/", args[0]);
                    				sender.sendMessage(ChatColor.RED + GhostPlayer.messages.Message_7); // Does not exist or not connected !
                    			}
                    		}
                    	}
                    else {
                    	sender.sendMessage(ChatColor.RED + GhostPlayer.messages.Message_S1); // You don't have permission to do this !
                    }
        		}
        		else {
        			try {
        				if(GhostPlayer.ghostFactory.isGhost(Bukkit.getPlayer(args[0])) == true) {
        					String message = GhostPlayer.messages.Message_4.replaceAll("/target/", Bukkit.getPlayer(args[0]).getName());
        					GhostPlayer.ghostFactory.setGhost(Bukkit.getPlayer(args[0]), false);
        					GhostPlayer.ghostFactory.removePlayer(Bukkit.getPlayer(args[0]));
        					sender.sendMessage("[Ghost Player] " + message); // Has been removed from the ghosts !
        					Bukkit.getPlayer(args[0]).sendMessage(GhostPlayer.messages.Message_8); // You have been removed from the ghosts !
        				}
        				else {
        					GhostPlayer.messages.Message_6 = GhostPlayer.messages.Message_6.replaceAll("/target/", Bukkit.getPlayer(args[0]).getName());
        					sender.sendMessage(ChatColor.RED + "[Ghost Player] " + GhostPlayer.messages.Message_6); // Is already an human !
        				}
        			}
        			catch(NullPointerException e) {
        				String message = GhostPlayer.messages.Message_7.replaceAll("/target/", args[0]);
        				sender.sendMessage(ChatColor.RED + "[Ghost Player] " + message); // Does not exist or not connected !
        			}
        		}
        	}
        	else {
        		if(sender instanceof Player) {
        			sender.sendMessage(ChatColor.RED + GhostPlayer.messages.Message_12); // Simply type /removeghost <player> or /rg <player> to remove <player> from the ghosts !
        		}
        		else {
        			sender.sendMessage(ChatColor.RED + "[Ghost Player] " + GhostPlayer.messages.Message_12); // Simply type /removeghost <player> or /rg <player> to remove <player> from the ghosts !
        		}
        	}
        }
        
        if(cmd.getName().equalsIgnoreCase("removeghosthunter")) {
        	if(args.length == 1) {
        		if(sender instanceof Player) {
                    if(player.hasPermission("ghostplayer.admin.removeghosthunter")) {
                    	if(GhostPlayer.config.WorldsDisabled.toUpperCase().indexOf(player.getWorld().getName().toUpperCase()) != -1) {
                    		sender.sendMessage(ChatColor.RED + GhostPlayer.messages.Message_S3); // This plugin is disabled in this world !
                    	}
                    	else {
                    		try {
                    			if(GhostPlayer.ghostFactory.isGhost(Bukkit.getPlayer(args[0])) == true) {
                    				String message = GhostPlayer.messages.Message_21.replaceAll("/target/", Bukkit.getPlayer(args[0]).getName());                  				
                    				sender.sendMessage(ChatColor.RED + message); // /target/ is not a ghost hunter !
                    			}
                    			else {
                    				if(GhostPlayer.ghostFactory.hasPlayer(Bukkit.getPlayer(args[0])) == true) {
                        				GhostPlayer.ghostFactory.removePlayer(Bukkit.getPlayer(args[0]));
                        				String message1 = GhostPlayer.messages.Message_22.replaceAll("/target/", Bukkit.getPlayer(args[0]).getName());
                        				String message2 = GhostPlayer.messages.Message_24.replaceAll("/sender/", player.getName());
                        				sender.sendMessage(message1); // /target/ has been removed from the ghosts hunters !
                        				Bukkit.getPlayer(args[0]).sendMessage(message2); // /sender/ been removed from the ghosts hunters !
                        			}
                    				else {
                    					GhostPlayer.messages.Message_6 = GhostPlayer.messages.Message_6.replaceAll("/target/", Bukkit.getPlayer(args[0]).getName());
        	        					sender.sendMessage(ChatColor.RED + GhostPlayer.messages.Message_6); // Is already an human !
                    				}
    	        					}
                    			}
                    			catch(Exception e) {
                    				GhostPlayer.messages.Message_7 = GhostPlayer.messages.Message_7.replaceAll("/target/", args[0]);
                    				sender.sendMessage(ChatColor.RED + GhostPlayer.messages.Message_7); // Does not exist or not connected !
                    			}
                    		}
                    	}
                    else {
                    	sender.sendMessage(ChatColor.RED + GhostPlayer.messages.Message_S1); // You don't have permission to do this !
                    }
        		}
        		else {
        			try {
            			if(GhostPlayer.ghostFactory.isGhost(Bukkit.getPlayer(args[0])) == true) {
            				String message = GhostPlayer.messages.Message_21.replaceAll("/target/", Bukkit.getPlayer(args[0]).getName());                  				
            				sender.sendMessage(ChatColor.RED + "[Ghost Player] " + message); // /target/ is not a ghost hunter !
            			}
            			else {
            				if(GhostPlayer.ghostFactory.hasPlayer(Bukkit.getPlayer(args[0])) == true) {
                				GhostPlayer.ghostFactory.removePlayer(Bukkit.getPlayer(args[0]));
                				String message = GhostPlayer.messages.Message_22.replaceAll("/target/", Bukkit.getPlayer(args[0]).getName());                  				
                				sender.sendMessage("[Ghost Player] " + message); // /target/ has been removed from the ghosts hunters !
                				Bukkit.getPlayer(args[0]).sendMessage(GhostPlayer.messages.Message_23); // You have been removed from the ghosts hunters !
                			}
            				else {
            					GhostPlayer.messages.Message_6 = GhostPlayer.messages.Message_6.replaceAll("/target/", Bukkit.getPlayer(args[0]).getName());
	        					sender.sendMessage(ChatColor.RED + "[Ghost Player] " + GhostPlayer.messages.Message_6); // Is already an human !
            				}
        					}
            			}
            			catch(Exception e) {
            				String message = GhostPlayer.messages.Message_7.replaceAll("/target/", args[0]);
            				sender.sendMessage(ChatColor.RED + "[Ghost Player] " + message); // Does not exist or not connected !
            			}
        		}
        	}
        	else {
        		if(sender instanceof Player) {
        			sender.sendMessage(ChatColor.RED + GhostPlayer.messages.Message_25); // Simply type /removeghosthunter <player> or /rgh <player> to remove <player> from the ghosts hunters !
        		}
        		else {
        			sender.sendMessage(ChatColor.RED + "[Ghost Player] " + GhostPlayer.messages.Message_25); // Simply type /removeghosthunter <player> or /rgh <player> to remove <player> from the ghosts hunters !
        		}
        	}
        }
        
        if(cmd.getName().equalsIgnoreCase("silenthuman")) {
        	if(sender instanceof Player) {
        		if(player.hasPermission("ghostplayer.player.behuman")) {
                	if(GhostPlayer.config.WorldsDisabled.toUpperCase().indexOf(player.getWorld().getName().toUpperCase()) != -1) {
                		sender.sendMessage(ChatColor.RED + GhostPlayer.messages.Message_S3); // This plugin is disabled in this world !
                	}
                	else {
                		if(GhostPlayer.ghostFactory.isGhost(player) == true) {
            				GhostPlayer.ghostFactory.setGhost(player, false);
            				GhostPlayer.ghostFactory.removePlayer(player);
        				}
                		else {
                			sender.sendMessage(ChatColor.RED + GhostPlayer.messages.Message_9); // You are already an human !
                		}
                	}
        		}
        		else {
        			sender.sendMessage(ChatColor.RED + GhostPlayer.messages.Message_S1); // You don't have permission to do this !
        		}
        	}
        	else {
        		if(args.length == 1) {
	        		if(Bukkit.getOfflinePlayer(args[0]).isOnline()) {
	        			player = Bukkit.getPlayer(args[0]);
	        			if(GhostPlayer.config.WorldsDisabled.toUpperCase().indexOf(player.getWorld().getName().toUpperCase()) != -1) {
                    		sender.sendMessage(ChatColor.RED + "[Ghost Player] " + GhostPlayer.messages.Message_S3); // This plugin is disabled in this world !
                    	}
                    	else {
                    		if(GhostPlayer.ghostFactory.isGhost(player) == true) {
                				GhostPlayer.ghostFactory.setGhost(player, false);
                				GhostPlayer.ghostFactory.removePlayer(player);
	        				}
                    		else {
                    			sender.sendMessage(ChatColor.RED + "[Ghost Player] " + GhostPlayer.messages.Message_30); // This player is already an human !
                    		}
                    	}
	        		}
	        		else {
	        			sender.sendMessage(ChatColor.RED + "[Ghost Player] " + GhostPlayer.messages.Message_S4); // This player is offline !
	        		}
        		}
        		else {
        			sender.sendMessage(ChatColor.RED + "[Ghost Player] " + GhostPlayer.messages.Message_S5); // You must have at least one argument !
        		}
        	}
        }
        
        if(cmd.getName().equalsIgnoreCase("human")) {
        	if(sender instanceof Player) {
        		if(player.hasPermission("ghostplayer.player.behuman")) {
                	if(GhostPlayer.config.WorldsDisabled.toUpperCase().indexOf(player.getWorld().getName().toUpperCase()) != -1) {
                		sender.sendMessage(ChatColor.RED + GhostPlayer.messages.Message_S3); // This plugin is disabled in this world !
                	}
                	else {
                		if(GhostPlayer.ghostFactory.hasPlayer(player) == true) {
	        				GhostPlayer.ghostFactory.setGhost(player, false);
	        				GhostPlayer.ghostFactory.removePlayer(player);
	        				sender.sendMessage(GhostPlayer.messages.Message_11); // You are an human now !
                		}
                		else {
                			sender.sendMessage(ChatColor.RED + GhostPlayer.messages.Message_9); // You are already an human !
                		}
                	}
        		}
        		else {
        			sender.sendMessage(ChatColor.RED + GhostPlayer.messages.Message_S1); // You don't have permission to do this !
        		}
        	}
        	else {
        		if(args.length == 1) {
	        		if(Bukkit.getOfflinePlayer(args[0]).isOnline()) {
	        			player = Bukkit.getPlayer(args[0]);
	        			if(GhostPlayer.config.WorldsDisabled.toUpperCase().indexOf(player.getWorld().getName().toUpperCase()) != -1) {
                    		sender.sendMessage(ChatColor.RED + "[Ghost Player] " + GhostPlayer.messages.Message_S3); // This plugin is disabled in this world !
                    	}
                    	else {
                    		if(GhostPlayer.ghostFactory.isGhost(player) == true) {
                				GhostPlayer.ghostFactory.setGhost(player, false);
                				GhostPlayer.ghostFactory.removePlayer(player);
                				player.sendMessage(GhostPlayer.messages.Message_11); // You are an human now !
	        				}
                    		else {
                    			sender.sendMessage(ChatColor.RED + "[Ghost Player] " + GhostPlayer.messages.Message_30); // This player is already an human !
                    		}
                    	}
	        		}
	        		else {
	        			sender.sendMessage(ChatColor.RED + "[Ghost Player] " + GhostPlayer.messages.Message_S4); // This player is offline !
	        		}
        		}
        		else {
        			sender.sendMessage(ChatColor.RED + "[Ghost Player] " + GhostPlayer.messages.Message_S5); // You must have at least one argument !
        		}
        	}
        }
        
        if(cmd.getName().equalsIgnoreCase("clearsghosts")) {
        	if(sender instanceof Player) {
        		if(player.hasPermission("ghostplayer.admin.clearsghosts")) {
                	if(GhostPlayer.config.WorldsDisabled.toUpperCase().indexOf(player.getWorld().getName().toUpperCase()) != -1) {
                		sender.sendMessage(ChatColor.RED + GhostPlayer.messages.Message_S3); // This plugin is disabled in this world !
                	}
                	else {
                		GhostPlayer.ghostFactory.clearMembers();
                		sender.sendMessage(GhostPlayer.messages.Message_10); // All ghosts have been cleared !
                	}
        		}
        		else {
        			sender.sendMessage(ChatColor.RED + GhostPlayer.messages.Message_S1); // You don't have permission to do this !
        		}
        	}
        	else {
    			GhostPlayer.ghostFactory.clearMembers();
    			sender.sendMessage("[Ghost Player] " + GhostPlayer.messages.Message_10); // All ghosts have been cleared !
        	}
        }
        return true;
    }

}
