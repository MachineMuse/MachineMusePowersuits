package net.machinemuse.powersuits.item

import net.machinemuse.api.electricity._
import net.minecraft.item.{ItemStack, ItemArmor}

abstract class ItemElectricArmor(material: ItemArmor.ArmorMaterial, index1: Int, index2: Int)
  extends ItemArmor(material, index1, index2)
  with ModularItemBase
  with MuseElectricItem {
  override def getToolTip(stack: ItemStack): String = null
}