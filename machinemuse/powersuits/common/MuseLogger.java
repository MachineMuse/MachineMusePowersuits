package machinemuse.powersuits.common;

import java.util.logging.Logger;

/**
 * Logger access class. May become more fleshed out in the future.
 * 
 * @author MachineMuse
 * 
 */
public abstract class MuseLogger {
	public static void logDebug(String string) {
		Logger.getLogger("STDOUT").info(string);
	}
}
