package net.machinemuse.powersuits.common;

import java.util.logging.Logger;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.relauncher.Side;

/**
 * Logger access class. May become more fleshed out in the future.
 * 
 * @author MachineMuse
 * 
 */
public abstract class MuseLogger {
	public static final Logger logger = Logger.getLogger("MMMPS");
	static {
		logger.setParent(FMLLog.getLogger());
	}

	public static void logDebug(String string) {
			Side side1 = FMLCommonHandler.instance().getEffectiveSide();
			logger.info(side1 + ": " + string);
	}

	/**
	 * @param string
	 */
	public static void logError(String string) {
		Side side = FMLCommonHandler.instance().getEffectiveSide();
		logger.warning(side + ": " + string);

	}
}
