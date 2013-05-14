package net.machinemuse.general.gui.frame

import net.machinemuse.general.geometry.{MuseRelativeRect, MusePoint2D, Colour}
import net.machinemuse.powersuits.client.render.modelspec.{ModelSpec, ModelRegistry}
import net.minecraft.item.ItemStack
import org.lwjgl.opengl.GL11._

/**
 * Author: MachineMuse (Claire Semple)
 * Created: 6:39 PM, 29/04/13
 */
class PartManipContainer(val itemSelect: ItemSelectionFrame, val colourSelect: ColourPickerFrame, topleft: MusePoint2D, bottomright: MusePoint2D, borderColour: Colour, insideColour: Colour)
  extends ScrollableFrame(topleft, bottomright, borderColour, insideColour) {


  def getItem = Option(itemSelect.getSelectedItem).map(e => e.getItem)

  def getItemSlot = Option(itemSelect.getSelectedItem).map(e => e.inventorySlot)
  var lastItemSlot: Option[Int] = None

  def getColour = if(getItem != None && colourSelect.selectedColour < colourSelect.colours.size && colourSelect.selectedColour >= 0) colourSelect.colours(colourSelect.selectedColour) else Colour.WHITE.getInt
  var lastColour = getColour

  def getColourIndex = colourSelect.selectedColour
  var lastColourIndex = getColourIndex


  val modelframes: Seq[PartManipSubFrame] =
    ((Seq.empty[PartManipSubFrame], None: Option[PartManipSubFrame]) /: ModelRegistry.apply.values) {
      case ((frameseq, prev), modelspec: ModelSpec) => {
        val newframe = createNewFrame(modelspec, prev)
        (frameseq :+ newframe, Some(newframe))
      }
    }._1

  def createNewFrame(modelspec: ModelSpec, prev: Option[PartManipSubFrame]) = {
    val newborder = new MuseRelativeRect(topleft.x + 4, topleft.y + 4, bottomright.x, topleft.y + 10)
    newborder.setBelow(prev.map(e => e.border) getOrElse null)
    new PartManipSubFrame(modelspec, colourSelect, itemSelect, newborder)
  }

  override def onMouseDown(x: Double, y: Double, button: Int) {
    if (button == 0) {
      for (frame <- modelframes) {
        frame.tryMouseClick(x, y + currentscrollpixels)
      }
    }
  }

  //
  //  override def onMouseUp(x: Double, y: Double, button: Int) {}
  //
  override def update(mousex: Double, mousey: Double) {
    super.update(mousex, mousey)
    if (lastItemSlot != getItemSlot) {
      lastItemSlot = getItemSlot
      colourSelect.refreshColours()
      this.totalsize = (0.0 /: modelframes) {
        (acc, subframe) => subframe.updateItems; subframe.border.bottom()
      }.toInt
    }
    if(colourSelect.decrAbove > -1) {
      decrAbove(colourSelect.decrAbove)
      colourSelect.decrAbove = -1
    }
  }

  def decrAbove(index:Int) {for(frame<-modelframes) frame.decrAbove(index)}


  override def draw() {
    super.preDraw()
    glPushMatrix()
    glTranslated(0, -currentscrollpixels, 0)
    for (f <- modelframes) {
      f.drawPartial(currentscrollpixels + 4 + border.top, this.currentscrollpixels + border.bottom - 4)
    }
    glPopMatrix()
    super.postDraw()
  }

  //  override def getToolTip(x: Int, y: Int): util.List[String] = ???
}
