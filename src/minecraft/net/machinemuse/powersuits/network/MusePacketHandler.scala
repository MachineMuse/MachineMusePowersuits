/**
 *
 */
package net.machinemuse.powersuits.network

import cpw.mods.fml.common.network.IPacketHandler
import cpw.mods.fml.common.network.NetworkRegistry
import cpw.mods.fml.common.network.Player
import net.machinemuse.general.MuseLogger
import net.machinemuse.powersuits.common.Config
import net.machinemuse.powersuits.network.packets._
import net.minecraft.client.entity.EntityClientPlayerMP
import net.minecraft.entity.player.EntityPlayerMP
import net.minecraft.network.INetworkManager
import net.minecraft.network.packet.Packet250CustomPayload
import java.io._
import net.machinemuse.utils.MuseNumericRegistry
import net.minecraft.item.ItemStack
import net.minecraft.nbt.{CompressedStreamTools, NBTTagCompound}
import scala.Some
import cpw.mods.fml.common.FMLCommonHandler
import cpw.mods.fml.relauncher.Side

/**
 * @author MachineMuse
 */
trait MusePackager {
  val READ_ERROR: Int = -150

  import RichInputStream._

  def read(datain: DataInputStream, player: Player): MusePacket

  def readInt(datain: DataInputStream): Int = safeRead(datain readInt) getOrElse READ_ERROR

  def readBoolean(datain: DataInputStream): Boolean = safeRead(datain readBoolean) getOrElse false

  def readDouble(datain: DataInputStream): Double = safeRead(datain readDouble) getOrElse READ_ERROR.toDouble

  def readIntArray(datain: DataInputStream): Array[Int] = safeRead(datain readIntArray) getOrElse Array.empty

  def readString(datain: DataInputStream): String = safeRead(datain readString) getOrElse ""

  def readItemStack(datain: DataInputStream): ItemStack = safeRead(datain readItemStack) getOrElse null

  def readNBTTagCompound(datain: DataInputStream): NBTTagCompound = safeRead(datain readNBTTagCompound) getOrElse null

  def safeRead[T](codec: () => T): Option[T] = {
    try {
      Some(codec.apply())
    } catch {
      case e: IOException => MuseLogger.logException("PROBLEM READING DATA FROM PACKET D:", e); return None
    }
  }
}

object RichInputStream {
  implicit def toRichStream(in: DataInputStream): RichInputStream = new RichInputStream(in)

  class RichInputStream(in: DataInputStream) {
    def readIntArray = () => {
      (for (k <- 0 until in.readInt) yield in.readInt).toArray
    }

    /**
     * Reads an ItemStack from the InputStream
     */
    def readItemStack = () => {
      val itemID: Short = in.readShort
      val stackSize: Byte = in.readByte
      val damageAmount: Short = in.readShort
      val stack = new ItemStack(itemID, stackSize, damageAmount)
      stack.stackTagCompound = readNBTTagCompound.apply()
      stack
    }

    /**
     * Reads a compressed NBTTagCompound from the InputStream
     */
    def readNBTTagCompound = () => {
      val fullData = new Array[Byte](in.readShort)
      in.readFully(fullData)
      CompressedStreamTools.decompress(fullData)
    }

    /**
     * Reads a string from a packet
     */

    def readString = () => {
      val builder: StringBuilder = StringBuilder.newBuilder
      for (i <- 0 until in.readShort) builder.append(in.readChar)
      builder.toString
    }
  }

}

object MusePacketHandler {
  val packagers = new MuseNumericRegistry[MusePackager]
  packagers.put(1, MusePacketInventoryRefresh)
  packagers.put(1, MusePacketInventoryRefresh)
  packagers.put(2, MusePacketInstallModuleRequest)
  packagers.put(3, MusePacketSalvageModuleRequest)
  packagers.put(4, MusePacketTweakRequest)
  packagers.put(5, MusePacketCosmeticInfo)
  packagers.put(6, MusePacketPlayerUpdate)
  packagers.put(7, MusePacketToggleRequest)
  packagers.put(8, MusePacketPlasmaBolt)
  packagers.put(9, MusePacketModeChangeRequest)
  packagers.put(10, MusePacketColourInfo)
}

class MusePacketHandler extends IPacketHandler {
  NetworkRegistry.instance.registerChannel(this, Config.getNetworkChannelName)


  override def onPacketData(manager: INetworkManager, payload: Packet250CustomPayload, player: Player) {
    if (payload.channel == Config.getNetworkChannelName) {
      val repackaged = repackage(payload, player)
      repackaged map {
        packet =>
          FMLCommonHandler.instance.getEffectiveSide match {
            case Side.SERVER => packet handleServer player.asInstanceOf[EntityPlayerMP]
            case Side.CLIENT => packet handleClient player.asInstanceOf[EntityClientPlayerMP]
          }
      }
    }
  }

  def repackage(payload: Packet250CustomPayload, player: Player): Option[MusePacket] = {
    val data: DataInputStream = new DataInputStream(new ByteArrayInputStream(payload.data))
    var packetType: Int = 0
    try {
      packetType = data.readInt
      MusePacketHandler.packagers.get(packetType).map(packager => packager.read(data, player))
    } catch {
      case e: IOException => {
        MuseLogger.logException("PROBLEM READING PACKET TYPE D:", e)
      }
    }
  }
}


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
    try {
      dataout.writeInt(i)
    } catch {
      case e: IOException => {
        e.printStackTrace
      }
    }
  }


  def writeIntArray(data: Array[Int]) {
    try {
      dataout.writeInt(data.length)
      for (k <- 0 until data.length) {
        dataout.writeInt(data(k))
      }
    } catch {
      case e: IOException => {
        e.printStackTrace
      }
    }
  }

  def writeBoolean(b: Boolean) {
    try {
      dataout.writeBoolean(b)
    } catch {
      case e: IOException => {
        e.printStackTrace
      }
    }
  }

  def writeDouble(i: Double) {
    try {
      dataout.writeDouble(i)
    } catch {
      case e: IOException => {
        e.printStackTrace
      }
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
    }
    catch {
      case e: IOException => {
        MuseLogger.logError("Problem writing itemstack D:")
        e.printStackTrace
      }
    }
  }

  /**
   * Writes a compressed NBTTagCompound to the OutputStream
   */
  protected def writeNBTTagCompound(nbt: NBTTagCompound) {
    if (nbt == null) {
      dataout.writeShort(-1)
    }
    else {
      val compressednbt: Array[Byte] = CompressedStreamTools.compress(nbt)
      dataout.writeShort(compressednbt.length.asInstanceOf[Short])
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
    }
    catch {
      case e: IOException => {
        MuseLogger.logError("String too big D:")
        e.printStackTrace
      }
    }
  }

}