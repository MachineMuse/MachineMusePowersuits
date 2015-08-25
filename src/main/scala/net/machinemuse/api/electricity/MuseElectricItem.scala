package net.machinemuse.api.electricity

import cofh.api.energy.IEnergyContainerItem
import ic2.api.item.ElectricItem
import ic2.api.item.IElectricItemManager
import ic2.api.item.ISpecialElectricItem

import cpw.mods.fml.common.Optional
import net.machinemuse.api.ModuleManager
import net.machinemuse.api.electricity.ElectricConversions._
import net.machinemuse.utils.{ElectricItemUtils, MuseItemUtils}
import net.minecraft.entity.EntityLivingBase
import net.minecraft.item.{Item, ItemStack}

/**
 * Author: MachineMuse (Claire Semple)
 * Created: 10:12 PM, 4/20/13
 */
@Optional.Interface(iface = "cofh.api.energy.IEnergyContainerItem", modid = "CoFHCore", striprefs = true)
trait MuseElectricItem extends Item
with IEnergyContainerItem {
  /**
   * Call to get the energy of an item
   *
   * @param stack ItemStack to set
   * @return Current energy level
   */
  def getCurrentEnergy(stack: ItemStack): Double = MuseItemUtils.getDoubleOrZero(stack, ElectricItemUtils.CURRENT_ENERGY)


  /**
   * Call to set the energy of an item
   *
   * @param stack ItemStack to set
   * @return Maximum energy level
   */
  def getMaxEnergy(stack: ItemStack): Double = ModuleManager.computeModularProperty(stack, ElectricItemUtils.MAXIMUM_ENERGY)

  /**
   * Call to set the energy of an item
   *
   * @param stack ItemStack to set
   * @param energy Level to set it to
   */
  def setCurrentEnergy(stack: ItemStack, energy: Double) {
    MuseItemUtils.setDoubleOrRemove(stack, ElectricItemUtils.CURRENT_ENERGY, Math.min(energy, getMaxEnergy(stack)))
  }

  /**
   * Call to drain energy from an item
   *
   * @param stack ItemStack being requested for energy
   * @param requested Amount of energy to drain
   * @return Amount of energy successfully drained
   */
  def drainEnergyFrom(stack: ItemStack, requested: Double): Double = {
    val available = getCurrentEnergy(stack)
    if (available > requested) {
      setCurrentEnergy(stack, available - requested)
      requested
    } else {
      setCurrentEnergy(stack, 0)
      available
    }
  }

  /**
   * Call to give energy to an item
   *
   * @param stack ItemStack being provided with energy
   * @param provided Amount of energy to add
   * @return Amount of energy added
   */
  def giveEnergyTo(stack: ItemStack, provided: Double): Double = {
    val available = getCurrentEnergy(stack)
    val max = getMaxEnergy(stack)
    if (available + provided < max) {
      setCurrentEnergy(stack, available + provided)
      provided
    } else {
      setCurrentEnergy(stack, max)
      max - available
    }
  }

  // TE
  def receiveEnergy(stack: ItemStack, energy: Int, simulate: Boolean): Int = {
    val current: Double = getCurrentEnergy(stack)
    val receivedME: Double = museEnergyFromRF(energy)
    val eatenME: Double = giveEnergyTo(stack, receivedME)
    if (simulate) {
      setCurrentEnergy(stack, current)
    }
    museEnergyToRF(eatenME)
  }

  def extractEnergy(stack: ItemStack, energy: Int, simulate: Boolean): Int = {
    val current: Double = getCurrentEnergy(stack)
    val requesteddME: Double = museEnergyFromRF(energy)
    val takenME: Double = drainEnergyFrom(stack, requesteddME)
    if (simulate) {
      setCurrentEnergy(stack, current)
    }
    museEnergyToRF(takenME)
  }

  def getEnergyStored(theItem: ItemStack) = museEnergyToRF(getCurrentEnergy(theItem)).toInt

  def getMaxEnergyStored(theItem: ItemStack) = museEnergyToRF(getMaxEnergy(theItem)).toInt

  override def getMaxDamage(itemStack: ItemStack) = 0
}
