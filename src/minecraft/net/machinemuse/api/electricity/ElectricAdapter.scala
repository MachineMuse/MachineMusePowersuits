package net.machinemuse.api.electricity

import net.minecraft.item.ItemStack
import thermalexpansion.api.item.IChargeableItem
import universalelectricity.core.item.IItemElectric
import net.machinemuse.powersuits.common.ModCompatability
import ic2.api.item.{ElectricItem, IElectricItem}
import universalelectricity.core.electricity.ElectricityPack
import net.machinemuse.api.electricity.ElectricConversions._

object ElectricAdapter {
  def wrap(stack: ItemStack): ElectricAdapter = {
    if (stack == null) return null
    stack.getItem match {
      case i: IChargeableItem => new TEElectricAdapter(stack)
      case i: IItemElectric => new UEElectricAdapter(stack)
      case i: IElectricItem => if (ModCompatability.isIndustrialCraftLoaded) new IC2ElectricAdapter(stack) else null
      case _ => null
    }
  }

}

abstract class ElectricAdapter {
  def getCurrentEnergy: Double

  def getMaxEnergy: Double

  def drainEnergy(requested: Double): Double

  def giveEnergy(provided: Double): Double
}

class IC2ElectricAdapter(val stack: ItemStack) extends ElectricAdapter {
  val item = stack.getItem.asInstanceOf[IElectricItem]

  def getCurrentEnergy: Double = museEnergyFromEU(ElectricItem.discharge(stack, Integer.MAX_VALUE, getTier, true, true))

  def getMaxEnergy: Double = museEnergyFromEU(item.getMaxCharge(stack))

  def drainEnergy(requested: Double) = museEnergyFromEU(ElectricItem.discharge(stack, museEnergyToEU(requested).toInt, getTier, true, false))

  def giveEnergy(provided: Double): Double = museEnergyFromEU(ElectricItem.charge(stack, museEnergyToEU(provided).toInt, getTier, true, false))

  def getTier = item.getTier(stack)
}

class TEElectricAdapter(val stack: ItemStack) extends ElectricAdapter {
  val item = stack.getItem.asInstanceOf[IChargeableItem]

  def getCurrentEnergy: Double = museEnergyFromMJ(item.getEnergyStored(stack))

  def getMaxEnergy: Double = museEnergyFromMJ(item.getMaxEnergyStored(stack))

  def drainEnergy(requested: Double): Double = museEnergyFromMJ(item.transferEnergy(stack, museEnergyToMJ(requested).toFloat, true))

  def giveEnergy(provided: Double): Double = museEnergyFromMJ(item.receiveEnergy(stack, museEnergyToMJ(provided).toFloat, true))

}


class UEElectricAdapter(val stack: ItemStack) extends ElectricAdapter {
  val item = stack.getItem.asInstanceOf[IItemElectric]

  def getCurrentEnergy: Double = museEnergyFromJoules(item.getElectricityStored(stack))

  def getMaxEnergy: Double = museEnergyFromJoules(item.getMaxElectricityStored(stack))

  def drainEnergy(requested: Double): Double = {
    val joules: Double = museEnergyToJoules(requested)
    val received: Float = item.discharge(stack, joules.toFloat, true)
    museEnergyFromJoules(received)
  }

  def giveEnergy(provided: Double): Double = {
    val joulesToProvide: Double = museEnergyToJoules(provided)
    val eatenJoules: Float = item.recharge(stack, joulesToProvide.toFloat, true)
    museEnergyFromJoules(eatenJoules)
  }
}