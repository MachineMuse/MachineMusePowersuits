/**
 *
 */
package net.machinemuse.powersuits.network.packets

import java.io.DataInputStream
import java.util.{HashSet, List}

import net.machinemuse.api.{IPowerModule, ModuleManager}
import net.machinemuse.numina.network.{IMusePackager, MusePacket, PacketSender}
import net.machinemuse.powersuits.common.Config
import net.machinemuse.utils.MuseItemUtils
import net.minecraft.entity.player.{EntityPlayer, EntityPlayerMP, InventoryPlayer}
import net.minecraft.item.ItemStack

/**
 * Packet for requesting to purchase an upgrade. Player-to-server. Server
 * decides whether it is a valid upgrade or not and replies with an associated
 * inventoryrefresh packet.
 *
 * Author: MachineMuse (Claire Semple)
 * Created: 12:28 PM, 5/6/13
 */
object MusePacketSalvageModuleRequest extends IMusePackager {
  def read(d: DataInputStream, p: EntityPlayer) = {

    val itemSlot = readInt(d)
    val moduleName = readString(d)
    new MusePacketSalvageModuleRequest(p, itemSlot, moduleName)
  }
}

class MusePacketSalvageModuleRequest(player: EntityPlayer, itemSlot: Int, moduleName: String) extends MusePacket {
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
      if (ModuleManager.itemHasModule(stack, moduleName)) {
        val slots: java.util.Set[Integer] = new HashSet[Integer]
        ModuleManager.removeModule(stack, moduleName)
        import scala.collection.JavaConversions._
        for (refundItem <- refund) {
          slots.addAll(MuseItemUtils.giveOrDropItemWithChance(refundItem.copy, playerEntity, Config.getSalvageChance))
        }
        slots.add(itemSlot)
        for (slotiter <- slots) {
          val reply: MusePacket = new MusePacketInventoryRefresh(player, slotiter, inventory.getStackInSlot(slotiter))
          PacketSender.sendTo(reply, playerEntity)
        }
      }
    }
  }
}