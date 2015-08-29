package net.machinemuse.general.gui.frame

import java.util

import net.machinemuse.general.gui.clickable.ClickableSlider
import net.machinemuse.numina.general.MuseLogger
import net.machinemuse.numina.geometry.{Colour, DrawableMuseRect, MusePoint2D, MuseRect}
import net.machinemuse.numina.network.PacketSender
import net.machinemuse.powersuits.item.ItemPowerArmor
import net.machinemuse.powersuits.network.packets.MusePacketColourInfo
import net.machinemuse.utils.MuseItemUtils
import net.machinemuse.utils.render.GuiIcons
import net.machinemuse.utils.render.GuiIcons._
import net.minecraft.client.Minecraft
import net.minecraft.nbt.NBTTagIntArray
import net.minecraft.util.StatCollector

import scala.collection.mutable

/**
 * Author: MachineMuse (Claire Semple)
 * Created: 4:19 AM, 03/05/13
 */
class ColourPickerFrame(val borderRef: MuseRect, val insideColour: Colour, val borderColour: Colour, val itemSelector: ItemSelectionFrame) extends IGuiFrame {
  val border = new DrawableMuseRect(borderRef, insideColour, borderColour)
  val rslider: ClickableSlider = new ClickableSlider(new MusePoint2D(border.centerx, border.top + 8), border.width - 10, StatCollector.translateToLocal("gui.red"))
  val gslider: ClickableSlider = new ClickableSlider(new MusePoint2D(border.centerx, border.top + 24), border.width - 10, StatCollector.translateToLocal("gui.green"))
  val bslider: ClickableSlider = new ClickableSlider(new MusePoint2D(border.centerx, border.top + 40), border.width - 10, StatCollector.translateToLocal("gui.blue"))

  def colours: Array[Int] = getOrCreateColourTag.map(e => e.func_150302_c /*getIntArray()*/  ).getOrElse(Array.empty)

  var selectedSlider: Option[ClickableSlider] = None
  var selectedColour: Int = 0
  var decrAbove: Int = -1

  def getOrCreateColourTag: Option[NBTTagIntArray] = {
    if (itemSelector.getSelectedItem == null) return None
    val renderSpec = MuseItemUtils.getMuseRenderTag(itemSelector.getSelectedItem.getItem)
    if (renderSpec.hasKey("colours") && renderSpec.getTag("colours").isInstanceOf[NBTTagIntArray]) Some(renderSpec.getTag("colours").asInstanceOf[NBTTagIntArray])
    else {
      itemSelector.getSelectedItem.getItem.getItem match {
        case itembase: ItemPowerArmor =>
          val intArray: Array[Int] = Array(itembase.getColorFromItemStack(itemSelector.getSelectedItem.getItem).getInt, itembase.getGlowFromItemStack(itemSelector.getSelectedItem.getItem).getInt)
          renderSpec.setIntArray("colours", intArray)
        case _ =>
          val intArray: Array[Int] = Array.empty
          renderSpec.setIntArray("colours", intArray)
      }
      val player = Minecraft.getMinecraft.thePlayer
      if (player.worldObj.isRemote)
        PacketSender.sendToServer(new MusePacketColourInfo(player, itemSelector.getSelectedItem.inventorySlot, colours))
      Some(renderSpec.getTag("colours").asInstanceOf[NBTTagIntArray])
    }
  }

  def setColourTagMaybe(newarray: Array[Int]): Option[NBTTagIntArray] = {
    if (itemSelector.getSelectedItem == null) return None
    val renderSpec = MuseItemUtils.getMuseRenderTag(itemSelector.getSelectedItem.getItem)
    renderSpec.setTag("colours", new NBTTagIntArray(newarray))
    val player = Minecraft.getMinecraft.thePlayer
    if (player.worldObj.isRemote)
      PacketSender.sendToServer(new MusePacketColourInfo(player, itemSelector.getSelectedItem.inventorySlot, colours))
    Some(renderSpec.getTag("colours").asInstanceOf[NBTTagIntArray])
  }

