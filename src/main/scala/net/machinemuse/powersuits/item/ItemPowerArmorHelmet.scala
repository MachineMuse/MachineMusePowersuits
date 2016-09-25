package net.machinemuse.powersuits.item

import forestry.api.core.IArmorNaturalist
import net.machinemuse.api.ModuleManager
import net.machinemuse.powersuits.powermodule.armor.ApiaristArmorModule
import net.machinemuse.utils.render.MuseRenderer
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.inventory.EntityEquipmentSlot
import net.minecraft.item.ItemStack
import net.minecraftforge.fml.common.Optional

//import thaumcraft.api.IGoggles
//import thaumcraft.api.nodes.IRevealer

@Optional.InterfaceList (Array(
new Optional.Interface (iface = "forestry.api.core.IArmorNaturalist", modid = "Forestry", striprefs = true)
))
class ItemPowerArmorHelmet extends ItemPowerArmor(0, EntityEquipmentSlot.HEAD)
with IArmorNaturalist {
  val iconpath = MuseRenderer.ICON_PREFIX + "armorhead"

//  setUnlocalizedName("powerArmorHelmet")

  @Optional.Method(modid = "Forestry")
  override def canSeePollination(player: EntityPlayer, helm: ItemStack, doSee: Boolean): Boolean = {
    ModuleManager.itemHasActiveModule(helm, ApiaristArmorModule.MODULE_APIARIST_ARMOR)
  }
}
