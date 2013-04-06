package playerattributes.listener;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerToggleSprintEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import playerattributes.PlayerAttributes;
import playerattributes.manager.CivPlayerManager;
import playerattributes.manager.EdibleManager;
import playerattributes.model.CivPlayer;
import playerattributes.model.Edible;

public class PlayerListener implements Listener
{
	private CivPlayerManager civPlayerManager = PlayerAttributes.getCivPlayerManager();
	private EdibleManager edibleManager       = PlayerAttributes.getEdibleManager();
	
	private Map<String, CivPlayer> sprinters = new HashMap<String, CivPlayer>();
	private Map<String, PotionEffectType> playerPotionEffects = new HashMap<String, PotionEffectType>();
	
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
			PotionEffect potionEffect = null;
			PotionEffectType potionEffectType = null;

			int amplifier = 0;
			int speed = civPlayer.getSpeed();			
			if(speed < 15)
			{
				potionEffectType = PotionEffectType.SLOW;
			}
			else if(speed >= 15)
			{
				if(speed >= 15 && speed < 25) amplifier = 0;
				if(speed >= 25 && speed < 35) amplifier = 1;
				if(speed >= 35 && speed < 45) amplifier = 2;
				if(speed >= 45 && speed < 55) amplifier = 3;
				if(speed >= 55 && speed < 65) amplifier = 4;
				if(speed >= 65 && speed < 75) amplifier = 5;
				if(speed >= 75 && speed < 85) amplifier = 6;
				if(speed >= 85 && speed < 95) amplifier = 7;
				if(speed >= 95 && speed < 100) amplifier = 8;
				
				potionEffectType = PotionEffectType.SPEED;
			}
			
			potionEffect = new PotionEffect(potionEffectType, 80, amplifier);
			player.addPotionEffect(potionEffect);
			this.playerPotionEffects.put(username, potionEffectType);
			
			this.sprinters.put(username, civPlayer);
		}
		else
		{
			player.removePotionEffect(this.playerPotionEffects.get(username));
			this.sprinters.remove(username);
		}
		
		System.out.println(civPlayer.toString());
	}
	
	@EventHandler
	public void move(PlayerMoveEvent event)
	{
		Player player = event.getPlayer();
		Location from = event.getFrom();
		Location to   = event.getTo();
		
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
			 double distanceRan = from.distance(to) * 2;
			 CivPlayer civPlayer = this.sprinters.get(username);
			 civPlayer.addDistanceRan(distanceRan);
		 }
	}
	
	@EventHandler
	public void eat(FoodLevelChangeEvent event)
	{
		Entity entity = event.getEntity();
		if (entity instanceof Player) {
			Player player      = (Player) entity;
	        ItemStack item     = player.getItemInHand();
	        Material material  = item.getType();
	     
	        boolean isAnEdible = edibleManager.isEdible(material);
	        if(isAnEdible)
	        {
	        	Edible edible = edibleManager.getEdible(material);
	        	CivPlayer civPlayer = civPlayerManager.getOrCreateCivPlayer(player);
	        	civPlayer.addBodyFat(edible.getFat());
	        }
	    }
	}
}
