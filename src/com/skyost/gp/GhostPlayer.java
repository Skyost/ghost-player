package com.skyost.gp;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.logging.Level;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import com.skyost.gp.config.GhostPlayerConfig;
import com.skyost.gp.config.GhostPlayerMessages;
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
	
	@SuppressWarnings("static-access")
	public void onEnable() {
		this.ghostFactory = new GhostFactory((Plugin) this);
		this.getServer().getPluginManager().registerEvents(new Listeners(), this);
		loadConfig();
		update();
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
	
	public void loadConfig() {
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
	
	public void startMetrics() {
		try {
		    Metrics metrics = new Metrics(this);
		    Graph ghostsGraph = metrics.createGraph("Ghosts Graph");
		    ghostsGraph.addPlotter(new Metrics.Plotter("Total ghosts") {
		    @Override
		    public int getValue() {
		        return totalGhosts;	
		       }
		    });
		    
    		Graph updateGraph = metrics.createGraph("Update Graph");
    		updateGraph.addPlotter(new Metrics.Plotter("Checking for Updates") {	
    			@Override
    			public int getValue() {	
    				return 1;
    			}
    			
    			@Override
    			public String getColumnName() {
    				if(config.AutoUpdateOnLoad == true) {
    					return "Yes";
    				}
    				else if(config.AutoUpdateOnLoad == false) {
    					return "No";
    				}
    				else {
    					return "Maybe";
    				}
    			}
    		});
    		
		    metrics.start();
		}
		catch (IOException ex) {
			getLogger().log(Level.SEVERE, "[Ghost Player] " + ex);
		}
	}
	
	public void deleteFile(String file) {
		File filename = new File(file);
		filename.delete();
	}
	
	public void update() {
		if(config.AutoUpdateOnLoad == true) {
			try {
				Updater updater = new Updater(this, "ghost-player", this.getFile(), Updater.UpdateType.DEFAULT, true);
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
	            		case FAIL_BADSLUG:
	            			System.out.println("[Ghost Player] " + messages.Update_FAILBADSLUG);
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
	}
}
