package net.machinemuse.general.gui.frame

import java.util
import net.machinemuse.general.geometry.{DrawableMuseRect, Colour, MusePoint2D}
import net.minecraft.entity.player.EntityPlayer
import org.lwjgl.input.Mouse
import net.machinemuse.powersuits.client.render.item.ArmorModel
import net.minecraft.client.Minecraft
import org.lwjgl.opengl.GL11._
import net.machinemuse.utils.MuseItemUtils
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.item.ItemArmor
import net.machinemuse.powersuits.item.ItemPowerArmor
import net.machinemuse.general.MuseLogger._
import net.machinemuse.powersuits.tick.ClientTickHandler

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
  var zoom: Double = 32

  def getArmorSlot = getSelectedItem.getItem.getItem.asInstanceOf[ItemPowerArmor].armorType

  def getSelectedItem = itemSelector.getSelectedItem

  def getRenderTag: NBTTagCompound = MuseItemUtils.getMuseRenderTag(getSelectedItem.getItem, getArmorSlot)

  def onMouseDown(x: Double, y: Double, button: Int) {
    if (border.containsPoint(x, y)) {
      dragging = button
      anchorx = button
      anchory = button
    }
  }

  def onMouseUp(x: Double, y: Double, button: Int) {
    dragging = -1
  }

  def update(mousex: Double, mousey: Double) {
    if (border.containsPoint(mousex, mousey)) {
      zoom = zoom * Math.pow(1.1, Mouse.getEventDWheel/120)
    }
    dragging match {
      case -1 => None
      case 0 => {
        rotx = (rotx + anchory - mousey)
        roty = (roty + anchorx - mousex)
        anchorx = mousex
        anchory = mousey
      }
      case 1 => {
        offsetx = (offsetx + anchorx - mousex)
        offsety = (offsety + anchory - mousey)
        anchorx = mousex
        anchory = mousey
      }
    }
  }

  def draw() {
    border.draw()
    if(itemSelector.getSelectedItem == null || !getSelectedItem.getItem.getItem.isInstanceOf[ItemPowerArmor]) return
    glPushMatrix()
    ArmorModel.instance.renderSpec = MuseItemUtils.getMuseRenderTag(getSelectedItem.getItem, getArmorSlot)
    ArmorModel.instance.visible = getArmorSlot
    glTranslated(border.centerx + offsetx, border.centery + offsety, 0)
    glScaled(zoom,zoom,zoom)

    glRotatef(rotx.toFloat, 1,0,0)
    glRotatef(roty.toFloat, 0,1,0)
    glDisable(GL_CULL_FACE)
    glDisable(GL_DEPTH_TEST)
    ArmorModel.instance.render(Minecraft.getMinecraft.thePlayer, 1,1, 1,1,1,0.0625f)
    glPopMatrix()
  }

  def getToolTip(x: Int, y: Int): util.List[String] = null
}
