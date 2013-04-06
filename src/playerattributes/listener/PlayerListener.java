package playerattributes.listener;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerToggleSprintEvent;

import playerattributes.PlayerAttributes;
import playerattributes.manager.CivPlayerManager;
import playerattributes.model.CivPlayer;

public class PlayerListener implements Listener
{
	private CivPlayerManager civPlayerManager = PlayerAttributes.getCivPlayerManager();
	
	private Map<String, CivPlayer> sprinters = new HashMap<String, CivPlayer>();
	
	@EventHandler
	public void login(PlayerLoginEvent event)
	{
		Player player = event.getPlayer();
		this.civPlayerManager.getOrCreateCivPlayer(player);
	}
	
	@EventHandler
	public void toggleSprint(PlayerToggleSprintEvent event)
	{
		Player player = event.getPlayer();
		String username = player.getDisplayName();

		CivPlayer civPlayer = this.civPlayerManager.getOrCreateCivPlayer(player);
		boolean isSprinting = event.isSprinting();
		if(isSprinting)
		{
			this.sprinters.put(username, civPlayer);
		}
		else
		{
			this.sprinters.remove(username);
		}
		
		System.out.println(civPlayer.toString());
	}
	
	@EventHandler
	public void move(PlayerMoveEvent event)
	{
		Player player = event.getPlayer();
		Location from = event.getFrom();
		Location to = event.getTo();
		
		 if (from.getBlockX() == to.getBlockX()
				 && from.getBlockY() == to.getBlockY()
	             && from.getBlockZ() == to.getBlockZ()
	             && from.getWorld().equals(to.getWorld())) 
		 {
			 // Player didn't move by at least one block.
			 return;
	     }

		 String username = player.getDisplayName();
		 boolean isSprinting = this.sprinters.containsKey(username);
		 if(isSprinting)
		 {
			 double distanceRan = from.distance(to);
			 this.sprinters.get(username).addDistanceRan(distanceRan);
		 }
	}
	
}
