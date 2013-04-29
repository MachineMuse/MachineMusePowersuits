package net.machinemuse.powersuits.powermodule.misc

import net.machinemuse.powersuits.powermodule.PowerModuleBase
import net.machinemuse.utils.{MusePlayerUtils, MuseItemUtils, MuseCommonStrings}
import net.machinemuse.powersuits.item.ItemComponent
import net.minecraft.item.ItemStack
import net.minecraft.entity.player.EntityPlayer
import net.machinemuse.powersuits.control.KeybindKeyHandler
import cpw.mods.fml.common.FMLCommonHandler
import net.machinemuse.api.{ModuleManager, IModularItem}
import net.machinemuse.api.moduletrigger.IPlayerTickModule

/**
 * Author: MachineMuse (Claire Semple)
 * Created: 1:08 AM, 4/24/13
 */
class BinocularsModule(list: java.util.List[IModularItem]) extends PowerModuleBase(list) with IPlayerTickModule {
  val FOV_MULTIPLIER = "Field of View"
  addInstallCost(MuseItemUtils.copyAndResize(ItemComponent.laserHologram, 1))
  addBaseProperty(FOV_MULTIPLIER, 0.5)
  addTradeoffProperty("FOV multiplier", FOV_MULTIPLIER, 9.5, "%")

  override def getCategory: String = MuseCommonStrings.CATEGORY_VISION

  override def getName: String = "Binoculars"

  override def getDescription: String = "With the problems that have been plaguing Optifine lately, you've decided to take that Zoom ability into your own hands."

  override def getTextureFile: String = "binoculars"

  override def onPlayerTickActive(player: EntityPlayer, item: ItemStack) {
    if (FMLCommonHandler.instance().getEffectiveSide.isClient) {
      val fov = KeybindKeyHandler.zoom.pressed match {
        case true => ModuleManager.computeModularProperty(item, FOV_MULTIPLIER).toFloat
        case false => 0.1f
      }
      MusePlayerUtils.setFOVMult(player, fov)
    }

  }

  override def onPlayerTickInactive(player: EntityPlayer, item: ItemStack) {
  }
}
