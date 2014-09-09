package net.machinemuse.powersuits.event

import cpw.mods.fml.common.eventhandler.SubscribeEvent
import cpw.mods.fml.common.gameevent.TickEvent
import net.machinemuse.numina.network.{MusePacket, PacketSender}
import net.machinemuse.powersuits.control.{KeybindManager, PlayerInputMap}
import net.machinemuse.powersuits.network.packets.MusePacketPlayerUpdate
import net.machinemuse.utils.MuseItemUtils
import net.minecraft.client.Minecraft
import net.minecraft.client.entity.EntityClientPlayerMP

/**
 * This handler is called before/after the game processes input events and
 * updates the gui state mainly. *independent of rendering, so don't do rendering here
 * -is also the parent class of KeyBindingHandler
 *
 * @author MachineMuse
 */
class ClientTickHandler {
  @SubscribeEvent def onPreClientTick(event: TickEvent.ClientTickEvent) {
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
          PacketSender.sendToServer(inputPacket.getPacket131)
        }
      }
    }
  }
}