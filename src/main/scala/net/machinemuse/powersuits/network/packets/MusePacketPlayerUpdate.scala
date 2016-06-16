package net.machinemuse.powersuits.network.packets

import java.io.DataInputStream

import net.machinemuse.numina.network.{PacketSender, MusePackager, MusePacket}
import net.machinemuse.powersuits.control.PlayerInputMap
import net.minecraft.entity.player.{EntityPlayer, EntityPlayerMP}


/**
 * Author: MachineMuse (Claire Semple)
 * Created: 12:28 PM, 5/6/13
 */
object MusePacketPlayerUpdate extends MusePackager {
  def read(d: DataInputStream, p: EntityPlayer) = {
    val username = readString(d)
    val inputMap: PlayerInputMap = PlayerInputMap.getInputMapFor(username)
    inputMap.readFromStream(d)
    new MusePacketPlayerUpdate(p, inputMap)
  }
}

class MusePacketPlayerUpdate(player: EntityPlayer, inputMap: PlayerInputMap) extends MusePacket {
  val packager = MusePacketPlayerUpdate

  def write {
    writeString(player.getCommandSenderEntity.getName)
    inputMap.writeToStream(dataout)
  }

  override def handleServer(player: EntityPlayerMP) {
    val updatePacket: MusePacketPlayerUpdate = new MusePacketPlayerUpdate(player, inputMap)
    player.motionX = inputMap.motionX
    player.motionY = inputMap.motionY
    player.motionZ = inputMap.motionZ
    PacketSender.sendToAllAround(updatePacket, player, 128)
  }
}