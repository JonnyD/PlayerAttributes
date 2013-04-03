package playerattributes.manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.entity.Player;

import playerattributes.model.CivPlayer;

public class CivPlayerManager 
{
	private Map<String, CivPlayer> civPlayers;

	public CivPlayerManager()
	{
		civPlayers = new HashMap<String, CivPlayer>();
	}
	
	public Map<String, CivPlayer> getCivPlayers()
	{
		return civPlayers;
	}
	
	public void setCivPlayers(Map<String, CivPlayer> civPlayers)
	{
		this.civPlayers = civPlayers;
	}
	
	public void addCivPlayer(CivPlayer civPlayer)
	{
		String username = civPlayer.getUsername();
		if(!civPlayers.containsKey(username))
		{
			civPlayers.put(username, civPlayer);
		}
	}
	
	public void removeCivPlayer(CivPlayer civPlayer)
	{
		civPlayers.remove(civPlayer.getUsername());
	}
	
	public CivPlayer getOrCreateCivPlayer(Player player)
	{
		CivPlayer civPlayer = null;
		String username = player.getDisplayName();
		if(!civPlayers.containsKey(username))
		{
			civPlayer = new CivPlayer(player);
			addCivPlayer(civPlayer);
		}
		return civPlayer;
	}
}
