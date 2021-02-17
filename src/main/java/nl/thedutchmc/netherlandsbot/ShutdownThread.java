package nl.thedutchmc.netherlandsbot;

import nl.thedutchmc.netherlandsbot.jda.JdaHandler;
import nl.thedutchmc.netherlandsbot.modules.ModuleHandler;

public class ShutdownThread extends Thread {

	private JdaHandler jdaHandler;
	private ModuleHandler moduleHandler;
	
	public ShutdownThread(JdaHandler jdaHandler, ModuleHandler moduleHandler) {
		this.jdaHandler = jdaHandler;
		this.moduleHandler = moduleHandler;
	}
	
	@Override
	public void run() {
		Bot.keepRunning = false;
		
		//Disable all modules
		this.moduleHandler.disableModules();
		
		//Shut down JDA
		try {
			this.jdaHandler.forceShutdown();
		} catch(Exception e) {}
	}
}