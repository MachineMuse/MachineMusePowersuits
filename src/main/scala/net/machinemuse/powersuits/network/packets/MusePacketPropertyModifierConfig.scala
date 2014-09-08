package net.machinemuse.powersuits.network.packets

import java.io.DataInputStream

import cpw.mods.fml.relauncher.{Side, SideOnly}
import net.machinemuse.api.ModuleManager
import net.machinemuse.numina.network.{MusePackager, MusePacket}
import net.machinemuse.powersuits.powermodule.{PowerModuleBase, PropertyModifierFlatAdditive, PropertyModifierLinearAdditive}
import net.minecraft.client.entity.EntityClientPlayerMP
import net.minecraft.entity.player.EntityPlayer

/**
 * Author: MachineMuse (Claire Semple)
 * Created: 7:23 PM, 12/05/13
 */
object MusePacketPropertyModifierConfig extends MusePackager {
  def read(d: DataInputStream, p: EntityPlayer) = {
    new MusePacketPropertyModifierConfig(p, d)
  }
}

class MusePacketPropertyModifierConfig(player: EntityPlayer, data: DataInputStream) extends MusePacket {
  val packager = MusePacketPropertyModifierConfig

  override def write {
    import scala.collection.JavaConverters._
    writeInt(ModuleManager.getAllModules.size())
    for (module <- ModuleManager.getAllModules.asScala) {
      writeString(module.getDataName)
      writeBoolean(module.isAllowed)
      writeInt(module.getPropertyModifiers.size())
      for ((propname, propmodlist) <- module.getPropertyModifiers.asScala) {
        writeString(propname)
        writeInt(propmodlist.size)
        for (propmod <- propmodlist.asScala) {
          propmod match {
            case x: PropertyModifierFlatAdditive => writeDouble(x.valueAdded)
            case x: PropertyModifierLinearAdditive => writeDouble(x.multiplier)
            case _ => writeDouble(0)
          }
        }
      }
    }
  }

  /**
   * Called by the network manager since it does all the packet mapping
   *
   * @param player
   */
  @SideOnly(Side.CLIENT)
  override def handleClient(player: EntityClientPlayerMP) {
    val d = MusePacketPropertyModifierConfig
    val numModules = d readInt data
    for (_ <- 0 until numModules) {
      val moduleName = d readString data
      val allowed = d readBoolean data
      val module = ModuleManager.getModule(moduleName)
      if (module.isInstanceOf[PowerModuleBase]) module.asInstanceOf[PowerModuleBase] setIsAllowed allowed
      val numProps = d readInt data
      for (_ <- 0 until numProps) {
        val propName = d readString data
        val numModifiers = d readInt data
        val proplist = module.getPropertyModifiers.get(propName)
        for (m <- 0 until numModifiers) {
          proplist.get(m) match {
            case x: PropertyModifierFlatAdditive => x.valueAdded = d readDouble data
            case x: PropertyModifierLinearAdditive => x.multiplier = d readDouble data
            case _ => d readDouble data
          }
        }
      }
    }

  }

}
