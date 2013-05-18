package net.machinemuse.powersuits.network

import cpw.mods.fml.common.network.Player
import java.io.{DataInputStream, IOException, DataOutputStream, ByteArrayOutputStream}
import net.minecraft.network.packet.Packet250CustomPayload
import net.machinemuse.powersuits.common.Config
import net.minecraft.client.entity.EntityClientPlayerMP
import net.minecraft.entity.player.EntityPlayerMP
import net.minecraft.item.ItemStack
import net.minecraft.nbt.{CompressedStreamTools, NBTTagCompound}
import net.machinemuse.general.MuseLogger
import net.machinemuse.utils.RichInputStream

/**
 * Author: MachineMuse (Claire Semple)
 * Created: 12:58 AM, 09/05/13
 */
abstract class MusePacket(val player: Player) {
  val packager: MusePackager

  def write()

  val bytes = new ByteArrayOutputStream
  val dataout = new DataOutputStream(bytes)

  /**
   * Gets the MC packet associated with this MusePacket
   *
   * @return Packet250CustomPayload
   */
  def getPacket250: Packet250CustomPayload = {
    dataout.writeInt(MusePacketHandler.packagers.inverse.get(packager).get)
    write()
    new Packet250CustomPayload(Config.getNetworkChannelName, bytes.toByteArray)
  }

  /**
   * Called by the network manager since it does all the packet mapping
   *
   * @param player
   */
  def handleClient(player: EntityClientPlayerMP) {}

  def handleServer(player: EntityPlayerMP) {}

  def writeInt(i: Int) {
    dataout.writeInt(i)
  }


  def writeIntArray(data: Array[Int]) {
    try {
      dataout.writeInt(data.length)
      for (k <- 0 until data.length) {
        dataout.writeInt(data(k))
      }
    } catch {
      case e: IOException => {
        e.printStackTrace()
      }
    }
  }

  def writeBoolean(b: Boolean) {
    try {
      dataout.writeBoolean(b)
    } catch {
      case e: IOException => e.printStackTrace()
    }
  }

  def writeDouble(i: Double) {
    try {
      dataout.writeDouble(i)
    } catch {
      case e: IOException => e.printStackTrace()
    }
  }

  /**
   * Writes the IC2ItemTest's ID (short), then size (byte), then damage. (short)
   */
  def writeItemStack(stack: ItemStack) {
    try {
      if (stack == null) {
        dataout.writeShort(-1)
      }
      else {
        dataout.writeShort(stack.itemID)
        dataout.writeByte(stack.stackSize)
        dataout.writeShort(stack.getItemDamage)
        var nbt: NBTTagCompound = null
        if (stack.getItem.isDamageable || stack.getItem.getShareTag) {
          nbt = stack.stackTagCompound
        }
        writeNBTTagCompound(nbt)
      }
    } catch {
      case e: IOException => e.printStackTrace()
    }
  }

  /**
   * Writes a compressed NBTTagCompound to the OutputStream
   */
  protected def writeNBTTagCompound(nbt: NBTTagCompound) {
    if (nbt == null) {
      dataout.writeShort(-1)
    } else {
      val compressednbt: Array[Byte] = CompressedStreamTools.compress(nbt)
      dataout.writeShort(compressednbt.length.toShort)
      dataout.write(compressednbt)
    }
  }

  /**
   * Writes a String to the DataOutputStream
   */
  def writeString(string: String) {
    try {
      dataout.writeShort(string.length)
      dataout.writeChars(string)
    } catch {
      case e: IOException => e.printStackTrace()
    }
  }
}

trait MusePackager {
  val READ_ERROR: Short = -150

  import RichInputStream._

  def read(datain: DataInputStream, player: Player): MusePacket

  def readByte(datain: DataInputStream): Byte = datain.readByte

  def readShort(datain: DataInputStream): Short = datain.readShort

  def readInt(datain: DataInputStream): Int = datain.readInt

  def readLong(datain: DataInputStream): Long = datain.readLong

  def readBoolean(datain: DataInputStream): Boolean = datain.readBoolean

  def readFloat(datain: DataInputStream): Float = datain.readFloat

  def readDouble(datain: DataInputStream): Double = datain.readDouble

  def readIntArray(datain: DataInputStream): Array[Int] = datain.readIntArray

  def readString(datain: DataInputStream): String = datain.readString

  def readItemStack(datain: DataInputStream): ItemStack = datain.readItemStack

  def readNBTTagCompound(datain: DataInputStream): NBTTagCompound = datain.readNBTTagCompound

  def safeRead[T](codec: () => T): Option[T] = {
    try {
      Some(codec.apply())
    } catch {
      case e: IOException => MuseLogger.logException("PROBLEM READING DATA FROM PACKET D:", e)
    }
  }
}
