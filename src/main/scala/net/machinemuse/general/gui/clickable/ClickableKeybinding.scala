package net.machinemuse.general.gui.clickable

import cpw.mods.fml.common.FMLCommonHandler
import cpw.mods.fml.relauncher.Side
import net.machinemuse.numina.geometry.Colour
import net.machinemuse.numina.geometry.MusePoint2D
import net.machinemuse.numina.network.PacketSender
import net.machinemuse.powersuits.common.Config
import net.machinemuse.powersuits.control.KeybindManager
import net.machinemuse.powersuits.network.packets.MusePacketToggleRequest
import net.machinemuse.utils.{MuseStringUtils, MuseItemUtils}
import net.machinemuse.utils.MuseStringUtils.FormatCodes
import net.machinemuse.utils.render.{GuiIcons, MuseRenderer}
import net.minecraft.client.Minecraft
import net.minecraft.client.entity.EntityClientPlayerMP
import net.minecraft.client.settings.KeyBinding
import net.minecraft.util.ChatComponentText
import org.lwjgl.input.Keyboard
import java.util.ArrayList
import java.util.Iterator
import java.util.List

import org.lwjgl.opengl.GL11

object ClickableKeybinding {
  def parseName(keybind: KeyBinding): String = {
    if (keybind.getKeyCode < 0) {
      return "Mouse" + (keybind.getKeyCode + 100)
    }
    else {
      return Keyboard.getKeyName(keybind.getKeyCode)
    }
  }
}

class ClickableKeybinding(val keybind: KeyBinding, position: MusePoint2D, val free: Boolean, var displayOnHUD: Boolean = false) extends ClickableButton(ClickableKeybinding.parseName(keybind), position, true) {
  protected var boundModules: List[ClickableModule] = new ArrayList[ClickableModule]
  var toggleval: Boolean = false
  var toggled: Boolean = false

  def doToggleTick {
    doToggleIf(keybind.getIsKeyPressed)
  }

  def doToggleIf(value: Boolean) {
    if (value && !toggled) {
      toggleModules()
      KeybindManager.writeOutKeybinds()
    }
    toggled = value
  }

  def toggleModules() {
    val player: EntityClientPlayerMP = Minecraft.getMinecraft.thePlayer
    if (player == null) {
      return
    }
    import scala.collection.JavaConversions._
    for (module <- boundModules) {
      val valstring: String = if (toggleval) " on" else " off"
      if ((FMLCommonHandler.instance.getEffectiveSide eq Side.CLIENT) && Config.toggleModuleSpam) {
        player.addChatMessage(new ChatComponentText("Toggled " + module.getModule.getDataName + valstring))
      }
      MuseItemUtils.toggleModuleForPlayer(player, module.getModule.getDataName, toggleval)
      val toggleRequest: MusePacketToggleRequest = new MusePacketToggleRequest(player, module.getModule.getDataName, toggleval)
      PacketSender.sendToServer(toggleRequest)
    }
    toggleval = !toggleval
  }

  override def draw() {
    super.draw()
    import scala.collection.JavaConversions._
    for (module <- boundModules) {
      MuseRenderer.drawLineBetween(this, module, Colour.LIGHTBLUE)
      GL11.glPushMatrix()
      GL11.glScaled(0.5, 0.5, 0.5)
      if (displayOnHUD) {
        MuseRenderer.drawString(MuseStringUtils.wrapFormatTags("HUD", FormatCodes.BrightGreen), this.position.x*2 + 6,this.position.y*2 + 6)
      } else {
        MuseRenderer.drawString(MuseStringUtils.wrapFormatTags("x", FormatCodes.Red), this.position.x*2 + 6,this.position.y*2 + 6)
      }
      GL11.glPopMatrix()
    }
  }

  def getKeyBinding: KeyBinding = {
    return keybind
  }

  def getBoundModules: List[ClickableModule] = {
    return boundModules
  }

  def bindModule(module: ClickableModule) {
    if (!boundModules.contains(module)) {
      boundModules.add(module)
    }
  }

  def unbindModule(module: ClickableModule) {
    boundModules.remove(module)
  }

  def unbindFarModules {
    val iterator: Iterator[ClickableModule] = boundModules.iterator
    var module: ClickableModule = null
    while (iterator.hasNext) {
      module = iterator.next
      val maxDistance: Int = getTargetDistance * 2
      val distanceSq: Double = module.getPosition.distanceSq(this.getPosition)
      if (distanceSq > maxDistance * maxDistance) {
        iterator.remove
      }
    }
  }

  def getTargetDistance: Int = {
    if (boundModules.size > 6) {
      16 + (boundModules.size - 6) * 3
    } else {
      16
    }
  }

  def attractBoundModules(exception: IClickable) {
    import scala.collection.JavaConversions._
    for (module <- boundModules) {
      if (module ne exception) {
        val euclideanDistance: MusePoint2D = module.getPosition.minus(this.getPosition)
        val directionVector: MusePoint2D = euclideanDistance.normalize
        val tangentTarget: MusePoint2D = directionVector.times(getTargetDistance).plus(this.getPosition)
        val midpointTangent: MusePoint2D = module.getPosition.midpoint(tangentTarget)
        module.move(midpointTangent.x, midpointTangent.y)
      }
    }
  }

  def equals(other: ClickableKeybinding): Boolean = {
    return other.keybind.getKeyCode == this.keybind.getKeyCode
  }

  def toggleHUDState {
    displayOnHUD = !displayOnHUD
  }
}