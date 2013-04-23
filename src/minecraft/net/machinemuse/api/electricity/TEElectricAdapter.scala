package net.machinemuse.api.electricity

import net.minecraft.item.ItemStack
import thermalexpansion.api.item.IChargeableItem
import net.machinemuse.api.electricity.PowerConversions._

class TEElectricAdapter(val stack: ItemStack) extends ElectricAdapter {
  val item = stack.getItem.asInstanceOf[IChargeableItem]

  def getCurrentEnergy: Double = {
    return museEnergyFromMJ(item.getEnergyStored(stack))
  }

  def getMaxEnergy: Double = {
    return museEnergyFromMJ(item.getMaxEnergyStored(stack))
  }

  def drainEnergy(requested: Double): Double = {
    val requestedMJ: Float = museEnergyToMJ(requested).asInstanceOf[Float]
    val receivedMJ: Float = item.transferEnergy(stack, requestedMJ, true)
    return museEnergyFromMJ(receivedMJ)
  }

  def giveEnergy(provided: Double): Double = {
    val providedMJ: Float = museEnergyToMJ(provided).asInstanceOf[Float]
    val takenMJ: Float = item.receiveEnergy(stack, providedMJ, true)
    return museEnergyFromMJ(takenMJ)
  }
}