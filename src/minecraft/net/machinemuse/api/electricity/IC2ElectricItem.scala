package net.machinemuse.api.electricity

import net.minecraft.item.ItemStack
import universalelectricity.core.item.IItemElectric
import net.machinemuse.api.electricity.PowerConversions._
import ic2.api.ICustomElectricItem

/**
 * Author: MachineMuse (Claire Semple)
 * Created: 10:00 PM, 4/20/13
 */
trait IC2ElectricItem extends ICustomElectricItem with MuseElectricItem {
  def canProvideEnergy(itemStack: ItemStack): Boolean = true

  def getChargedItemId(itemStack: ItemStack): Int = itemStack.itemID

  def getEmptyItemId(itemStack: ItemStack): Int = itemStack.itemID

  def getMaxCharge(itemStack: ItemStack): Int = museEnergyToEU(getCurrentEnergy(itemStack)).asInstanceOf[Int]

  def getTier(itemStack: ItemStack): Int = getTier(itemStack)

  def getTransferLimit(itemStack: ItemStack): Int = museEnergyToEU(Math.sqrt(getMaxEnergy(itemStack))).asInstanceOf[Int]

  def charge(itemStack: ItemStack, amount: Int, tier: Int, ignoreTransferLimit: Boolean, simulate: Boolean): Int = {
    val current: Double = getCurrentEnergy(itemStack)
    val given: Double = giveEnergyTo(itemStack, museEnergyFromEU(amount))
    if (simulate) {
      setCurrentEnergy(itemStack, current)
    }
    museEnergyToEU(given).asInstanceOf[Int]
  }

  def discharge(itemStack: ItemStack, amount: Int, tier: Int, ignoreTransferLimit: Boolean, simulate: Boolean): Int = {
    val current: Double = getCurrentEnergy(itemStack)
    val taken: Double = drainEnergyFrom(itemStack, museEnergyFromEU(amount))
    if (simulate) {
      setCurrentEnergy(itemStack, current)
    }
    museEnergyToEU(taken).asInstanceOf[Int]
  }

  def canUse(itemStack: ItemStack, amount: Int): Boolean =       museEnergyFromEU(amount) < getCurrentEnergy(itemStack)


  def canShowChargeToolTip(itemStack: ItemStack): Boolean = false
}
