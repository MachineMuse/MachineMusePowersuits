package net.machinemuse.api.electricity

import ic2.api.ElectricItem
import ic2.api.IElectricItem
import net.machinemuse.api.electricity.PowerConversions._
import net.minecraft.item.ItemStack

class IC2ElectricAdapter(val stack:ItemStack) extends ElectricAdapter {
    val item = stack.getItem.asInstanceOf[IElectricItem]


  def getCurrentEnergy: Double = {
    return museEnergyFromEU(ElectricItem.discharge(stack, Integer.MAX_VALUE, getTier, true, true))
  }

  def getMaxEnergy: Double = {
    return museEnergyFromEU(item.getMaxCharge(stack))
  }

  def drainEnergy(requested: Double): Double = {
    val requestedEU: Double = museEnergyToEU(requested)
    val drainedEU: Double = ElectricItem.discharge(stack, requestedEU.asInstanceOf[Int], getTier, true, false)
    return museEnergyFromEU(drainedEU)
  }

  def giveEnergy(provided: Double): Double = {
    val providedEU: Double = museEnergyToEU(provided)
    val givenEU: Double = ElectricItem.charge(stack, providedEU.asInstanceOf[Int], getTier, true, false)
    return museEnergyFromEU(givenEU)
  }

  def getTier: Int = {
    return PowerConversions.getTier(stack)
  }
}