package com.skyost.gp;

import java.io.File;

import org.bukkit.plugin.Plugin;

public class GhostPlayerConfig extends Config {
	public GhostPlayerConfig(Plugin plugin) {
		CONFIG_FILE = new File(plugin.getDataFolder(), "config v" + plugin.getDescription().getVersion() + ".yml");
		CONFIG_HEADER = "Ghost Player Configuration - v" + plugin.getDescription().getVersion();
		CONFIG_HEADER += "\n\nWorlds where the plugin is disabled must be separed with space.";
		CONFIG_HEADER += "\nUpdating the config gives you the opportunity to benefit all features from the new config.";
	}
	public boolean AutoUpdateOnLoad = true;
	public boolean TurnIntoGhostOnDeath = false;
	public boolean UpdateConfigOnPluginUpdate = true;
	public String WorldsDisabled = "";
}
