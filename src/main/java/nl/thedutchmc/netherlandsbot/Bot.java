package nl.thedutchmc.netherlandsbot;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import net.dv8tion.jda.api.entities.Activity.ActivityType;
import nl.thedutchmc.netherlandsbot.config.ConfigHandler;
import nl.thedutchmc.netherlandsbot.jda.JdaHandler;
import nl.thedutchmc.netherlandsbot.modules.ModuleHandler;

public class Bot {

	public static volatile boolean keepRunning = true;
	private static final Logger LOGGER = LogManager.getLogger(Bot.class);
	
	private ConfigHandler configHandler;
	private ModuleHandler moduleHandler;
	private JdaHandler jdaHandler;
	
	public static void main(String[] args) {		
		Bot bot = new Bot();
		bot.init();
		bot.setupShutdown();
	}
	
	private void init() {
		
		Bot.logInfo("Loading NetherlandsBot");
		
		//Load and read the configuration file
		Bot.logInfo("Reading configuration file");
		this.configHandler = new ConfigHandler();
		this.configHandler.read();
		
		//Prepare JDA
		Bot.logInfo("Preparing JDA");
		this.jdaHandler = new JdaHandler();

		//Load modules
		Bot.logInfo("Loading BotModules");
		this.moduleHandler = new ModuleHandler(this);
		this.moduleHandler.loadModules();
		
		//Load JDA
		Bot.logInfo("Loading JDA");
		this.jdaHandler.load(
				(String) this.configHandler.getValue("token"), 
				ActivityType.valueOf((String) this.configHandler.getValue("activityType")),
				(String) this.configHandler.getValue("activityMessage"));

		//Enable modules
		Bot.logInfo("Enabling modules");
		this.moduleHandler.enableModules();
		
		Bot.logInfo("Startup complete.");
	}
	
	private void setupShutdown() {
		ShutdownThread shutdownThread = new ShutdownThread(
				this.jdaHandler, 
				this.moduleHandler);
		Runtime.getRuntime().addShutdownHook(shutdownThread);
	}
	
	/**
	 * Get the JdaHandler
	 * @return Returns an instance of JdaHandler
	 */
	public JdaHandler getJdaHandler() {
		return this.jdaHandler;
	}
	
	/**
	 * Log an Object with log level INFO
	 * @param log The object to log
	 */
	public static void logInfo(Object log) {
		LOGGER.info(log);
	}
	
	/**
	 * Log an Object with log level ERROR
	 * @param log The object to log
	 */
	public static void logError(Object log) {
		LOGGER.warn(log);
	}
}
