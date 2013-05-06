package net.machinemuse.general.gui.frame

import java.util
import net.machinemuse.general.gui.clickable.ClickableSlider
import net.machinemuse.general.geometry.{Colour, DrawableMuseRect, MusePoint2D, MuseRect}
import net.machinemuse.general.NBTTagAccessor
import net.machinemuse.utils.MuseItemUtils
import scala.collection.mutable.ArrayBuffer
import scala.collection.immutable.HashSet
import net.machinemuse.utils.GuiIcons._
import scala.collection.mutable
import scala.Some

/**
 * Author: MachineMuse (Claire Semple)
 * Created: 4:19 AM, 03/05/13
 */
class ColourPickerFrame(val borderRef: MuseRect, val insideColour: Colour, val borderColour: Colour, val itemSelector: ItemSelectionFrame) extends IGuiFrame {
  val border = new DrawableMuseRect(borderRef, insideColour, borderColour)
  val rslider: ClickableSlider = new ClickableSlider(new MusePoint2D(border.centerx, border.top + 10), border.width - 10, "Red")
  val gslider: ClickableSlider = new ClickableSlider(new MusePoint2D(border.centerx, border.top + 22), border.width - 10, "Green")
  val bslider: ClickableSlider = new ClickableSlider(new MusePoint2D(border.centerx, border.top + 34), border.width - 10, "Blue")

  var colours = new ArrayBuffer[Int]()

  var selectedSlider: Option[ClickableSlider] = None
  var selectedColour: Int = 0

  def refreshColours() {
    if (itemSelector.getSelectedItem != null) {
      import scala.collection.JavaConverters._
      val renderSpec = MuseItemUtils.getMuseRenderTag(itemSelector.getSelectedItem.getItem)
      val colourints: mutable.Buffer[Option[Int]] = {
        for (tag <- NBTTagAccessor.getValues(renderSpec).asScala) yield {
          if (tag.hasKey("colour")) Some(tag.getInteger("colour"))
          else None
        }
      }
      val colourset: HashSet[Int] = HashSet.empty ++ colours ++ colourints.flatten + Colour.WHITE.getInt
      colours.clear()
      for (c <- colourset) colours += c
    }
    onSelectColour(0)
  }

  def onMouseUp(x: Double, y: Double, button: Int) {
    selectedSlider = None
  }

  def update(mousex: Double, mousey: Double) {
    selectedSlider.map(s => {
      s.setValueByX(mousex)
    })
    if (colours.size > selectedColour) {
      val c = new Colour(rslider.value, gslider.value, bslider.value, 1.0)
      colours(selectedColour) = c.getInt
    }
  }

  def draw() {
    border.draw()
    rslider.draw()
    gslider.draw()
    bslider.draw()
    for (i <- 0 until colours.size) {
      ArmourColourPatch(border.left + 8 + i * 8, border.bottom - 16, new Colour(colours(i)))
    }
    ArmourColourPatch(border.left + 8 + colours.size * 8, border.bottom - 16, Colour.WHITE)
    SelectedArmorOverlay(border.left + 8 + selectedColour * 8, border.bottom - 16, Colour.WHITE)
  }

  def getToolTip(x: Int, y: Int): util.List[String] = null

  def onSelectColour(i: Int) {
    val c: Colour = new Colour(colours(i))
    rslider.setValue(c.r)
    gslider.setValue(c.g)
    bslider.setValue(c.b)
    selectedColour = i
  }

  def onMouseDown(x: Double, y: Double, button: Int) {
    if (rslider.hitBox(x, y)) {
      selectedSlider = Some(rslider)
    } else if (gslider.hitBox(x, y)) {
      selectedSlider = Some(gslider)
    } else if (bslider.hitBox(x, y)) {
      selectedSlider = Some(bslider)
    } else {
      selectedSlider = None
    }
    if (y > border.bottom - 16 && y < border.bottom - 8) {
      val colourCol = (x - border.left - 8.0) / 8.0
      if (colourCol > 0 && colourCol < colours.size) {
        onSelectColour(colourCol.toInt)
      } else if (colourCol == colours.size) {
        colours.append(Colour.WHITE.getInt)
      }
    }

  }
}
