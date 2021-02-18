package nl.thedutchmc.netherlandsbot.commands.executors;

import nl.thedutchmc.netherlandsbot.Bot;
import nl.thedutchmc.netherlandsbot.commands.CommandData;
import nl.thedutchmc.netherlandsbot.commands.CommandListener;

public class ModulesCommandExecutor extends CommandListener {

	private Bot bot;
	
	public ModulesCommandExecutor(Bot bot) {
		this.bot = bot;
	}

	@Override
	public void onCommand(CommandData commandData) {
		commandData.getChannel().sendMessage("Hoi!").queue();
	}
}
