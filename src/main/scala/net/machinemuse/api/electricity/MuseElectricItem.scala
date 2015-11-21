package net.machinemuse.api.electricity

import cofh.api.energy.IEnergyContainerItem
import cpw.mods.fml.common.Optional
import ic2.api.item.{IElectricItemManager, ISpecialElectricItem, IElectricItem, ElectricItem}
import net.machinemuse.api.ModuleManager
import net.machinemuse.api.electricity.ElectricConversions._
import net.machinemuse.utils.{ElectricItemUtils, MuseItemUtils}
import net.minecraft.entity.EntityLivingBase
import net.minecraft.item.{Item, ItemStack}

/**
 * Author: MachineMuse (Claire Semple)
 * Created: 10:12 PM, 4/20/13
 */

@Optional.InterfaceList(Array(
  new Optional.Interface(iface = "cofh.api.energy.IEnergyContainerItem", modid = "CoFHCore", striprefs = true),
  new Optional.Interface(iface = "ic2.api.item.IElectricItemManager", modid = "IC2", striprefs = true),
  new Optional.Interface(iface = "ic2.api.item.ISpecialElectricItem", modid = "IC2", striprefs = true)))
trait MuseElectricItem extends Item 
with IEnergyContainerItem 
with ISpecialElectricItem 
with IElectricItemManager 
with IElectricItem {
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

  // IC2
  def getManager(itemStack: ItemStack) = this

  def chargeFromArmor(itemStack: ItemStack, entity: EntityLivingBase) = ElectricItem.rawManager.chargeFromArmor(itemStack, entity)

  def use(itemStack: ItemStack, amount: Double, entity: EntityLivingBase) = ElectricItem.rawManager.use(itemStack, museEnergyToEU(amount), entity)

  def canProvideEnergy(itemStack: ItemStack): Boolean = true

  def getCharge(itemStack: ItemStack): Double = museEnergyToEU(getCurrentEnergy(itemStack))

  def getMaxCharge(itemStack: ItemStack): Double = museEnergyToEU(getMaxEnergy(itemStack))

  def getTier(itemStack: ItemStack): Int = ElectricConversions.getTier(itemStack)

  def getTransferLimit(itemStack: ItemStack): Double = museEnergyToEU(Math.sqrt(getMaxEnergy(itemStack)))

  def charge(itemStack: ItemStack, amount: Double, tier: Int, ignoreTransferLimit: Boolean, simulate: Boolean): Double = {
    val current: Double = getCurrentEnergy(itemStack)
    val transfer = {
      if (ignoreTransferLimit || amount < getTransferLimit(itemStack))
        museEnergyFromEU(amount)
      else
        getTransferLimit(itemStack)
    }
    val given: Double = giveEnergyTo(itemStack, transfer)
    if (simulate) {
      setCurrentEnergy(itemStack, current)
    }
    museEnergyToEU(given)
  }

  def discharge(itemStack: ItemStack, amount: Double, tier: Int, ignoreTransferLimit: Boolean, externally: Boolean, simulate: Boolean): Double = {
    val current: Double = getCurrentEnergy(itemStack)
    val transfer = {
      if (ignoreTransferLimit || amount < getTransferLimit(itemStack))
        museEnergyFromEU(amount)
      else
        getTransferLimit(itemStack)
    }
    val taken: Double = drainEnergyFrom(itemStack, transfer)
    if (simulate) {
      setCurrentEnergy(itemStack, current)
    }
    museEnergyToEU(taken)
  }

  def canUse(itemStack: ItemStack, amount: Double): Boolean = museEnergyFromEU(amount) < getCurrentEnergy(itemStack)

  def getToolTip(itemStack: ItemStack): String = this.getToolTip(itemStack)

  def getChargedItem(itemStack: ItemStack) : Item = this
  def getEmptyItem(itemStack: ItemStack) : Item = this

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