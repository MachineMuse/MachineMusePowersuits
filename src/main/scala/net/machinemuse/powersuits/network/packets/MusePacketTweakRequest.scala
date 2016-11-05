/**
 *
 */
package net.machinemuse.powersuits.network.packets

import java.io.DataInputStream

import net.machinemuse.api.ModuleManager
import net.machinemuse.numina.general.MuseMathUtils
import net.machinemuse.numina.network.{IMusePackager, MusePacket}
import net.machinemuse.utils.MuseItemUtils
import net.minecraft.entity.player.{EntityPlayer, EntityPlayerMP}
import net.minecraft.nbt.NBTTagCompound

/**
 * Packet for requesting to purchase an upgrade. Player-to-server. Server
 * decides whether it is a valid upgrade or not and <strike>replies with an associated
 * inventoryrefresh packet</strike>.
 *
 * Author: MachineMuse (Claire Semple)
 * Created: 12:28 PM, 5/6/13
 */
object MusePacketTweakRequest extends IMusePackager {
  def read(d: DataInputStream, p: EntityPlayer) = {
    val itemSlot = readInt(d)
    val moduleName = readString(d)
    val tweakName = readString(d)
    val tweakValue = readDouble(d)
    new MusePacketTweakRequest(p, itemSlot, moduleName, tweakName, tweakValue)
  }
}

class MusePacketTweakRequest(player: EntityPlayer, itemSlot: Int, moduleName: String, tweakName: String, tweakValue: Double) extends MusePacket {
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
      if (itemTag != null && ModuleManager.tagHasModule(itemTag, moduleName)) {
        val moduleTag: NBTTagCompound = itemTag.getCompoundTag(moduleName)
        moduleTag.setDouble(tweakName, MuseMathUtils.clampDouble(tweakValue, 0, 1))
      }
    }
  }
}