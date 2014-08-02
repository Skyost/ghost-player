package fr.skyost.ghosts.listeners;

import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.skyost.ghosts.GhostPlayer;
import fr.skyost.ghosts.tasks.TurnHuman;

public class CommandsExecutor implements CommandExecutor {

	@Override
	@SuppressWarnings("deprecation")
	public boolean onCommand(final CommandSender sender, final Command cmd, final String label, final String[] args) {
		Player player = null;

		if(sender instanceof Player) {
			player = (Player) sender;
		}

		if(cmd.getName().equalsIgnoreCase("ghostview")) {
			if(sender instanceof Player) {
				if(sender.hasPermission("ghostplayer.player.ghostview")) {
					if(GhostPlayer.config.humanWorlds.contains(player.getWorld().getName())) {
						sender.sendMessage(ChatColor.RED + GhostPlayer.messages.messageS3); // This plugin is disabled in this world !
					}
					else {
						if(GhostPlayer.ghostManager.hasPlayer(player)) {
							player.sendBlockChange(player.getTargetBlock(null, 10).getLocation(), Material.AIR, (byte)0);
						}
						else {
							sender.sendMessage(ChatColor.RED + GhostPlayer.messages.message1); // An human can't do this !
						}
					}
				}
				else {
					sender.sendMessage(ChatColor.RED + GhostPlayer.messages.messageS1); // You don't have permission to do this !
				}
			}
			else {
				sender.sendMessage(ChatColor.RED + GhostPlayer.messages.messageS2); // You can't do this from the console !
			}
		}

		if(cmd.getName().equalsIgnoreCase("ghosthunter")) {
			if(sender.hasPermission("ghostplayer.player.beghosthunter")) {
				if(!(sender instanceof Player)) {
					if(args.length >= 1) {
						player = Bukkit.getPlayer(args[0]);
						if(player == null) {
							sender.sendMessage(ChatColor.RED + GhostPlayer.messages.messageS4); // This player is offline !
							return true;
						}
					}
					else {
						sender.sendMessage(ChatColor.RED + GhostPlayer.messages.messageS5); // You must have at least one argument !
						return true;
					}
				}
				if(GhostPlayer.config.humanWorlds.contains(player.getWorld().getName())) {
					sender.sendMessage(ChatColor.RED + GhostPlayer.messages.messageS3); // This plugin is disabled in this world !
				}
				else {
					if(GhostPlayer.ghostManager.isGhost(player)) {
						if(sender instanceof Player) {
							sender.sendMessage(ChatColor.RED + GhostPlayer.messages.message18); // You are already a ghost so you don't need to be a ghost hunter to see your friends !
						}
						else {
							sender.sendMessage(ChatColor.RED + GhostPlayer.messages.message26); // This player is already a ghost so he don't need to be a ghost hunter !
						}
					}
					else if(GhostPlayer.ghostManager.hasPlayer(player)) {
						if(sender instanceof Player) {
							sender.sendMessage(ChatColor.RED + GhostPlayer.messages.message19); // You are already a ghost hunter !
						}
						else {
							sender.sendMessage(ChatColor.RED + GhostPlayer.messages.message27); // This player is already a ghost hunter !
						}
					}
					else {
						GhostPlayer.ghostManager.addPlayer(player);
						player.sendMessage(GhostPlayer.messages.message20); // You are a ghost hunter now !
					}
				}
			}
			else {
				sender.sendMessage(ChatColor.RED + GhostPlayer.messages.messageS1); // You don't have permission to do this !
			}
		}

		if(cmd.getName().equalsIgnoreCase("silentghosthunter")) {
			if(sender.hasPermission("ghostplayer.player.beghosthunter")) {
				if(!(sender instanceof Player)) {
					if(args.length >= 1) {
						player = Bukkit.getPlayer(args[0]);
						if(player == null) {
							sender.sendMessage(ChatColor.RED + GhostPlayer.messages.messageS4); // This player is offline !
							return true;
						}
					}
					else {
						sender.sendMessage(ChatColor.RED + GhostPlayer.messages.messageS5); // You must have at least one argument !
						return true;
					}
				}
				if(GhostPlayer.config.humanWorlds.contains(player.getWorld().getName())) {
					sender.sendMessage(ChatColor.RED + GhostPlayer.messages.messageS3); // This plugin is disabled in this world !
				}
				else {
					if(GhostPlayer.ghostManager.isGhost(player)) {
						if(sender instanceof Player) {
							sender.sendMessage(ChatColor.RED + GhostPlayer.messages.message18); // You are already a ghost so you don't need to be a ghost hunter to see your friends !
						}
						else {
							sender.sendMessage(ChatColor.RED + GhostPlayer.messages.message26); // This player is already a ghost so he don't need to be a ghost hunter !
						}
					}
					else if(GhostPlayer.ghostManager.hasPlayer(player)) {
						if(sender instanceof Player) {
							sender.sendMessage(ChatColor.RED + GhostPlayer.messages.message19); // You are already a ghost hunter !
						}
						else {
							sender.sendMessage(ChatColor.RED + GhostPlayer.messages.message27); // This player is already a ghost hunter !
						}
					}
					else {
						GhostPlayer.ghostManager.addPlayer(player);
					}
				}
			}
			else {
				sender.sendMessage(ChatColor.RED + GhostPlayer.messages.messageS1); // You don't have permission to do this !
			}
		}

		if(cmd.getName().equalsIgnoreCase("humanworld")) {
			if(args.length >= 1) {
				if(sender.hasPermission("ghostplayer.admin.sethumanworld")) {
					try {
						final String worldName = Arrays.toString(args).substring(1,  Arrays.toString(args).length() - 1).replaceAll(",", "");
						if(!GhostPlayer.config.humanWorlds.contains(worldName)) {
							GhostPlayer.config.humanWorlds.add(worldName);
							GhostPlayer.config.save();
						}
						final World world = Bukkit.getWorld(worldName);
						if(world != null) {
							for(final Player ghostPlayer : world.getPlayers()) {
								if(GhostPlayer.ghostManager.isGhost(ghostPlayer)) {
									GhostPlayer.ghostManager.setGhost(ghostPlayer, false);
									GhostPlayer.ghostManager.removePlayer(ghostPlayer);
								}
							}
						}
						final String message = GhostPlayer.messages.message13.replaceAll("/world/", worldName);
						sender.sendMessage(message); // /world/ has been added to the list !
					}
					catch(final Exception ex) {
						ex.printStackTrace();
					}
				}
				else {
					sender.sendMessage(ChatColor.RED + GhostPlayer.messages.messageS1); // You don't have permission to do this !
				}
			}
			else {
				sender.sendMessage(ChatColor.RED + GhostPlayer.messages.message14); // Simply type /humanworld <world> or /hw <world> to add a world where the plugin is disabled !
			}
		}

		if(cmd.getName().equalsIgnoreCase("ghostworld")) {
			if(args.length >= 1) {
				if(sender.hasPermission("ghostplayer.admin.setghostworld")) {
					try {
						final String worldName = Arrays.toString(args).substring(1,  Arrays.toString(args).length() - 1).replaceAll(",", "");
						if(GhostPlayer.config.humanWorlds.contains(worldName)) {
							GhostPlayer.config.humanWorlds.remove(worldName);
							GhostPlayer.config.save();
						}
						final String message = GhostPlayer.messages.message15.replaceAll("/world/", worldName);
						sender.sendMessage(message); // /world/ has been removed to the list !
					}
					catch(final Exception ex) {
						ex.printStackTrace();
					}
				}
				else {
					sender.sendMessage(ChatColor.RED + GhostPlayer.messages.messageS1); // You don't have permission to do this !
				}
			}
			else {
				sender.sendMessage(ChatColor.RED + GhostPlayer.messages.message16); // Simply type /humanworld <world> or /hw <world> to add a world where the plugin is disabled !
			}
		}

		if(cmd.getName().equalsIgnoreCase("silentghost")) {
			if(sender.hasPermission("ghostplayer.player.beghost")) {
				if(!(sender instanceof Player)) {
					if(args.length >= 1) {
						player = Bukkit.getPlayer(args[0]);
						if(player == null) {
							sender.sendMessage(ChatColor.RED + GhostPlayer.messages.messageS4); // This player is offline !
							return true;
						}
					}
					else {
						sender.sendMessage(ChatColor.RED + GhostPlayer.messages.messageS5); // You must have at least one argument !
						return true;
					}
				}
				if(GhostPlayer.config.humanWorlds.contains(player.getWorld().getName())) {
					sender.sendMessage(ChatColor.RED + GhostPlayer.messages.messageS3); // This plugin is disabled in this world !
				}
				else {
					if(GhostPlayer.ghostManager.isGhost(player)) {
						if(sender instanceof Player) {
							sender.sendMessage(ChatColor.RED + GhostPlayer.messages.message2); // You are already a ghost !
						}
						else {
							sender.sendMessage(ChatColor.RED + GhostPlayer.messages.message28); // This player is already a ghost !
						}
					}
					else {
						try {
							GhostPlayer.ghostManager.setGhost(player, true);
							GhostPlayer.ghostManager.addPlayer(player);
							GhostPlayer.totalGhosts++;
							if(GhostPlayer.config.ghostTime != -1) {
								new TurnHuman(player.getName(), true).runTaskLaterAsynchronously(Bukkit.getPluginManager().getPlugin("Ghost Player"), GhostPlayer.config.ghostTime);
							}
						}
						catch(final Exception e) {
							try {
								sender.sendMessage(ChatColor.RED + GhostPlayer.messages.message17); // Invalid number in config for 'GhostTime'. It will be set to 'FOREVER'.
								GhostPlayer.config.ghostTime = -1;
								GhostPlayer.config.save();
							}
							catch(final Exception ex) {
								ex.printStackTrace();
							}
						}
					}
				}
			}
			else {
				sender.sendMessage(ChatColor.RED + GhostPlayer.messages.messageS1); // You don't have permission to do this !
			}
		}

		if(cmd.getName().equalsIgnoreCase("ghost")) {
			if(sender.hasPermission("ghostplayer.player.beghost")) {
				if(!(sender instanceof Player)) {
					if(args.length >= 1) {
						player = Bukkit.getPlayer(args[0]);
						if(player == null) {
							sender.sendMessage(ChatColor.RED + GhostPlayer.messages.messageS4); // This player is offline !
							return true;
						}
					}
					else {
						sender.sendMessage(ChatColor.RED + GhostPlayer.messages.messageS5); // You must have at least one argument !
						return true;
					}
				}
				if(GhostPlayer.config.humanWorlds.contains(player.getWorld().getName())) {
					sender.sendMessage(ChatColor.RED + GhostPlayer.messages.messageS3); // This plugin is disabled in this world !
				}
				else {
					if(GhostPlayer.ghostManager.isGhost(player)) {
						if(sender instanceof Player) {
							sender.sendMessage(ChatColor.RED + GhostPlayer.messages.message2); // You are already a ghost !
						}
						else {
							sender.sendMessage(ChatColor.RED + GhostPlayer.messages.message28); // This player is already a ghost !
						}
					}
					else {
						try {
							GhostPlayer.ghostManager.setGhost(player, true);
							GhostPlayer.ghostManager.addPlayer(player);
							player.sendMessage(GhostPlayer.messages.message3); // You are a ghost now !
							GhostPlayer.totalGhosts++;
							if(GhostPlayer.config.ghostTime != -1) {
								new TurnHuman(player.getName(), false).runTaskLaterAsynchronously(Bukkit.getPluginManager().getPlugin("Ghost Player"), GhostPlayer.config.ghostTime);
							}
						}
						catch(final Exception e) {
							try {
								sender.sendMessage(ChatColor.RED + GhostPlayer.messages.message17); // Invalid number in config for 'GhostTime'. It will be set to 'FOREVER'.
								GhostPlayer.config.ghostTime = -1;
								GhostPlayer.config.save();
							}
							catch(final Exception ex) {
								ex.printStackTrace();
							}
						}
					}
				}
			}
			else {
				sender.sendMessage(ChatColor.RED + GhostPlayer.messages.messageS1); // You don't have permission to do this !
			}
		}

		if(cmd.getName().equalsIgnoreCase("removeghost")) {
			if(args.length >= 1) {
				if(sender.hasPermission("ghostplayer.admin.removeghost")) {
					player = Bukkit.getPlayer(args[0]);
					if(player != null) {
						if(GhostPlayer.config.humanWorlds.contains(player.getWorld().getName())) {
							sender.sendMessage(ChatColor.RED + GhostPlayer.messages.messageS3); // This plugin is disabled in this world !
							return true;
						}
						if(GhostPlayer.ghostManager.isGhost(player)) {
							final String message1 = GhostPlayer.messages.message4.replaceAll("/target/", player.getName());
							GhostPlayer.ghostManager.setGhost(player, false);
							GhostPlayer.ghostManager.removePlayer(player);
							sender.sendMessage(message1); // Has been removed from the ghosts !
							if(sender instanceof Player) {
								final String message2 = GhostPlayer.messages.message5.replaceAll("/sender/", sender.getName());
								player.sendMessage(message2); // Has removed you from the ghosts !
							}
							else {
								player.sendMessage(GhostPlayer.messages.message8); // You have been removed from the ghosts !
							}
						}
						else {
							final String message = GhostPlayer.messages.message6.replaceAll("/target/", player.getName());
							sender.sendMessage(ChatColor.RED + message); // Is already an human !
						}
					}
					else {
						final String message = GhostPlayer.messages.message7.replaceAll("/target/", args[0]);
						sender.sendMessage(ChatColor.RED + message); // Does not exist or not connected !
					}
				}
				else {
					sender.sendMessage(ChatColor.RED + GhostPlayer.messages.messageS1); // You don't have permission to do this !
				}
			}
			else {
				sender.sendMessage(ChatColor.RED + GhostPlayer.messages.message12); // Simply type /removeghost <player> or /rg <player> to remove <player> from the ghosts !
			}
		}

		if(cmd.getName().equalsIgnoreCase("removeghosthunter")) {
			if(args.length >= 1) {
				if(sender.hasPermission("ghostplayer.admin.removeghosthunter")) {
					player = Bukkit.getPlayer(args[0]);
					if(player != null) {
						if(GhostPlayer.config.humanWorlds.contains(player.getWorld().getName())) {
							sender.sendMessage(ChatColor.RED + GhostPlayer.messages.messageS3); // This plugin is disabled in this world !
							return true;
						}
						if(GhostPlayer.ghostManager.isGhost(player)) {
							final String message = GhostPlayer.messages.message21.replaceAll("/target/", player.getName());
							sender.sendMessage(ChatColor.RED + message); // /target/ is not a ghost hunter !
						}
						else {
							if(GhostPlayer.ghostManager.hasPlayer(player)) {
								final String message1 = GhostPlayer.messages.message22.replaceAll("/target/", player.getName());
								GhostPlayer.ghostManager.removePlayer(player);
								sender.sendMessage(message1); // /target/ has been removed from the ghosts hunters !
								if(sender instanceof Player) {
									final String message2 = GhostPlayer.messages.message24.replaceAll("/sender/", sender.getName());
									player.sendMessage(message2); // /sender/ been removed from the ghosts hunters !
								}
								else {
									player.sendMessage(GhostPlayer.messages.message23); // You have been removed from the ghosts hunters !
								}
							}
							else {
								GhostPlayer.messages.message6 = GhostPlayer.messages.message6.replaceAll("/target/", player.getName());
								sender.sendMessage(ChatColor.RED + GhostPlayer.messages.message6); // Is already an human !
							}
						}
					}
					else {
						final String message = GhostPlayer.messages.message7.replaceAll("/target/", args[0]);
						sender.sendMessage(ChatColor.RED + message); // Does not exist or not connected !
					}
				}
				else {
					sender.sendMessage(ChatColor.RED + GhostPlayer.messages.messageS1); // You don't have permission to do this !
				}
			}
			else {
				sender.sendMessage(ChatColor.RED + GhostPlayer.messages.message25); // Simply type /removeghosthunter <player> or /rgh <player> to remove <player> from the ghosts hunters !
			}
		}

		if(cmd.getName().equalsIgnoreCase("silenthuman")) {
			if(sender.hasPermission("ghostplayer.player.behuman")) {
				if(!(sender instanceof Player)) {
					if(args.length >= 1) {
						player = Bukkit.getPlayer(args[0]);
						if(player == null) {
							sender.sendMessage(ChatColor.RED + GhostPlayer.messages.messageS4); // This player is offline !
							return true;
						}
					}
					else {
						sender.sendMessage(ChatColor.RED + GhostPlayer.messages.messageS5); // You must have at least one argument !
						return true;
					}
				}
				if(GhostPlayer.config.humanWorlds.contains(player.getWorld().getName())) {
					sender.sendMessage(ChatColor.RED + GhostPlayer.messages.messageS3); // This plugin is disabled in this world !
				}
				else {
					if(GhostPlayer.ghostManager.isGhost(player)) {
						GhostPlayer.ghostManager.setGhost(player, false);
						GhostPlayer.ghostManager.removePlayer(player);
					}
					else {
						if(sender instanceof Player) {
							sender.sendMessage(ChatColor.RED + GhostPlayer.messages.message9); // You are already an human !
						}
						else {
							sender.sendMessage(ChatColor.RED + GhostPlayer.messages.message29); // This player is already an human !
						}
					}
				}
			}
			else {
				sender.sendMessage(ChatColor.RED + GhostPlayer.messages.messageS1); // You don't have permission to do this !
			}
		}

		if(cmd.getName().equalsIgnoreCase("human")) {
			if(sender.hasPermission("ghostplayer.player.behuman")) {
				if(!(sender instanceof Player)) {
					if(args.length >= 1) {
						player = Bukkit.getPlayer(args[0]);
						if(player == null) {
							sender.sendMessage(ChatColor.RED + GhostPlayer.messages.messageS4); // This player is offline !
							return true;
						}
					}
					else {
						sender.sendMessage(ChatColor.RED + GhostPlayer.messages.messageS5); // You must have at least one argument !
						return true;
					}
				}
				if(GhostPlayer.config.humanWorlds.contains(player.getWorld().getName())) {
					sender.sendMessage(ChatColor.RED + GhostPlayer.messages.messageS3); // This plugin is disabled in this world !
				}
				else {
					if(GhostPlayer.ghostManager.isGhost(player)) {
						GhostPlayer.ghostManager.setGhost(player, false);
						GhostPlayer.ghostManager.removePlayer(player);
						player.sendMessage(GhostPlayer.messages.message11); // You are an human now !
					}
					else {
						if(sender instanceof Player) {
							sender.sendMessage(ChatColor.RED + GhostPlayer.messages.message9); // You are already an human !
						}
						else {
							sender.sendMessage(ChatColor.RED + GhostPlayer.messages.message29); // This player is already an human !
						}
					}
				}
			}
			else {
				sender.sendMessage(ChatColor.RED + GhostPlayer.messages.messageS1); // You don't have permission to do this !
			}
		}

		if(cmd.getName().equalsIgnoreCase("clearsghosts")) {
			if(sender instanceof Player) {
				if(sender.hasPermission("ghostplayer.admin.clearsghosts")) {
					if(GhostPlayer.config.humanWorlds.contains(player.getWorld().getName())) {
						sender.sendMessage(ChatColor.RED + GhostPlayer.messages.messageS3); // This plugin is disabled in this world !
					}
					else {
						GhostPlayer.ghostManager.clearMembers();
						sender.sendMessage(GhostPlayer.messages.message10); // All ghosts have been cleared !
					}
				}
				else {
					sender.sendMessage(ChatColor.RED + GhostPlayer.messages.messageS1); // You don't have permission to do this !
				}
			}
			else {
				GhostPlayer.ghostManager.clearMembers();
				sender.sendMessage(GhostPlayer.messages.message10); // All ghosts have been cleared !
			}
		}
		return true;
	}

}