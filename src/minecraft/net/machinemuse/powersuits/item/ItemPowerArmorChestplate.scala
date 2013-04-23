package net.machinemuse.powersuits.item

import net.minecraft.client.renderer.texture.IconRegister
import atomicscience.api.poison.Poison.ArmorType
import cpw.mods.fml.common.registry.LanguageRegistry
import cpw.mods.fml.relauncher.Side
import cpw.mods.fml.relauncher.SideOnly
import atomicscience.api.poison.Poison
import net.machinemuse.utils.MuseRenderer

class ItemPowerArmorChestplate(id: Int) extends ItemPowerArmor(id, 0, 1) {
  var assignedItemID: Int = 0
  val iconpath = MuseRenderer.ICON_PREFIX + "armortorso"

  setUnlocalizedName("powerArmorChestplate")
  LanguageRegistry.addName(this, "Power Armor Chestplate")

  @SideOnly(Side.CLIENT)
  override def registerIcons(iconRegister: IconRegister) {
    itemIcon = iconRegister.registerIcon(iconpath)
  }

  def getArmorType: Poison.ArmorType = ArmorType.BODY
}