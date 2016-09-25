package net.machinemuse.powersuits.common

import net.machinemuse.powersuits.item._


/**
 * Created by Claire Semple on 9/9/2014.
 */
object MPSItems {
  val powerArmorHead = MPSRegistry.registerItem(new ItemPowerArmorHelmet, "powerArmorHead", "powerArmorHelmet")
  val powerArmorTorso = MPSRegistry.registerItem(new ItemPowerArmorChestplate, "powerArmorTorso", "powerArmorChestplate")
  val powerArmorLegs = MPSRegistry.registerItem(new ItemPowerArmorLeggings, "powerArmorLegs", "powerArmorLeggings")
  val powerArmorFeet = MPSRegistry.registerItem(new ItemPowerArmorBoots, "powerArmorFeet", "powerArmorBoots")
  val powerTool = MPSRegistry.registerItem(new ItemPowerFist, "powerTool", "powerFist")






//  val tinkerTable = MPSRegistry.registerBlock(BlockTinkerTable.instance)
//  val luxCapacitor = MPSRegistry.registerBlock(BlockLuxCapacitor.instance)





//  GameRegistry.registerBlock(tinkerTable, tinkerTable.getUnlocalizedName)

//  GameRegistry.registerBlock(luxCapacitor, luxCapacitor.getUnlocalizedName)
  val components = MPSRegistry.registerItem(new ItemComponent(), "powerArmorComponent", "powerArmorComponent")
  components.populate()
//  var powerArmorHead: Item = new ItemPowerArmorHelmet
//  var powerArmorTorso: Item = new ItemPowerArmorChestplate
//  var powerArmorLegs: Item = new ItemPowerArmorLeggings
//  var powerArmorFeet: Item = new ItemPowerArmorBoots
//  var powerTool: Item = new ItemPowerFist
//  var tinkerTable: Block = new BlockTinkerTable
//  var luxCapacitor: Block = new BlockLuxCapacitor

//  def InitItems {
//    registerItem(powerArmorHead, "powerArmorHead", powerArmorHead.getUnlocalizedName)
//    registerItem(powerArmorTorso, "powerArmorTorso", powerArmorTorso.getUnlocalizedName)
//    registerItem(powerArmorLegs, "powerArmorLegs", powerArmorLegs.getUnlocalizedName)
//    registerItem(powerArmorFeet, "powerArmorFeet", powerArmorFeet.getUnlocalizedName)
//    registerItem(powerTool, "powerTool", powerTool.getUnlocalizedName)
//  }
//
//  def InitBlocks {
//    registerBlock(tinkerTable)
//    registerBlock(luxCapacitor)
//  }
//
//  def InitTEs {
//  }

  /**
    * Sets the unlocalized name and registry name and registers the item.
    */

}
