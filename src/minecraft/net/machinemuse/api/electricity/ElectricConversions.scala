package net.machinemuse.api.electricity

import net.minecraft.item.ItemStack
import net.machinemuse.powersuits.common.ModCompatability
import universalelectricity.core.electricity.ElectricityPack
import net.machinemuse.api.ModuleManager

/**
 * Author: MachineMuse (Claire Semple)
 * Created: 4:51 AM, 4/28/13
 */
object ElectricConversions {
  // IC2
  final val IC2_TIER: String = "IC2 Tier"

  def getTier(stack: ItemStack): Int = ModuleManager.computeModularProperty(stack, IC2_TIER).toInt

  def museEnergyToEU(museEnergy: Double): Double = museEnergy / ModCompatability.getIC2Ratio

  def museEnergyFromEU(eu: Double): Double = eu * ModCompatability.getIC2Ratio

  // TE
  def museEnergyToMJ(museEnergy: Double): Double = museEnergy / ModCompatability.getBCRatio

  def museEnergyFromMJ(mj: Double): Double = mj * ModCompatability.getBCRatio

  // UE
  def museEnergyToElectricityPack(museEnergy: Double, voltage: Double): ElectricityPack = new ElectricityPack(museEnergyToJoules(museEnergy) * 20.0 / voltage, voltage)

  def museEnergyFromElectricityPack(pack: ElectricityPack): Double = museEnergyFromJoules(pack.amperes / 20.0 * pack.voltage)

  def museEnergyToJoules(energy: Double): Double = energy / ModCompatability.getUERatio

  def museEnergyFromJoules(energy: Double): Double = energy * ModCompatability.getUERatio
}
