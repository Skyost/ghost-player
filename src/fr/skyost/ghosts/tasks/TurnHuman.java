package fr.skyost.ghosts.tasks;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import fr.skyost.ghosts.GhostPlayer;

public class TurnHuman extends BukkitRunnable {
	private final String playername;
	private final Boolean isSilent;

	public TurnHuman(final String playername, final Boolean isSilent) {
		this.playername = playername;
		this.isSilent = isSilent;
	}

	@Override
	public void run() {
		final Player player = Bukkit.getPlayer(playername);
		if(player != null && player.isOnline()) {
			if(isSilent == true) {
				GhostPlayer.ghostManager.setGhost(player, false);
				GhostPlayer.ghostManager.removePlayer(player);
			}
			else {
				GhostPlayer.ghostManager.setGhost(player, false);
				GhostPlayer.ghostManager.removePlayer(player);
				player.sendMessage(GhostPlayer.messages.Message_11); // You are an human now !
			}
		}
	}

}
