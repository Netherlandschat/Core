package nl.thedutchmc.netherlandsbot.modules.io;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import nl.thedutchmc.netherlandsbot.annotations.NotNull;
import nl.thedutchmc.netherlandsbot.modules.BotModuleMetaInformation;
import nl.thedutchmc.netherlandsbot.modules.ModuleHandler;
import nl.thedutchmc.netherlandsbot.utils.FileUtils;

public class ModuleFileHandler {

	private final BotModuleMetaInformation meta;
	private final ModuleHandler moduleHandler;
	
	private ModuleConfig config;
	@SuppressWarnings("unused")
	private ModuleStorage storage;
	
	public ModuleFileHandler(BotModuleMetaInformation meta, ModuleHandler moduleHandler) {
		this.meta = meta;
		this.moduleHandler = moduleHandler;
	
		File moduleConfigDir = new File(FileUtils.getJarDirectory() + File.separator + "configs");
		if(!moduleConfigDir.exists()) {
			moduleConfigDir.mkdirs();
		}
		
		File moduleStorageDir = new File(FileUtils.getJarDirectory() + File.separator + "storages");
		if(!moduleStorageDir.exists()) {
			moduleStorageDir.mkdirs();
		}
	}
	
	/**
	 * Get the module configuration
	 * @return Returns an instance of ModuleConfig
	 */
	@NotNull
	public ModuleConfig getModuleConfig() {
		if(this.config == null) {
			this.config = new ModuleConfig(this.meta);
		}
		
		this.config.read();
		return this.config;
	}
	
	/**
	 * Save a premade configuration file from the module jar
	 * @param fileType The type of file
	 * @param name The name of the file, including extension
	 */
	public void saveDefault(FileType fileType, String name) {
		InputStream in = null;
		try {
			in = this.moduleHandler.getModuleClassLoader().getResourceAsStream(name);
			
			OutputStream out;
			switch(fileType) {
			case CONFIG:
				out = new FileOutputStream(this.getConfigFile());
				break;
			case STORAGE:
				out = new FileOutputStream(this.getStorageFile());
				break;
			default:
				out = null;
			}
			
			if(in == null) {				
				throw new FileNotFoundException(String.format("Unable to find file %s for module %s. Using ClassLoader: %s", 
						name, 
						this.meta.getName(), 
						this.moduleHandler.getModuleClassLoader().getName()));
			}
			
			byte[] buffer = in.readAllBytes();
			
			out.write(buffer);
			out.flush();
			out.close();
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Get the File used for configuration for this module
	 * @return Returns a File
	 */
	@NotNull
	public File getConfigFile() {
		return new File(FileUtils.getJarDirectory() + File.separator + "configs", this.meta.getName() + ".yml");
	}
	
	/**
	 * Get the File used for storage for this module
	 * @return Returns a File
	 */
	@NotNull
	public File getStorageFile() {
		return new File(FileUtils.getJarDirectory() + File.separator + "storages", this.meta.getName() + ".yml");
	}
}
