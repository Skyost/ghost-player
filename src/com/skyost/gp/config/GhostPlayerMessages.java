package com.skyost.gp.config;

import java.io.File;

import org.bukkit.plugin.Plugin;

import com.skyost.gp.utils.Config;

public class GhostPlayerMessages extends Config {
	
	public String Message_1 = "An human can't do this !";
	public String Message_2 = "You are already a ghost !";
	public String Message_3 = "You are a ghost now !";
	public String Message_4 = "/target/ has been removed from the ghosts !";
	public String Message_5 = "/sender/ has removed you from the ghosts !";
	public String Message_6 = "/target/ is already an human !";
	public String Message_7 = "/target/ does not exist or not connected !";
	public String Message_8 = "You have been removed from the ghosts !";
	public String Message_9 = "You are already an human !";
	public String Message_10 = "All ghosts have been cleared ! It take effect on restart/reload.";
	public String Message_11 = "You are an human now !";
	public String Message_12 = "Simply type /removeghost <player> or /rg <player> to remove <player> from the ghosts !";
	public String Message_13 = "/world/ has been added to the list !";
	public String Message_14 = "Simply type /humanworld <world> or /hw <world> to add a world where the plugin is disabled !";
	public String Message_15 = "/world/ has been removed from the list !";
	public String Message_16 = "Simply type /ghostworld <world> or /gw <world> to remove a world from the list where the plugin is disabled !";
	public String Message_17 = "Invalid number in config for 'GhostTime'. It will be set to 'FOREVER'.";
	public String Message_18 = "You are already a ghost so you don't need to be a ghost hunter to see your friends !";
	public String Message_19 = "You are already a ghost hunter !";
	public String Message_20 = "You are a ghost hunter now !";
	public String Message_21 = "/target/ is not a ghost hunter !";
	public String Message_22 = "/target/ has been removed from the ghosts hunters !";
	public String Message_23 = "You have been removed from the ghosts hunters !";
	public String Message_24 = "/sender/ has been removed you from the ghosts hunters !";
	public String Message_25 = "Simply type /removeghosthunter <player> or /rgh <player> to remove <player> from the ghosts hunters !";
	public String Message_26 = "Invalid data in config for 'TurnedIntoOnJoin'. It will be set to 'HUMAN'.";
	public String Message_27 = "This player is already a ghost so he don't need to be a ghost hunter !";
	public String Message_28 = "This player is already a ghost hunter !";
	public String Message_29 = "This player is already a ghost !";
	public String Message_30 = "This player is already an human !";
	public String Message_31 = "Ghosts can't interact !";
	
	public String Message_S1 = "You don't have permission to do this !";
	public String Message_S2 = "You can't do this from the console !";
	public String Message_S3 = "This plugin is disabled in this world !";
	public String Message_S4 = "This player is offline !";
	public String Message_S5 = "You must have at least one argument !";
	
	public GhostPlayerMessages(Plugin plugin) {
		CONFIG_FILE = new File(plugin.getDataFolder(), "messages.yml");
		CONFIG_HEADER = "Ghost Player Messages";
	}
	
}
