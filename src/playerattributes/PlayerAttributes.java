package playerattributes;

import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import playerattributes.listener.PlayerListener;

public class PlayerAttributes extends JavaPlugin
{
	private static PlayerAttributes instance;
	
	public void onEnable()
	{
		instance = this;
		
		registerEvents();
	}
	
	public void registerEvents()
	{
		PluginManager pm = getServer().getPluginManager();
		pm.registerEvents(new PlayerListener(), this);
	}
}
