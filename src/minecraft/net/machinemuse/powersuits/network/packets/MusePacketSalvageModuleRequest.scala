/**
 *
 */
package net.machinemuse.powersuits.network.packets

import cpw.mods.fml.common.network.PacketDispatcher
import cpw.mods.fml.common.network.Player
import net.machinemuse.api.IPowerModule
import net.machinemuse.api.ModuleManager
import net.machinemuse.powersuits.common.Config
import net.machinemuse.powersuits.network.{MusePackager, MusePacket}
import net.machinemuse.utils.MuseItemUtils
import net.minecraft.entity.player.EntityPlayerMP
import net.minecraft.entity.player.InventoryPlayer
import net.minecraft.item.ItemStack
import java.io.DataInputStream
import java.util.HashSet
import java.util.List
import java.util.Set
import scala.Predef._

/**
 * Packet for requesting to purchase an upgrade. Player-to-server. Server
 * decides whether it is a valid upgrade or not and replies with an associated
 * inventoryrefresh packet.
 *
 * Author: MachineMuse (Claire Semple)
 * Created: 12:28 PM, 5/6/13
 */
object MusePacketSalvageModuleRequest extends MusePackager {
  def read(d: DataInputStream, p: Player) = {

    val itemSlot = readInt(d)
    val moduleName = readString(d)
    new MusePacketSalvageModuleRequest(p, itemSlot, moduleName)
  }
}

class MusePacketSalvageModuleRequest(player: Player, itemSlot: Int, moduleName: String) extends MusePacket(player) {
  val packager = MusePacketSalvageModuleRequest

  def write {
    writeInt(itemSlot)
    writeString(moduleName)
  }

  override def handleServer(playerEntity: EntityPlayerMP) {
    if (moduleName != null) {
      val inventory: InventoryPlayer = playerEntity.inventory
      val stack = playerEntity.inventory.getStackInSlot(itemSlot)
      val moduleType: IPowerModule = ModuleManager.getModule(moduleName)
      val refund: List[ItemStack] = moduleType.getInstallCost
      if (MuseItemUtils.itemHasModule(stack, moduleName)) {
        val slots: Set[Integer] = new HashSet[Integer]
        MuseItemUtils.removeModule(stack, moduleName)
        import scala.collection.JavaConversions._
        for (refundItem <- refund) {
          slots.addAll(MuseItemUtils.giveOrDropItemWithChance(refundItem.copy, playerEntity, Config.getSalvageChance))
        }
        slots.add(itemSlot)
        import scala.collection.JavaConversions._
        for (slotiter <- slots) {
          val reply: MusePacket = new MusePacketInventoryRefresh(player, slotiter, inventory.getStackInSlot(slotiter))
          PacketDispatcher.sendPacketToPlayer(reply.getPacket250, player)
        }
      }
    }
  }
}