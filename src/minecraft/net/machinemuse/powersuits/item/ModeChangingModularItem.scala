package net.machinemuse.powersuits.item

import net.machinemuse.numina.item.{NuminaItemUtils, ModeChangingItem}
import net.minecraft.item.ItemStack
import net.minecraft.util.Icon
import net.minecraft.entity.player.EntityPlayer
import net.machinemuse.api.{ModuleManager, IModularItem}
import net.minecraft.nbt.NBTTagCompound
import net.machinemuse.utils.MuseItemUtils
import net.machinemuse.powersuits.common.ModularPowersuits
import net.machinemuse.api.moduletrigger.IRightClickModule
import net.machinemuse.numina.general.MuseLogger

/**
 * Author: MachineMuse (Claire Semple)
 * Created: 4:52 PM, 9/5/13
 */
trait ModeChangingModularItem extends ModeChangingItem {

  def getPrevModeIcon(stack: ItemStack): Icon = null

  def getCurrentModeIcon(stack: ItemStack): Icon = ModuleManager.getModule(getActiveMode(stack)).getIcon(stack)

  def getNextModeIcon(stack: ItemStack): Icon = null

  def cycleMode(stack: ItemStack, dMode: Int, player: EntityPlayer) {
    if (stack != null && stack.getItem.isInstanceOf[IModularItem]) {
      var mode: String = getActiveMode(stack)
      val modes = MuseItemUtils.getModes(stack, player)
      if (mode.isEmpty && modes.size > 0) {
        mode = modes.get(0)
      }
      MuseLogger.logDebug(modes.toString)
      MuseLogger.logDebug(mode)
      if (modes.size > 0 && dMode != 0) {
        val modeIndex: Int = modes.indexOf(mode)
        val newMode: String = modes.get(clampMode(modeIndex + dMode, modes.size))
        setActiveMode(stack, newMode)
        ModularPowersuits.proxy.sendModeChange(player, dMode, newMode)
      }
    }
  }

  def getModes(stack: ItemStack, player: EntityPlayer) = {
    import scala.collection.JavaConversions._
    for {
      module <- ModuleManager.getAllModules
      if module.isValidForItem(stack, player)
      if module.isInstanceOf[IRightClickModule]
      if ModuleManager.itemHasModule(stack, module.getDataName)
    } yield {
      module.getDataName
    }
  }

  private def clampMode(selection: Int, modesSize: Int): Int = {
    if (selection > 0) {
      selection % modesSize
    } else {
      (selection + modesSize * (-selection)) % modesSize
    }
  }
}
