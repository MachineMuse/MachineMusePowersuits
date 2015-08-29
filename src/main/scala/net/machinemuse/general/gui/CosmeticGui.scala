package net.machinemuse.general.gui

import net.machinemuse.general.gui.frame._
import net.machinemuse.numina.geometry.{Colour, MusePoint2D, MuseRect}
import net.machinemuse.powersuits.common.Config
import net.minecraft.client.gui.ScaledResolution
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack

/**
 * Author: MachineMuse (Claire Semple)
 * Created: 6:32 PM, 29/04/13
 */
class CosmeticGui(val player: EntityPlayer, val worldx: Int, val worldy: Int, val worldz: Int) extends MuseGui {
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
      new MusePoint2D(absX(-0.78F), absY(-0.025F)),
      Colour.LIGHTBLUE.withAlpha(0.8F),
      Colour.DARKBLUE.withAlpha(0.8F), player
    )
    frames.add(itemSelect)

    val renderframe = new ItemModelViewFrame(
      itemSelect,
      new MusePoint2D(absX(-0.75F), absY(-0.95f)),
      new MusePoint2D(absX(0.15F), absY(-0.025f)),
      Colour.LIGHTBLUE.withAlpha(0.8F),
      Colour.DARKBLUE.withAlpha(0.8F)
    )
    frames.add(renderframe)

    val colourpicker = new ColourPickerFrame(
      new MuseRect(absX(0.18f), absY(-0.95f),
        absX(0.95f), absY(-0.025f)),
      Colour.LIGHTBLUE.withAlpha(0.8F),
      Colour.DARKBLUE.withAlpha(0.8F),
      itemSelect
    )
    frames.add(colourpicker)


    val partframe = new PartManipContainer(
      itemSelect, colourpicker,
      new MusePoint2D(absX(-0.95F), absY(0.025f)),
      new MusePoint2D(absX(+0.95F), absY(0.95f)),
      Colour.LIGHTBLUE.withAlpha(0.8F),
      Colour.DARKBLUE.withAlpha(0.8F)
    )
    frames.add(partframe)

    val tabFrame: TabSelectFrame = new TabSelectFrame(
      player,
      new MusePoint2D(absX(-0.95F), absY(-1.05f)),
      new MusePoint2D(absX(0.95F), absY(-0.95f)),
      worldx, worldy, worldz)
    frames.add(tabFrame)
  }

  override def update() {
    super.update()
  }

  override def drawScreen(x: Int, y: Int, z: Float) {
    super.drawScreen(x, y, z)
  }
}
