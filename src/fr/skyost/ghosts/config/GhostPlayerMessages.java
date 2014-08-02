package fr.skyost.ghosts.config;

import java.io.File;
import java.util.Arrays;

import fr.skyost.ghosts.utils.Skyoconfig;

public class GhostPlayerMessages extends Skyoconfig {

	@ConfigOptions(name = "messages.1")
	public String message1 = "An human can't do this !";
	@ConfigOptions(name = "messages.2")
	public String message2 = "You are already a ghost !";
	@ConfigOptions(name = "messages.3")
	public String message3 = "You are a ghost now !";
	@ConfigOptions(name = "messages.4")
	public String message4 = "/target/ has been removed from the ghosts !";
	@ConfigOptions(name = "messages.5")
	public String message5 = "/sender/ has removed you from the ghosts !";
	@ConfigOptions(name = "messages.6")
	public String message6 = "/target/ is already an human !";
	@ConfigOptions(name = "messages.7")
	public String message7 = "/target/ does not exist or not connected !";
	@ConfigOptions(name = "messages.8")
	public String message8 = "You have been removed from the ghosts !";
	@ConfigOptions(name = "messages.9")
	public String message9 = "You are already an human !";
	@ConfigOptions(name = "messages.10")
	public String message10 = "All ghosts have been cleared ! It take effect on restart/reload.";
	@ConfigOptions(name = "messages.11")
	public String message11 = "You are an human now !";
	@ConfigOptions(name = "messages.12")
	public String message12 = "Simply type /removeghost <player> or /rg <player> to remove <player> from the ghosts !";
	@ConfigOptions(name = "messages.13")
	public String message13 = "/world/ has been added to the list !";
	@ConfigOptions(name = "messages.14")
	public String message14 = "Simply type /humanworld <world> or /hw <world> to add a world where the plugin is disabled !";
	@ConfigOptions(name = "messages.15")
	public String message15 = "/world/ has been removed from the list !";
	@ConfigOptions(name = "messages.16")
	public String message16 = "Simply type /ghostworld <world> or /gw <world> to remove a world from the list where the plugin is disabled !";
	@ConfigOptions(name = "messages.17")
	public String message17 = "Invalid number in config for 'ghost-time'. It will be set to '-1'.";
	@ConfigOptions(name = "messages.18")
	public String message18 = "You are already a ghost so you don't need to be a ghost hunter to see your friends !";
	@ConfigOptions(name = "messages.19")
	public String message19 = "You are already a ghost hunter !";
	@ConfigOptions(name = "messages.20")
	public String message20 = "You are a ghost hunter now !";
	@ConfigOptions(name = "messages.21")
	public String message21 = "/target/ is not a ghost hunter !";
	@ConfigOptions(name = "messages.22")
	public String message22 = "/target/ has been removed from the ghosts hunters !";
	@ConfigOptions(name = "messages.23")
	public String message23 = "You have been removed from the ghosts hunters !";
	@ConfigOptions(name = "messages.24")
	public String message24 = "/sender/ has been removed you from the ghosts hunters !";
	@ConfigOptions(name = "messages.25")
	public String message25 = "Simply type /removeghosthunter <player> or /rgh <player> to remove <player> from the ghosts hunters !";
	@ConfigOptions(name = "messages.26")
	public String message26 = "This player is already a ghost so he don't need to be a ghost hunter !";
	@ConfigOptions(name = "messages.27")
	public String message27 = "This player is already a ghost hunter !";
	@ConfigOptions(name = "messages.28")
	public String message28 = "This player is already a ghost !";
	@ConfigOptions(name = "messages.29")
	public String message29 = "This player is already an human !";
	@ConfigOptions(name = "messages.30")
	public String message30 = "Ghosts can't interact !";

	@ConfigOptions(name = "messages-s.1")
	public String messageS1 = "You don't have permission to do that !";
	@ConfigOptions(name = "messages-s.2")
	public String messageS2 = "You can't do this from the console !";
	@ConfigOptions(name = "messages-s.3")
	public String messageS3 = "This plugin is disabled in this world !";
	@ConfigOptions(name = "messages-s.4")
	public String messageS4 = "This player is offline !";
	@ConfigOptions(name = "messages-s.5")
	public String messageS5 = "You must have at least one argument !";

	public GhostPlayerMessages(final File dataFolder) {
		super(new File(dataFolder, "messages.yml"), Arrays.asList("Ghost Player Messages"));
	}

}
