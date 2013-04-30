package net.machinemuse.general.gui

import net.minecraft.entity.player.EntityPlayer
import net.machinemuse.general.gui.frame.ItemSelectionFrame
import net.machinemuse.general.geometry.{Colour, MusePoint2D}
import net.minecraft.item.ItemStack

/**
 * Author: MachineMuse (Claire Semple)
 * Created: 6:32 PM, 29/04/13
 */
class CosmeticGui(val player: EntityPlayer) extends MuseGui {
  var itemSelect: ItemSelectionFrame = null
  var lastSelectedItem: ItemStack = null

  /**
   * Add the buttons (and other controls) to the screen.
   */
  override def initGui {
    super.initGui
    itemSelect = new ItemSelectionFrame(
      new MusePoint2D(absX(-0.95F), absY(-0.95F)),
      new MusePoint2D(absX(-0.78F), absY(0.95F)),
      Colour.LIGHTBLUE.withAlpha(0.8F),
      Colour.DARKBLUE.withAlpha(0.8F), player)
    frames.add(itemSelect)

  }

  override def update() {
    super.update()
    if (itemSelect.getSelectedItem != null) {

    }
  }


}
