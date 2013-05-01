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
  val detail=Config.fontDetail
  val antialias = Config.fontAntiAliasing
  val awtfont = new Font(Config.fontString, Font.PLAIN, (8*detail).toInt)
  val slickfont: TrueTypeFont = new TrueTypeFont(awtfont,false)

  def apply(x:Double,y:Double,s:String,c:Colour=Colour.WHITE) = {
    TextureImpl.bindNone()

    implicit def f(x:Double) = x.toFloat
    glPushMatrix
    glScaled(1.0/detail, 1.0/detail, 1.0/detail)
    slickfont.drawString(x*detail,y*detail,s,new Color(c.r,c.g,c.b,c.a))
    glPopMatrix
  }
  def getStringWidth(s:String) = slickfont.getWidth(s)/detail
}
