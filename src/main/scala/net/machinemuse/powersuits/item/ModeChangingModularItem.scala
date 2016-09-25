package net.machinemuse.powersuits.item

import net.machinemuse.api.ModuleManager
import net.machinemuse.numina.item.{ModeChangingItem, NuminaItemUtils}
import net.machinemuse.numina.scala.OptionCast
import net.minecraft.client.renderer.texture.TextureAtlasSprite
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack

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
  def getModeIcon(mode: String, stack: ItemStack, player: EntityPlayer): Option[TextureAtlasSprite] = Option(ModuleManager.getModule(mode)).map(m => m.getIcon(stack))

  def getValidModes(stack: ItemStack, player: EntityPlayer): Seq[String] = {
    getValidModes(stack)
  }

  def getValidModes(stack: ItemStack): Seq[String] = {
    import scala.collection.JavaConversions._
    for {
      module <- ModuleManager.getRightClickModules
      if module.isValidForItem(stack)
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
      if (validModes.nonEmpty) {
        validModes(0)
      } else {
        ""
      }
    }
  }
}
