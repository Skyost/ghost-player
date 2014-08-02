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
	private final void onPlayerQuit(final PlayerQuitEvent event) {
		if(GhostPlayer.ghostManager.isGhost(event.getPlayer())) {
			GhostPlayer.ghostManager.setGhost(event.getPlayer(), false);
		}
		if(GhostPlayer.ghostManager.hasPlayer(event.getPlayer())) {
			GhostPlayer.ghostManager.removePlayer(event.getPlayer());
		}
	}

	@EventHandler
	private final void onPlayerDeath(final PlayerDeathEvent event) {
		final Player entity = event.getEntity();
		if(!GhostPlayer.config.humanWorlds.contains(entity.getWorld().getName())) {
			if(GhostPlayer.ghostManager.hasPlayer(entity)) {
				GhostPlayer.ghostManager.addPlayer(event.getEntity());
				if(GhostPlayer.config.ghostOnDeath) {
					GhostPlayer.ghostManager.setGhost(event.getEntity(), true);
				}
				else {
					GhostPlayer.ghostManager.setGhost(event.getEntity(), false);
				}
			}
		}
	}

	@EventHandler
	private final void onPlayerInteract(final PlayerInteractEvent event) {
		final Player player = event.getPlayer();
		if(!GhostPlayer.config.humanWorlds.contains(player.getWorld().getName())) {
			if(!GhostPlayer.config.ghostscanInteract) {
				if(GhostPlayer.ghostManager.isGhost(player)) {
					player.sendMessage(ChatColor.RED + GhostPlayer.messages.message30);
					event.setCancelled(true);
				}
			}
		}
	}

	@EventHandler
	private final void onPlayerDropItem(final PlayerDropItemEvent event) {
		final Player player = event.getPlayer();
		if(!GhostPlayer.config.humanWorlds.contains(player.getWorld().getName())) {
			if(!GhostPlayer.config.ghostscanInteract) {
				if(GhostPlayer.ghostManager.isGhost(player)) {
					player.sendMessage(ChatColor.RED + GhostPlayer.messages.message30);
					event.setCancelled(true);
				}
			}
		}
	}

	@EventHandler
	private final void onEntityDamage(final EntityDamageEvent event) {
		final Entity entity = event.getEntity();
		if(entity instanceof Player) {
			final Player player = (Player)entity;
			if(!GhostPlayer.config.humanWorlds.contains(player.getWorld().getName())) {
				if(!GhostPlayer.config.ghostscanInteract) {
					if(GhostPlayer.ghostManager.isGhost(player)) {
						event.setCancelled(true);
					}
				}
			}
		}
	}

	@EventHandler
	private final void onPlayerJoin(final PlayerJoinEvent event) {
		final Player player = event.getPlayer();
		if(!GhostPlayer.config.humanWorlds.contains(player.getWorld().getName())) {
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
				player.sendMessage(GhostPlayer.messages.message11); // You are an human now !
			}
			else if(player.hasPermission("ghostplayer.login.silentghost")) {
				if(!GhostPlayer.ghostManager.isGhost(player)) {
					if(!(GhostPlayer.config.ghostTime.equals(-1))) {
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
								player.sendMessage(ChatColor.RED + GhostPlayer.messages.message17); // Invalid number in config for 'GhostTime'. It will be set to 'FOREVER'.
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
			else if(player.hasPermission("ghostplayer.login.ghost")) {
				if(!GhostPlayer.ghostManager.isGhost(player)) {
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
							player.sendMessage(ChatColor.RED + GhostPlayer.messages.message17); // Invalid number in config for 'GhostTime'. It will be set to 'FOREVER'.
							GhostPlayer.config.ghostTime = -1;
							GhostPlayer.config.save();
						}
						catch(final Exception ex) {
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
					player.sendMessage(GhostPlayer.messages.message20); // You are a ghost hunter now !
				}
			}
		}
	}

}
