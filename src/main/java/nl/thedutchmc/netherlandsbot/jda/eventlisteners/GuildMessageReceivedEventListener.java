package nl.thedutchmc.netherlandsbot.jda.eventlisteners;

import java.util.Arrays;

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import nl.thedutchmc.netherlandsbot.commands.CommandData;
import nl.thedutchmc.netherlandsbot.commands.CommandRegistry;

public class GuildMessageReceivedEventListener extends ListenerAdapter {

	private CommandRegistry commandRegistry;
	
	public GuildMessageReceivedEventListener(CommandRegistry commandRegistry) {
		this.commandRegistry = commandRegistry;
	}
	
	@Override
	public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
		final String[] parts = event.getMessage().getContentDisplay().split(" ");
		
		if(parts.length == 0) {
			return;
		}
		
		final String commandName = parts[0];
		if(!this.commandRegistry.commandExists(commandName)) {
			return;
		}
		
		final String[] arguments = Arrays.copyOfRange(parts, 1, parts.length);
		final CommandData commandData = new CommandData(commandName, arguments, event.getMember(), event.getChannel(), event.getGuild(), event.getMessage());
		
		this.commandRegistry.fireCommand(commandData);
	}
}