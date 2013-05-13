package net.machinemuse.powersuits.network.packets

import net.machinemuse.powersuits.network.{MusePackager, MusePacket}
import java.io.DataInputStream
import cpw.mods.fml.common.network.Player
import net.machinemuse.api.ModuleManager
import net.machinemuse.powersuits.powermodule.{PowerModuleBase, PropertyModifierLinearAdditive, PropertyModifierFlatAdditive}

/**
 * Author: MachineMuse (Claire Semple)
 * Created: 7:23 PM, 12/05/13
 */
object MusePacketPropertyModifierConfig extends MusePackager {
  def read(d: DataInputStream, p: Player) = {
    new MusePacketPropertyModifierConfig(p, d)
  }
}

class MusePacketPropertyModifierConfig(player: Player, data: DataInputStream) extends MusePacket(player) {
  val packager = MusePacketPropertyModifierConfig

  def write {
    import scala.collection.JavaConverters._
    writeInt(ModuleManager.getAllModules.size())
    for (module <- ModuleManager.getAllModules.asScala) {
      writeString(module.getName)
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

  def handleClient() {
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
