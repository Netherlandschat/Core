package nl.thedutchmc.netherlandsbot.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import nl.thedutchmc.netherlandsbot.annotations.Nullable;

public class FileUtils {

	/**
	 * Save a file from the JAR to disk
	 * @param resourceName The name of the resource to save
	 * @param outputPath The path to save it to
	 */
	public static void saveResource(String resourceName, String outputPath) {
		Validate.notNull(resourceName);
		Validate.notNull(outputPath);
		
		InputStream in = null;
		
		try {
			in = FileUtils.class.getResourceAsStream("/" + resourceName);
			
			if(in == null) {
				throw new FileNotFoundException("Unable to find " + resourceName + "!");
			}
			
			Path exportPath = Paths.get(outputPath);
			Files.copy(in, exportPath);
		} catch(IOException e) {
			//TODO error handling
			e.printStackTrace();
		}
	}
	
	/**
	 * Get the directory the core bot JAR is in
	 * @return Returns a File. Null if an exception occured
	 */
	@Nullable
	public static File getJarDirectory() {
		File jarPath = null;
		try {
			jarPath = new File(FileUtils.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath());
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if(jarPath == null) {
			return null;
		}
		
		return new File(jarPath.getParentFile().getPath());
	}
}
