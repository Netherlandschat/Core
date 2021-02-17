
package nl.thedutchmc.netherlandsbot.utils;

public class Validate {

	/**
	 * Validate that an Object is not null<br>
	 * Throws a RuntimeException if it is null
	 * @param o The Object to validate
	 * @return True if the Object is not null.
	 */
	public static boolean notNull(Object o) {
		if(o != null) {
			return true;
		}
		
		throw new RuntimeException("Object is not allowed to be null!");
	}
}
