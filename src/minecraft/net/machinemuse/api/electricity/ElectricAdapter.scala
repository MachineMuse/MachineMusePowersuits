package net.machinemuse.api.electricity

import net.minecraft.item.ItemStack
import thermalexpansion.api.item.IChargeableItem
import universalelectricity.core.item.IItemElectric
import net.machinemuse.powersuits.common.ModCompatability
import ic2.api.IElectricItem

object ElectricAdapter {
  implicit def wrap(stack: ItemStack): ElectricAdapter = {
    if (stack == null) return null
    stack.getItem match {
      case i: IChargeableItem => new TEElectricAdapter(stack)
      case i: IItemElectric => new UEElectricAdapter(stack)
      case i: IElectricItem => if (ModCompatability.isIndustrialCraftLoaded) new IC2ElectricAdapter(stack) else null
      case _ => null
    }
  }
}

abstract class ElectricAdapter {
  /**
   * Call to get the energy of an item
   *
   * @param stack
	 * ItemStack to set
   * @return Current energy level
   */
  def getCurrentEnergy: Double

  /**
   * Call to set the energy of an item
   *
   * @param stack
	 * ItemStack to set
   * @return Maximum energy level
   */
  def getMaxEnergy: Double

  /**
   * Call to drain energy from an item
   *
   * @param stack
	 * ItemStack being requested for energy
   * @param requested
	 * Amount of energy to drain
   * @return Amount of energy successfully drained
   */
  def drainEnergy(requested: Double): Double

  /**
   * Call to give energy to an item
   *
   * @param stack
	 * ItemStack being requested for energy
   * @param provided
	 * Amount of energy to drain
   * @return Amount of energy used
   */
  def giveEnergy(provided: Double): Double
}
