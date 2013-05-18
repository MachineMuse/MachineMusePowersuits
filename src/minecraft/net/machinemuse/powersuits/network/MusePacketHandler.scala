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
import cpw.mods.fml.common.FMLCommonHandler
import cpw.mods.fml.relauncher.Side

/**
 * @author MachineMuse
 */
object MusePacketHandler {
  val packagers = new MuseNumericRegistry[MusePackager]
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
  packagers.put(11, MusePacketPropertyModifierConfig)
}

class MusePacketHandler extends IPacketHandler {
  NetworkRegistry.instance.registerChannel(this, Config.getNetworkChannelName)

  override def onPacketData(manager: INetworkManager, payload: Packet250CustomPayload, player: Player) {
    if (payload.channel == Config.getNetworkChannelName) {
      val repackaged = repackage(payload, player)
      repackaged map {
        packet => FMLCommonHandler.instance.getEffectiveSide match {
          case Side.SERVER => packet handleServer player.asInstanceOf[EntityPlayerMP]
          case Side.CLIENT => packet handleClient player.asInstanceOf[EntityClientPlayerMP]
          case Side.BUKKIT => MuseLogger.logError("Wat O.o")
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
        MuseLogger.logException("PROBLEM READING PACKET D:", e)
      }
    }
  }
}

