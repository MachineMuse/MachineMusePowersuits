package net.machinemuse.powersuits.item

import cpw.mods.fml.relauncher.{Side, SideOnly}
import net.machinemuse.utils.render.MuseRenderer
import net.minecraft.client.renderer.texture.IIconRegister

class ItemPowerArmorLeggings extends ItemPowerArmor(0, 2) {
  val iconpath = MuseRenderer.ICON_PREFIX + "armorlegs"

  setUnlocalizedName("powerArmorLeggings")


  @SideOnly(Side.CLIENT)
  override def registerIcons(iconRegister: IIconRegister) {
    itemIcon = iconRegister.registerIcon(iconpath)
  }
}