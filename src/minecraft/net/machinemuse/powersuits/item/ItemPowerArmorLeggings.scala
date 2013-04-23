package net.machinemuse.powersuits.item

import net.minecraft.client.renderer.texture.IconRegister
import atomicscience.api.poison.Poison.ArmorType
import cpw.mods.fml.common.registry.LanguageRegistry
import cpw.mods.fml.relauncher.Side
import cpw.mods.fml.relauncher.SideOnly
import atomicscience.api.poison.Poison
import net.machinemuse.utils.MuseRenderer

class ItemPowerArmorLeggings(id: Int) extends ItemPowerArmor(id, 0, 2) {
  var assignedItemID: Int = 0
  val iconpath = MuseRenderer.ICON_PREFIX + "armorlegs"

  setUnlocalizedName("powerArmorLeggings")
  LanguageRegistry.addName(this, "Power Armor Leggings")

  @SideOnly(Side.CLIENT)
  override def registerIcons(iconRegister: IconRegister) {
    itemIcon = iconRegister.registerIcon(iconpath)
  }

  def getArmorType: Poison.ArmorType = ArmorType.LEGGINGS
}