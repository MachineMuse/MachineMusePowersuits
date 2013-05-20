package net.machinemuse.general.gui.frame

import java.util
import net.machinemuse.general.gui.clickable.ClickableSlider
import net.machinemuse.general.geometry._
import net.machinemuse.general.MuseLogger
import net.machinemuse.utils.MuseItemUtils
import net.machinemuse.utils.render.GuiIcons
import GuiIcons._
import scala.collection.mutable
import scala.Array
import net.machinemuse.powersuits.item.ItemPowerArmor
import net.minecraft.nbt.NBTTagIntArray
import net.machinemuse.powersuits.network.packets.MusePacketColourInfo
import cpw.mods.fml.common.network.Player
import net.minecraft.client.Minecraft
import scala.Some

/**
 * Author: MachineMuse (Claire Semple)
 * Created: 4:19 AM, 03/05/13
 */
class ColourPickerFrame(val borderRef: MuseRect, val insideColour: Colour, val borderColour: Colour, val itemSelector: ItemSelectionFrame) extends IGuiFrame {
  val border = new DrawableMuseRect(borderRef, insideColour, borderColour)
  val rslider: ClickableSlider = new ClickableSlider(new MusePoint2D(border.centerx, border.top + 10), border.width - 10, "Red")
  val gslider: ClickableSlider = new ClickableSlider(new MusePoint2D(border.centerx, border.top + 22), border.width - 10, "Green")
  val bslider: ClickableSlider = new ClickableSlider(new MusePoint2D(border.centerx, border.top + 34), border.width - 10, "Blue")

  def colours: Array[Int] = getOrCreateColourTag.map(e => e.intArray).getOrElse(Array.empty)

  var selectedSlider: Option[ClickableSlider] = None
  var selectedColour: Int = 0
  var decrAbove: Int = -1

  def getOrCreateColourTag: Option[NBTTagIntArray] = {
    if (itemSelector.getSelectedItem == null) return None
    val renderSpec = MuseItemUtils.getMuseRenderTag(itemSelector.getSelectedItem.getItem)
    if (renderSpec.hasKey("colours") && renderSpec.getTag("colours").isInstanceOf[NBTTagIntArray]) Some(renderSpec.getTag("colours").asInstanceOf[NBTTagIntArray])
    else {
      val nbt = new NBTTagIntArray("colours")
      if (itemSelector.getSelectedItem.getItem.getItem.isInstanceOf[ItemPowerArmor]) {
        val itembase = itemSelector.getSelectedItem.getItem.getItem.asInstanceOf[ItemPowerArmor]
        val intArray: Array[Int] = Array(itembase.getColorFromItemStack(itemSelector.getSelectedItem.getItem).getInt, itembase.getGlowFromItemStack(itemSelector.getSelectedItem.getItem).getInt)
        renderSpec.setIntArray("colours", intArray)
      } else {
        val intArray: Array[Int] = Array.empty
        renderSpec.setIntArray("colours", intArray)
      }
      val player = Minecraft.getMinecraft.thePlayer
      if (player.worldObj.isRemote)
        player.sendQueue.addToSendQueue(new MusePacketColourInfo(player.asInstanceOf[Player], itemSelector.getSelectedItem.inventorySlot, colours).getPacket250)
      Some(renderSpec.getTag("colours").asInstanceOf[NBTTagIntArray])
    }
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
          player.sendQueue.addToSendQueue(new MusePacketColourInfo(player.asInstanceOf[Player], itemSelector.getSelectedItem.inventorySlot, colours).getPacket250)
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
          e.intArray = (e.intArray :+ Colour.WHITE.getInt)
          val player = Minecraft.getMinecraft.thePlayer
          if (player.worldObj.isRemote)
            player.sendQueue.addToSendQueue(new MusePacketColourInfo(player.asInstanceOf[Player], itemSelector.getSelectedItem.inventorySlot, e.intArray).getPacket250)
        })

      }
    }
    // remove
    if (y > border.bottom - 24 && y < border.bottom - 16 && x > border.left + 8 + selectedColour * 8 && x < border.left + 16 + selectedColour * 8) {
      getOrCreateColourTag.map(e => {
        if (e.intArray.size > 1) {
          e.intArray = e.intArray diff Array(e.intArray(selectedColour))
          decrAbove = selectedColour
          if (selectedColour == e.intArray.size) {
            selectedColour = selectedColour - 1
          }
          val player = Minecraft.getMinecraft.thePlayer
          if (player.worldObj.isRemote)
            player.sendQueue.addToSendQueue(new MusePacketColourInfo(player.asInstanceOf[Player], itemSelector.getSelectedItem.inventorySlot, e.intArray).getPacket250)
        }
      })

    }

  }
}
