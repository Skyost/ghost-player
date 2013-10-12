package com.skyost.gp;

import java.io.IOException;
import java.io.PrintStream;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import com.skyost.gp.config.GhostPlayerConfig;
import com.skyost.gp.config.GhostPlayerMessages;
import com.skyost.gp.listeners.Commands;
import com.skyost.gp.listeners.Listeners;
import com.skyost.gp.util.GhostFactory;
import com.skyost.gp.util.Metrics;
import com.skyost.gp.util.Updater;
import com.skyost.gp.util.Metrics.Graph;

public class GhostPlayer extends JavaPlugin {
	
	public static GhostFactory ghostFactory;
	public static GhostPlayerConfig config;
	public static GhostPlayerMessages messages;
	public static int totalGhosts;
	
	public void onEnable() {
		ghostFactory = new GhostFactory((Plugin) this);
		setListeners();
		loadConfig();
		if(config.AutoUpdateOnLoad) {
			update();
		}
		startMetrics();
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
	
	private final void loadConfig() {
		try {
			System.setOut(new PrintStream(System.out, true, "UTF-8"));
			config = new GhostPlayerConfig(this);
			config.init();
			messages = new GhostPlayerMessages(this);
			messages.init();
		}
		catch(Exception ex) {
			getLogger().log(Level.SEVERE, "[Ghost Player] " + ex);
			getServer().getPluginManager().disablePlugin(this);
        return;
		}
	}
	
	private final void startMetrics() {
		try {
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
		catch (IOException ex) {
			getLogger().log(Level.SEVERE, "[Ghost Player] " + ex);
		}
	}
	
	@SuppressWarnings("incomplete-switch")
	private final void update() {
		try {
			Updater updater = new Updater(this, 58232, this.getFile(), Updater.UpdateType.DEFAULT, true);
			Updater.UpdateResult result = updater.getResult();
	       	switch(result) {
	       		case SUCCESS:
		           	System.out.println("[Ghost Player] " + messages.Update_SUCCESS);
		           	getServer().getPluginManager().disablePlugin(this);
	            	break;
	            case NO_UPDATE:
	            	System.out.println("[Ghost Player] " + messages.Update_NOUPDATE);
	            	break;
	           	case FAIL_DOWNLOAD:
	            	System.out.println("[Ghost Player] " + messages.Update_FAILDOWNLOAD);
	           		break;
	           	case FAIL_DBO:
	            	System.out.println("[Ghost Player] " + messages.Update_FAILDBO);
	            	break;
	           	case FAIL_NOVERSION:
	           		System.out.println("[Ghost Player] " + messages.Update_FAILNOVERSION);
	           		break;
	           	case UPDATE_AVAILABLE:
	           		System.out.println("[Ghost Player] " + messages.Update_UPDATEAVAILABLE);
	           		break;
	       	}
		}	
		catch (Exception ex) {
			getLogger().log(Level.SEVERE, "[Ghost Player] " + ex);
		}
	}
	
	private final void setListeners() {
		Bukkit.getPluginManager().registerEvents(new Listeners(), this);
		Commands executor = new Commands();
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
