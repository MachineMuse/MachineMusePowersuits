package net.machinemuse.powersuits.item

import net.machinemuse.utils.render.MuseRenderer
import net.minecraft.inventory.EntityEquipmentSlot

class ItemPowerArmorLeggings extends ItemPowerArmor(0, EntityEquipmentSlot.LEGS) {
  val iconpath = MuseRenderer.ICON_PREFIX + "armorlegs"
}