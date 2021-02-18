package nl.thedutchmc.netherlandsbot.commands;

import java.lang.reflect.Field;
import java.util.HashMap;

import nl.thedutchmc.netherlandsbot.Bot;
import nl.thedutchmc.netherlandsbot.modules.BotModuleMetaInformation;

public class CommandRegistry {

	private HashMap<String, CommandListener> commandListeners = new HashMap<>();

	/**
	 * Register a ModuleCommandListener
	 * @param command The name of the command, including prefix
	 * @param listener An instance of ModuleCommandListener which wants to listen for the command
	 * @param metaInfo BotModuleMetaInformation for the module registering this command 
	 */
	public void registerModuleCommandListener(String command, ModuleCommandListener listener, BotModuleMetaInformation metaInfo) {
		if(this.commandListeners.containsKey(command)) {
			throw new RuntimeException(String.format("Command '%s' already has a listener registered. A command may not have two listeners!", command));
		}
		
		Bot.logInfo(String.format("Registering command '%s' for Module '%s'", command, metaInfo.getName()));
		
		//Use reflection to add the meta information to the ModuleCommandListener
		Field metaInfoField = null;
		try {
			metaInfoField = listener.getClass().getSuperclass().getDeclaredField("metaInfo");
		} catch (NoSuchFieldException | SecurityException e) {
			e.printStackTrace();
		}
		
		//Allow access
		metaInfoField.setAccessible(true);
		
		//Set the meta info to the field
		try {
			metaInfoField.set(listener, metaInfo);
		} catch (IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace();
		}
		
		//Remove access
		metaInfoField.setAccessible(false);
		
		//Finally, add the command listener to the list of listeners
		this.commandListeners.put(command, listener);
	}
	
	/**
	 * Register a CommandListener. This should only be used by Core
	 * @param command The command including prefix to register
	 * @param listener The CommandListener to register for the command
	 */
	public void registerCommandListener(String command, CommandListener listener) {
		if(this.commandListeners.containsKey(command)) {
			throw new RuntimeException(String.format("Command '%s' already has a listener registered. A command may not have two listeners!", command));
		}
		
		Bot.logInfo(String.format("Registering command '%s' for Core", command));

		this.commandListeners.put(command, listener);
	}
	
	/**
	 * Fire a command
	 * @param commandData CommandData object for the command
	 */
	public void fireCommand(CommandData commandData) {
		CommandListener commandListener = this.commandListeners.get(commandData.getCommand());
		
		//If commandListener is null, there is no commandListener so we can stop
		if(commandListener == null) {
			return;
		}
		
		if(commandListener instanceof ModuleCommandListener) {
			ModuleCommandListener modCommandListener = (ModuleCommandListener) commandListener; 
			Bot.logInfo(String.format("Firing command %s for BotModule %s", commandData.getCommand(), modCommandListener.getMetaInfo().getName()));
			
			commandListener.onCommand(commandData);
		} else {
			Bot.logInfo(String.format("Firing command %s for Core", commandData.getCommand()));
			commandListener.onCommand(commandData);
		}
	}
	
	/**
	 * Check if a command exists
	 * @param command The command to check
	 * @return True if it exists, false if it does not
	 */
	public boolean commandExists(String command) {
		return this.commandListeners.containsKey(command);
	}
}
