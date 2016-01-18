package net.machinemuse.powersuits.item

import cpw.mods.fml.common.Optional
import cpw.mods.fml.relauncher.{Side, SideOnly}
import forestry.api.core.IArmorNaturalist;
import micdoodle8.mods.galacticraft.api.item.IBreathableArmor
import micdoodle8.mods.galacticraft.api.item.IBreathableArmor.EnumGearType
import net.machinemuse.api.ModuleManager
import net.machinemuse.powersuits.powermodule.armor.ApiaristArmorModule
import net.machinemuse.powersuits.powermodule.misc.{AirtightSealModule, ThaumGogglesModule}
import net.machinemuse.utils.render.MuseRenderer
import net.minecraft.client.renderer.texture.IIconRegister
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import thaumcraft.api.IGoggles
import thaumcraft.api.nodes.IRevealer

@Optional.InterfaceList (Array(
new Optional.Interface (iface = "forestry.api.core.IArmorNaturalist", modid = "Forestry", striprefs = true),
new Optional.Interface (iface = "thaumcraft.api.IGoggles", modid = "Thaumcraft", striprefs = true),
new Optional.Interface (iface = "thaumcraft.api.nodes.IRevealer", modid = "Thaumcraft", striprefs = true),
new Optional.Interface (iface = "micdoodle8.mods.galacticraft.api.item.IBreathableArmor", modid = "GalacticraftCore", striprefs = true)
))
class ItemPowerArmorHelmet extends ItemPowerArmor(0, 0)
with IArmorNaturalist
with IBreathableArmor
with IGoggles
with IRevealer {
  val iconpath = MuseRenderer.ICON_PREFIX + "armorhead"

  setUnlocalizedName("powerArmorHelmet")

  @SideOnly(Side.CLIENT)
  override def registerIcons(iconRegister: IIconRegister) {
    itemIcon = iconRegister.registerIcon(iconpath)
  }

  override def showIngamePopups(itemstack: ItemStack, player: EntityLivingBase): Boolean =
    ModuleManager.itemHasActiveModule(itemstack, ThaumGogglesModule.MODULE_THAUM_GOGGLES)

  override def showNodes(itemstack: ItemStack, player: EntityLivingBase): Boolean =
    ModuleManager.itemHasActiveModule(itemstack, ThaumGogglesModule.MODULE_THAUM_GOGGLES)

  @Optional.Method(modid = "Forestry")
  override def canSeePollination(player: EntityPlayer, helm: ItemStack, doSee: Boolean): Boolean = {
    ModuleManager.itemHasActiveModule(helm, ApiaristArmorModule.MODULE_APIARIST_ARMOR)
  }

  @Optional.Method(modid = "GalacticraftCore")
  override def handleGearType(geartype: EnumGearType): Boolean = {
    geartype == EnumGearType.HELMET
  }

  @Optional.Method(modid = "GalacticraftCore")
  override def canBreathe(helm: ItemStack, player: EntityPlayer, geartype: EnumGearType): Boolean =
    ModuleManager.itemHasActiveModule(helm, AirtightSealModule.AIRTIGHT_SEAL_MODULE)
}
