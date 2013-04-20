package net.machinemuse.general;

import java.util.logging.Logger;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.FMLLog;

/**
 * Logger access class. May become more fleshed out in the future.
 * 
 * @author MachineMuse
 * 
 */
object MuseLogger {
  val logger = Logger.getLogger("MMMPS-" + FMLCommonHandler.instance().getEffectiveSide());
		logger.setParent(FMLLog.getLogger());

  def logDebug(string : String) = {logger.info(string)}
  def logError(string : String) = {logger.warning(string)}
}
