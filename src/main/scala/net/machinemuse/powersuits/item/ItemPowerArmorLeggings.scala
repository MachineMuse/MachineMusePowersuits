package net.machinemuse.powersuits.item

import net.minecraft.client.renderer.texture.IIconRegister
import net.machinemuse.utils.render.MuseRenderer
import net.minecraftforge.fml.relauncher.{Side, SideOnly}

class ItemPowerArmorLeggings extends ItemPowerArmor(0, 2) {
  val iconpath = MuseRenderer.ICON_PREFIX + "armorlegs"

  setUnlocalizedName("powerArmorLeggings")


  @SideOnly(Side.CLIENT)
  override def registerIcons(iconRegister: IIconRegister) {
    itemIcon = iconRegister.registerIcon(iconpath)
  }
}