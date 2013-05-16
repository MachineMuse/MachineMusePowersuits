package net.machinemuse.general.gui.frame

import net.machinemuse.powersuits.client.render.modelspec.{ModelRegistry, ModelPartSpec, ModelSpec}
import net.machinemuse.general.geometry.{Colour, MuseRect, MuseRelativeRect}
import net.machinemuse.utils.{MuseItemUtils, MuseMathUtils}
import org.lwjgl.opengl.GL11._
import net.machinemuse.general.gui.clickable.ClickableItem
import net.minecraft.nbt.NBTTagCompound
import net.machinemuse.general.MuseLogger
import net.machinemuse.powersuits.network.packets.MusePacketCosmeticInfo
import net.minecraft.client.Minecraft
import cpw.mods.fml.common.network.Player
import net.minecraft.item.ItemArmor
import net.machinemuse.utils.render.{MuseRenderer, GuiIcons}

/**
 * Author: MachineMuse (Claire Semple)
 * Created: 1:46 AM, 30/04/13
 */
class PartManipSubFrame(val model: ModelSpec, val colourframe: ColourPickerFrame, val itemSelector: ItemSelectionFrame, val border: MuseRelativeRect) {
  var specs: Array[ModelPartSpec] = model.apply.values.filter(spec => isValidArmor(getSelectedItem, spec.slot)).toArray
  var open: Boolean = true
  var mousex: Double = 0
  var mousey: Double = 0


  def getArmorSlot = getSelectedItem.getItem.getItem.asInstanceOf[ItemArmor].armorType

  def getSelectedItem = itemSelector.getSelectedItem

  def getRenderTag: NBTTagCompound = MuseItemUtils.getMuseRenderTag(getSelectedItem.getItem, getArmorSlot)

  def getItemTag = MuseItemUtils.getMuseItemTag(getSelectedItem.getItem)

  def isValidArmor(clickie: ClickableItem, slot: Int): Boolean = if (clickie == null) false else clickie.getItem.getItem.isValidArmor(clickie.getItem, slot)

  def getSpecTag(spec: ModelPartSpec) = getRenderTag.getCompoundTag(ModelRegistry.makeName(spec))

  def getOrDontGetSpecTag(spec: ModelPartSpec): Option[NBTTagCompound] = {
    val name = ModelRegistry.makeName(spec)
    if (!getRenderTag.hasKey(name)) None
    else Some(getRenderTag.getCompoundTag(name))
  }

  def getOrMakeSpecTag(spec: ModelPartSpec): NBTTagCompound = {
    val name = ModelRegistry.makeName(spec)
    if (!getRenderTag.hasKey(name)) {
      val k = new NBTTagCompound()
      spec.multiSet(k, None, None, None)
      getRenderTag.setCompoundTag(name, k)
      k
    } else {
      getRenderTag.getCompoundTag(name)
    }
  }

  def updateItems {
    specs = model.apply.values.filter(spec => isValidArmor(getSelectedItem, spec.slot)).toArray
    border.setHeight(if (specs.size > 0) specs.size * 8 + 10 else 0)
  }

  def drawPartial(min: Double, max: Double) {
    if (specs.size > 0) {
      ModelRegistry.getName(model).map(s => MuseRenderer.drawString(s, border.left + 8, border.top))
      drawOpenArrow(min, max)
      if (open) {
        ((border.top + 8) /: specs) {
          case (y, spec) => {
            drawSpecPartial(border.left, y, spec, min, max)
            y + 8
          }
        }
      }
    }
  }

  def decrAbove(index: Int) {
    for (spec <- specs) {
      val tagname = ModelRegistry.makeName(spec)
      val player = Minecraft.getMinecraft.thePlayer
      val tagdata = getOrDontGetSpecTag(spec)
      tagdata.map(e => {
        val oldindex = spec.getColourIndex(e)
        if (oldindex >= index) {
          spec.setColourIndex(e, oldindex - 1)
          if (player.worldObj.isRemote) player.sendQueue.addToSendQueue(new MusePacketCosmeticInfo(player.asInstanceOf[Player], getSelectedItem.inventorySlot, tagname, e).getPacket250)
        }
      })

    }
  }

