package net.machinemuse.powersuits.item

import net.machinemuse.api.electricity._
import net.minecraft.inventory.EntityEquipmentSlot
import net.minecraft.item.{ItemArmor, ItemStack}

abstract class ItemElectricArmor(material: ItemArmor.ArmorMaterial, index1: Int, index2: EntityEquipmentSlot)
  extends ItemArmor(material, index1, index2)
  with ModularItemBase
  with MuseElectricItem {
  override def getToolTip(stack: ItemStack): String = null
}