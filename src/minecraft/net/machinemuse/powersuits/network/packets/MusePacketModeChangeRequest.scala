/**
 *
 */
package net.machinemuse.powersuits.network.packets

import cpw.mods.fml.common.network.Player
import net.machinemuse.utils.MuseItemUtils
import net.minecraft.entity.player.EntityPlayerMP
import net.minecraft.item.ItemStack
import java.io.DataInputStream
import java.util.List
import scala.Predef._
import net.machinemuse.numina.network.{MusePackager, MusePacket}
import net.machinemuse.numina.item.ModeChangingItem
import net.machinemuse.numina.scala.OptionCast
import net.machinemuse.powersuits.item.ModeChangingModularItem

/**
 * Author: MachineMuse (Claire Semple)
 * Created: 12:28 PM, 5/6/13
 */
object MusePacketModeChangeRequest extends MusePackager {
  def read(d: DataInputStream, p: Player) = {
    val slot = readInt(d)
    val mode = readString(d)
    new MusePacketModeChangeRequest(p, mode, slot)
  }
}

class MusePacketModeChangeRequest(player: Player, mode: String, slot: Int) extends MusePacket(player) {
  val packager = MusePacketModeChangeRequest

  def write {
    writeInt(slot)
    writeString(mode)
  }

  override def handleServer(player: EntityPlayerMP) {
    var stack: ItemStack = null
    if (slot > -1 && slot < 9) {
      for {
        stack <- Option(player.inventory.mainInventory(slot))
        item <- OptionCast[ModeChangingModularItem](stack.getItem)
      } {
        val modes = item.getModes(stack)
        if (modes.contains(mode)) {
          OptionCast[ModeChangingItem](stack.getItem).map(i => i.setActiveMode(stack, mode))
        }
      }
    }
  }

}
