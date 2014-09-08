package net.machinemuse.powersuits.common

import cpw.mods.fml.common.FMLCommonHandler
import net.machinemuse.powersuits.event.PlayerLoginHandlerThingy

/**
 * Author: MachineMuse (Claire Semple)
 * Created: 8:04 PM, 12/05/13
 */
class ServerProxy extends CommonProxy {
  override def registerEvents {
    FMLCommonHandler.instance().bus().register(PlayerLoginHandlerThingy)
  }

}
