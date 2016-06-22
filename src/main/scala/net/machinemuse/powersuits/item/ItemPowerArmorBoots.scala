package net.machinemuse.powersuits.item

import ic2.api.item.IMetalArmor
import net.machinemuse.utils.render.MuseRenderer
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.inventory.EntityEquipmentSlot
import net.minecraft.item.ItemStack
import net.minecraftforge.fml.common.Optional

@Optional.InterfaceList (Array(
  new Optional.Interface (iface = "ic2.api.item.IMetalArmor", modid = "IC2", striprefs = true)
) )
class ItemPowerArmorBoots extends ItemPowerArmor(0, EntityEquipmentSlot.FEET) with IMetalArmor{
  val iconpath = MuseRenderer.ICON_PREFIX + "armorfeet"

  setUnlocalizedName("powerArmorBoots")

  override def isMetalArmor(itemStack: ItemStack, entityPlayer: EntityPlayer): Boolean = true

  override def protectEntity(entityLivingBase: EntityLivingBase, itemStack: ItemStack, s: String, b: Boolean): Boolean = ???
}