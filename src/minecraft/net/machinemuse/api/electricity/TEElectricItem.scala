package net.machinemuse.api.electricity

import net.minecraft.item.ItemStack
import thermalexpansion.api.item.IChargeableItem
import net.machinemuse.api.electricity.PowerConversions._

/**
 * Author: MachineMuse (Claire Semple)
 * Created: 10:05 PM, 4/20/13
 */
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
