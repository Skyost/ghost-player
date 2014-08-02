package fr.skyost.ghosts.config;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import fr.skyost.ghosts.utils.Skyoconfig;

public class GhostPlayerConfig extends Skyoconfig {

	@ConfigOptions(name = "enable-updater")
	public boolean enableUpdater = true;
	@ConfigOptions(name = "ghost-on-death")
	public boolean ghostOnDeath = false;
	@ConfigOptions(name = "ghosts-can-interact")
	public boolean ghostscanInteract = true;
	@ConfigOptions(name = "human-worlds")
	public List<String> humanWorlds = new ArrayList<String>();
	@ConfigOptions(name = "ghost-time")
	public Integer ghostTime = -1;

	public GhostPlayerConfig(final File dataFolder) {
		super(new File(dataFolder, "config.yml"), Arrays.asList("Ghost Player Configuration", "\nSet '-1' to 'ghost-time' for ignoring it. If you don't want to disable it, juste set the time you want (in ticks).\nFor more informations about tick visit http://bit.ly/S1IclO."));
		humanWorlds.addAll(Arrays.asList("WorldA", "WorldB", "WorldC", "You can add or remove any world you want !"));
	}

}
