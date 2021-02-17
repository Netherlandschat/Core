package nl.thedutchmc.netherlandsbot.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import net.dv8tion.jda.api.requests.GatewayIntent;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface RegisterBotModule {
	public String name() default "";
	public String version() default "";
	public GatewayIntent[] intents() default {};
}
