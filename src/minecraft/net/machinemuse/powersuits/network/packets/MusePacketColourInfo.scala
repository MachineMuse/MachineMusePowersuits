package net.machinemuse.powersuits.network.packets

import net.minecraft.nbt.NBTTagCompound
import net.minecraft.entity.player.EntityPlayerMP
import net.machinemuse.api.IModularItem
import net.machinemuse.utils.MuseItemUtils
import cpw.mods.fml.common.network.Player
import net.machinemuse.powersuits.network.{MusePackager, MusePacket}
import java.io.DataInputStream


/**
 * Author: MachineMuse (Claire Semple)
 * Created: 12:28 PM, 5/6/13
 */
object MusePacketColourInfo extends MusePackager {
  def read(d: DataInputStream, p: Player) = {
    val itemSlot = readInt(d)
    val tagData = readIntArray(d)
    new MusePacketColourInfo(p, itemSlot, tagData)
  }
}

class MusePacketColourInfo(player: Player, itemSlot: Int, tagData: Array[Int]) extends MusePacket(player) {
  val packager = MusePacketColourInfo

  def write {
    writeInt(itemSlot)
    writeIntArray(tagData)
  }

  override def handleServer(playerEntity: EntityPlayerMP) {
    val stack = playerEntity.inventory.getStackInSlot(itemSlot)
    if (stack != null && stack.getItem.isInstanceOf[IModularItem]) {
      val renderTag: NBTTagCompound = MuseItemUtils.getMuseRenderTag(stack)
      renderTag.setIntArray("colours", tagData)
    }
  }
}