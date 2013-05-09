/**
 *
 */
package net.machinemuse.powersuits.network.packets

import java.io.DataInputStream
import net.machinemuse.general.gui.MuseGui
import net.machinemuse.powersuits.network.{MusePackager, MusePacket}
import net.minecraft.client.Minecraft
import net.minecraft.client.entity.EntityClientPlayerMP
import net.minecraft.client.gui.GuiScreen
import net.minecraft.inventory.IInventory
import net.minecraft.item.ItemStack
import cpw.mods.fml.common.network.Player


/**
 * Author: MachineMuse (Claire Semple)
 * Created: 12:28 PM, 5/6/13
 */
object MusePacketInventoryRefresh extends MusePackager {
  def read(d: DataInputStream, p: Player) = {
    val itemSlot = readInt(d)
    val stack = readItemStack(d)
    new MusePacketInventoryRefresh(p, itemSlot, stack)
  }
}

class MusePacketInventoryRefresh(player: Player, slot: Int, stack: ItemStack) extends MusePacket(player) {
  val packager = MusePacketInventoryRefresh

  def write {
    writeInt(slot)
    writeItemStack(stack)
  }

  override def handleClient(player: EntityClientPlayerMP) {
    val inventory: IInventory = player.inventory
    inventory.setInventorySlotContents(slot, stack)
    val playerscreen: GuiScreen = Minecraft.getMinecraft.currentScreen
    if (playerscreen.isInstanceOf[MuseGui]) {
      (playerscreen.asInstanceOf[MuseGui]).refresh()
    }
  }
}