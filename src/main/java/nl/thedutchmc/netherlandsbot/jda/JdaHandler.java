package nl.thedutchmc.netherlandsbot.jda;

import java.util.Arrays;

import javax.security.auth.login.LoginException;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Activity.ActivityType;
import net.dv8tion.jda.api.requests.GatewayIntent;
import nl.thedutchmc.netherlandsbot.Bot;
import nl.thedutchmc.netherlandsbot.ShutdownThread;
import nl.thedutchmc.netherlandsbot.annotations.Nullable;

public class JdaHandler {

	private JDA jda;
	private GatewayIntent[] intentsToEnable;
	
	/**
	 * Load JDA
	 * @param token The bot token
	 * @param activityType ActivityType to use for the activity
	 * @param activityMessage The activity message to use for activity
	 */
	public void load(String token, ActivityType activityType, String activityMessage) {
		JDABuilder builder = JDABuilder.createDefault(token)
				.setAutoReconnect(true);		
		
		//Activity, shown on the profile of the bot
		builder.setActivity(Activity.of(activityType, activityMessage));
		
		//Intents
		builder.enableIntents(Arrays.asList(intentsToEnable));

		String gatewayIntentsStr = "";
		for(GatewayIntent intent : this.intentsToEnable) {
			gatewayIntentsStr += intent.toString().toLowerCase();
		}
		
		Bot.logInfo((gatewayIntentsStr == "") ? "Not enabling any intents" : "Enabling GatewayIntents: " + gatewayIntentsStr);
		
		
		try {
			this.jda = builder.build();
			this.jda.awaitReady();
		} catch(InterruptedException e) {
			
		} catch(LoginException e) {
			e.printStackTrace();
			//TODO error handling
		}
	}
	
	/**
	 * The gateway intents to enable. Should be called before {@link #load(String, ActivityType, String)}
	 * @param intents The intents to enable
	 */
	public void setGatewayIntentsToEnable(GatewayIntent[] intents) {
		this.intentsToEnable = intents;
	}
	
	/**
	 * Force shut down JDA. Should only be called by {@link ShutdownThread}
	 * @throws Exception
	 */
	public void forceShutdown() throws Exception {
		this.jda.shutdownNow();
	}
	
	/**
	 * Get the JDA instance
	 * @return Returns JDA instance, or null when {@link #load(String, ActivityType, String)} has not yet been called
	 */
	@Nullable
	public JDA getJda() {
		return this.jda;
	}
	
	/**
	 * Add an event listener to the JDA
	 * @param listener The listener to add
	 */
	public void addEventListener(Object listener) {
		this.jda.addEventListener(listener);
	}
	
}
