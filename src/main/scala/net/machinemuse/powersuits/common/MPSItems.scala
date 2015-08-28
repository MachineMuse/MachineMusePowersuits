package net.machinemuse.powersuits.common

import cpw.mods.fml.common.registry.GameRegistry
import net.machinemuse.powersuits.block.{BlockLuxCapacitor, BlockTinkerTable}
import net.machinemuse.powersuits.item._

/**
 * Created by Claire Semple on 9/9/2014.
 */
object MPSItems {
  val powerArmorHead = new ItemPowerArmorHelmet
  GameRegistry.registerItem(powerArmorHead, powerArmorHead.getUnlocalizedName)
  val powerArmorTorso = new ItemPowerArmorChestplate
  GameRegistry.registerItem(powerArmorTorso, powerArmorTorso.getUnlocalizedName)
  val powerArmorLegs = new ItemPowerArmorLeggings
  GameRegistry.registerItem(powerArmorLegs, powerArmorLegs.getUnlocalizedName)
  val powerArmorFeet = new ItemPowerArmorBoots
  GameRegistry.registerItem(powerArmorFeet, powerArmorFeet.getUnlocalizedName)
  val powerTool = new ItemPowerFist
  GameRegistry.registerItem(powerTool, powerTool.getUnlocalizedName)
  val tinkerTable = BlockTinkerTable
  GameRegistry.registerBlock(tinkerTable, tinkerTable.getUnlocalizedName)
  val luxCapacitor = new BlockLuxCapacitor
  GameRegistry.registerBlock(luxCapacitor, luxCapacitor.getUnlocalizedName)
  val components = new ItemComponent()
  components.setUnlocalizedName("powerArmorComponent")
  components.populate()
  GameRegistry.registerItem(components, "powerArmorComponent")
}
