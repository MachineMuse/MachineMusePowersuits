package net.machinemuse.utils.render

import java.awt.Font
import org.newdawn.slick.{Color, TrueTypeFont}
import net.machinemuse.general.geometry.Colour
import org.newdawn.slick.opengl.TextureImpl
import org.lwjgl.opengl.GL11._
import net.machinemuse.powersuits.common.Config
import net.machinemuse.general.MuseLogger

/**
 * Author: MachineMuse (Claire Semple)
 * Created: 7:18 PM, 4/30/13
 */
object SlickFont {
  MuseLogger.logDebug("SlickFont creating...")
  val detail: Double = Config.fontDetail
  val antialias = Config.fontAntiAliasing
  val fontname = Config.fontName
  val fontURI = Config.fontURI
  val awtfont = createFont(fontname, Font.BOLD, 8 * detail).getOrElse(createFont(fontURI, (8 * detail)))
  val slickfont = new TrueTypeFont(awtfont, false)
  val boldfont = new TrueTypeFont(awtfont.deriveFont(Font.BOLD), false)
  val italfont = new TrueTypeFont(awtfont.deriveFont(Font.ITALIC), false)
  MuseLogger.logDebug("SlickFont created!")

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

  def apply(x: Double, y: Double, s: String, cm: Colour = Colour.WHITE) {
    TextureImpl.bindNone()
    val basecolor = new Color(cm.r.toFloat, cm.g.toFloat, cm.b.toFloat, cm.a.toFloat)
    glPushMatrix
    glScaled(1.0 / detail, 1.0 / detail, 1.0 / detail)
    val segments = s.split("\u00a7")
    ((basecolor, slickfont, x, true) /: segments) {
      case ((pen, font, xtail, first), segment) => {
        val (newpen, newfont, newsegment) = if (first) (pen, font, segment)
        else
          (switchpen(segment.charAt(0), pen, basecolor),
            switchfont(segment.charAt(0), font),
            segment.substring(1))
        drawStringWithShadow(x * detail, y * detail, newsegment, newpen, newfont)
        (newpen, newfont, xtail + newfont.getWidth(newsegment), false)
      }
    }
    glPopMatrix
  }

  private def drawStringWithShadow(x: Double, y: Double, s: String, c: Color, f: TrueTypeFont) {
    drawStringDetailed(x + detail, y + detail, s, Color.black, f)
    drawStringDetailed(x, y, s, c, f)
  }

  private def switchpen(s: Char, pen: Color, base: Color): Color = {
    s match {
      case '0' => Color.black
      case '1' => new Color(0, 0, 0.5f, 1.0f)
      case '2' => new Color(0, 0.5f, 0, 1.0f)
      case '3' => new Color(0, 0.5f, 0.5f, 1.0f)
      case '4' => new Color(0.5f, 0.0f, 0.0f, 1.0f)
      case '5' => new Color(0.5f, 0.0f, 0.5f, 1.0f)
      case '6' => new Color(0.5f, 0.5f, 0.0f, 1.0f)
      case '7' => new Color(0.75f, 0.75f, 0.75f, 1.0f)
      case '8' => new Color(0.5f, 0.5f, 0.5f, 1.0f)
      case '9' => new Color(0.0f, 0.0f, 1.0f, 1.0f)
      case 'a' => new Color(0.0f, 1.0f, 0.0f, 1.0f)
      case 'b' => new Color(0.0f, 1.0f, 1.0f, 1.0f)
      case 'c' => new Color(1.0f, 0.0f, 0.0f, 1.0f)
      case 'd' => new Color(1.0f, 0.0f, 1.0f, 1.0f)
      case 'e' => new Color(1.0f, 1.0f, 0.0f, 1.0f)
      case 'f' => new Color(1.0f, 1.0f, 1.0f, 1.0f)
      case 'r' => base
      case _ => pen
    }
  }

  private def switchfont(s: Char, font: TrueTypeFont): TrueTypeFont = {
    s match {
      case 'l' => boldfont
      case 'm' => font // strikethrough
      case 'n' => font // underline
      case 'o' => italfont
      case 'r' => slickfont
      case _ => font
    }
  }

  private def drawStringDetailed(x: Double, y: Double, s: String, c: Color, f: TrueTypeFont) {
    f.drawString((x).toFloat, (y).toFloat, s, c)
  }

  def getStringWidth(s: String) = {
    ((0, true) /: s.split("\u00a7")) {
      case ((width, first), sub) => first match {
        case true => (slickfont.getWidth(sub), false)
        case false => (width + slickfont.getWidth(sub.substring(1)), false)
      }

    }._1 / detail
  }
}
