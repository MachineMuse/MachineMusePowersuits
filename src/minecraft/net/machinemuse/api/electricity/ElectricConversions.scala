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
  def museEnergyToRF(museEnergy: Double): Int = Math.ceil(museEnergy / ModCompatability.getRFRatio).toInt

  def museEnergyFromRF(rf: Int): Double = rf * ModCompatability.getRFRatio

  // BC
  def museEnergyToMJ(museEnergy: Double): Double = museEnergy / ModCompatability.getBCRatio

  def museEnergyFromMJ(mj: Double): Double = mj * ModCompatability.getBCRatio

  // UE
  def museEnergyToElectricityPack(museEnergy: Double, voltage: Float): ElectricityPack = new ElectricityPack(museEnergyToJoules(museEnergy) * 20.0F / voltage, voltage)

  def museEnergyFromElectricityPack(pack: ElectricityPack): Double = museEnergyFromJoules(pack.amperes / 20.0F * pack.voltage)

  def museEnergyToJoules(energy: Double): Float = (energy / ModCompatability.getUERatio).toFloat

  def museEnergyFromJoules(energy: Float): Double = energy * ModCompatability.getUERatio
}
