package net.machinemuse.powersuits.client

import net.machinemuse.powersuits.block.{BlockLuxCapacitor, BlockTinkerTable}
import net.machinemuse.powersuits.common.{Config, ModularPowersuits}
import net.minecraft.client.renderer.block.model.ModelResourceLocation
import net.minecraftforge.fml.relauncher.{Side, SideOnly}

/**
  * Created by leon on 8/1/16.
  */
@SideOnly(Side.CLIENT) object ModelLocations {
  // PowerArmor
  val powerArmorHead: ModelResourceLocation = new ModelResourceLocation(Config.RESOURCE_PREFIX + "powerArmorHead", "inventory")
  val powerArmorTorso: ModelResourceLocation = new ModelResourceLocation(Config.RESOURCE_PREFIX + "powerArmorTorso", "inventory")
  val powerArmorLegs: ModelResourceLocation = new ModelResourceLocation(Config.RESOURCE_PREFIX + "powerArmorLegs", "inventory")
  val powerArmorFeet: ModelResourceLocation = new ModelResourceLocation(Config.RESOURCE_PREFIX + "powerArmorFeet", "inventory")
  // PowerFist
  val powerFistGUI: ModelResourceLocation = new ModelResourceLocation(Config.RESOURCE_PREFIX + "powerFistGUI", "inventory")
  val powerFistLeft: ModelResourceLocation = new ModelResourceLocation(Config.RESOURCE_PREFIX + "powerFistLeft", "inventory")
  val powerFistLeftFiring: ModelResourceLocation = new ModelResourceLocation(Config.RESOURCE_PREFIX + "powerFistLeftFiring", "inventory")
  val powerFistRight: ModelResourceLocation = new ModelResourceLocation(Config.RESOURCE_PREFIX + "powerFistRight", "inventory")
  val powerFistRightFiring: ModelResourceLocation = new ModelResourceLocation(Config.RESOURCE_PREFIX + "powerFistRightFiring", "inventory")
  // Lux Capacicitor
  val luxCapacitor: ModelResourceLocation = new ModelResourceLocation(ModularPowersuits.MODID.toLowerCase + ":" + BlockLuxCapacitor.name, "inventory")
  // Tinker Table
  val tinkerTable: ModelResourceLocation = new ModelResourceLocation(ModularPowersuits.MODID.toLowerCase + ":" + BlockTinkerTable.name, "inventory")
}