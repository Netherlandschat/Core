package nl.thedutchmc.netherlandsbot.commands;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

public class CommandData {

	private String command;
	private String[] arguments;
	private Member member;
	private TextChannel channel;
	private Guild guild;
	private Message message;
	
	public CommandData(String command, String[] arguments, Member member, TextChannel channel, Guild guild, Message message) {
		this.command = command;
		this.arguments = arguments;
		this.member = member;
		this.channel = channel;
		this.guild = guild;
		this.message = message;
	}

	/**
	 * @return the channel
	 */
	public TextChannel getChannel() {
		return channel;
	}

	/**
	 * @return the member
	 */
	public Member getMember() {
		return member;
	}

	/**
	 * @return the guild
	 */
	public Guild getGuild() {
		return guild;
	}

	/**
	 * @return the message
	 */
	public Message getMessage() {
		return message;
	}

	/**
	 * @return the command
	 */
	public String getCommand() {
		return command;
	}

	/**
	 * @return the arguments
	 */
	public String[] getArguments() {
		return arguments;
	}
}
