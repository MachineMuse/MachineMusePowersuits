package net.machinemuse.general.gui.frame

import net.machinemuse.powersuits.client.render.modelspec.{ModelRegistry, ModelPartSpec, ModelSpec}
import net.machinemuse.general.geometry.{Colour, MuseRect, MuseRelativeRect}
import net.machinemuse.utils.{GuiIcons, MuseRenderer}
import org.lwjgl.opengl.GL11._
import net.minecraft.item.ItemStack
import net.machinemuse.general.gui.clickable.ClickableItem

/**
 * Author: MachineMuse (Claire Semple)
 * Created: 1:46 AM, 30/04/13
 */
class PartManipFrame(val model: ModelSpec, val selectFrame:ItemSelectionFrame, val border: MuseRelativeRect) {
  var specs: Iterable[ModelPartSpec] = model.apply.values.filter(spec => isValidArmor(selectFrame.getSelectedItem, spec.slot))
  var open: Boolean = true
  var mousex: Double = 0
  var mousey: Double = 0

  def update {
    specs = model.apply.values.filter(spec => isValidArmor(selectFrame.getSelectedItem, spec.slot))
  }

  def isValidArmor(stack: ClickableItem, slot: Int): Boolean = Option(stack).map(s => s.getItem.getItem.isValidArmor(s.getItem, slot)).getOrElse(false)

  def drawPartial(min: Int, max: Int) {
    ModelRegistry.getName(model).map(s => drawPartialString(s, min, max, border.left + 8, border.top))
    drawOpenArrow
    if (open) {
      (border.top /: specs) {
        case (y, spec) => {
          drawSpecPartial(border.left, y, spec, min, max)
          y + 8
        }
      }
    }
  }

  def drawSpecPartial(x: Double, y: Double, spec: ModelPartSpec, ymin: Int, ymax: Int) = {
    GuiIcons.TransparentArmor(x, y, ymin = ymin, ymax = ymax)
    GuiIcons.NormalArmor(x + 8, y, ymin = ymin, ymax = ymax)
    GuiIcons.GlowArmor(x + 16, y, ymin = ymin, ymax = ymax)
    drawPartialString(spec.displayName, ymin, ymax, x + 24, y)
  }

  def drawPartialString(s: String, min: Double, max: Double, x: Double, y: Double) {
    if (min < border.top + 2 && max > border.top + 6) {
      MuseRenderer.drawString(s, x, y)
    }
  }

  def drawOpenArrow {
    Colour.LIGHTBLUE.doGL
    glBegin(GL_TRIANGLES)
    if (open) {
      glVertex2d(border.left, border.top)
      glVertex2d(border.left + 4, border.top + 8)
      glVertex2d(border.left + 8, border.top)
    } else {
      glVertex2d(border.left, border.top)
      glVertex2d(border.left, border.top + 8)
      glVertex2d(border.left + 8, border.top + 4)
    }
    glEnd
    Colour.WHITE.doGL
  }

  def getBorder: MuseRect = {
    if (open) border.setHeight(9 + 9 * specs.size)
    else border.setHeight(9)
    return border
  }
}
