package nl.thedutchmc.netherlandsbot.commands;

import nl.thedutchmc.netherlandsbot.modules.BotModuleMetaInformation;

public abstract class ModuleCommandListener extends CommandListener {
	
	private BotModuleMetaInformation metaInfo;
	
	public BotModuleMetaInformation getMetaInfo() {
		return this.metaInfo;
	}
}
