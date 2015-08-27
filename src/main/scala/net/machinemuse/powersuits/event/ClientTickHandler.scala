package net.machinemuse.powersuits.event

import cpw.mods.fml.common.eventhandler.SubscribeEvent
import cpw.mods.fml.common.gameevent.TickEvent
import cpw.mods.fml.common.gameevent.TickEvent.{ClientTickEvent, RenderTickEvent}
import net.machinemuse.general.gui.{EnergyMeter, HeatMeter}
import net.machinemuse.numina.general.MuseLogger
import net.machinemuse.numina.network.{MusePacket, PacketSender}
import net.machinemuse.powersuits.block.BlockTinkerTable
import net.machinemuse.powersuits.common.Config
import net.machinemuse.powersuits.control.{KeybindManager, PlayerInputMap}
import net.machinemuse.powersuits.network.packets.MusePacketPlayerUpdate
import net.machinemuse.utils.render.MuseRenderer
import net.machinemuse.utils.{ElectricItemUtils, MuseHeatUtils, MuseItemUtils, MuseStringUtils}
import net.minecraft.client.Minecraft
import net.minecraft.client.entity.EntityClientPlayerMP
import net.minecraft.client.gui.ScaledResolution
import net.minecraft.entity.player.EntityPlayer

/**
 * This handler is called before/after the game processes input events and
 * updates the gui state mainly. *independent of rendering, so don't do rendering here
 * -is also the parent class of KeyBindingHandler
 *
 * @author MachineMuse
 */
class ClientTickHandler {
  @SubscribeEvent def onPreClientTick(event: ClientTickEvent) {
    if (event.phase == TickEvent.Phase.START) {
      import scala.collection.JavaConversions._
      for (kb <- KeybindManager.getKeybindings) {
        kb.doToggleTick()
      }
    }
    else {
      val player: EntityClientPlayerMP = Minecraft.getMinecraft.thePlayer
      if (player != null && MuseItemUtils.getModularItemsInInventory(player).size > 0) {
        val inputmap: PlayerInputMap = PlayerInputMap.getInputMapFor(player.getCommandSenderName)
        inputmap.forwardKey = Math.signum(player.movementInput.moveForward)
        inputmap.strafeKey = Math.signum(player.movementInput.moveStrafe)
        inputmap.jumpKey = player.movementInput.jump
        inputmap.sneakKey = player.movementInput.sneak
        inputmap.motionX = player.motionX
        inputmap.motionY = player.motionY
        inputmap.motionZ = player.motionZ
        if (inputmap.hasChanged) {
          inputmap.refresh()
          val inputPacket: MusePacket = new MusePacketPlayerUpdate(player, inputmap)
          PacketSender.sendToServer(inputPacket)
        }
      }
    }
  }

  @SubscribeEvent def onRenderTickEvent(event: RenderTickEvent) {
    if (event.phase == TickEvent.Phase.END) {
      val player: EntityPlayer = Minecraft.getMinecraft.thePlayer
      if (player != null && MuseItemUtils.modularItemsEquipped(player).size > 0 && Minecraft.getMinecraft.currentScreen == null) {
        val mc: Minecraft = Minecraft.getMinecraft
        val screen: ScaledResolution = new ScaledResolution(mc, mc.displayWidth, mc.displayHeight)
        drawMeters(player, screen)
      }
    }
  }

  protected var heat: HeatMeter = null
  protected var energy: HeatMeter = null
  private var lightningCounter: Int = 0

  private def drawMeters(player: EntityPlayer, screen: ScaledResolution) {
    val currEnergy: Double = ElectricItemUtils.getPlayerEnergy(player)
    val maxEnergy: Double = ElectricItemUtils.getMaxEnergy(player)
    val currHeat: Double = MuseHeatUtils.getPlayerHeat(player)
    val maxHeat: Double = MuseHeatUtils.getMaxHeat(player)
    if (maxEnergy > 0 && BlockTinkerTable.energyIcon != null) {
      val currStr: String = MuseStringUtils.formatNumberShort(currEnergy)
      val maxStr: String = MuseStringUtils.formatNumberShort(maxEnergy)
      val currHeatStr: String = MuseStringUtils.formatNumberShort(currHeat)
      val maxHeatStr: String = MuseStringUtils.formatNumberShort(maxHeat)
      if (Config.useGraphicalMeters) {
        if (energy == null) {
          energy = new EnergyMeter
          heat = new HeatMeter
        }
        val left: Double = screen.getScaledWidth - 20
        val top: Double = screen.getScaledHeight / 2.0 - 16
        energy.draw(left, top, currEnergy / maxEnergy)
        heat.draw(left + 8, top, currHeat / maxHeat)
        MuseRenderer.drawRightAlignedString(currStr, left - 2, top + 10)
        MuseRenderer.drawRightAlignedString(currHeatStr, left - 2, top + 20)
      }
      else {
        MuseRenderer.drawString(currStr + '/' + maxStr + " \u1D60", 1, 1)
        MuseRenderer.drawString(currHeatStr + '/' + maxHeatStr + " C", 1, 10)
      }
    }
  }
}