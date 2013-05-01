package net.machinemuse.utils

import java.awt.Font
import org.newdawn.slick.{Color, TrueTypeFont}
import net.machinemuse.general.geometry.Colour
import org.newdawn.slick.opengl.TextureImpl
import org.lwjgl.opengl.GL11._
import net.machinemuse.powersuits.common.Config

/**
 * Author: MachineMuse (Claire Semple)
 * Created: 7:18 PM, 4/30/13
 */
object SlickFont {
  // Chemical Reaction A -BRK-, retreived from http://www.fontpalace.com/
  val detail: Double = Config.fontDetail
  val antialias = Config.fontAntiAliasing
  val fontname = Config.fontName
  val fontURI = Config.fontURI
  val awtfont = createFont(fontname, Font.BOLD, 8 * detail).getOrElse(createFont(fontURI, (8 * detail)))
  val slickfont = new TrueTypeFont(awtfont, false)


  def createFont(uri: String, size: Double) = {
    val resource = getClass.getResourceAsStream(uri)
    Font.createFont(Font.TRUETYPE_FONT, resource).deriveFont(size.toFloat)
  }

  def createFont(name: String, style: Int, size: Double) = {
    name match {
      case "" => None
      case n: String => val font = new Font(name, style, size.toInt); if (font.canDisplay('a')) Some(font) else None
      case _ => None
    }
  }

  def apply(x: Double, y: Double, s: String, c: Colour = Colour.WHITE) {
    TextureImpl.bindNone()

    glPushMatrix
    glScaled(1.0 / detail, 1.0 / detail, 1.0 / detail)
    slickfont.drawString(((x + 1) * detail).toFloat, ((y + 1) * detail).toFloat, s, Color.black)
    slickfont.drawString((x * detail).toFloat, (y * detail).toFloat, s, new Color(c.r.toFloat, c.g.toFloat, c.b.toFloat, c.a.toFloat))
    glPopMatrix
  }

  def getStringWidth(s: String) = slickfont.getWidth(s) / detail
}
