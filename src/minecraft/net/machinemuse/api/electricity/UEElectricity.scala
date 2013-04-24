package net.machinemuse.api.electricity

import net.machinemuse.powersuits.common.ModCompatability
import net.minecraft.item.ItemStack
import universalelectricity.core.electricity.ElectricityPack
import universalelectricity.core.item.IItemElectric
import UEPowerConversions._

class UEElectricAdapter(val stack: ItemStack) extends ElectricAdapter {
  val item = stack.getItem.asInstanceOf[IItemElectric]


  def getCurrentEnergy: Double = {
    val currentEnergy: Double = museEnergyFromJoules(item.getJoules(stack))
    return currentEnergy
  }

  def getMaxEnergy: Double = {
    val maxEnergy: Double = museEnergyFromJoules(item.getMaxJoules(stack))
    return maxEnergy
  }

  def drainEnergy(requested: Double): Double = {
    val voltage: Double = item.getVoltage(stack)
    val requestedPack: ElectricityPack = museEnergyToElectricityPack(requested, voltage)
    val receivedPack: ElectricityPack = item.onProvide(requestedPack, stack)
    return museEnergyFromElectricityPack(receivedPack)
  }

  def giveEnergy(provided: Double): Double = {
    val voltage: Double = item.getVoltage(stack)
    val packToProvide: ElectricityPack = museEnergyToElectricityPack(provided, voltage)
    val eatenPack: ElectricityPack = item.onReceive(packToProvide, stack)
    return museEnergyFromElectricityPack(eatenPack)
  }
}

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

object UEPowerConversions {
  // UE
  def museEnergyToElectricityPack(museEnergy: Double, voltage: Double): ElectricityPack = new ElectricityPack(museEnergyToJoules(museEnergy) * 20.0 / voltage, voltage)

  def museEnergyFromElectricityPack(pack: ElectricityPack): Double = museEnergyFromJoules(pack.amperes / 20.0 * pack.voltage)

  def museEnergyToJoules(energy: Double): Double = energy / ModCompatability.getUERatio

  def museEnergyFromJoules(energy: Double): Double = energy * ModCompatability.getUERatio
}
