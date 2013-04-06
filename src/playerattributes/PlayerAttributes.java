package playerattributes;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import playerattributes.listener.PlayerListener;
import playerattributes.manager.CivPlayerManager;
import playerattributes.manager.ConfigManager;

public class PlayerAttributes extends JavaPlugin
{
	private static PlayerAttributes instance;
	private static CivPlayerManager civPlayerManager;
	private static ConfigManager configManager;
	
	public void onEnable()
	{
		instance = this;
		initialiseManagers();
		registerEvents();
	}
	
	public void initialiseManagers()
	{
		configManager    = new ConfigManager();
		civPlayerManager = new CivPlayerManager();
	}
	
	public void registerEvents()
	{
		PluginManager pm = getServer().getPluginManager();
		pm.registerEvents(new PlayerListener(), this);
	}
	
	public static CivPlayerManager getCivPlayerManager()
	{
		return civPlayerManager;
	}
	
	public static ConfigManager getConfigManager()
	{
		return configManager;
	}

	public static PlayerAttributes getInstance()
	{
		return instance;
	}
}
