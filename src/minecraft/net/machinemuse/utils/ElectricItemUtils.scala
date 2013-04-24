package net.machinemuse.utils

import net.machinemuse.api.electricity.ElectricAdapter
import net.machinemuse.powersuits.item.ItemComponent
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack

object ElectricItemUtils {
  def electricItemsEquipped(player: EntityPlayer): Seq[ElectricAdapter] = {
    val electrics = scala.collection.mutable.LinkedList
    for (i <- 0 until player.inventory.getSizeInventory) {
      val adapter: ElectricAdapter = ElectricAdapter.wrap(player.inventory.getStackInSlot(i))
      if (adapter != null) {
        electrics.add(adapter)
      }
    }
    electrics
  }

  def getPlayerEnergy(player: EntityPlayer): Double = {
    0 /: electricItemsEquipped(player) {
      case (avail, adapter) => avail + adapter.getCurrentEnergy
    }
  }

  def getMaxEnergy(player: EntityPlayer): Double = {
    0 /: electricItemsEquipped(player) {
      case (avail, adapter) => avail + adapter.getMaxEnergy
    }
  }

  def drainPlayerEnergy(player: EntityPlayer, drainAmount: Double) {
    drainAmount /: electricItemsEquipped(player) {
      case (drainleft, adapter) => drainleft - adapter.drainEnergy(drainleft)
    }
  }

  def givePlayerEnergy(player: EntityPlayer, joulesToGive: Double) {
    joulesToGive /: electricItemsEquipped(player) {
      case (joulesleft, adapter) => joulesleft - adapter.giveEnergy(joulesleft)
    }
  }

  def jouleValueOfComponent(stackInCost: ItemStack): Double = {
    stackInCost.getItem match {
      case i: ItemComponent => stackInCost.getItemDamage match {
        case ItemComponent.lvcapacitor.getItemDamage => 20000 * stackInCost.stackSize
        case ItemComponent.mvcapacitor.getItemDamage => 100000 * stackInCost.stackSize
        case ItemComponent.hvcapacitor.getItemDamage => 750000 * stackInCost.stackSize
        case _ => 0
      }
      case _ => 0
    }
  }

  final val MAXIMUM_ENERGY: String = "Maximum Energy"
  final val CURRENT_ENERGY: String = "Current Energy"
}