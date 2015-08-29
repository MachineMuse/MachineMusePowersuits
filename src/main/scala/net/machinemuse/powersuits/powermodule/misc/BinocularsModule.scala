package net.machinemuse.powersuits.powermodule.misc

import net.machinemuse.api.IModularItem
import net.machinemuse.api.moduletrigger.IToggleableModule
import net.machinemuse.powersuits.item.ItemComponent
import net.machinemuse.powersuits.powermodule.PowerModuleBase
import net.machinemuse.utils.{MuseCommonStrings, MuseItemUtils}

/**
 * Author: MachineMuse (Claire Semple)
 * Created: 1:08 AM, 4/24/13
 */
object BinocularsModule {
  val BINOCULARS_MODULE = "Binoculars"
  val FOV_MULTIPLIER = "Field of View"
}

class BinocularsModule(list: java.util.List[IModularItem]) extends PowerModuleBase(list) with IToggleableModule {
  addInstallCost(MuseItemUtils.copyAndResize(ItemComponent.laserHologram, 1))
  addBaseProperty(BinocularsModule.FOV_MULTIPLIER, 0.5)
  addTradeoffProperty("FOV multiplier", BinocularsModule.FOV_MULTIPLIER, 9.5, "%")

  override def getCategory: String = MuseCommonStrings.CATEGORY_VISION

  override def getDataName: String = BinocularsModule.BINOCULARS_MODULE

  override def getUnlocalizedName = "binoculars"

  override def getDescription: String = "With the problems that have been plaguing Optifine lately, you've decided to take that Zoom ability into your own hands."

  override def getTextureFile: String = "binoculars"

}
