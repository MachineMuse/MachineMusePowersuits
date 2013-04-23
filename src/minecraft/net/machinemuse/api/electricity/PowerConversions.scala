package net.machinemuse.api.electricity

import universalelectricity.core.electricity.ElectricityPack
import net.machinemuse.powersuits.common.ModCompatability
import net.machinemuse.api.ModuleManager
import java.lang.String
import net.minecraft.item.ItemStack

/**
 * By: Claire
 * Created: 4/23/13 5:13 PM
 */
object PowerConversions {
  // IC2
  final val IC2_TIER: String = "IC2 Tier"

  def getTier(stack: ItemStack): Int = ModuleManager.computeModularProperty(stack, IC2_TIER).toInt

  def museEnergyToEU(museEnergy: Double): Double = museEnergy / ModCompatability.getIC2Ratio

  def museEnergyFromEU(eu: Double): Double = eu * ModCompatability.getIC2Ratio

  // UE
  def museEnergyToElectricityPack(museEnergy: Double, voltage: Double): ElectricityPack = new ElectricityPack(museEnergyToJoules(museEnergy) * 20.0 / voltage, voltage)

  def museEnergyFromElectricityPack(pack: ElectricityPack): Double = museEnergyFromJoules(pack.amperes / 20.0 * pack.voltage)

  def museEnergyToJoules(energy: Double): Double = energy / ModCompatability.getUERatio

  def museEnergyFromJoules(energy: Double): Double = energy * ModCompatability.getUERatio

  // TE
  def museEnergyToMJ(museEnergy: Double): Double = museEnergy / ModCompatability.getBCRatio

  def museEnergyFromMJ(mj: Double): Double = mj * ModCompatability.getBCRatio
}
