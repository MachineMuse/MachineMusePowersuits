package net.machinemuse.api.electricity

import net.minecraft.item.ItemStack
import thermalexpansion.api.item.IChargeableItem
import net.machinemuse.powersuits.common.ModCompatability
import TEPowerConversions._

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

trait TEElectricItem extends IChargeableItem with MuseElectricItem {
  def receiveEnergy(theItem: ItemStack, energy: Float, doReceive: Boolean): Float = {
    val receivedME: Double = museEnergyFromMJ(energy)
    val eatenME: Double = giveEnergyTo(theItem, receivedME)
    museEnergyToMJ(eatenME).asInstanceOf[Float]
  }

  def transferEnergy(theItem: ItemStack, energy: Float, doTransfer: Boolean): Float = {
    val requesteddME: Double = museEnergyFromMJ(energy)
    val takenME: Double = drainEnergyFrom(theItem, requesteddME)
    museEnergyToMJ(takenME).asInstanceOf[Float]
  }

  def getEnergyStored(theItem: ItemStack): Float = {
    museEnergyToMJ(getCurrentEnergy(theItem)).asInstanceOf[Float]
  }

  def getMaxEnergyStored(theItem: ItemStack): Float = {
    museEnergyToMJ(getMaxEnergy(theItem)).asInstanceOf[Float]
  }
}

object TEPowerConversions {
  // TE
  def museEnergyToMJ(museEnergy: Double): Double = museEnergy / ModCompatability.getBCRatio

  def museEnergyFromMJ(mj: Double): Double = mj * ModCompatability.getBCRatio
}
