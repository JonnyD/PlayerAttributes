package playerattributes.model;

import java.util.LinkedHashMap;

import org.bukkit.Material;

public class Edible 
{
	private Material material;
	private double fat;
	private double protein;

	public Edible(Material material, double fat, double protein)
	{
		this.material = material;
		this.fat = fat;
		this.protein = protein;
	}

	public Material getMaterial() 
	{
		return material;
	}

	public void setMaterial(Material material) 
	{
		this.material = material;
	}

	public double getFat() 
	{
		return fat;
	}

	public void setFat(double fat) 
	{
		this.fat = fat;
	}

	public double getProtein() 
	{
		return protein;
	}

	public void setProtein(double protein) 
	{
		this.protein = protein;
	}
	
	
}
