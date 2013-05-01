package net.machinemuse.general.gui.clickable

import net.machinemuse.api.IPowerModule
import net.machinemuse.general.geometry.Colour
import net.machinemuse.general.geometry.MusePoint2D
import net.machinemuse.utils.GuiIcons.Checkmark
import net.machinemuse.utils.MuseRenderer
import net.machinemuse.utils.MuseStringUtils
import java.util.ArrayList
import java.util.List

/**
 * Extends the Clickable class to make a clickable Augmentation; note that this
 * will not be an actual item.
 *
 * @author MachineMuse
 */
class ClickableModule(val module: IPowerModule, position: MusePoint2D) extends Clickable(position) {
  protected var allowed: Boolean = true
  protected var installed: Boolean = false
  val checkmarkcolour = new Colour(0.0F, 0.667F, 0.0F, 1.0F)

  def getToolTip: List[String] = {
    val toolTipText: List[String] = new ArrayList[String]
    toolTipText.add(getModule.getName)
    toolTipText.addAll(MuseStringUtils.wrapStringToLength(getModule.getDescription, 30))
    return toolTipText
  }

  def draw {
    val k: Double = Integer.MAX_VALUE
    val left: Double = getPosition.x - 8
    val top: Double = getPosition.y - 8
    drawPartial(left, top, left + 16, top + 16)
  }

  def drawPartial(xmino: Double, ymino: Double, xmaxo: Double, ymaxo: Double) {
    val prevMap = MuseRenderer.TEXTURE_MAP
    MuseRenderer.TEXTURE_MAP = getModule.getStitchedTexture(null)
    val left: Double = getPosition.x - 8
    val top: Double = getPosition.y - 8
    MuseRenderer.drawIconPartialOccluded(left, top, getModule.getIcon(null), Colour.WHITE, xmino, ymino, xmaxo, ymaxo)
    if (!allowed) {
      val string: String = MuseStringUtils.wrapFormatTags("x", MuseStringUtils.FormatCodes.DarkRed)
      MuseRenderer.drawString(string, getPosition.x + 3, getPosition.y + 1)
    }
    else if (installed) {
      Checkmark(getPosition.x - 8 + 1, getPosition.y - 8 + 1, c = checkmarkcolour, ymin = ymino, ymax = ymaxo)
    }
    MuseRenderer.TEXTURE_MAP = prevMap
  }

  def hitBox(x: Double, y: Double): Boolean = {
    val hitx: Boolean = Math.abs(x - getPosition.x) < 8
    val hity: Boolean = Math.abs(y - getPosition.y) < 8
    return hitx && hity
  }

  def getModule: IPowerModule = {
    return module
  }

  def equals(other: ClickableModule): Boolean = {
    return this.module == other.getModule
  }

  def setAllowed(allowed: Boolean) {
    this.allowed = allowed
  }

  def setInstalled(installed: Boolean) {
    this.installed = installed
  }

}