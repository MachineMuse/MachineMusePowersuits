package net.machinemuse.powersuits.network.packets

import cpw.mods.fml.common.network.Player
import net.machinemuse.api.IModularItem
import net.machinemuse.general.MuseLogger
import net.machinemuse.powersuits.network.{MusePackager, MusePacket}
import net.machinemuse.utils.MuseItemUtils
import net.minecraft.entity.player.EntityPlayerMP
import net.minecraft.nbt.NBTTagCompound
import java.io.DataInputStream
import scala.Predef._

/**
 * Author: MachineMuse (Claire Semple)
 * Created: 10:16 AM, 01/05/13
 */
object MusePacketCosmeticInfo extends MusePackager {
  def read(d: DataInputStream, p: Player) = {
    val itemSlot = readInt(d)
    val tagName = readString(d)
    val tagData = readNBTTagCompound(d)
    new MusePacketCosmeticInfo(p, itemSlot, tagName, tagData)
  }
}

class MusePacketCosmeticInfo(player: Player, itemSlot: Int, tagName: String, tagData: NBTTagCompound) extends MusePacket(player) {
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
        itemTag.setCompoundTag("render", renderTag)
      }
      else {
        renderTag = itemTag.getCompoundTag("render")
      }
      if (tagData.hasNoTags) {
        MuseLogger.logDebug("Removing tag " + tagName)
        renderTag.removeTag(tagName)
      }
      else {
        MuseLogger.logDebug("Adding tag " + tagName + " : " + tagData)
        renderTag.setCompoundTag(tagName, tagData)
      }
    }
  }
}