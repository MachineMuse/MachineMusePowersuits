package net.machinemuse.powersuits.event

import net.machinemuse.numina.network.PacketSender
import net.machinemuse.powersuits.network.packets.MusePacketPropertyModifierConfig
import net.minecraft.entity.player.EntityPlayerMP
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent

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
