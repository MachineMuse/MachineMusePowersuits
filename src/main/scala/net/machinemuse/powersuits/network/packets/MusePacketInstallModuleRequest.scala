/**
 *
 */
package net.machinemuse.powersuits.network.packets

import java.io.DataInputStream
import java.util.{ArrayList, List}

import net.machinemuse.api.{IPowerModule, ModuleManager}
import net.machinemuse.numina.network.{IMusePackager, MusePackager, MusePacket, PacketSender}
import net.machinemuse.utils.{ElectricItemUtils, MuseItemUtils}
import net.minecraft.entity.player.{EntityPlayer, EntityPlayerMP, InventoryPlayer}
import net.minecraft.item.ItemStack
import net.minecraft.util.ChatComponentText

/**
 * Packet for requesting to purchase an upgrade. Player-to-server. Server decides whether it is a valid upgrade or not and replies with an associated
 * inventoryrefresh packet.
 *
 * Author: MachineMuse (Claire Semple)
 * Created: 10:16 AM, 01/05/13
 */

object MusePacketInstallModuleRequest extends MusePackager {
  override def read(d: DataInputStream, p: EntityPlayer) = {
    val itemSlot = readInt(d)
    val moduleName = readString(d)
    new MusePacketInstallModuleRequest(p, itemSlot, moduleName)
  }
}

class MusePacketInstallModuleRequest(player: EntityPlayer, itemSlot: Int, moduleName: String) extends MusePacket {
  val packager = MusePacketInstallModuleRequest

  def write {
    writeInt(itemSlot)
    writeString(moduleName)
  }

  override def handleServer(playerEntity: EntityPlayerMP) {
    val stack = playerEntity.inventory.getStackInSlot(itemSlot)
    if (moduleName != null) {
      val inventory: InventoryPlayer = playerEntity.inventory
      val moduleType: IPowerModule = ModuleManager.getModule(moduleName)
      if (moduleType == null || !moduleType.isAllowed) {
        playerEntity.addChatComponentMessage(new ChatComponentText("Server has disallowed this module. Sorry!"))
        return
      }
      val cost: List[ItemStack] = moduleType.getInstallCost
      if ((!ModuleManager.itemHasModule(stack, moduleName) && MuseItemUtils.hasInInventory(cost, playerEntity.inventory)) || playerEntity.capabilities.isCreativeMode) {
        ModuleManager.itemAddModule(stack, moduleType)
        import scala.collection.JavaConversions._
        for (stackInCost <- cost) {
          ElectricItemUtils.givePlayerEnergy(playerEntity, ElectricItemUtils.jouleValueOfComponent(stackInCost))
        }
        var slotsToUpdate: List[Integer] = new ArrayList[Integer]
        if (!playerEntity.capabilities.isCreativeMode) {
          slotsToUpdate = MuseItemUtils.deleteFromInventory(cost, inventory)
        }
        slotsToUpdate.add(itemSlot)
        import scala.collection.JavaConversions._
        for (slotiter <- slotsToUpdate) {
          val reply: MusePacket = new MusePacketInventoryRefresh(playerEntity, slotiter, inventory.getStackInSlot(slotiter))
          PacketSender.sendTo(reply, playerEntity)
        }
      }
    }
  }
}
