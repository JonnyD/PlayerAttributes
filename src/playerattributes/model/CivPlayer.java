package playerattributes.model;

import org.bukkit.entity.Player;

public class CivPlayer 
{
	private final Player player;
	private int speed;
	private int stamina;
	private double bodyfat;
	private double muscle;
	private double distanceRan;

	public CivPlayer(Player player) 
	{
		this.player = player;
	}
	
	public String getUsername()
	{
		return player.getDisplayName();
	}

	public int getSpeed() {
		return speed;
	}

	public void setSpeed(int speed) {
		this.speed = speed;
	}

	public int getStamina() {
		return stamina;
	}

	public void setStamina(int stamina) {
		this.stamina = stamina;
	}

	public double getBodyfat() {
		return bodyfat;
	}

	public void setBodyfat(double bodyfat) {
		this.bodyfat = bodyfat;
	}
	
	public void addBodyFat(double fat)
	{
		this.bodyfat += fat;
	}

	public double getMuscle() {
		return muscle;
	}

	public void setMuscle(double muscle) {
		this.muscle = muscle;
	}

	public double getDistanceRan() {
		return distanceRan;
	}

	public void setDistanceRan(double distanceRan) {
		this.distanceRan = distanceRan;
	}
	
	public void addDistanceRan(double distanceRan)
	{
		this.distanceRan += distanceRan;
	}

	public Player getPlayer() {
		return player;
	}

	@Override
	public String toString() {
		return "CivPlayer [player=" + player + ", speed=" + speed
				+ ", stamina=" + stamina + ", bodyfat=" + bodyfat + ", muscle="
				+ muscle + ", distanceRan=" + distanceRan + "]";
	}
	
	

}
