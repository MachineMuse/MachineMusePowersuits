/**
 *
 */
package net.machinemuse.powersuits.network.packets

import cpw.mods.fml.common.FMLCommonHandler
import cpw.mods.fml.common.network.PacketDispatcher
import cpw.mods.fml.common.network.Player
import cpw.mods.fml.relauncher.Side
import net.machinemuse.api.{IModularItem, IPowerModule, ModuleManager}
import net.machinemuse.powersuits.network.{MusePackager, MusePacket}
import net.machinemuse.utils.ElectricItemUtils
import net.machinemuse.utils.MuseItemUtils
import net.minecraft.client.entity.EntityClientPlayerMP
import net.minecraft.entity.player.EntityPlayerMP
import net.minecraft.entity.player.InventoryPlayer
import net.minecraft.item.ItemStack
import java.io.DataInputStream
import java.io.IOException
import java.util.ArrayList
import java.util.List
import net.minecraft.nbt.NBTTagCompound
import net.machinemuse.general.MuseLogger

/**
 * Packet for requesting to purchase an upgrade. Player-to-server. Server decides whether it is a valid upgrade or not and replies with an associated
 * inventoryrefresh packet.
 *
 * Author: MachineMuse (Claire Semple)
 * Created: 10:16 AM, 01/05/13
 */

object MusePacketInstallModuleRequest extends MusePackager {
  def read(d: DataInputStream, p: Player) = {
    val itemSlot = readInt(d)
    val moduleName = readString(d)
    new MusePacketInstallModuleRequest(p, itemSlot, moduleName)
  }
}

class MusePacketInstallModuleRequest(player: Player, itemSlot: Int, moduleName: String) extends MusePacket(player) {
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
        playerEntity.sendChatToPlayer("Server has disallowed this module. Sorry!")
        return
      }
      val cost: List[ItemStack] = moduleType.getInstallCost
      if ((!MuseItemUtils.itemHasModule(stack, moduleName) && MuseItemUtils.hasInInventory(cost, playerEntity.inventory)) || playerEntity.capabilities.isCreativeMode) {
        MuseItemUtils.itemAddModule(stack, moduleType)
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
          val reply: MusePacket = new MusePacketInventoryRefresh(player, slotiter, inventory.getStackInSlot(slotiter))
          PacketDispatcher.sendPacketToPlayer(reply.getPacket250, player)
        }
      }
    }
  }
}
