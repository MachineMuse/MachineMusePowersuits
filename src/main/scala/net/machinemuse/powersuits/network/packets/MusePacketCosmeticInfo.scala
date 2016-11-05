package net.machinemuse.powersuits.network.packets

import java.io.DataInputStream

import net.machinemuse.api.electricity.IModularItem
import net.machinemuse.numina.general.MuseLogger
import net.machinemuse.numina.network.{IMusePackager, MusePacket}
import net.machinemuse.utils.MuseItemUtils
import net.minecraft.entity.player.{EntityPlayer, EntityPlayerMP}
import net.minecraft.nbt.NBTTagCompound

/**
 * Author: MachineMuse (Claire Semple)
 * Created: 10:16 AM, 01/05/13
 */
object MusePacketCosmeticInfo extends IMusePackager {
  def read(d: DataInputStream, p: EntityPlayer) = {
    val itemSlot = readInt(d)
    val tagName = readString(d)
    val tagData = readNBTTagCompound(d)
    new MusePacketCosmeticInfo(p, itemSlot, tagName, tagData)
  }
}

class MusePacketCosmeticInfo(player: EntityPlayer, itemSlot: Int, tagName: String, tagData: NBTTagCompound) extends MusePacket {
  val packager = MusePacketCosmeticInfo

  def write {
    writeInt(itemSlot)
    writeString(tagName)
    writeNBTTagCompound(tagData)
  }

  override def handleServer(playerEntity: EntityPlayerMP) {
    val stack = playerEntity.inventory.getStackInSlot(itemSlot)
    if (tagName != null && stack != null && stack.getItem.isInstanceOf[IModularItem]) {
      val itemTag: NBTTagCompound = MuseItemUtils.getMuseItemTag(stack)
      var renderTag: NBTTagCompound = null
      if (!itemTag.hasKey("render")) {
        renderTag = new NBTTagCompound
        itemTag.setTag("render", renderTag)
      } else {
        renderTag = itemTag.getCompoundTag("render")
      }
      if (tagData.hasNoTags) {
        MuseLogger.logDebug("Removing tag " + tagName)
        renderTag.removeTag(tagName)
      } else {
        MuseLogger.logDebug("Adding tag " + tagName + " : " + tagData)
        renderTag.setTag(tagName, tagData)
      }
    }
  }
}