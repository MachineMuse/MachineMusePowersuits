package net.machinemuse.powersuits.item

import net.machinemuse.numina.item.ModeChangingItem
import net.minecraft.item.ItemStack
import net.minecraft.util.Icon
import net.machinemuse.api.ModuleManager
import net.machinemuse.powersuits.common.ModularPowersuits
import net.machinemuse.api.moduletrigger.IRightClickModule

/**
 * Author: MachineMuse (Claire Semple)
 * Created: 4:52 PM, 9/5/13
 */
trait ModeChangingModularItem extends ModeChangingItem {
  override def getActiveMode(stack: ItemStack): String = {
    val mode = super.getActiveMode(stack)
    val modes = getModes(stack)
    if (modes.isEmpty) {
      ""
    } else if (mode.isEmpty) {
      modes(0)
    } else {
      mode
    }
  }

  def getPrevModeIcon(stack: ItemStack): Option[Icon] = Option(ModuleManager.getModule(prevMode(stack))).map(m=>m.getIcon(stack))

  def getCurrentModeIcon(stack: ItemStack): Option[Icon] = Option(ModuleManager.getModule(getActiveMode(stack))).map(m=>m.getIcon(stack))

  def getNextModeIcon(stack: ItemStack): Option[ Icon] = Option(ModuleManager.getModule(nextMode(stack))).map(m=>m.getIcon(stack))

  def nextMode(stack: ItemStack): String = {
    val mode: String = getActiveMode(stack)
    val modes = getModes(stack)
    if (modes.size > 0) {
      val modeIndex: Int = modes.indexOf(mode)
      modes(clampMode(modeIndex + 1, modes.size))
    } else {
      mode
    }
  }

  def prevMode(stack: ItemStack): String = {
    val mode: String = getActiveMode(stack)
    val modes = getModes(stack)
    if (modes.size > 0) {
      val modeIndex: Int = modes.indexOf(mode)
      modes(clampMode(modeIndex - 1, modes.size))
    } else {
      mode
    }
  }


  def cycleMode(stack: ItemStack, dMode: Int) {
    if (stack != null && stack.getItem.isInstanceOf[ModeChangingItem]) {
      val mode: String = getActiveMode(stack)
      val modes = getModes(stack)
      if (modes.size > 0 && dMode != 0) {
        val modeIndex: Int = modes.indexOf(mode)
        val newMode: String = modes(clampMode(modeIndex + dMode, modes.size))
        this.setActiveMode(stack, newMode)
        ModularPowersuits.proxy.sendModeChange(dMode, newMode)
      }
    }
  }

  def getModes(stack: ItemStack) = {
    import scala.collection.JavaConversions._
    for {
      module <- ModuleManager.getAllModules
      if module.isValidForItem(stack)
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
