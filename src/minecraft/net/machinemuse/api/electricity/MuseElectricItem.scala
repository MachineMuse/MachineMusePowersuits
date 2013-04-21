package net.machinemuse.api.electricity

import net.minecraft.item.ItemStack
import net.machinemuse.api.{ModuleManager, MuseItemUtils}

/**
 * Author: MachineMuse (Claire Semple)
 * Created: 10:12 PM, 4/20/13
 */
trait MuseElectricItem {
  /**
   * Call to get the energy of an item
   *
   * @param stack
	 * ItemStack to set
   * @return Current energy level
   */
  def getCurrentEnergy(stack: ItemStack): Double = {
    MuseItemUtils.getDoubleOrZero(stack, ElectricItemUtils.CURRENT_ENERGY)
  }

  /**
   * Call to set the energy of an item
   *
   * @param stack
	 * ItemStack to set
   * @return Maximum energy level
   */
  def getMaxEnergy(stack: ItemStack): Double = {
    ModuleManager.computeModularProperty(stack, ElectricItemUtils.MAXIMUM_ENERGY)
  }

  /**
   * Call to set the energy of an item
   *
   * @param stack
	 * ItemStack to set
   * @param energy
	 * Level to set it to
   */
  def setCurrentEnergy(stack: ItemStack, energy: Double) {
    MuseItemUtils.setDoubleOrRemove(stack, ElectricItemUtils.CURRENT_ENERGY, energy)
  }

  /**
   * Call to drain energy from an item
   *
   * @param stack
	 * ItemStack being requested for energy
   * @param requested
	 * Amount of energy to drain
   * @return Amount of energy successfully drained
   */
  def drainEnergyFrom(stack: ItemStack, requested: Double): Double = {
    val available: Double = getCurrentEnergy(stack)
    if (available > requested) {
      setCurrentEnergy(stack, available - requested)
      requested
    }
    else {
      setCurrentEnergy(stack, 0)
      available
    }
  }

  /**
   * Call to give energy to an item
   *
   * @param stack
	 * ItemStack being provided with energy
   * @param provided
	 * Amount of energy to add
   * @return Amount of energy added
   */
  def giveEnergyTo(stack: ItemStack, provided: Double): Double = {
    val available: Double = getCurrentEnergy(stack)
    val max: Double = getMaxEnergy(stack)
    if (available + provided < max) {
      setCurrentEnergy(stack, available + provided)
      provided
    }
    else {
      setCurrentEnergy(stack, max)
      max - available
    }
  }
}
