package net.machinemuse.general.gui.frame

import java.util

import net.machinemuse.general.gui.clickable.ClickableButton
import net.machinemuse.numina.geometry.MusePoint2D
import net.machinemuse.powersuits.common.ModularPowersuits
import net.minecraft.client.resources.I18n
import net.minecraft.entity.player.EntityPlayer

/**
 * @author MachineMuse
 */
class TabSelectFrame(p: EntityPlayer, topleft: MusePoint2D, bottomright: MusePoint2D,
                     worldx: Int, worldy: Int, worldz: Int)
  extends IGuiFrame {
  val buttons = Array(
    (new ClickableButton( I18n.format("gui.tab.tinker"), topleft.midpoint(bottomright).minus(100, 0), worldy < 256  && worldy > 0), 0),
    (new ClickableButton( I18n.format("gui.tab.keybinds"), topleft.midpoint(bottomright), true), 1),
    (new ClickableButton( I18n.format("gui.tab.visual"), topleft.midpoint(bottomright).plus(100, 0), true), 3)
  )

  def onMouseDown(x: Double, y: Double, button: Int): Unit = {
    for (b <- buttons) {
      if (b._1.isEnabled && b._1.hitBox(x, y)) {
        p.openGui(ModularPowersuits, b._2, p.worldObj, worldx, worldy, worldz)
      }
    }
  }

  def onMouseUp(x: Double, y: Double, button: Int): Unit = {}

  def update(mousex: Double, mousey: Double): Unit = {}

  def draw(): Unit = for (b <- buttons) {
    b._1.draw()
  }

  val toolTip = new util.ArrayList[String]()

  def getToolTip(x: Int, y: Int): util.List[String] = null
}
