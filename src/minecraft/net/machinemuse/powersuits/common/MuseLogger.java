package net.machinemuse.powersuits.common;

import java.util.logging.Logger;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;

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
		if (Config.isDebugging()) {
			Side side = FMLCommonHandler.instance().getEffectiveSide();
			Logger.getLogger("STDOUT").info(DEBUGPREFIX + side + ": " + string);
		}
	}

	/**
	 * @param string
	 */
	public static void logError(String string) {
		Side side = FMLCommonHandler.instance().getEffectiveSide();
		Logger.getLogger("STDERR").info(ERRORPREFIX + side + ": " + string);

	}
}
