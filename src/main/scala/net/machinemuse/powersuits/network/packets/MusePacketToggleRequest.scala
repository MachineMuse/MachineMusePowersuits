package net.machinemuse.powersuits.network.packets

import java.io.DataInputStream

import net.machinemuse.numina.network.{IMusePackager, MusePackager, MusePacket}
import net.machinemuse.utils.MuseItemUtils
import net.minecraft.entity.player.{EntityPlayer, EntityPlayerMP}


/**
 * Author: MachineMuse (Claire Semple)
 * Created: 12:28 PM, 5/6/13
 */
object MusePacketToggleRequest extends MusePackager {
  override def read(d: DataInputStream, p: EntityPlayer) = {

    val module = readString(d)
    val value = readBoolean(d)
    new MusePacketToggleRequest(p, module, value)
  }
}

class MusePacketToggleRequest(player: EntityPlayer, module: String, active: Boolean) extends MusePacket {
  val packager = MusePacketToggleRequest

  def write() {
    writeString(module)
    writeBoolean(active)
  }

  override def handleServer(player: EntityPlayerMP) {
    MuseItemUtils.toggleModuleForPlayer(player, module, active)
  }
}