package net.machinemuse.powersuits.item

import net.machinemuse.numina.item.{NuminaItemUtils, ModeChangingItem}
import net.minecraft.item.ItemStack
import net.minecraft.util.Icon
import net.machinemuse.api.ModuleManager
import net.machinemuse.api.moduletrigger.IRightClickModule
import net.minecraft.entity.player.EntityPlayer
import net.machinemuse.numina.scala.OptionCast

/**
 * Author: MachineMuse (Claire Semple)
 * Created: 4:52 PM, 9/5/13
 */
object ModeChangingModularItem {

  def cycleModeForItem(stack: ItemStack, player: EntityPlayer, dMode: Int) {
    for (
      s <- Option(stack);
      i <- OptionCast[ModeChangingModularItem](stack.getItem)
    ) {
      i.cycleMode(stack, player, dMode)
    }
  }
}

trait ModeChangingModularItem extends ModeChangingItem {
  def getModeIcon(mode: String, stack: ItemStack, player: EntityPlayer): Option[Icon] = Option(ModuleManager.getModule(mode)).map(m => m.getIcon(stack))

  def getValidModes(stack: ItemStack, player: EntityPlayer): Seq[String] = {
    getValidModes(stack)
  }

  def getValidModes(stack: ItemStack): Seq[String] = {
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

  def getActiveMode(stack: ItemStack): String = {
    val modeFromNBT = NuminaItemUtils.getTagCompound(stack).getString("mode")
    if (!modeFromNBT.isEmpty) {
      modeFromNBT
    } else {
      val validModes = getValidModes(stack)
      if (!validModes.isEmpty) {
        validModes(0)
      } else {
        ""
      }
    }
  }
}