  def importColours = {
    val colours = mutable.Buffer.empty[Int]
    colours
  }

  def refreshColours() {
    //    getOrCreateColourTag.map(coloursTag => {
    //      val colourints: Array[Int] = coloursTag.intArray
    //      val colourset: HashSet[Int] = HashSet.empty ++ colours ++ colourints
    //      val colourarray = colourset.toArray
    //      coloursTag.intArray = colourarray
    //    })
  }

  def onMouseUp(x: Double, y: Double, button: Int) {
    selectedSlider = None
  }

  def update(mousex: Double, mousey: Double) {
    selectedSlider.map(s => {
      s.setValueByX(mousex)
      if (colours.size > selectedColour) {
        colours(selectedColour) = Colour.getInt(rslider.value, gslider.value, bslider.value, 1.0)
        val player = Minecraft.getMinecraft.thePlayer
        if (player.worldObj.isRemote)
          PacketSender.sendToServer(new MusePacketColourInfo(player, itemSelector.getSelectedItem.inventorySlot, colours))
      }
    })
  }

  def draw() {
    border.draw()
    rslider.draw()
    gslider.draw()
    bslider.draw()
    for (i <- 0 until colours.size) {
      ArmourColourPatch(border.left + 8 + i * 8, border.bottom - 16, new Colour(colours(i)))
    }
    ArmourColourPatch(border.left + 8 + colours.size * 8, border.bottom - 16, Colour.WHITE)
    SelectedArmorOverlay(border.left + 8 + selectedColour * 8, border.bottom - 16, Colour.WHITE)
    MinusSign(border.left + 8 + selectedColour * 8, border.bottom - 24, Colour.RED)
    PlusSign(border.left + 8 + colours.size * 8, border.bottom - 16, Colour.GREEN)
  }

  def getToolTip(x: Int, y: Int): util.List[String] = null

  def onSelectColour(i: Int) {
    val c: Colour = new Colour(colours(i))
    rslider.setValue(c.r)
    gslider.setValue(c.g)
    bslider.setValue(c.b)
    selectedColour = i
  }

  def onMouseDown(x: Double, y: Double, button: Int) {
    if (rslider.hitBox(x, y)) {
      selectedSlider = Some(rslider)
    } else if (gslider.hitBox(x, y)) {
      selectedSlider = Some(gslider)
    } else if (bslider.hitBox(x, y)) {
      selectedSlider = Some(bslider)
    } else {
      selectedSlider = None
    }
    // add
    if (y > border.bottom - 16 && y < border.bottom - 8) {
      val colourCol: Int = (x - border.left - 8.0).toInt / 8
      if (colourCol >= 0 && colourCol < colours.size) {
        onSelectColour(colourCol.toInt)
      } else if (colourCol == colours.size) {
        MuseLogger.logDebug("Adding")
        getOrCreateColourTag.map(e => {
          setColourTagMaybe(getIntArray(e) :+ Colour.WHITE.getInt) // append White

        })

      }
    }
    // remove
    if (y > border.bottom - 24 && y < border.bottom - 16 && x > border.left + 8 + selectedColour * 8 && x < border.left + 16 + selectedColour * 8) {
      getOrCreateColourTag.map(e => {
        if (getIntArray(e).size > 1) {
          setColourTagMaybe( getIntArray(e) diff Array(getIntArray(e)(selectedColour)))
          decrAbove = selectedColour
          if (selectedColour == getIntArray(e).size) {
            selectedColour = selectedColour - 1
          }
          val player = Minecraft.getMinecraft.thePlayer
          if (player.worldObj.isRemote)
            PacketSender.sendToServer(new MusePacketColourInfo(player, itemSelector.getSelectedItem.inventorySlot, e.func_150302_c))
        }
      })

    }

  }
  def getIntArray(e:NBTTagIntArray) = e.func_150302_c()
}
