package nl.thedutchmc.netherlandsbot.modules;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.net.URLClassLoader;

import nl.thedutchmc.netherlandsbot.Bot;
import nl.thedutchmc.netherlandsbot.annotations.Nullable;

public class ModuleClassLoader extends URLClassLoader {
	
	public ModuleClassLoader(URL[] urls, ClassLoader parent) {
		super("NetherlandsBotClassLoader", urls, parent);
	}
	
	/**
	 * Load a module class
	 * @param clazz The Class to load
	 * @return Returns an instance of BotModule
	 */
	@Nullable
	public BotModule loadModuleClass(Class<?> clazz) {
		
		//Try to get the Class as a subclass of BotModule
		Class<? extends BotModule> moduleClazz = null;
		try {
			moduleClazz = clazz.asSubclass(BotModule.class);
		} catch(ClassCastException e) {
			Bot.logError(String.format("Unable to load %s. Class annotated with @RegisterBotModule does not extend BotModule.", clazz.getName()));
			return null;
		}
		
		//Attempt to get the constructor of the class
		Constructor<?> moduleClazzConstructor = null;
		try {
			moduleClazzConstructor = moduleClazz.getConstructor();
		} catch (NoSuchMethodException | SecurityException e) {
			e.printStackTrace();
		}

		//Create an instance of the class and return it
		try {
			return (BotModule) moduleClazzConstructor.newInstance();
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
				| InvocationTargetException e) {
			e.printStackTrace();
		}
		
		return null;
	}
}
