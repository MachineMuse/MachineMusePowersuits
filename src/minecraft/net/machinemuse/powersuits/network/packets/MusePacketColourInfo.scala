package net.machinemuse.powersuits.network.packets

import net.minecraft.item.ItemStack
import java.lang.String
import scala.Predef.String
import net.minecraft.nbt.NBTTagCompound
import cpw.mods.fml.relauncher.Side
import cpw.mods.fml.common.FMLCommonHandler
import net.minecraft.entity.player.EntityPlayerMP
import net.machinemuse.api.IModularItem
import net.machinemuse.utils.MuseItemUtils
import net.machinemuse.general.MuseLogger
import cpw.mods.fml.common.network.Player
import net.machinemuse.powersuits.network.MusePacket
import java.io.{DataInputStream, IOException}
import net.minecraft.client.entity.EntityClientPlayerMP

/**
 * Author: MachineMuse (Claire Semple)
 * Created: 12:28 PM, 5/6/13
 */
class MusePacketColourInfo(player: Player) extends MusePacket(player) {
  protected var stack: ItemStack = null
  protected var itemSlot: Int = 0
  protected var tagData: Array[Int] = null

  /**
   * Constructor for sending this packet.
   *
   * @param player   Player making the request
   * @param itemSlot Slot containing the item for which the cosmetic change is requested
   * @param tagData  ref of the tag to be updated
   */
  def this(player: Player, itemSlot: Int, tagData: Array[Int]) {
    this(player)
    try {
      writeInt(itemSlot)
      writeIntArray(tagData)
    }
    catch {
      case e: IOException => {
        e.printStackTrace
      }
    }
  }

  /**
   * Constructor for receiving this packet.
   *
   * @param player
   * @param data
   * @throws java.io.IOException
   */
  def this(data: DataInputStream, player: Player) {
    this(player)
    this.datain = data
    try {
      itemSlot = readInt
      tagData = readIntArray
      val side: Side = FMLCommonHandler.instance.getEffectiveSide
      if (side eq Side.SERVER) {
        val srvplayer: EntityPlayerMP = player.asInstanceOf[EntityPlayerMP]
        stack = srvplayer.inventory.getStackInSlot(itemSlot)
      }
    }
    catch {
      case e: IOException => {
      }
    }
  }

  def handleServer(playerEntity: EntityPlayerMP) {
    if (stack != null && stack.getItem.isInstanceOf[IModularItem]) {
      val renderTag: NBTTagCompound = MuseItemUtils.getMuseRenderTag(stack)
      renderTag.setIntArray("colours", tagData)
    }
  }

  def handleClient(player: EntityClientPlayerMP) {
  }
}
