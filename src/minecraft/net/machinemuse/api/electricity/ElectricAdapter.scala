package net.machinemuse.api.electricity

import net.minecraft.item.ItemStack
import thermalexpansion.api.item.IChargeableItem
import universalelectricity.core.item.IItemElectric
import net.machinemuse.powersuits.common.ModCompatability
import ic2.api.item.IElectricItem

object ElectricAdapter {
  def wrap(stack: ItemStack): ElectricAdapter = {
    if (stack == null) return null
    stack.getItem match {
      case i: IChargeableItem => return new TEElectricAdapter(stack)
      case i: IItemElectric => return new UEElectricAdapter(stack)
      case i: IElectricItem => if (ModCompatability.isIndustrialCraftLoaded) return new IC2ElectricAdapter(stack) else return null
      case _ => return null
    }
  }

}

abstract class ElectricAdapter {
  /**
   * Call to get the energy of an item
   *
   * @param stack
	 * IC2ItemTest to set
   * @return Current energy level
   */
  def getCurrentEnergy: Double

  /**
   * Call to set the energy of an item
   *
   * @param stack
	 * IC2ItemTest to set
   * @return Maximum energy level
   */
  def getMaxEnergy: Double

  /**
   * Call to drain energy from an item
   *
   * @param stack
	 * IC2ItemTest being requested for energy
   * @param requested
	 * Amount of energy to drain
   * @return Amount of energy successfully drained
   */
  def drainEnergy(requested: Double): Double

  /**
   * Call to give energy to an item
   *
   * @param stack
	 * IC2ItemTest being requested for energy
   * @param provided
	 * Amount of energy to drain
   * @return Amount of energy used
   */
  def giveEnergy(provided: Double): Double
}
