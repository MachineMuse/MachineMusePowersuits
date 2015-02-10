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
import net.minecraft.util.StatCollector

/**
 * Author: MachineMuse (Claire Semple)
 * Created: 1:08 AM, 4/24/13
 */
class BinocularsModule(list: java.util.List[IModularItem]) extends PowerModuleBase(list) with IPlayerTickModule {
  val FOV_MULTIPLIER = "Field of View"
  addInstallCost(MuseItemUtils.copyAndResize(ItemComponent.laserHologram, 1))
  addBaseProperty(FOV_MULTIPLIER, 0.5)
  addTradeoffProperty("FOV multiplier", FOV_MULTIPLIER, 9.5, "%")
  addPropertyLocalString(FOV_MULTIPLIER, StatCollector.translateToLocal("module.binoculars.view"))
  addPropertyLocalString("FOV multiplier", StatCollector.translateToLocal("module.binoculars.multiplier"))

  override def getCategory: String = MuseCommonStrings.CATEGORY_VISION

  override def getDataName: String = "Binoculars"

  override def getLocalizedName: String = StatCollector.translateToLocal("module.binoculars.name")

  override def getDescription: String = StatCollector.translateToLocal("module.binoculars.desc")

  override def getTextureFile: String = "binoculars"

  override def onPlayerTickActive(player: EntityPlayer, item: ItemStack) {
    if (FMLCommonHandler.instance().getEffectiveSide.isClient) {
      val fov = KeybindKeyHandler.zoom.getIsKeyPressed match {
        case true => ModuleManager.computeModularProperty(item, FOV_MULTIPLIER).toFloat
        case false => 0.1f
      }
      MusePlayerUtils.setFOVMult(player, fov)
    }

  }

  override def onPlayerTickInactive(player: EntityPlayer, item: ItemStack) {
  }
}
