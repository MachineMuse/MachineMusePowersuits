package net.machinemuse.api.electricity

import net.minecraft.item.ItemStack
import net.machinemuse.powersuits.common.ModCompatibility
import net.machinemuse.api.ModuleManager

/**
 * Author: MachineMuse (Claire Semple)
 * Created: 4:51 AM, 4/28/13
 */
object ElectricConversions {
  // IC2
  final val IC2_TIER: String = "IC2 Tier"

  def getTier(stack: ItemStack): Int = ModuleManager.computeModularProperty(stack, IC2_TIER).toInt

  def museEnergyToEU(museEnergy: Double): Double = museEnergy / ModCompatibility.getIC2Ratio

  def museEnergyFromEU(eu: Double): Double = eu * ModCompatibility.getIC2Ratio

  // TE
  def museEnergyToRF(museEnergy: Double): Int = Math.ceil(museEnergy / ModCompatibility.getRFRatio).toInt

  def museEnergyFromRF(rf: Int): Double = rf * ModCompatibility.getRFRatio

}
