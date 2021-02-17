package nl.thedutchmc.netherlandsbot.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashMap;

import org.yaml.snakeyaml.Yaml;

import nl.thedutchmc.netherlandsbot.annotations.Nullable;
import nl.thedutchmc.netherlandsbot.utils.FileUtils;

public class ConfigHandler {

	private HashMap<String, Object> config = new HashMap<>();
	
	private File jarDirectory;
	
	public ConfigHandler() {
		this.jarDirectory = FileUtils.getJarDirectory();
	}
	
	/**
	 * Read the config.yml file. <br>
	 * Will save it to disk if it does not yet exist
	 */
	@SuppressWarnings("unchecked")
	public void read() {
		File configFile = new File(this.jarDirectory, "config.yml");
		
		if(!configFile.exists()) {
			FileUtils.saveResource("coreconfig.yml", configFile.getAbsolutePath());
		}
		
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(configFile);
		} catch(FileNotFoundException e) {
			e.printStackTrace();
		
			System.exit(1);
		}
		
		Yaml yaml = new Yaml();
		this.config = (HashMap<String, Object>) yaml.load(fis);
	}
	
	/**
	 * Get a config value
	 * @param key The key of the value
	 * @return The value, or null if no value exists for the provided key
	 */
	@Nullable
	public Object getValue(String key) {
		return this.config.get(key);
	}
}
