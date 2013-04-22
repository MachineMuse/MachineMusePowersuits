package net.machinemuse.api.electricity

import net.minecraft.item.ItemStack
import universalelectricity.core.item.IItemElectric
import ic2.api.ICustomElectricItem

/**
 * Author: MachineMuse (Claire Semple)
 * Created: 10:00 PM, 4/20/13
 */
trait IC2ElectricItem extends ICustomElectricItem with MuseElectricItem {
  def canProvideEnergy(itemStack: ItemStack): Boolean = true

  def getChargedItemId(itemStack: ItemStack): Int = itemStack.itemID

  def getEmptyItemId(itemStack: ItemStack): Int = itemStack.itemID

  def getMaxCharge(itemStack: ItemStack): Int = IC2ElectricAdapter.museEnergyToEU(getCurrentEnergy(itemStack)).asInstanceOf[Int]

  def getTier(itemStack: ItemStack): Int = IC2ElectricAdapter.getTier(itemStack)

  def getTransferLimit(itemStack: ItemStack): Int = IC2ElectricAdapter.museEnergyToEU(Math.sqrt(getMaxEnergy(itemStack))).asInstanceOf[Int]

  def charge(itemStack: ItemStack, amount: Int, tier: Int, ignoreTransferLimit: Boolean, simulate: Boolean): Int = {
    val current: Double = getCurrentEnergy(itemStack)
    val given: Double = giveEnergyTo(itemStack, IC2ElectricAdapter.museEnergyFromEU(amount))
    if (simulate) {
      setCurrentEnergy(itemStack, current)
    }
    IC2ElectricAdapter.museEnergyToEU(given).asInstanceOf[Int]
  }

  def discharge(itemStack: ItemStack, amount: Int, tier: Int, ignoreTransferLimit: Boolean, simulate: Boolean): Int = {
    val current: Double = getCurrentEnergy(itemStack)
    val taken: Double = drainEnergyFrom(itemStack, IC2ElectricAdapter.museEnergyFromEU(amount))
    if (simulate) {
      setCurrentEnergy(itemStack, current)
    }
    IC2ElectricAdapter.museEnergyToEU(taken).asInstanceOf[Int]
  }

  def canUse(itemStack: ItemStack, amount: Int): Boolean = {
    val requested: Double = IC2ElectricAdapter.museEnergyFromEU(amount)
    requested < getCurrentEnergy(itemStack)
  }

  def canShowChargeToolTip(itemStack: ItemStack): Boolean = false
}
