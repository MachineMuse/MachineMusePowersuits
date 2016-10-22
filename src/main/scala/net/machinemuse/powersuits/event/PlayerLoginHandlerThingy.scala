package net.machinemuse.powersuits.event

import cpw.mods.fml.common.eventhandler.SubscribeEvent
import cpw.mods.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent
import net.machinemuse.numina.network.PacketSender
import net.machinemuse.powersuits.network.packets.MusePacketPropertyModifierConfig
import net.minecraft.entity.player.EntityPlayerMP

/**
 * Author: MachineMuse (Claire Semple)
 * Created: 8:01 PM, 12/05/13
 */
object PlayerLoginHandlerThingy {
  @SubscribeEvent
  def onPlayerLogin(e: PlayerLoggedInEvent) {
    val player = e.player
    PacketSender.sendTo(
      new MusePacketPropertyModifierConfig(player, null),
      player.asInstanceOf[EntityPlayerMP]
    )
  }
}
