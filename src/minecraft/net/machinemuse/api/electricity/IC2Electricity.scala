package net.machinemuse.api.electricity

import net.minecraft.item.ItemStack
import net.machinemuse.api.ModuleManager
import net.machinemuse.powersuits.common.ModCompatability
import IC2PowerConversions._
import ic2.api.{ICustomElectricItem, ElectricItem, IElectricItem}

class IC2ElectricAdapter(val stack: ItemStack) extends ElectricAdapter {
  val item = stack.getItem.asInstanceOf[IElectricItem]

  def getCurrentEnergy: Double = museEnergyFromEU(ElectricItem.discharge(stack, Integer.MAX_VALUE, getTier, true, true))

  def getMaxEnergy: Double = museEnergyFromEU(item.getMaxCharge(stack))

  def drainEnergy(requested: Double) = museEnergyFromEU(ElectricItem.discharge(stack, museEnergyToEU(requested).toInt, getTier, true, false))

  def giveEnergy(provided: Double): Double = museEnergyFromEU(ElectricItem.charge(stack, museEnergyToEU(provided).toInt, getTier, true, false))

  def getTier = item.getTier(stack)
}

trait IC2ElectricItem extends ICustomElectricItem with MuseElectricItem {
  def canProvideEnergy(itemStack: ItemStack): Boolean = true

  def getChargedItemId(itemStack: ItemStack): Int = itemStack.itemID

  def getEmptyItemId(itemStack: ItemStack): Int = itemStack.itemID

  def getMaxCharge(itemStack: ItemStack): Int = museEnergyToEU(getCurrentEnergy(itemStack)).asInstanceOf[Int]

  def getTier(itemStack: ItemStack): Int = IC2PowerConversions.getTier(itemStack)

  def getTransferLimit(itemStack: ItemStack): Int = museEnergyToEU(Math.sqrt(getMaxEnergy(itemStack))).asInstanceOf[Int]

  def charge(itemStack: ItemStack, amount: Int, tier: Int, ignoreTransferLimit: Boolean, simulate: Boolean): Int = {
    val current: Double = getCurrentEnergy(itemStack)
    val given: Double = giveEnergyTo(itemStack, museEnergyFromEU(amount))
    if (simulate) {
      setCurrentEnergy(itemStack, current)
    }
    museEnergyToEU(given).asInstanceOf[Int]
  }

  def discharge(itemStack: ItemStack, amount: Int, tier: Int, ignoreTransferLimit: Boolean, simulate: Boolean): Int = {
    val current: Double = getCurrentEnergy(itemStack)
    val taken: Double = drainEnergyFrom(itemStack, museEnergyFromEU(amount))
    if (simulate) {
      setCurrentEnergy(itemStack, current)
    }
    museEnergyToEU(taken).asInstanceOf[Int]
  }

  def canUse(itemStack: ItemStack, amount: Int): Boolean = museEnergyFromEU(amount) < getCurrentEnergy(itemStack)


  def canShowChargeToolTip(itemStack: ItemStack): Boolean = false
}

object IC2PowerConversions {
  // IC2
  final val IC2_TIER: String = "IC2 Tier"

  def getTier(stack: ItemStack): Int = ModuleManager.computeModularProperty(stack, IC2_TIER).toInt

  def museEnergyToEU(museEnergy: Double): Double = museEnergy / ModCompatability.getIC2Ratio

  def museEnergyFromEU(eu: Double): Double = eu * ModCompatability.getIC2Ratio
}