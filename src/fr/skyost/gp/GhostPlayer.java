package fr.skyost.gp;

import java.io.IOException;
import java.io.PrintStream;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import fr.skyost.gp.config.GhostPlayerConfig;
import fr.skyost.gp.config.GhostPlayerMessages;
import fr.skyost.gp.listeners.CommandsExecutor;
import fr.skyost.gp.listeners.EventsListener;
import fr.skyost.gp.utils.GhostFactory;
import fr.skyost.gp.utils.Metrics;
import fr.skyost.gp.utils.Skyupdater;
import fr.skyost.gp.utils.Metrics.Graph;

public class GhostPlayer extends JavaPlugin {
	
	public static GhostFactory ghostFactory;
	public static GhostPlayerConfig config;
	public static GhostPlayerMessages messages;
	public static int totalGhosts;
	
	public void onEnable() {
		try {
			ghostFactory = new GhostFactory((Plugin) this);
			setListeners();
			loadConfig();
			if(config.AutoUpdateOnLoad) {
				new Skyupdater(this, 58232, this.getFile(), true, true);
			}
			startMetrics();
		}
		catch(Exception ex) {
			ex.printStackTrace();
		}
	}
	
	public void onDisable() {
		try {
			config.save();
			getServer().getPluginManager().disablePlugin(this);
		} 
		catch (InvalidConfigurationException ex) {
			getLogger().log(Level.SEVERE, "[Ghost Player] " + ex);
			getServer().getPluginManager().disablePlugin(this);
		}
	}
	
	private final void loadConfig() throws Exception {
		System.setOut(new PrintStream(System.out, true, "UTF-8"));
		config = new GhostPlayerConfig(this);
		config.init();
		messages = new GhostPlayerMessages(this);
		messages.init();
	}
	
	private final void startMetrics() throws IOException {
		Metrics metrics = new Metrics(this);
		Graph ghostsGraph = metrics.createGraph("Ghosts Graph");
		ghostsGraph.addPlotter(new Metrics.Plotter("Total ghosts") {
			
			@Override
			public int getValue() {
				return totalGhosts;
			}
				
		});
		metrics.start();
	}
	
	private final void setListeners() {
		Bukkit.getPluginManager().registerEvents(new EventsListener(), this);
		CommandsExecutor executor = new CommandsExecutor();
		this.getCommand("ghostview").setExecutor(executor);
		this.getCommand("ghost").setExecutor(executor);
		this.getCommand("silentghost").setExecutor(executor);
		this.getCommand("human").setExecutor(executor);
		this.getCommand("silenthuman").setExecutor(executor);
		this.getCommand("removeghost").setExecutor(executor);
		this.getCommand("clearsghosts").setExecutor(executor);
		this.getCommand("humanworld").setExecutor(executor);
		this.getCommand("ghostworld").setExecutor(executor);
		this.getCommand("ghosthunter").setExecutor(executor);
		this.getCommand("silentghosthunter").setExecutor(executor);
		this.getCommand("removeghosthunter").setExecutor(executor);
	}
	
}
