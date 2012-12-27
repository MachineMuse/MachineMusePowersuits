package net.machinemuse.powersuits.common;

import java.util.logging.Logger;

/**
 * Logger access class. May become more fleshed out in the future.
 * 
 * @author MachineMuse
 * 
 */
public abstract class MuseLogger {
	protected final static String DEBUGPREFIX = "MMMPS - DEBUG - ";

	protected final static String ERRORPREFIX = "MMMPS - ERROR - ";

	public static void logDebug(String string) {
		Logger.getLogger("STDOUT").info(DEBUGPREFIX + string);
	}

	/**
	 * @param string
	 */
	public static void logError(String string) {
		Logger.getLogger("STDERR").info(ERRORPREFIX + string);

	}
}