  def drawSpecPartial(x: Double, y: Double, spec: ModelPartSpec, ymino: Double, ymaxo: Double) = {
    val tag = getSpecTag(spec)
    val selcomp = if (tag.hasNoTags) 0 else if (spec.getGlow(tag)) 2 else 1
    val selcolour = spec.getColourIndex(tag)

    GuiIcons.TransparentArmor(x, y, ymin = ymino, ymax = ymaxo)
    GuiIcons.NormalArmor(x + 8, y, ymin = ymino, ymax = ymaxo)
    GuiIcons.GlowArmor(x + 16, y, ymin = ymino, ymax = ymaxo)
    GuiIcons.SelectedArmorOverlay(x + selcomp * 8, y, ymin = ymino, ymax = ymaxo)
    val textstartx = ((x + 28) /: colourframe.colours) {
      case (acc, colour) =>
        GuiIcons.ArmourColourPatch(acc, y, new Colour(colour), ymin = ymino, ymax = ymaxo)
        acc + 8
    }
    if (selcomp > 0) {
      GuiIcons.SelectedArmorOverlay(x + 28 + selcolour * 8, y, ymin = ymino, ymax = ymaxo)
    }


    MuseRenderer.drawString(spec.displayName, textstartx + 4, y)
  }

  def drawOpenArrow(min: Double, max: Double) {
    MuseRenderer.texturelessOn()
    Colour.LIGHTBLUE.doGL()
    glBegin(GL_TRIANGLES)
    import MuseMathUtils._
    if (open) {
      glVertex2d(border.left + 3, clampDouble(border.top + 3, min, max))
      glVertex2d(border.left + 5, clampDouble(border.top + 7, min, max))
      glVertex2d(border.left + 7, clampDouble(border.top + 3, min, max))
    } else {
      glVertex2d(border.left + 3, clampDouble(border.top + 3, min, max))
      glVertex2d(border.left + 3, clampDouble(border.top + 7, min, max))
      glVertex2d(border.left + 7, clampDouble(border.top + 5, min, max))
    }
    glEnd()
    Colour.WHITE.doGL()
    MuseRenderer.texturelessOff()
  }

  def getBorder: MuseRect = {
    if (open) border.setHeight(9 + 9 * specs.size)
    else border.setHeight(9)
    border
  }

  def tryMouseClick(x: Double, y: Double): Boolean = {
    if (x < border.left || x > border.right || y < border.top || y > border.bottom) false
    else if (x > border.left + 2 && x < border.left + 8 && y > border.top + 2 && y < border.top + 8) {
      open = !open
      getBorder
      true
    } else if (x < border.left + 24 && y > border.top + 8) {
      val lineNumber = ((y - border.top - 8) / 8).toInt
      val columnNumber = ((x - border.left) / 8).toInt
      val spec = specs(lineNumber.min(specs.size - 1).max(0))
      MuseLogger.logDebug("Line " + lineNumber + " Column " + columnNumber)
      columnNumber match {
        case 0 => {
          val renderTag = getRenderTag
          val tagname = ModelRegistry.makeName(spec)
          val player = Minecraft.getMinecraft.thePlayer
          renderTag.removeTag(ModelRegistry.makeName(spec))
          if (player.worldObj.isRemote) player.sendQueue.addToSendQueue(new MusePacketCosmeticInfo(player.asInstanceOf[Player], getSelectedItem.inventorySlot, tagname, new NBTTagCompound()).getPacket250)
          updateItems
          true
        }
        case 1 => {
          val tagname = ModelRegistry.makeName(spec)
          val player = Minecraft.getMinecraft.thePlayer
          val tagdata = getOrMakeSpecTag(spec)
          spec.setGlow(tagdata, false)
          if (player.worldObj.isRemote) player.sendQueue.addToSendQueue(new MusePacketCosmeticInfo(player.asInstanceOf[Player], getSelectedItem.inventorySlot, tagname, tagdata).getPacket250)
          updateItems
          true
        }
        case 2 => {
          val tagname = ModelRegistry.makeName(spec)
          val player = Minecraft.getMinecraft.thePlayer
          val tagdata = getOrMakeSpecTag(spec)
          spec.setGlow(tagdata, true)
          if (player.worldObj.isRemote) player.sendQueue.addToSendQueue(new MusePacketCosmeticInfo(player.asInstanceOf[Player], getSelectedItem.inventorySlot, tagname, tagdata).getPacket250)
          updateItems
          true
        }
        case _ => false
      }
    } else if (x > border.left + 28 && x < border.left + 28 + colourframe.colours.size * 8) {
      val lineNumber = ((y - border.top - 8) / 8).toInt
      val columnNumber = ((x - border.left - 28) / 8).toInt
      val spec = specs(lineNumber.min(specs.size - 1).max(0))
      val tagname = ModelRegistry.makeName(spec)
      val player = Minecraft.getMinecraft.thePlayer
      val tagdata = getOrMakeSpecTag(spec)
      spec.setColourIndex(tagdata, columnNumber)
      if (player.worldObj.isRemote) player.sendQueue.addToSendQueue(new MusePacketCosmeticInfo(player.asInstanceOf[Player], getSelectedItem.inventorySlot, tagname, tagdata).getPacket250)
      true
    }

    else false
  }

}
