package net.machinemuse.general.gui.frame

import java.util
import net.machinemuse.general.geometry.{DrawableMuseRect, Colour, MusePoint2D}
import org.lwjgl.input.Mouse
import net.machinemuse.powersuits.client.render.item.ArmorModel
import net.minecraft.client.Minecraft
import org.lwjgl.opengl.GL11._
import net.machinemuse.utils.{MuseMathUtils, MuseItemUtils}
import net.minecraft.nbt.NBTTagCompound
import net.machinemuse.powersuits.item.ItemPowerArmor

/**
 * Author: MachineMuse (Claire Semple)
 * Created: 12:25 PM, 5/2/13
 */
class ItemModelViewFrame(itemSelector: ItemSelectionFrame, topleft: MusePoint2D, bottomright: MusePoint2D, borderColour: Colour, insideColour: Colour)
  extends IGuiFrame {
  val border = new DrawableMuseRect(topleft, bottomright, borderColour, insideColour)

  var anchorx: Double = 0
  var anchory: Double = 0
  var dragging: Int = -1
  var lastdWheel: Double = 0

  var rotx: Double = 0
  var roty: Double = 0
  var offsetx: Double = 0
  var offsety: Double = 0
  var zoom: Double = 64

  def getArmorSlot = getSelectedItem.getItem.getItem.asInstanceOf[ItemPowerArmor].armorType

  def getSelectedItem = itemSelector.getSelectedItem

  def getRenderTag: NBTTagCompound = MuseItemUtils.getMuseRenderTag(getSelectedItem.getItem, getArmorSlot)

  def onMouseDown(x: Double, y: Double, button: Int) {
    if (border.containsPoint(x, y)) {
      dragging = button
      anchorx = x
      anchory = y
    }
  }

  def onMouseUp(x: Double, y: Double, button: Int) {
    dragging = -1
  }

  def update(mousex: Double, mousey: Double) {
    if (border.containsPoint(mousex, mousey)) {
      val dscroll: Double = (lastdWheel - Mouse.getDWheel) / 120
      zoom = zoom * Math.pow(1.1, dscroll)
      lastdWheel = Mouse.getDWheel
    }
    val dx = mousex - anchorx
    val dy = mousey - anchory
    dragging match {
      case -1 => None
      case 0 => {
        rotx = MuseMathUtils.clampDouble((rotx + dy), -90, 90)
        roty = (roty - dx)
        anchorx = mousex
        anchory = mousey
      }
      case 1 => {
        offsetx = (offsetx + dx)
        offsety = (offsety + dy)
        anchorx = mousex
        anchory = mousey
      }
    }
  }

  def draw() {
    border.draw()
    if (itemSelector.getSelectedItem == null || !getSelectedItem.getItem.getItem.isInstanceOf[ItemPowerArmor]) return
    glPushMatrix()
    ArmorModel.instance.renderSpec = MuseItemUtils.getMuseRenderTag(getSelectedItem.getItem, getArmorSlot)
    ArmorModel.instance.visible = getArmorSlot
    glTranslated(border.centerx + offsetx, border.centery + offsety, 0)
    glScaled(zoom, zoom, zoom)
    glClear(GL_DEPTH_BUFFER_BIT)
    glDisable(GL_CULL_FACE)
    glRotatef(rotx.toFloat, 1, 0, 0)
    glRotatef(roty.toFloat, 0, 1, 0)
    glTranslated(0, -getArmorSlot / 2.0, 0)
    ArmorModel.instance.render(Minecraft.getMinecraft.thePlayer, 0, 0, 0, 0, 0, 0.0625f)
    glPopMatrix()
  }

  def getToolTip(x: Int, y: Int): util.List[String] = null
}
