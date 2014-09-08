package net.machinemuse.powersuits.item

import net.minecraft.item.EnumArmorMaterial
import net.minecraft.item.ItemArmor
import net.machinemuse.api.electricity._

abstract class ItemElectricArmor(id: Int, material: EnumArmorMaterial, index1: Int, index2: Int)
  extends ItemArmor(id, material, index1, index2)
  with ModularItemBase
  with MuseElectricItem {

}