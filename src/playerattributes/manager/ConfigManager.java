package playerattributes.manager;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import playerattributes.PlayerAttributes;

public class ConfigManager 
{
	private PlayerAttributes plugin;
	
	private int ticksPerSecond;
	
	private String username;
	private String host;
	private String password;
	private String database;
	private int port;
	
	private File main;
	private FileConfiguration config;
	private FileConfiguration cleanConfig;
	
	private int defaultSpeed;
	private int defaultStamina;
	private int defaultBodyFat;
	private int defaultMuscle;
	
	private List<?> edibles;
	
	public ConfigManager()
	{
		this.plugin = PlayerAttributes.getInstance();
		this.config = plugin.getConfig();
		this.cleanConfig = new YamlConfiguration();
		this.main = new File(plugin.getDataFolder() + File.separator + "config.yml");
		this.load();
	}
	
	private void load()
	{
		boolean exists = main.exists();
		if(exists)
		{
			try
			{
				config.options().copyDefaults(true);
				config.load(main);
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}
		else
		{
			config.options().copyDefaults(true);
		}
		
		username = loadString("mysql.username");
		host     = loadString("mysql.host");
		password = loadString("mysql.password");
		database = loadString("mysql.database");
		port     = loadInt("mysql.port");
		
		defaultSpeed   = loadInt("defaults.speed");
		defaultStamina = loadInt("defaults.stamina");
		defaultBodyFat = loadInt("defaults.bodyfat");
		defaultMuscle  = loadInt("defaults.muscle");
		
		edibles = loadList("edibles");
		
		ticksPerSecond = 20;
	}
	
	private Boolean loadBoolean(String path)
    {
        if (config.isBoolean(path))
        {
            boolean value = config.getBoolean(path);
            cleanConfig.set(path, value);
            return value;
        }
        return false;
    }

    private String loadString(String path)
    {
        if (config.isString(path))
        {
            String value = config.getString(path);
            cleanConfig.set(path, value);
            return value;
        }

        return "";
    }

    private int loadInt(String path)
    {
        if (config.isInt(path))
        {
            int value = config.getInt(path);
            cleanConfig.set(path, value);
            return value;
        }

        return 0;
    }

    private double loadDouble(String path)
    {
        if (config.isDouble(path))
        {
            double value = config.getDouble(path);
            cleanConfig.set(path, value);
            return value;
        }

        return 0;
    }
    
    private List<String> loadStringList(String path)
    {
    	if(config.isList(path))
    	{
    		List<String> value = config.getStringList(path);
    		cleanConfig.set(path, value);
    		return value;
    	}
    	
    	return null;
    }
    
    private List<?> loadList(String path)
    {
    	if(config.isList(path))
    	{
    		List<?> value = config.getList(path);
    		cleanConfig.set(path, value);
    		return value;
    	}
    	
    	return null;
    }
    
    public void save()
    {
        try
        {
            cleanConfig.save(main);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getDatabase() {
		return database;
	}

	public void setDatabase(String database) {
		this.database = database;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public File getMain() {
		return main;
	}

	public void setMain(File main) {
		this.main = main;
	}

	public FileConfiguration getConfig() {
		return config;
	}

	public void setConfig(FileConfiguration config) {
		this.config = config;
	}

	public FileConfiguration getCleanConfig() {
		return cleanConfig;
	}

	public void setCleanConfig(FileConfiguration cleanConfig) {
		this.cleanConfig = cleanConfig;
	}

	public int getDefaultSpeed() {
		return defaultSpeed;
	}

	public void setDefaultSpeed(int defaultSpeed) {
		this.defaultSpeed = defaultSpeed;
	}

	public int getDefaultStamina() {
		return defaultStamina;
	}

	public void setDefaultStamina(int defaultStamina) {
		this.defaultStamina = defaultStamina;
	}

	public int getDefaultBodyFat() {
		return defaultBodyFat;
	}

	public void setDefaultBodyFat(int defaultBodyFat) {
		this.defaultBodyFat = defaultBodyFat;
	}

	public int getDefaultMuscle() {
		return defaultMuscle;
	}

	public void setDefaultMuscle(int defaultMuscle) {
		this.defaultMuscle = defaultMuscle;
	}

	public int getTicksPerSecond() {
		return ticksPerSecond;
	}

	public void setTicksPerSecond(int ticksPerSecond) {
		this.ticksPerSecond = ticksPerSecond;
	}

	public List<?> getEdibles() {
		return edibles;
	}

	public void setEdibles(List<?> edibles) {
		this.edibles = edibles;
	}
}
