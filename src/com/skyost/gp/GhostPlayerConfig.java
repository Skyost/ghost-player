package com.skyost.gp;

import java.io.File;

import org.bukkit.plugin.Plugin;

public class GhostPlayerConfig extends Config {
	public GhostPlayerConfig(Plugin plugin) {
		CONFIG_FILE = new File(plugin.getDataFolder(), "config.yml");
		CONFIG_HEADER = "Ghost Player Configuration";
		CONFIG_HEADER += "\n\nWorlds where the plugin is disabled must be separed with space.";
		CONFIG_HEADER += "\nSet '-1' to 'GhostTime' for ignoring it. If you don't want to disable it, juste set the time you want (in ticks).\nFor more informations about tick visit http://bit.ly/S1IclO.";
	}
	public boolean AutoUpdateOnLoad = true;
	public boolean TurnIntoGhostOnDeath = false;
	public String WorldsDisabled = "";
	public Integer GhostTime = -1;
}
