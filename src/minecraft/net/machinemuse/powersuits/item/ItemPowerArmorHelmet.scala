package net.machinemuse.powersuits.item

import micdoodle8.mods.galacticraft.API.EnumGearType
import micdoodle8.mods.galacticraft.API.IBreathableArmor
import net.machinemuse.powersuits.powermodule.misc.AirtightSealModule
import net.minecraft.client.renderer.texture.IconRegister
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import atomicscience.api.poison.Poison.ArmorType
import cpw.mods.fml.common.registry.LanguageRegistry
import cpw.mods.fml.relauncher.Side
import cpw.mods.fml.relauncher.SideOnly
import atomicscience.api.poison.Poison
import net.machinemuse.utils.{MuseItemUtils}
import net.machinemuse.utils.render.MuseRenderer

class ItemPowerArmorHelmet(id: Int) extends ItemPowerArmor(id, 0, 0) with IBreathableArmor {
  val iconpath = MuseRenderer.ICON_PREFIX + "armorhead"

  setUnlocalizedName("powerArmorHelmet")
  LanguageRegistry.addName(this, "Power Armor Helmet")

  def getArmorType: Poison.ArmorType = ArmorType.HELM

  // IBreathableArmor
  def handleGearType(gearType: EnumGearType): Boolean = gearType eq EnumGearType.HELMET

  def canBreathe(helm: ItemStack, player: EntityPlayer, gearType: EnumGearType): Boolean = {
    return MuseItemUtils.itemHasActiveModule(helm, AirtightSealModule.AIRTIGHT_SEAL_MODULE)
  }

  @SideOnly(Side.CLIENT)
  override def registerIcons(iconRegister: IconRegister) {
    itemIcon = iconRegister.registerIcon(iconpath)
  }
}