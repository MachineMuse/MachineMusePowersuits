/**
 *
 */
package net.machinemuse.powersuits.network.packets

import cpw.mods.fml.common.network.Player
import net.machinemuse.powersuits.network.{MusePackager, MusePacket}
import net.machinemuse.utils.{MuseMathUtils, MuseItemUtils}
import net.minecraft.entity.player.EntityPlayerMP
import net.minecraft.nbt.NBTTagCompound
import java.io.DataInputStream
import scala.Predef._

/**
 * Packet for requesting to purchase an upgrade. Player-to-server. Server
 * decides whether it is a valid upgrade or not and <strike>replies with an associated
 * inventoryrefresh packet</strike>.
 *
 * Author: MachineMuse (Claire Semple)
 * Created: 12:28 PM, 5/6/13
 */
object MusePacketTweakRequest extends MusePackager {
  def read(d: DataInputStream, p: Player) = {
    val itemSlot = readInt(d)
    val moduleName = readString(d)
    val tweakName = readString(d)
    val tweakValue = readDouble(d)
    new MusePacketTweakRequest(p, itemSlot, moduleName, tweakName, tweakValue)
  }
}

class MusePacketTweakRequest(player: Player, itemSlot: Int, moduleName: String, tweakName: String, tweakValue: Double) extends MusePacket(player) {
  val packager = MusePacketTweakRequest

  def write {
    writeInt(itemSlot)
    writeString(moduleName)
    writeString(tweakName)
    writeDouble(tweakValue)
  }

  override def handleServer(playerEntity: EntityPlayerMP) {
    if (moduleName != null && tweakName != null) {
      val stack = playerEntity.inventory.getStackInSlot(itemSlot)
      val itemTag: NBTTagCompound = MuseItemUtils.getMuseItemTag(stack)
      if (itemTag != null && MuseItemUtils.tagHasModule(itemTag, moduleName)) {
        val moduleTag: NBTTagCompound = itemTag.getCompoundTag(moduleName)
        moduleTag.setDouble(tweakName, MuseMathUtils.clampDouble(tweakValue, 0, 1))
      }
    }
  }
}