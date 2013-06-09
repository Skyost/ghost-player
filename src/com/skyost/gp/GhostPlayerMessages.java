package com.skyost.gp;

import java.io.File;

import org.bukkit.plugin.Plugin;

public class GhostPlayerMessages extends Config {
	public GhostPlayerMessages(Plugin plugin) {
		CONFIG_FILE = new File(plugin.getDataFolder(), "messages v" + plugin.getDescription().getVersion() + ".yml");
		CONFIG_HEADER = "Ghost Player Messages - v" + plugin.getDescription().getVersion();;
	}
	public String Update_SUCCESS = "Update found: The updater found an update, and has readied it to be loaded the next time the server restarts/reloads.";
	public String Update_NOUPDATE = "No Update: The updater did not find an update, and nothing was downloaded.";
	public String Update_FAILDOWNLOAD = "Download Failed: The updater found an update, but was unable to download it.";
	public String Update_FAILDBO = "dev.bukkit.org Failed: For some reason, the updater was unable to contact DBO to download the file.";
	public String Update_FAILNOVERSION = "No version found: When running the version check, the file on DBO did not contain the a version in the format 'vVersion' such as 'v1.0'.";
	public String Update_FAILBADSLUG = "Bad slug: The slug provided by the plugin running the updater was invalid and doesn't exist on DBO.";
	public String Update_UPDATEAVAILABLE = "Update found: There was an update found but not be downloaded !";
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
	public String Message_12 = "Simply type /clearsghosts or /cg to clears all ghosts !";
	public String Message_S1 = "You don't have permission to do this !";
	public String Message_S2 = "You can't do this from the console !";
	public String Message_S3 = "This plugin is disabled in this world !";
}
