package net.machinemuse.powersuits.network.packets

import cpw.mods.fml.common.network.Player
import net.machinemuse.powersuits.network.{MusePackager, MusePacket}
import net.machinemuse.utils.MuseItemUtils
import net.minecraft.entity.player.EntityPlayerMP
import java.io.DataInputStream
import scala.Predef._


/**
 * Author: MachineMuse (Claire Semple)
 * Created: 12:28 PM, 5/6/13
 */
object MusePacketToggleRequest extends MusePackager {
  def read(d: DataInputStream, p: Player) = {

    val module = readString(d)
    val value = readBoolean(d)
    new MusePacketToggleRequest(p, module, value)
  }
}

class MusePacketToggleRequest(player: Player, module: String, active: Boolean) extends MusePacket(player) {
  val packager = MusePacketToggleRequest

  def write() {
    writeString(module)
    writeBoolean(active)
  }

  override def handleServer(player: EntityPlayerMP) {
    MuseItemUtils.toggleModuleForPlayer(player, module, active)
  }
}