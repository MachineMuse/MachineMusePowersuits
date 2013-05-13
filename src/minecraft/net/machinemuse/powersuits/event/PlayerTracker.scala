package net.machinemuse.powersuits.event

import cpw.mods.fml.common.IPlayerTracker
import net.minecraft.entity.player.EntityPlayer
import net.machinemuse.powersuits.network.packets.MusePacketPropertyModifierConfig
import cpw.mods.fml.common.network.{Player, PacketDispatcher}

/**
 * Author: MachineMuse (Claire Semple)
 * Created: 8:01 PM, 12/05/13
 */
class PlayerTracker extends IPlayerTracker {
  def onPlayerLogin(player: EntityPlayer) {
    PacketDispatcher.sendPacketToPlayer(
      new MusePacketPropertyModifierConfig(player.asInstanceOf[Player], null).getPacket250,
      player.asInstanceOf[Player]
    )
  }

  def onPlayerLogout(player: EntityPlayer) {}

  def onPlayerChangedDimension(player: EntityPlayer) {}

  def onPlayerRespawn(player: EntityPlayer) {}
}
