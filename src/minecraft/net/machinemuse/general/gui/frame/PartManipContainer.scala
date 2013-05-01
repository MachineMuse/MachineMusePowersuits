package net.machinemuse.general.gui.frame

import net.machinemuse.general.geometry.{MuseRelativeRect, MusePoint2D, Colour}
import net.machinemuse.powersuits.client.render.modelspec.{ModelSpec, ModelRegistry}
import net.minecraft.item.ItemStack

/**
 * Author: MachineMuse (Claire Semple)
 * Created: 6:39 PM, 29/04/13
 */
class PartManipContainer(val itemSelect: ItemSelectionFrame, topleft: MusePoint2D, bottomright: MusePoint2D, borderColour: Colour, insideColour: Colour)
  extends ScrollableFrame(topleft, bottomright, borderColour, insideColour) {
  var lastItem: Option[ItemStack] = getItem

  val modelframes: Seq[PartManipFrame] =
    ((Seq.empty[PartManipFrame], None: Option[PartManipFrame]) /: ModelRegistry.apply.values) {
      case ((frameseq, prev), modelspec: ModelSpec) => {
        val newframe = createNewFrame(modelspec, prev)
        (frameseq :+ newframe, Some(newframe))
      }
    }._1

  def createNewFrame(modelspec: ModelSpec, prev: Option[PartManipFrame]) = {
    val newborder = new MuseRelativeRect(topleft.x, topleft.y, bottomright.x, topleft.y + 10)
    newborder.setBelow(prev.map(e => e.border) getOrElse null)
    new PartManipFrame(modelspec, itemSelect, newborder)
  }

  //  override def onMouseDown(x: Double, y: Double, button: Int) {}
  //
  //  override def onMouseUp(x: Double, y: Double, button: Int) {}
  //
  override def update(mousex: Double, mousey: Double) {
    super.update(mousex, mousey)
    if (lastItem != getItem) {
      lastItem = getItem
      for (f <- modelframes) {
        f.update
      }
    }
  }

  def getItem = Option(itemSelect.getSelectedItem).map(e => e.getItem)


  override def draw() {
    super.draw
    for (f <- modelframes) {
      f.drawPartial(this.currentscrollpixels, this.currentscrollpixels + this.border.height.toInt)
    }
  }

  //  override def getToolTip(x: Int, y: Int): util.List[String] = ???
}
