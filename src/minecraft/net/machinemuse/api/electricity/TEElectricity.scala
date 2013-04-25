package net.machinemuse.api.electricity

import net.minecraft.item.ItemStack
import thermalexpansion.api.item.IChargeableItem
import net.machinemuse.powersuits.common.ModCompatability
import TEPowerConversions._

class TEElectricAdapter(val stack: ItemStack) extends ElectricAdapter {
  val item = stack.getItem.asInstanceOf[IChargeableItem]

  def getCurrentEnergy: Double = museEnergyFromMJ(item.getEnergyStored(stack))

  def getMaxEnergy: Double = museEnergyFromMJ(item.getMaxEnergyStored(stack))

  def drainEnergy(requested: Double): Double = museEnergyFromMJ(item.transferEnergy(stack, museEnergyToMJ(requested).toFloat, true))

  def giveEnergy(provided: Double): Double = museEnergyFromMJ(item.receiveEnergy(stack, museEnergyToMJ(provided).toFloat, true))

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
