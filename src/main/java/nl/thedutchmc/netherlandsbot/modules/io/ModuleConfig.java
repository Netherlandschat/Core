package nl.thedutchmc.netherlandsbot.modules.io;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

import org.yaml.snakeyaml.Yaml;

import nl.thedutchmc.netherlandsbot.annotations.Nullable;
import nl.thedutchmc.netherlandsbot.utils.Validate;

public class ModuleConfig {

	private ModuleFileHandler moduleFileHandler;
	
	private HashMap<String, Object> config = new HashMap<>();
	
	public ModuleConfig(ModuleFileHandler moduleFileHandler) {
		this.moduleFileHandler = moduleFileHandler;
	}
	
	/**
	 * Read the configuration file for the module
	 */
	@SuppressWarnings("unchecked")
	public void read() {		
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(this.moduleFileHandler.getConfigFile());
		} catch(FileNotFoundException e) {
			e.printStackTrace();
		}
		
		Yaml yaml = new Yaml();
		this.config = (HashMap<String, Object>) yaml.load(fis);
	}
	
	/**
	 * Save the configuration to disk
	 */
	public void save() {
		Validate.notNull(this.config);
			
		FileWriter writer = null;
		try {
			writer = new FileWriter(this.moduleFileHandler.getConfigFile().getAbsolutePath());
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		Yaml yaml = new Yaml();
		yaml.dump(this.config, writer);
	}
	
	/**
	 * Get a value for a key from the configuration
	 * @param key The key of the value to get
	 * @return The value, or null if no value is associated with the provided key
	 */
	@Nullable
	public Object get(String key) {
		Validate.notNull(key);
		
		return this.config.get(key);
	}
	
	/**
	 * Set a key-value pair for the configuration<br>
	 * This call does not write to disk, you should use {@link #save()} for that.
	 * @param key The key to use
	 * @param value The value for the provided key
	 */
	public void set(String key, Object value) {
		Validate.notNull(key);
		
		this.config.put(key, value);
	}
}
