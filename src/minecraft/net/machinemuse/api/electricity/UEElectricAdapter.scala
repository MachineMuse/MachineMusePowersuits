package net.machinemuse.api.electricity

import net.machinemuse.powersuits.common.ModCompatability
import net.minecraft.item.ItemStack
import universalelectricity.core.electricity.ElectricityPack
import universalelectricity.core.item.IItemElectric
import net.machinemuse.api.electricity.PowerConversions._

class UEElectricAdapter(val stack:ItemStack) extends ElectricAdapter {
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