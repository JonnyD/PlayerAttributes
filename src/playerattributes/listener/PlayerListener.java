package playerattributes.listener;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerToggleSprintEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class PlayerListener implements Listener
{

	private int totalDistanceRan = 0;
	private Location start;
	private Location finish;
	
	@EventHandler
	public void toggleSprint(PlayerToggleSprintEvent event)
	{
		
	}
}
