package playerattributes.manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Material;

import playerattributes.PlayerAttributes;
import playerattributes.model.Edible;

public class EdibleManager 
{
	private Map<Material, Edible> edibles;
	
	public EdibleManager()
	{
		this.edibles = new HashMap<Material, Edible>();
		this.loadFromConfig();
	}
	
	public void loadFromConfig()
	{
		ConfigManager configManager = PlayerAttributes.getConfigManager();
		List<?> edibles = configManager.getEdibles();
		
		for(Object object : edibles)
		{
			createEdible((LinkedHashMap) object);
		}
	}
	
	public void createEdible(LinkedHashMap map)
	{
		Material material = Material.matchMaterial(map.get("material").toString());
		double fat        = (double) map.get("fat");
		double protein    = (double) map.get("protein");
		
		addEdible(new Edible(material, fat, protein));
	}
	
	public Map<Material, Edible> getEdibles()
	{
		return this.edibles;
	}
	
	public void setEdibles(Map<Material, Edible> edibles)
	{
		this.edibles = edibles;
	}
	
	public void addEdible(Edible edible)
	{
		Material material = edible.getMaterial();
		if(!isEdible(material))
		{
			this.edibles.put(material, edible);
		}
	}
	
	public boolean isEdible(Material material)
	{
		return this.edibles.containsKey(material);
	}
	
	public Edible getEdible(Material material)
	{
		return this.edibles.get(material);
	}
}
