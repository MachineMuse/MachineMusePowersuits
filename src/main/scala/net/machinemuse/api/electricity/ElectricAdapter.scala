package net.machinemuse.api.electricity

import cofh.api.energy.IEnergyContainerItem
import ic2.api.item.ElectricItem
import ic2.api.item.IElectricItem
import net.machinemuse.powersuits.common.ModCompatibility
import net.minecraft.item.ItemStack

object ElectricAdapter {
  def wrap(stack: ItemStack): ElectricAdapter = {
    if (stack == null) return null
    val i = stack.getItem
    if (i.isInstanceOf[MuseElectricItem]) {
      new MuseElectricAdapter(stack)
    } else if (ModCompatibility.isCoFHCoreLoaded && i.isInstanceOf[IEnergyContainerItem]) {
      new TEElectricAdapter(stack)
    } else if (ModCompatibility.isIndustrialCraftLoaded && i.isInstanceOf[IElectricItem]) {
      new IC2ElectricAdapter(stack)
    } else {
      null
    }
  }
}

abstract class ElectricAdapter {
  def getCurrentEnergy: Double

  def getMaxEnergy: Double

  def drainEnergy(requested: Double): Double

  def giveEnergy(provided: Double): Double
}

class MuseElectricAdapter(val stack: ItemStack) extends ElectricAdapter {
  val item = stack.getItem.asInstanceOf[MuseElectricItem]

  def getCurrentEnergy = item.getCurrentEnergy(stack)

  def getMaxEnergy = item.getMaxEnergy(stack)

  def drainEnergy(requested: Double) = item.drainEnergyFrom(stack, requested)

  def giveEnergy(provided: Double) = item.giveEnergyTo(stack, provided)
}


class IC2ElectricAdapter(val stack: ItemStack) extends ElectricAdapter {
  val item = stack.getItem.asInstanceOf[IElectricItem]

  import net.machinemuse.api.electricity.ElectricConversions._

  def getCurrentEnergy: Double = museEnergyFromEU(ElectricItem.manager.getCharge(stack))

  def getMaxEnergy: Double = museEnergyFromEU(item.getMaxCharge(stack))

  def drainEnergy(requested: Double): Double = museEnergyFromEU(ElectricItem.manager.discharge(stack, museEnergyToEU(requested), getTier, true, false, false))

  def giveEnergy(provided: Double): Double = museEnergyFromEU(ElectricItem.manager.charge(stack, museEnergyToEU(provided), getTier, true, false))

  def getTier = item.getTier(stack)
}

class TEElectricAdapter(val stack: ItemStack) extends ElectricAdapter {
  val item = stack.getItem.asInstanceOf[IEnergyContainerItem]

  import net.machinemuse.api.electricity.ElectricConversions._

  def getCurrentEnergy: Double = museEnergyFromRF(item.getEnergyStored(stack))

  def getMaxEnergy: Double = museEnergyFromRF(item.getMaxEnergyStored(stack))

  def drainEnergy(requested: Double): Double = museEnergyFromRF(item.extractEnergy(stack, museEnergyToRF(requested), false))

  def giveEnergy(provided: Double): Double = museEnergyFromRF(item.receiveEnergy(stack, museEnergyToRF(provided), false))

}