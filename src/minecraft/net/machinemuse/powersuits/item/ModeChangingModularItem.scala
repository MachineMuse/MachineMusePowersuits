package net.machinemuse.powersuits.item

import net.machinemuse.numina.item.ModeChangingItem
import net.minecraft.item.ItemStack
import net.minecraft.util.Icon
import net.minecraft.entity.player.EntityPlayer
import net.machinemuse.api.ModuleManager
import net.machinemuse.utils.MuseItemUtils
import net.machinemuse.powersuits.common.ModularPowersuits
import net.machinemuse.api.moduletrigger.IRightClickModule
import net.minecraft.client.Minecraft

/**
 * Author: MachineMuse (Claire Semple)
 * Created: 4:52 PM, 9/5/13
 */
trait ModeChangingModularItem extends ModeChangingItem {
  override def getActiveMode(stack: ItemStack): String = {
    val mode = super.getActiveMode(stack)
    val modes = MuseItemUtils.getModes(stack, Minecraft.getMinecraft.thePlayer)
    if (mode.isEmpty && modes.size > 0) {
      modes.get(0)
    } else {
      mode
    }
  }

  def getPrevModeIcon(stack: ItemStack): Icon = ModuleManager.getModule(prevMode(stack)).getIcon(stack)

  def getCurrentModeIcon(stack: ItemStack): Icon = ModuleManager.getModule(getActiveMode(stack)).getIcon(stack)

  def getNextModeIcon(stack: ItemStack): Icon = ModuleManager.getModule(nextMode(stack)).getIcon(stack)

  def nextMode(stack: ItemStack): String = {
    val mode: String = getActiveMode(stack)
    val modes = MuseItemUtils.getModes(stack, Minecraft.getMinecraft.thePlayer)
    if (modes.size > 0) {
      val modeIndex: Int = modes.indexOf(mode)
      modes.get(clampMode(modeIndex + 1, modes.size))
    } else {
      mode
    }
  }

  def prevMode(stack: ItemStack): String = {
    val mode: String = getActiveMode(stack)
    val modes = MuseItemUtils.getModes(stack, Minecraft.getMinecraft.thePlayer)
    if (modes.size > 0) {
      val modeIndex: Int = modes.indexOf(mode)
      modes.get(clampMode(modeIndex - 1, modes.size))
    } else {
      mode
    }
  }


  def cycleMode(stack: ItemStack, dMode: Int, player: EntityPlayer) {
    if (stack != null && stack.getItem.isInstanceOf[ModeChangingItem]) {
      val mode: String = getActiveMode(stack)
      val modes = MuseItemUtils.getModes(stack, player)
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
