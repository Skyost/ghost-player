package fr.skyost.ghosts.config;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.plugin.Plugin;

import fr.skyost.ghosts.utils.Config;

public class GhostPlayerConfig extends Config {
	
	public boolean AutoUpdateOnLoad = true;
	public boolean TurnIntoGhostOnDeath = false;
	public boolean GhostsCanInteract = true;
	public List<String> HumanWorlds = new ArrayList<String>();
	public Integer GhostTime = -1;
	
	public GhostPlayerConfig(Plugin plugin) {
		CONFIG_FILE = new File(plugin.getDataFolder(), "config.yml");
		CONFIG_HEADER = "Ghost Player Configuration";
		CONFIG_HEADER += "\nSet '-1' to 'GhostTime' for ignoring it. If you don't want to disable it, juste set the time you want (in ticks).\nFor more informations about tick visit http://bit.ly/S1IclO.";
		CONFIG_HEADER += "\nTurnedIntoOnJoin can be (SILENT) HUMAN, GHOST or GHOST HUNTER.";
		
		HumanWorlds.add("WorldA");
		HumanWorlds.add("WorldB");
		HumanWorlds.add("WorldC");
	}
	
}
