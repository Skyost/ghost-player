package fr.skyost.ghosts;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.plugin.java.JavaPlugin;

import fr.skyost.ghosts.config.GhostPlayerConfig;
import fr.skyost.ghosts.config.GhostPlayerMessages;
import fr.skyost.ghosts.listeners.CommandsExecutor;
import fr.skyost.ghosts.listeners.EventsListener;
import fr.skyost.ghosts.utils.GhostManager;
import fr.skyost.ghosts.utils.Metrics;
import fr.skyost.ghosts.utils.Metrics.Graph;
import fr.skyost.ghosts.utils.Skyupdater;

public class GhostPlayer extends JavaPlugin {

	public static GhostManager ghostManager;
	public static GhostPlayerConfig config;
	public static GhostPlayerMessages messages;
	public static int totalGhosts;

	@Override
	public void onEnable() {
		try {
			ghostManager = new GhostManager(this);
			setupListeners();
			loadConfig();
			if(config.enableUpdater) {
				new Skyupdater(this, 58232, getFile(), true, true);
			}
			startMetrics();
		}
		catch(final Exception ex) {
			ex.printStackTrace();
		}
	}

	@Override
	public void onDisable() {
		try {
			config.save();
			getServer().getPluginManager().disablePlugin(this);
		}
		catch (final InvalidConfigurationException ex) {
			getLogger().log(Level.SEVERE, "[Ghost Player] " + ex);
			getServer().getPluginManager().disablePlugin(this);
		}
	}

	private final void loadConfig() throws Exception {
		final File dataFolder = this.getDataFolder();
		config = new GhostPlayerConfig(dataFolder);
		config.load();
		messages = new GhostPlayerMessages(dataFolder);
		messages.load();
	}

	private final void startMetrics() throws IOException {
		final Metrics metrics = new Metrics(this);
		final Graph ghostsGraph = metrics.createGraph("Ghosts Graph");
		ghostsGraph.addPlotter(new Metrics.Plotter("Total ghosts") {

			@Override
			public int getValue() {
				return totalGhosts;
			}

		});
		metrics.start();
	}

	private final void setupListeners() {
		Bukkit.getPluginManager().registerEvents(new EventsListener(), this);
		final CommandsExecutor executor = new CommandsExecutor();
		getCommand("ghostview").setExecutor(executor);
		getCommand("ghost").setExecutor(executor);
		getCommand("silentghost").setExecutor(executor);
		getCommand("human").setExecutor(executor);
		getCommand("silenthuman").setExecutor(executor);
		getCommand("removeghost").setExecutor(executor);
		getCommand("clearsghosts").setExecutor(executor);
		getCommand("humanworld").setExecutor(executor);
		getCommand("ghostworld").setExecutor(executor);
		getCommand("ghosthunter").setExecutor(executor);
		getCommand("silentghosthunter").setExecutor(executor);
		getCommand("removeghosthunter").setExecutor(executor);
	}

}
