package playerattributes.listener;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerToggleSprintEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import playerattributes.PlayerAttributes;
import playerattributes.manager.CivPlayerManager;

public class PlayerListener implements Listener
{
	@EventHandler
	public void login(PlayerLoginEvent event)
	{
		Player player = event.getPlayer();
		CivPlayerManager civPlayerManager = PlayerAttributes.getCivPlayerManager();
		civPlayerManager.getOrCreateCivPlayer(player);
	}
}
