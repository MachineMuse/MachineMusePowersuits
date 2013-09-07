package net.machinemuse.powersuits.network.packets

import java.io.DataInputStream
import net.machinemuse.powersuits.entity.EntityPlasmaBolt
import net.minecraft.client.Minecraft
import net.minecraft.client.entity.EntityClientPlayerMP
import cpw.mods.fml.common.network.Player
import net.machinemuse.numina.network.{MusePackager, MusePacket}
import cpw.mods.fml.relauncher.{Side, SideOnly}

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

  @SideOnly(Side.CLIENT)
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