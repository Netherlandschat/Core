package nl.thedutchmc.netherlandsbot.modules;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.jar.JarFile;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;

import net.dv8tion.jda.api.requests.GatewayIntent;
import nl.thedutchmc.netherlandsbot.Bot;
import nl.thedutchmc.netherlandsbot.annotations.NotNull;
import nl.thedutchmc.netherlandsbot.annotations.Nullable;
import nl.thedutchmc.netherlandsbot.annotations.RegisterBotModule;
import nl.thedutchmc.netherlandsbot.modules.io.ModuleFileHandler;
import nl.thedutchmc.netherlandsbot.types.Pair;
import nl.thedutchmc.netherlandsbot.utils.AnnotationUtils;
import nl.thedutchmc.netherlandsbot.utils.FileUtils;

public class ModuleHandler {

	private Bot bot;
	
	private ModuleClassLoader classLoader;
	private List<BotModule> botModules = new ArrayList<>();
	
	public ModuleHandler(Bot bot) {
		this.bot = bot;
	}
	
	/**
	 * Load all modules
	 */
	public void loadModules() {
		//Check if a modules directory exists
		File moduleDirectory = new File(FileUtils.getJarDirectory() + File.separator + "modules");
		
		if(!moduleDirectory.exists()) {
			moduleDirectory.mkdirs();
		}
		
		//Get all JAR files in the modules directory
		List<File> jarFiles = discoverModuleFiles(moduleDirectory);
		
		Bot.logInfo(String.format("Discovered %d modules!", jarFiles.size()));
		
		//Get the URLs for the discovered JAR files
		URL[] jarUrls = new URL[jarFiles.size()];
		for(int i = 0; i < jarFiles.size(); i++) {
			File file = jarFiles.get(i);
			
			//Get the URL of the jar and add it to the array
			try {
				jarUrls[i] = file.toURI().toURL();
			} catch(MalformedURLException e) {
				e.printStackTrace();
			}
		}
		
		//Init the module class loader
		this.classLoader = new ModuleClassLoader(jarUrls, this.getClass().getClassLoader());
		
		List<GatewayIntent> intentsToEnable = new ArrayList<>();
		
		//Iterate over all jarfiles again
		for(File file : jarFiles) {
			JarFile jarFile = null;
			try {
				jarFile = new JarFile(file);
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			//Get all classes in the jar
			List<String> classNamesInJar = jarFile.stream()
					.map(ZipEntry::getName)
					.filter(name -> name.endsWith(".class"))
					.map(name -> name
							.replace(".class", "")
							.replace('/', '.'))
					.distinct()
					.collect(Collectors.toList());
				
			//Iterate over all classes and check if they're annotated with the RegisterBotModule annotation
			//if they are load them
			for(String className : classNamesInJar) {
				
				Pair<Boolean, Class<?>> annotated = AnnotationUtils.isClassAnnotatedWith(className, RegisterBotModule.class, this.classLoader);
				if(annotated.getFirst()) {
					
					BotModule botModule = this.classLoader.loadModuleClass(annotated.getSecond());
					
					//Get meta information from the BotModule
					//Stored in the RegisterBotModule annotation
					RegisterBotModule annotation = botModule.getClass().getAnnotation(RegisterBotModule.class);

					BotModuleMetaInformation meta = new BotModuleMetaInformation(
							annotation.name(),
							annotation.version());
					
					if(annotation.intents() != null) {
						intentsToEnable.addAll(Arrays.asList(annotation.intents()));
					}
					
					//Get the 'botModuleMetaInformation' field. This is a private field so we must use reflection
					Field metaField = null;
					try {
						metaField = botModule.getClass().getSuperclass().getDeclaredField("botModuleMetaInformation");
					} catch (NoSuchFieldException | SecurityException e) {
						e.printStackTrace();
					}
					
					//Allow access
					metaField.setAccessible(true);

					//Set the meta information
					try {
						metaField.set(botModule, meta);
					} catch (IllegalArgumentException | IllegalAccessException e) {
						e.printStackTrace();
					}
					
					//Remove access
					metaField.setAccessible(false);
					
					//Get the 'moduleFileHandler' field
					Field fileHandlerField = null;
					try {
						fileHandlerField = botModule.getClass().getSuperclass().getDeclaredField("moduleFileHandler");
					} catch (NoSuchFieldException | SecurityException e) {
						e.printStackTrace();
					}
					
					//Allow access
					fileHandlerField.setAccessible(true);
					
					//Set the value of the field
					ModuleFileHandler fileHandler = new ModuleFileHandler(botModule.getMetaInformation(), this);
					try {
						fileHandlerField.set(botModule, fileHandler);
					} catch (IllegalArgumentException | IllegalAccessException e) {
						e.printStackTrace();
					}
					
					//Remove access
					fileHandlerField.setAccessible(false);
					
					//Get the 'jdaHandler' field
					Field jdaHandlerField = null;
					try {
						jdaHandlerField = botModule.getClass().getSuperclass().getDeclaredField("jdaHandler");
					} catch (NoSuchFieldException | SecurityException e) {
						e.printStackTrace();
					}
					
					//Allow access
					jdaHandlerField.setAccessible(true);
					
					//Set the value of the field
					try {
						jdaHandlerField.set(botModule, this.bot.getJdaHandler());
					} catch (IllegalArgumentException | IllegalAccessException e) {
						e.printStackTrace();
					}
					
					//Remove access
					jdaHandlerField.setAccessible(false);
					
					//Lastly, add the BotModule to the list of botmodules
					this.botModules.add(botModule);
					
					break;
				}
			}
		}
		
		bot.getJdaHandler().setGatewayIntentsToEnable(intentsToEnable.toArray(new GatewayIntent[0]));
	}
	
	/**
	 * Get the ClassLoader used for loading modules
	 * @return Returns an instance of ModuleClassLoader. This is null before {@link #loadModules()} is called.
	 */
	@Nullable
	public ModuleClassLoader getModuleClassLoader() {
		return this.classLoader;
	}
	
	/**
	 * Enable all loaded modules
	 */
	public void enableModules() {
		//Iterate over all BotModules discovered and load them
		for(BotModule botModule : this.botModules) {
			Bot.logInfo(String.format("Loading BotModule %s version %s!", botModule.getMetaInformation().getName(), botModule.getMetaInformation().getVersion()));
			botModule.onLoad();
		}
		
		//Call the postLoad on all BotModules discovered
		for(BotModule botModule : this.botModules) {
			botModule.onPostLoad();
		}
	}
	
	/**
	 * Disable all loaded modules
	 */
	public void disableModules() {
		for(BotModule botModule : this.botModules) {
			botModule.onUnload();
		}
	}
	
	/**
	 * Discover module JAR files in moduleFolder
	 * @param moduleFolder The folder in which to look
	 * @return Returns a List of Files
	 */
	@NotNull
	private List<File> discoverModuleFiles(File moduleFolder) {
		Stream<Path> walk = null;
		try {
			walk = Files.walk(Paths.get(moduleFolder.getAbsolutePath()));
		} catch(IOException e) {
			e.printStackTrace();
		}
		
		List<String> fileNames = walk.map(x -> x.toString()).filter(f -> f.endsWith(".jar")).collect(Collectors.toList());
		List<File> result = new ArrayList<>();
		for(String str : fileNames) {
			result.add(new File(str));
		}
		
		return result;
	}
}
