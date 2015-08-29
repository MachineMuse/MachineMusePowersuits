package net.machinemuse.general.gui.clickable

import net.machinemuse.api.{ILocalizeableModule, IPowerModule}
import net.machinemuse.utils.render.{MuseRenderer, GuiIcons}
import GuiIcons.Checkmark
import net.machinemuse.utils.MuseStringUtils
import java.util.ArrayList
import java.util.List
import net.machinemuse.numina.geometry.{Colour, MusePoint2D}
import net.machinemuse.numina.render.{MuseIconUtils, MuseTextureUtils}
import net.minecraft.util.StatCollector

/**
 * Extends the Clickable class to make a clickable Augmentation; note that this
 * will not be an actual item.
 *
 * @author MachineMuse
 */
class ClickableModule(val module: IPowerModule, position: MusePoint2D) extends Clickable(position) {
  var allowed: Boolean = true
  var installed: Boolean = false
  val checkmarkcolour = new Colour(0.0F, 0.667F, 0.0F, 1.0F)

  override def getToolTip: List[String] = {
    val toolTipText: List[String] = new ArrayList[String]
    toolTipText.add(getLocalizedName(getModule))
    toolTipText.addAll(MuseStringUtils.wrapStringToLength(getLocalizedDescription(getModule), 30))
    toolTipText
  }
  def getLocalizedName(m:IPowerModule): String = {
    m match {
      case m:ILocalizeableModule => StatCollector.translateToLocal("module." + m.getUnlocalizedName + ".name")
      case m => m.getLocalizedName
    }
  }

  def getLocalizedDescription(m:IPowerModule): String = {
    m match {
      case m:ILocalizeableModule => StatCollector.translateToLocal("module." + m.getUnlocalizedName + ".desc")
      case m => m.getDescription
    }
  }

  def draw {
    val k: Double = Integer.MAX_VALUE
    val left: Double = getPosition.x - 8
    val top: Double = getPosition.y - 8
    drawPartial(left, top, left + 16, top + 16)
  }

  def drawPartial(xmino: Double, ymino: Double, xmaxo: Double, ymaxo: Double) {
    val left: Double = getPosition.x - 8
    val top: Double = getPosition.y - 8
    MuseTextureUtils.pushTexture(getModule.getStitchedTexture(null))
    MuseIconUtils.drawIconAt(left, top, getModule.getIcon(null), Colour.WHITE)
    MuseTextureUtils.popTexture()
    if (!allowed) {
      val string: String = MuseStringUtils.wrapFormatTags("x", MuseStringUtils.FormatCodes.DarkRed)
      MuseRenderer.drawString(string, getPosition.x + 3, getPosition.y + 1)
    }
    else if (installed) {
      Checkmark(getPosition.x - 8 + 1, getPosition.y - 8 + 1, c = checkmarkcolour)
    }
  }

  def hitBox(x: Double, y: Double): Boolean = {
    val hitx: Boolean = Math.abs(x - getPosition.x) < 8
    val hity: Boolean = Math.abs(y - getPosition.y) < 8
    hitx && hity
  }

  def getModule: IPowerModule = module

  def equals(other: ClickableModule): Boolean = this.module == other.getModule


  def setAllowed(allowed: Boolean) {
    this.allowed = allowed
  }

  def setInstalled(installed: Boolean) {
    this.installed = installed
  }

}