package net.machinemuse.powersuits.network.packets

import java.io.DataInputStream

import cpw.mods.fml.relauncher.{Side, SideOnly}
import net.machinemuse.numina.network.{IMusePackager, MusePackager, MusePacket}
import net.machinemuse.powersuits.entity.EntityPlasmaBolt
import net.minecraft.client.Minecraft
import net.minecraft.client.entity.EntityClientPlayerMP
import net.minecraft.entity.player.EntityPlayer

/**
 * Author: MachineMuse (Claire Semple)
 * Created: 12:28 PM, 5/6/13
 */
object MusePacketPlasmaBolt extends MusePackager {
  override def read(d: DataInputStream, p: EntityPlayer) = {
    val entityID = readInt(d)
    val size = readDouble(d)
    new MusePacketPlasmaBolt(p, entityID, size)
  }
}

class MusePacketPlasmaBolt(player: EntityPlayer, entityID: Int, size: Double) extends MusePacket {
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