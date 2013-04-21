package net.machinemuse.api.electricity

import net.minecraft.item.ItemStack
import thermalexpansion.api.item.IChargeableItem

/**
 * Author: MachineMuse (Claire Semple)
 * Created: 10:05 PM, 4/20/13
 */
trait TEElectricItem extends IChargeableItem with MuseElectricItem {
  def receiveEnergy(theItem: ItemStack, energy: Float, doReceive: Boolean): Float = {
    val receivedME: Double = TEElectricAdapter.museEnergyFromMJ(energy)
    val eatenME: Double = giveEnergyTo(theItem, receivedME)
    TEElectricAdapter.museEnergyToMJ(eatenME).asInstanceOf[Float]
  }

  def transferEnergy(theItem: ItemStack, energy: Float, doTransfer: Boolean): Float = {
    val requesteddME: Double = TEElectricAdapter.museEnergyFromMJ(energy)
    val takenME: Double = drainEnergyFrom(theItem, requesteddME)
    TEElectricAdapter.museEnergyToMJ(takenME).asInstanceOf[Float]
  }

  def getEnergyStored(theItem: ItemStack): Float = {
    TEElectricAdapter.museEnergyToMJ(getCurrentEnergy(theItem)).asInstanceOf[Float]
  }

  def getMaxEnergyStored(theItem: ItemStack): Float = {
    TEElectricAdapter.museEnergyToMJ(getMaxEnergy(theItem)).asInstanceOf[Float]
  }
}
