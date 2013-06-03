package com.skyost.gp;

import java.io.File;

import org.bukkit.plugin.Plugin;

public class GhostPlayerConfig extends Config {
	public GhostPlayerConfig(Plugin plugin) {
		CONFIG_FILE = new File(plugin.getDataFolder(), "config.yml");
		CONFIG_HEADER = "Ghost Player Configuration";
	}
	public boolean AutoUpdateOnLoad = true;
	public boolean TurnIntoGhostOnDeath = false;
}
