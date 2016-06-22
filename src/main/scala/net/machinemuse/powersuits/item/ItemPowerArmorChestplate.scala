package net.machinemuse.powersuits.item

import net.machinemuse.utils.render.MuseRenderer
import net.minecraft.entity.EntityLivingBase
import net.minecraft.inventory.EntityEquipmentSlot
import net.minecraft.item.ItemStack
import net.minecraftforge.fml.relauncher.{Side, SideOnly}

class ItemPowerArmorChestplate extends ItemPowerArmor(0, EntityEquipmentSlot.CHEST) {
  val iconpath = MuseRenderer.ICON_PREFIX + "armortorso"

  setUnlocalizedName("powerArmorChestplate")

  override def protectEntity(entityLivingBase: EntityLivingBase, itemStack: ItemStack, s: String, b: Boolean): Boolean = ??? //Todo: Finish
}