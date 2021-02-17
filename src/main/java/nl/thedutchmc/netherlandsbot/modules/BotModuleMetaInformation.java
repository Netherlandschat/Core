package nl.thedutchmc.netherlandsbot.modules;

import nl.thedutchmc.netherlandsbot.annotations.NotNull;

public class BotModuleMetaInformation {

	private String name, version;
	
	public BotModuleMetaInformation(String name, String version) {
		this.name = name;
		this.version = version;
	}
	
	/**
	 * Get the name of the module
	 * @return Returns the name of the module
	 */
	@NotNull
	public String getName() {
		return this.name;
	}
	
	/**
	 * Get the version of the module
	 * @return Returns the version of the module
	 */
	@NotNull
	public String getVersion() {
		return this.version;
	}
}
