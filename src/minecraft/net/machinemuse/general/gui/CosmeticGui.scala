package net.machinemuse.general.gui

import net.minecraft.entity.player.EntityPlayer
import net.machinemuse.general.gui.frame.{PartManipContainer, ItemSelectionFrame}
import net.machinemuse.general.geometry.{Colour, MusePoint2D}
import net.minecraft.item.ItemStack

/**
 * Author: MachineMuse (Claire Semple)
 * Created: 6:32 PM, 29/04/13
 */
class CosmeticGui(val player: EntityPlayer) extends MuseGui {
  var itemSelect: ItemSelectionFrame = null
  var lastSelectedItem: ItemStack = null
  this.xSize = 256
  this.ySize = 200

  /**
   * Add the buttons (and other controls) to the screen.
   */
  override def initGui {
    super.initGui
    itemSelect = new ItemSelectionFrame(
      new MusePoint2D(absX(-0.95F), absY(-0.95F)),
      new MusePoint2D(absX(-0.78F), absY(0.95F)),
      Colour.LIGHTBLUE.withAlpha(0.8F),
      Colour.DARKBLUE.withAlpha(0.8F), player
    )
    frames.add(itemSelect)
    val partframe = new PartManipContainer(
      itemSelect,
      new MusePoint2D(absX(-0.75F), absY(-0.95f)),
      new MusePoint2D(absX(+0.25F), absY(0.25f)),
      Colour.LIGHTBLUE.withAlpha(0.8F),
      Colour.DARKBLUE.withAlpha(0.8F)
    )
    frames.add(partframe)
  }

  override def update() {
    super.update()
  }


}
