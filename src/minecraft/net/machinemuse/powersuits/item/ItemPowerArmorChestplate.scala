package net.machinemuse.powersuits.item

import net.minecraft.client.renderer.texture.IconRegister
import cpw.mods.fml.relauncher.Side
import cpw.mods.fml.relauncher.SideOnly
import net.machinemuse.utils.render.MuseRenderer

class ItemPowerArmorChestplate(id: Int) extends ItemPowerArmor(id, 0, 1) {
  val iconpath = MuseRenderer.ICON_PREFIX + "armortorso"

  setUnlocalizedName("powerArmorChestplate")

  @SideOnly(Side.CLIENT) override def registerIcons(iconRegister: IconRegister) {
    itemIcon = iconRegister.registerIcon(iconpath)
  }
}