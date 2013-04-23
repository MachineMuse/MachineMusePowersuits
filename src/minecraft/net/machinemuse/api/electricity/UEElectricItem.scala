package net.machinemuse.api.electricity

import universalelectricity.core.item.IItemElectric
import universalelectricity.core.electricity.ElectricityPack
import net.minecraft.item.ItemStack
import net.machinemuse.api.electricity.PowerConversions._

/**
 * Created with IntelliJ IDEA.
 * User: MachineMuse
 */
trait UEElectricItem extends IItemElectric with MuseElectricItem {
  def getJoules(itemStack: ItemStack): Double = museEnergyToJoules(getCurrentEnergy(itemStack))

  def setJoules(joules: Double, itemStack: ItemStack) = setCurrentEnergy(itemStack, museEnergyFromJoules(joules))


  def getMaxJoules(itemStack: ItemStack): Double = museEnergyToJoules(getMaxEnergy(itemStack))

  def getVoltage(itemStack: ItemStack): Double = 120

  def onReceive(electricityPack: ElectricityPack, itemStack: ItemStack): ElectricityPack = {
    val energyReceiving: Double = museEnergyFromElectricityPack(electricityPack)
    val energyConsumed: Double = giveEnergyTo(itemStack, energyReceiving)
    val packConsumed: ElectricityPack = museEnergyToElectricityPack(energyConsumed, getVoltage(itemStack))
    packConsumed
  }

  def onProvide(electricityPack: ElectricityPack, itemStack: ItemStack): ElectricityPack = {
    val energyRequested: Double = museEnergyFromElectricityPack(electricityPack)
    val energyGiven: Double = drainEnergyFrom(itemStack, energyRequested)
    val packGiven: ElectricityPack = museEnergyToElectricityPack(energyGiven, getVoltage(itemStack))
    packGiven
  }

  def getReceiveRequest(itemStack: ItemStack): ElectricityPack = {
    museEnergyToElectricityPack(getMaxEnergy(itemStack) - getCurrentEnergy(itemStack), getVoltage(itemStack))
  }

  def getProvideRequest(itemStack: ItemStack): ElectricityPack = {
    museEnergyToElectricityPack(getMaxEnergy(itemStack) - getCurrentEnergy(itemStack), getVoltage(itemStack))
  }
}
