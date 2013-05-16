package net.machinemuse.general.gui.clickable

import java.lang.String
import net.machinemuse.general.geometry.{MusePoint2D, Colour, DrawableMuseRect}
import net.machinemuse.utils.{MuseMathUtils}
import net.machinemuse.utils.render.MuseRenderer

/**
 * Author: MachineMuse (Claire Semple)
 * Created: 7:08 AM, 06/05/13
 */
class ClickableSlider(val pos: MusePoint2D, val width: Double, val name: String) extends Clickable {
  this.position = pos
  val cornersize: Int = 3
  val insideRect = new DrawableMuseRect(position.x - width / 2.0 - cornersize, position.y + 8, 0, position.y + 16, Colour.LIGHTBLUE, Colour.ORANGE)
  val outsideRect = new DrawableMuseRect(position.x - width / 2.0 - cornersize, position.y + 8, position.x + width / 2.0 + cornersize, position.y + 16, Colour.LIGHTBLUE, Colour.DARKBLUE)

  override def draw() {
    MuseRenderer.drawCenteredString(name, position.x, position.y)
    this.insideRect.setRight(position.x + width * (value - 0.5) + cornersize)
    this.outsideRect.draw()
    this.insideRect.draw()
  }

  def hitBox(x: Double, y: Double): Boolean = {
    Math.abs(position.x - x) < width / 2 &&
      Math.abs(position.y + 12 - y) < 4
  }

  var value: Double = 0

  def setValueByX(x: Double) {
    val v = (x - pos.x) / width + 0.5
    value = MuseMathUtils.clampDouble(v, 0, 1)
  }

  def setValue(v: Double) {
    value = v
  }

}
