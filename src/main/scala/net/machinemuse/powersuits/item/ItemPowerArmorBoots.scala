package net.machinemuse.powersuits.item

import cpw.mods.fml.common.Optional
import ic2.api.item.IMetalArmor
import net.minecraft.client.renderer.texture.IIconRegister
import cpw.mods.fml.relauncher.Side
import cpw.mods.fml.relauncher.SideOnly
import net.machinemuse.utils.render.MuseRenderer
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack

@Optional.InterfaceList (Array(
  new Optional.Interface (iface = "ic2.api.item.IMetalArmor", modid = "IC2", striprefs = true)
) )
class ItemPowerArmorBoots extends ItemPowerArmor(0, 3) with IMetalArmor{
  val iconpath = MuseRenderer.ICON_PREFIX + "armorfeet"

  setUnlocalizedName("powerArmorBoots")

  @SideOnly(Side.CLIENT) override def registerIcons(iconRegister: IIconRegister) {
    itemIcon = iconRegister.registerIcon(iconpath)
  }

  override def isMetalArmor(itemStack: ItemStack, entityPlayer: EntityPlayer): Boolean = true
}