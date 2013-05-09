package net.machinemuse.powersuits.network.packets

import java.io.DataInputStream
import net.machinemuse.powersuits.entity.EntityPlasmaBolt
import net.machinemuse.powersuits.network.{MusePackager, MusePacket}
import net.minecraft.client.Minecraft
import net.minecraft.client.entity.EntityClientPlayerMP
import cpw.mods.fml.common.network.Player

/**
 * Author: MachineMuse (Claire Semple)
 * Created: 12:28 PM, 5/6/13
 */
object MusePacketPlasmaBolt extends MusePackager {
  def read(d: DataInputStream, p: Player) = {
    val entityID = readInt(d)
    val size = readDouble(d)
    new MusePacketPlasmaBolt(p, entityID, size)
  }
}

class MusePacketPlasmaBolt(player: Player, entityID: Int, size: Double) extends MusePacket(player) {
  val packager = MusePacketPlasmaBolt

  def write {
    writeInt(entityID)
    writeDouble(size)
  }

  override def handleClient(player: EntityClientPlayerMP) {
    try {
      val entity: EntityPlasmaBolt = Minecraft.getMinecraft.theWorld.getEntityByID(entityID).asInstanceOf[EntityPlasmaBolt]
      entity.size = this.size
    }
    catch {
      case e: Exception => {
        return
      }
    }
  }
}