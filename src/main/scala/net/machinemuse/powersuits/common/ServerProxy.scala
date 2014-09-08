package net.machinemuse.powersuits.common

import cpw.mods.fml.common.registry.GameRegistry
import net.machinemuse.powersuits.event.PlayerTracker

/**
 * Author: MachineMuse (Claire Semple)
 * Created: 8:04 PM, 12/05/13
 */
class ServerProxy extends CommonProxy {
  override def registerEvents {
    GameRegistry.registerPlayerTracker(new PlayerTracker)
  }

}
