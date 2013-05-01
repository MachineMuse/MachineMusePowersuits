package net.machinemuse.utils

import java.awt.Font
import org.newdawn.slick.{Color, TrueTypeFont}
import net.machinemuse.general.geometry.Colour
import net.machinemuse.general.MuseLogger
import org.newdawn.slick.opengl.TextureImpl
import org.lwjgl.opengl.GL11._
import net.machinemuse.powersuits.common.Config

/**
 * Created with IntelliJ IDEA.
 * User: Claire2
 * Date: 4/30/13
 * Time: 7:18 PM
 * To change this template use File | Settings | File Templates.
 */
object SlickFont {
  // Chemical Reaction A -BRK-, retreived from http://www.fontpalace.com/
  val is = this.getClass.getResourceAsStream(Config.RESOURCE_PREFIX + "fonts/cra.ttf")
  val detail:Double=Config.fontDetail
  val antialias = Config.fontAntiAliasing
  val awtfont = Font.createFont(Font.TRUETYPE_FONT,is).deriveFont(32f)
  //val awtfont = new Font(Config.fontString, Font.BOLD, (8*detail).toInt)
  val slickfont: TrueTypeFont = new TrueTypeFont(awtfont,false)

  def apply(x:Double,y:Double,s:String,c:Colour=Colour.WHITE) = {
    TextureImpl.bindNone()

    glPushMatrix
    glScaled(1.0/detail, 1.0/detail, 1.0/detail)
    slickfont.drawString(((x+1)*detail).toFloat,((y+1)*detail).toFloat,s,Color.black)
    slickfont.drawString((x*detail).toFloat,(y*detail).toFloat,s,new Color(c.r.toFloat,c.g.toFloat,c.b.toFloat,c.a.toFloat))
    glPopMatrix
  }
  def getStringWidth(s:String) = slickfont.getWidth(s)/detail
}
