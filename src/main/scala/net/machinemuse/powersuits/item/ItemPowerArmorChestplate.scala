package net.machinemuse.powersuits.item

import net.machinemuse.utils.render.MuseRenderer
import net.minecraft.inventory.EntityEquipmentSlot

class ItemPowerArmorChestplate extends ItemPowerArmor(0, EntityEquipmentSlot.CHEST) {
  val iconpath = MuseRenderer.ICON_PREFIX + "armortorso"
}