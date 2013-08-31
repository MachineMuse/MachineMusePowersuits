package net.machinemuse.api.electricity

import net.minecraft.item.ItemStack
import net.machinemuse.utils.{ElectricItemUtils, MuseItemUtils}
import ic2.api.item.ICustomElectricItem
import universalelectricity.core.item.IItemElectric
import thermalexpansion.api.item.IChargeableItem
import net.machinemuse.api.electricity.ElectricConversions._
import icbm.api.explosion.IEMPItem
import net.minecraft.entity.Entity
import net.machinemuse.api.ModuleManager

/**
 * Author: MachineMuse (Claire Semple)
 * Created: 10:12 PM, 4/20/13
 */
trait MuseElectricItem
  extends ICustomElectricItem // IC2
  with IItemElectric // UE
  with IChargeableItem // TE
  with IEMPItem {
  // ICBM
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
  def canProvideEnergy(itemStack: ItemStack): Boolean = true

  def getChargedItemId(itemStack: ItemStack): Int = itemStack.itemID

  def getEmptyItemId(itemStack: ItemStack): Int = itemStack.itemID

  def getMaxCharge(itemStack: ItemStack): Int = museEnergyToEU(getCurrentEnergy(itemStack)).asInstanceOf[Int]

  def getTier(itemStack: ItemStack): Int = ElectricConversions.getTier(itemStack)

  def getTransferLimit(itemStack: ItemStack): Int = museEnergyToEU(Math.sqrt(getMaxEnergy(itemStack))).asInstanceOf[Int]

  def charge(itemStack: ItemStack, amount: Int, tier: Int, ignoreTransferLimit: Boolean, simulate: Boolean): Int = {
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
    museEnergyToEU(given).toInt
  }

  def discharge(itemStack: ItemStack, amount: Int, tier: Int, ignoreTransferLimit: Boolean, simulate: Boolean): Int = {
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
    museEnergyToEU(taken).toInt
  }

  def canUse(itemStack: ItemStack, amount: Int): Boolean = museEnergyFromEU(amount) < getCurrentEnergy(itemStack)


  def canShowChargeToolTip(itemStack: ItemStack): Boolean = false

  // ICBM
  def onEMP(itemStack: ItemStack, entity: Entity, empExplosive: IExplosion) {
    setCurrentEnergy(itemStack, 0)
  }

  // UE
  def getElectricityStored(itemStack: ItemStack): Float = museEnergyToJoules(getCurrentEnergy(itemStack))

  def setElectricity(itemStack: ItemStack, joules: Float) {
    setCurrentEnergy(itemStack, museEnergyFromJoules(joules))
  }

  def getMaxElectricityStored(itemStack: ItemStack): Float = museEnergyToJoules(getMaxEnergy(itemStack))

  def getVoltage(itemStack: ItemStack): Float = 120

  def recharge(itemStack: ItemStack, energy: Float, doCharge: Boolean): Float = {
    var joulesConsumed: Float = 0
    val energyReceiving: Double = museEnergyFromJoules(energy)
    if (doCharge) {
      val energyConsumed: Double = giveEnergyTo(itemStack, energyReceiving)
      joulesConsumed = museEnergyToJoules(energyConsumed)
    } else {
      val energyChargeable: Double = getMaxEnergy(itemStack) - getCurrentEnergy(itemStack)
      joulesConsumed = museEnergyToJoules(math.min(energyReceiving, energyChargeable))
    }
    joulesConsumed
  }

  def discharge(itemStack: ItemStack, energy: Float, doCharge: Boolean): Float = {
    val energyRequested: Double = museEnergyFromJoules(energy)
    var joulesGiven: Float = 0
    if (doCharge) {
      val energyGiven: Double = drainEnergyFrom(itemStack, energyRequested)
      joulesGiven = museEnergyToJoules(energyGiven)
    } else {
      val energyAvailable: Double = getCurrentEnergy(itemStack)
      joulesGiven = museEnergyToJoules(math.min(energyAvailable, energyRequested))
    }
    joulesGiven
  }

  def getTransfer(itemStack: ItemStack): Float = {
    museEnergyToJoules(getMaxEnergy(itemStack) - getCurrentEnergy(itemStack))
  }

  // TE
  def receiveEnergy(theItem: ItemStack, energy: Float, doReceive: Boolean): Float = {
    val current: Double = getCurrentEnergy(theItem)
    val receivedME: Double = museEnergyFromMJ(energy)
    val eatenME: Double = giveEnergyTo(theItem, receivedME)
    if (!doReceive) {
      o
      setCurrentEnergy(theItem, current)
    }
    museEnergyToMJ(eatenME).toFloat
  }

  def transferEnergy(theItem: ItemStack, energy: Float, doTransfer: Boolean): Float = {
    val current: Double = getCurrentEnergy(theItem)
    val requesteddME: Double = museEnergyFromMJ(energy)
    val takenME: Double = drainEnergyFrom(theItem, requesteddME)
    if (!doTransfer) {
      setCurrentEnergy(theItem, current)
    }
    museEnergyToMJ(takenME).toFloat
  }

  def getEnergyStored(theItem: ItemStack) = museEnergyToMJ(getCurrentEnergy(theItem)).toFloat

  def getMaxEnergyStored(theItem: ItemStack) = museEnergyToMJ(getMaxEnergy(theItem)).toFloat

}