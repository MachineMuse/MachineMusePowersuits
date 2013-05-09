/**
 *
 */
package net.machinemuse.powersuits.network.packets

import cpw.mods.fml.common.network.Player
import net.machinemuse.powersuits.network.{MusePackager, MusePacket}
import net.machinemuse.utils.MuseItemUtils
import net.minecraft.client.entity.EntityClientPlayerMP
import net.minecraft.entity.player.EntityPlayerMP
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound
import java.io.DataInputStream
import java.util.List
import scala.Predef._
/**
 * Author: MachineMuse (Claire Semple)
 * Created: 12:28 PM, 5/6/13
 */
object MusePacketModeChangeRequest extends MusePackager {
  def read(d: DataInputStream, p: Player) = {
    val slot = readInt(d)
    val mode = readString(d)
    new MusePacketModeChangeRequest(p, mode, slot)
  }
}

class MusePacketModeChangeRequest(player: Player, mode: String, slot: Int) extends MusePacket(player) {
  val packager = MusePacketModeChangeRequest

  def write {
    writeInt(slot)
    writeString(mode)
  }

  override def handleServer(player: EntityPlayerMP) {
    var stack: ItemStack = null
    if (slot > -1 && slot < 9) {
      stack = player.inventory.mainInventory(slot)
    }
    if (stack == null) {
      return
    }
    val modes: List[String] = MuseItemUtils.getModes(stack, player)
    if (modes.contains(mode)) {
      val itemTag: NBTTagCompound = MuseItemUtils.getMuseItemTag(stack)
      itemTag.setString("Mode", mode)
    }
  }

}
