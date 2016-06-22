package net.machinemuse.powersuits.item

import net.machinemuse.utils.render.MuseRenderer
import net.minecraft.inventory.EntityEquipmentSlot
import net.minecraftforge.fml.relauncher.{Side, SideOnly}

class ItemPowerArmorLeggings extends ItemPowerArmor(0, EntityEquipmentSlot.LEGS) {
  val iconpath = MuseRenderer.ICON_PREFIX + "armorlegs"

  setUnlocalizedName("powerArmorLeggings")
}