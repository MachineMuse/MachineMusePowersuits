package net.machinemuse.powersuits.common;

import java.util.logging.Logger;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.FMLLog;

/**
 * Logger access class. May become more fleshed out in the future.
 * 
 * @author MachineMuse
 * 
 */
public abstract class MuseLogger {
	public static final Logger logger = Logger.getLogger("MMMPS-" + FMLCommonHandler.instance().getEffectiveSide());
	static {
		logger.setParent(FMLLog.getLogger());
	}

	public static void logDebug(String string) {
		logger.info(string);
	}

	/**
	 * @param string
	 */
	public static void logError(String string) {
		logger.warning(string);

	}
}
