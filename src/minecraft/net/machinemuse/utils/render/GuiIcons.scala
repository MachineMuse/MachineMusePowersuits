package net.machinemuse.utils.render

import net.minecraft.util.Icon
import net.machinemuse.general.geometry.Colour
import net.machinemuse.powersuits.common.Config
import org.lwjgl.opengl.GL11._

/**
 * Author: MachineMuse (Claire Semple)
 * Created: 4:01 AM, 30/04/13
 */
object GuiIcons {

  trait GuiIcon {
    val filepath: String
    val size: Double

    def apply(x: Double, y: Double, c: Colour = Colour.WHITE, xmin: Double = Integer.MIN_VALUE, ymin: Double = Integer.MIN_VALUE, xmax: Double = Integer.MAX_VALUE, ymax: Double = Integer.MAX_VALUE) {
      MuseRenderer.pushTexture(filepath)
      glPushMatrix()
      MuseRenderer.blendingOn()
      val s = size / 16.0
      glScaled(s, s, s)
      MuseRenderer.drawIconPartialOccluded(x / s, y / s, GuiIconDrawer, c, xmin / s, ymin / s, xmax / s, ymax / s)
      MuseRenderer.blendingOff()
      glPopMatrix()
      MuseRenderer.popTexture()
    }
  }

  object Checkmark extends GuiIcon {
    val size = 16.0
    val filepath = Config.TEXTURE_PREFIX + "gui/checkmark.png"
  }

  object TransparentArmor extends GuiIcon {
    val size = 8.0
    val filepath = Config.TEXTURE_PREFIX + "gui/transparentarmor.png"
  }

  object NormalArmor extends GuiIcon {
    val size = 8.0
    val filepath = Config.TEXTURE_PREFIX + "gui/normalarmor.png"
  }

  object GlowArmor extends GuiIcon {
    val size = 8.0
    val filepath = Config.TEXTURE_PREFIX + "gui/glowarmor.png"
  }

  object SelectedArmorOverlay extends GuiIcon {
    val size = 8.0
    val filepath = Config.TEXTURE_PREFIX + "gui/armordisplayselect.png"
  }

  object ArmourColourPatch extends GuiIcon {
    val size = 8.0
    val filepath = Config.TEXTURE_PREFIX + "gui/colourclicker.png"
  }

  object MinusSign extends GuiIcon {
    val size = 8.0
    val filepath = Config.TEXTURE_PREFIX + "gui/minussign.png"
  }

  object PlusSign extends GuiIcon {
    val size = 8.0
    val filepath = Config.TEXTURE_PREFIX + "gui/plussign.png"
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
