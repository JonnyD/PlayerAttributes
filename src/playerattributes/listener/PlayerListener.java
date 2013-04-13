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
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerToggleSprintEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitScheduler;

import playerattributes.PlayerAttributes;
import playerattributes.manager.CivPlayerManager;
import playerattributes.manager.ConfigManager;
import playerattributes.manager.EdibleManager;
import playerattributes.model.CivPlayer;
import playerattributes.model.Edible;

public class PlayerListener implements Listener
{
	private PlayerAttributes plugin           = PlayerAttributes.getInstance();
	private CivPlayerManager civPlayerManager = PlayerAttributes.getCivPlayerManager();
	private EdibleManager edibleManager       = PlayerAttributes.getEdibleManager();
	private ConfigManager configManager       = PlayerAttributes.getConfigManager();

	private Map<String, PotionEffectType> playerPotionEffects = new HashMap<String, PotionEffectType>();
	private Map<String, CivPlayer> sprinters    = new HashMap<String, CivPlayer>();
	private Map<String, Integer> scheduledTasks = new HashMap<String, Integer>();
	
	@EventHandler
	public void login(PlayerLoginEvent event)
	{
		Player player = event.getPlayer();
		this.civPlayerManager.getOrCreateCivPlayer(player);
	}
	
	@EventHandler 
	public void quit(PlayerQuitEvent event)
	{
		Player player   = event.getPlayer();

		// In case the player disconnects in the middle of a sprint
		if(this.sprinters.containsKey(player.getDisplayName()))
		{
			CivPlayer civPlayer = this.civPlayerManager.getOrCreateCivPlayer(player);
			calculateStamina(civPlayer);
			calculateSpeed(civPlayer);
		}
		
		reset(player);
	}
	
	@EventHandler
	public void toggleSprint(PlayerToggleSprintEvent event)
	{
		Player player       = event.getPlayer();
		String username     = player.getDisplayName();
		CivPlayer civPlayer = this.civPlayerManager.getOrCreateCivPlayer(player);
		
		boolean isSprinting = event.isSprinting();
		if(isSprinting)
		{			
			this.sprinters.put(username, civPlayer);
			int ticksPerSprint = (civPlayer.getStamina() * 5) * configManager.getTicksPerSecond();
			processPotionEffect(civPlayer, ticksPerSprint);
			scheduleWhenToStop(player, ticksPerSprint);
		}
		else // Player was sprinting and then stopped
		{
			calculateStamina(civPlayer);
			calculateSpeed(civPlayer);
			reset(player);
		}
	}
	
	@EventHandler
	public void move(PlayerMoveEvent event)
	{
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

		 Player player = event.getPlayer();
		 calculateDistanceRan(from, to, player.getDisplayName());
	}
	
	@EventHandler
	public void eat(FoodLevelChangeEvent event)
	{
		Entity entity = event.getEntity();
		if (entity instanceof Player) 
		{
			Player player      = (Player) entity;
	        ItemStack item     = player.getItemInHand();
	        Material material  = item.getType();
	     
	        Edible edible = edibleManager.getEdible(material);
	        if(edible != null)
	        {
	        	CivPlayer civPlayer = civPlayerManager.getOrCreateCivPlayer(player);
	        	civPlayer.addBodyFat(edible.getFat());
	        }
	    }
	}
	
	private double log2(double value)
	{
		return Math.log(value) / Math.log(2);
	}
	
	private void reset(Player player)
	{
		String username = player.getDisplayName();
		cancelWhenToStop(username);
		cancelPotionEffect(player);
		this.sprinters.remove(username);
	}
	
	private void scheduleWhenToStop(final Player player, int ticks)
	{
		BukkitScheduler scheduler = this.plugin.getServer().getScheduler();
		int taskId = scheduler.scheduleSyncDelayedTask(this.plugin, new Runnable() 
		{
            public void run() 
            {
            	player.setSprinting(false);
            }
        }, ticks);
		this.scheduledTasks.put(player.getDisplayName(), taskId);
	}
	
	private void cancelWhenToStop(String username)
	{
		if(this.scheduledTasks.containsKey(username))
		{
			int taskId = this.scheduledTasks.get(username);
			BukkitScheduler scheduler = this.plugin.getServer().getScheduler();
			if(taskId > 0 && scheduler.isQueued(taskId))
			{
	            scheduler.cancelTask(taskId);
	            this.scheduledTasks.remove(username);
			}
		}
	}
	
	private void processPotionEffect(CivPlayer civPlayer, int ticks)
	{
		PotionEffect potionEffect = null;
		PotionEffectType potionEffectType = null;
		
		int speed = civPlayer.getSpeed();
		
		potionEffectType = PotionEffectType.SPEED;
		potionEffect = new PotionEffect(potionEffectType, ticks, speed);
		civPlayer.getPlayer().addPotionEffect(potionEffect);
		this.playerPotionEffects.put(civPlayer.getUsername(), potionEffectType);
	}
	
	private void cancelPotionEffect(Player player)
	{
		String username = player.getDisplayName();
		player.removePotionEffect(this.playerPotionEffects.get(username));
		this.playerPotionEffects.remove(username);
	}
	
	private void calculateDistanceRan(Location from, Location to, String username)
	{
		CivPlayer civPlayer = this.sprinters.get(username);
		 if(civPlayer != null)
		 {
			/*
			 * Multiply by 2. The condition in the move() event only 
			 * passes when the player moves from a block to another
			 * which means it doesn't pass when a player moves across
			 * a single block. So we multiple by 2 to improve accuracy.
			 */
			 double distanceRan = from.distance(to) * 2; 
			 civPlayer.addDistanceRan(distanceRan);
		 }
	}
	
	private void calculateStamina(CivPlayer civPlayer)
	{
		if(civPlayer == null)
			return;
		
		double distanceRan = civPlayer.getDistanceRan();
		double log2 = log2(distanceRan);
		int stamina = civPlayer.getStamina();
		System.out.println(log2 + " " + civPlayer.getStamina() + " " + civPlayer.getSpeed() + " " + civPlayer.getDistanceRan());
		if((int) log2 > stamina && stamina < 100)
		{
			civPlayer.setStamina((int) log2);
			int difference = (int) log2 - stamina;
			sendPositiveAlert(civPlayer.getPlayer(), "Stamina", difference);
		}
	}
	
	private void calculateSpeed(CivPlayer civPlayer)
	{
		if(civPlayer == null)
			return;
		
		int stamina = civPlayer.getStamina();
		
		int amplifier = 0;
		int[] boundaries = {10, 25, 50};
		for(int i = 0; i < boundaries.length; i++)
		{
			if(stamina > boundaries[i])
			{
				amplifier = i + 1;
			}
		}
		
		civPlayer.setSpeed(amplifier);
	}
	
	private void sendPositiveAlert(Player player, String message, int difference)
	{
		player.sendMessage(message + ": +" + difference);
	}
}
