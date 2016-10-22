package net.machinemuse.powersuits.item

import cpw.mods.fml.relauncher.{Side, SideOnly}
import net.machinemuse.utils.render.MuseRenderer
import net.minecraft.client.renderer.texture.IIconRegister

class ItemPowerArmorChestplate extends ItemPowerArmor(0, 1) {
  val iconpath = MuseRenderer.ICON_PREFIX + "armortorso"

  setUnlocalizedName("powerArmorChestplate")

  @SideOnly(Side.CLIENT) override def registerIcons(iconRegister: IIconRegister) {
    itemIcon = iconRegister.registerIcon(iconpath)
  }
}