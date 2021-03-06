package nl.thedutchmc.netherlandsbot.modules;

import net.dv8tion.jda.api.JDA;
import nl.thedutchmc.netherlandsbot.Bot;
import nl.thedutchmc.netherlandsbot.annotations.NotNull;
import nl.thedutchmc.netherlandsbot.commands.CommandRegistry;
import nl.thedutchmc.netherlandsbot.commands.ModuleCommandListener;
import nl.thedutchmc.netherlandsbot.jda.JdaHandler;
import nl.thedutchmc.netherlandsbot.modules.io.ModuleFileHandler;

public abstract class BotModule {
	
	//These fields are set using Reflection when the BotModule is loaded
	private BotModuleMetaInformation botModuleMetaInformation;
	private ModuleFileHandler moduleFileHandler;
	private JdaHandler jdaHandler;
	private CommandRegistry commandRegistry;
	
	/**
	 * Register a ModuleCommandListener
	 * @param command The name of the command, including prefix. E.g <pre>$funcommand</pre>
	 * @param listener An instance of a ModuleCommandListener
	 */
	public void registerCommandListener(String command, ModuleCommandListener listener) {
		this.commandRegistry.registerModuleCommandListener(command, listener, this.botModuleMetaInformation);
	}
	
	/**
	 * Get the module's meta information
	 * @return Returns BotModuleMetaInformation
	 */
	@NotNull
	public BotModuleMetaInformation getMetaInformation() {
		return this.botModuleMetaInformation;
	}
	
	/**
	 * Get the ModuleFileHandler for this module
	 * @return Returns ModuleFileHandler
	 */
	@NotNull
	public ModuleFileHandler getModuleFileHandler() {
		return this.moduleFileHandler;
	}
	
	/**
	 * Register a JDA event listener
	 * @param listener The listener to register. Must be a JDA event listener
	 */
	public void registerEventListener(Object listener) {
		this.jdaHandler.addEventListener(listener);
	}
	
	/**
	 * Get the JDA instance
	 * @return Returns the JDA instance
	 */
	public JDA getJda() {
		return this.jdaHandler.getJda();
	}
	
	/**
	 * Load the BotModule<br>
	 */
	public abstract void onLoad();
	
	/**
	 * Called after every BotModule has been loaded
	 */
	public void onPostLoad() {}
	
	/**
	 * Called when the application is shut down
	 */
	public void onUnload() {}
	
	/**
	 * Log at INFO level
	 * @param log The object to log
	 */
	public void logInfo(Object log) {
		Bot.logInfo("[" + botModuleMetaInformation.getName() + "] "+ log.toString());
	}
	
	/**
	 * Log at ERROR level
	 * @param log The object to log
	 */
	public void logError(Object log) {
		Bot.logError("[" + botModuleMetaInformation.getName() + "] "+ log.toString());
	}
}
