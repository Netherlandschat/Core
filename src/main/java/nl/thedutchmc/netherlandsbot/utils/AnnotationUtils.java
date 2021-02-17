package nl.thedutchmc.netherlandsbot.utils;

import java.lang.annotation.Annotation;
import nl.thedutchmc.netherlandsbot.types.Pair;

public class AnnotationUtils {

	//TODO this could use some reworking
	public static Pair<Boolean, Class<?>> isClassAnnotatedWith(String className, Class<? extends Annotation> annotation, ClassLoader classLoader) {
		Class<?> clazz = null;
		try {
			clazz = Class.forName(className, false, classLoader);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		return new Pair<Boolean, Class<?>>(clazz.isAnnotationPresent(annotation), clazz);
	}
}
