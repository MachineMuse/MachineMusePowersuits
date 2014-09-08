package net.machinemuse.utils

import net.machinemuse.api.electricity.ElectricAdapter
import net.machinemuse.powersuits.item.ItemComponent
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import scala.collection.JavaConverters._
import scala.collection.mutable.ListBuffer

object ElectricItemUtils {
  final val MAXIMUM_ENERGY: String = "Maximum Energy"
  final val CURRENT_ENERGY: String = "Current Energy"

  def electricItemsEquipped(player: EntityPlayer): Seq[ElectricAdapter] = {
    val electrics = new ListBuffer[ElectricAdapter]()
    for (i <- 0 until player.inventory.getSizeInventory) {
      val adapter: ElectricAdapter = ElectricAdapter.wrap(player.inventory.getStackInSlot(i))
      if (adapter != null) {
        electrics.prepend(adapter)
      }
    }
    electrics
  }

  def getPlayerEnergy(player: EntityPlayer): Double = {
    (0.0 /: electricItemsEquipped(player)) {
      case (avail, adapter) => avail + adapter.getCurrentEnergy
    }
  }

  def getMaxEnergy(player: EntityPlayer): Double = {
    (0.0 /: electricItemsEquipped(player)) {
      case (avail, adapter) => avail + adapter.getMaxEnergy
    }
  }

  def drainPlayerEnergy(player: EntityPlayer, drainAmount: Double) {
    (drainAmount /: electricItemsEquipped(player)) {
      case (drainleft, adapter) => drainleft - adapter.drainEnergy(drainleft)
    }
  }

  def givePlayerEnergy(player: EntityPlayer, joulesToGive: Double) {
    (joulesToGive /: electricItemsEquipped(player)) {
      case (joulesleft, adapter) => joulesleft - adapter.giveEnergy(joulesleft)
    }
  }

  def jouleValueOfComponent(stackInCost: ItemStack): Double = {
    stackInCost.getItem match {
      case i: ItemComponent => (stackInCost.getItemDamage - ItemComponent.lvcapacitor.getItemDamage) match {
        case 0 => 20000 * stackInCost.stackSize
        case 1 => 100000 * stackInCost.stackSize
        case 2 => 750000 * stackInCost.stackSize
        case _ => 0
      }
      case _ => 0
    }
  }
}