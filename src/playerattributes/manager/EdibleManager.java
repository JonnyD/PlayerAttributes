package playerattributes.manager;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import org.bukkit.Material;

import playerattributes.model.Edible;

public class EdibleManager 
{
	private List<Edible> edibles;
	
	public EdibleManager()
	{
		this.edibles = new ArrayList<Edible>();
	}
	
	public List<Edible> getEdibles()
	{
		return this.edibles;
	}
	
	public void setEdibles(List<Edible> edibles)
	{
		this.edibles = edibles;
	}
	
	public void addEdible(Edible edible)
	{
		if(!this.edibles.contains(edible))
		{
			this.edibles.add(edible);
		}
	}
	
	public void createEdible(LinkedHashMap map)
	{
		Material material = Material.matchMaterial(map.get("name").toString());
		double fat = (double) map.get("fat");
		double protein = (double) map.get("protein");
		Edible edible = new Edible(material, fat, protein);
		addEdible(edible);
	}
}
