package net.machinemuse.utils

import net.minecraft.util.Icon
import net.machinemuse.general.geometry.Colour
import net.machinemuse.powersuits.common.Config

/**
 * Author: MachineMuse (Claire Semple)
 * Created: 4:01 AM, 30/04/13
 */
object GuiIcons {

  trait GuiIcon {
    val filepath: String

    def apply(x: Double, y: Double, xmin: Int = Integer.MIN_VALUE, ymin: Int = Integer.MIN_VALUE, xmax: Int = Integer.MAX_VALUE, ymax: Int = Integer.MAX_VALUE) {
      MuseRenderer.TEXTURE_MAP = filepath
      //      glPushMatrix
      //      glScaled(0.5,0.5,0.5)
      MuseRenderer.drawIconPartial(x, y, GuiIconDrawer, Colour.WHITE, xmin, ymin, xmax, ymax)
      //      glPopMatrix
    }
  }

  object Checkmark {
    val filepath = Config.TEXTURE_PREFIX + "gui/checkmark.png"
  }

  object TransparentArmor extends GuiIcon {
    val filepath = Config.TEXTURE_PREFIX + "gui/transparentarmor.png"
  }

  object NormalArmor extends GuiIcon {
    val filepath = Config.TEXTURE_PREFIX + "gui/normalarmor.png"
  }

  object GlowArmor extends GuiIcon {
    val filepath = Config.TEXTURE_PREFIX + "gui/glowarmor.png"
  }


  object GuiIconDrawer extends Icon {
    def getOriginX: Int = 0

    def getOriginY: Int = 0

    def getMinU: Float = 0

    def getMaxU: Float = 1

    def getInterpolatedU(d0: Double): Float = d0.toFloat

    def getMinV: Float = 0

    def getMaxV: Float = 1

    def getInterpolatedV(d0: Double): Float = d0.toFloat

    def getIconName: String = "GuiIcon"

    def getSheetWidth: Int = 8

    def getSheetHeight: Int = 8
  }

}
