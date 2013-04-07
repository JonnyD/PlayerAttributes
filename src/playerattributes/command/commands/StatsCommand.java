package playerattributes.command.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import playerattributes.PlayerAttributes;
import playerattributes.command.PlayerCommand;
import playerattributes.manager.CivPlayerManager;
import playerattributes.model.CivPlayer;

public class StatsCommand extends PlayerCommand
{

	public StatsCommand() 
	{
		super("Stats");
		setDescription("Displays stats");
		setArgumentRange(0,0);
		setUsage("/pastats");
		setIdentifier("pastats");
	}

	@Override
	public boolean execute(CommandSender sender, String[] args) 
	{
		Player player = (Player) sender;
		CivPlayerManager civPlayerManager = PlayerAttributes.getCivPlayerManager();
		CivPlayer civPlayer = civPlayerManager.getOrCreateCivPlayer(player);
		player.sendMessage(civPlayer.toString());
		return true;		
	}

}
