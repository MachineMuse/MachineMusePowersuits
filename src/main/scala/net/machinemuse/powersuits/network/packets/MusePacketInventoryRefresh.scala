/**
 *
 */
package net.machinemuse.powersuits.network.packets

import java.io.DataInputStream

import net.machinemuse.general.gui.MuseGui
import net.machinemuse.numina.network.{MusePackager, MusePacket}
import net.machinemuse.numina.scala.OptionCast
import net.minecraft.client.Minecraft
import net.minecraft.client.entity.EntityPlayerSP
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.inventory.IInventory
import net.minecraft.item.ItemStack
import net.minecraftforge.fml.relauncher.{SideOnly, Side}


/**
 * Author: MachineMuse (Claire Semple)
 * Created: 12:28 PM, 5/6/13
 */
object MusePacketInventoryRefresh extends MusePackager {
  def read(d: DataInputStream, p: EntityPlayer) = {
    val itemSlot = readInt(d)
    val stack = readItemStack(d)
    new MusePacketInventoryRefresh(p, itemSlot, stack)
  }
}

class MusePacketInventoryRefresh(player: EntityPlayer, slot: Int, stack: ItemStack) extends MusePacket {
  val packager = MusePacketInventoryRefresh

  def write {
    writeInt(slot)
    writeItemStack(stack)
  }

  @SideOnly(Side.CLIENT)
  override def handleClient(player: EntityPlayerSP) {
    val inventory: IInventory = player.inventory
    inventory.setInventorySlotContents(slot, stack)
    OptionCast[MuseGui](Minecraft.getMinecraft.currentScreen) map (s => s.refresh())
  }
}