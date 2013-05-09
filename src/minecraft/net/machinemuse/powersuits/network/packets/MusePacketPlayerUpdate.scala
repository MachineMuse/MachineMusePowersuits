package net.machinemuse.powersuits.network.packets

import java.io.DataInputStream
import net.machinemuse.powersuits.control.PlayerInputMap
import net.machinemuse.powersuits.network.{MusePackager, MusePacket}
import net.minecraft.client.entity.EntityClientPlayerMP
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.entity.player.EntityPlayerMP
import cpw.mods.fml.common.network.PacketDispatcher
import cpw.mods.fml.common.network.Player
import net.minecraft.item.ItemStack


/**
 * Author: MachineMuse (Claire Semple)
 * Created: 12:28 PM, 5/6/13
 */
object MusePacketPlayerUpdate extends MusePackager {
  def read(d: DataInputStream, p: Player) = {
    val username = readString(d)
    val inputMap:PlayerInputMap = PlayerInputMap.getInputMapFor(username)
    inputMap.readFromStream(d)
    new MusePacketPlayerUpdate(p, inputMap)
  }
}
class MusePacketPlayerUpdate(player: Player, inputMap: PlayerInputMap) extends MusePacket(player) {
  val packager = MusePacketPlayerUpdate

  def write {
    writeString(player.asInstanceOf[EntityPlayer].username)
    inputMap.writeToStream(dataout)
  }

  override def handleServer(player: EntityPlayerMP) {
    val updatePacket: MusePacketPlayerUpdate = new MusePacketPlayerUpdate(player.asInstanceOf[Player], inputMap)
    player.motionX = inputMap.motionX
    player.motionY = inputMap.motionY
    player.motionZ = inputMap.motionZ
    PacketDispatcher.sendPacketToAllAround(player.posX, player.posY, player.posZ, 128, player.dimension, updatePacket.getPacket250)
  }

}