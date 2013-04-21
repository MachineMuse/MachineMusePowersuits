package net.machinemuse.powersuits.item

import net.minecraft.item.EnumArmorMaterial
import net.minecraft.item.ItemArmor
import net.machinemuse.api.electricity.{UEElectricItem, TEElectricItem, IC2ElectricItem, EMPElectricItem}

abstract class ItemElectricArmor(id: Int, material: EnumArmorMaterial, index1: Int, index2: Int)
  extends ItemArmor(id, material, index1, index2)
  with IC2ElectricItem
  with TEElectricItem
  with EMPElectricItem
  with UEElectricItem {

}