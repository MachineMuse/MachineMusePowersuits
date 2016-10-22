package net.machinemuse.api

import cpw.mods.fml.common.Optional
import forestry.api.apiculture.IArmorApiarist
import net.machinemuse.powersuits.powermodule.armor.ApiaristArmorModule
import net.machinemuse.utils.ElectricItemUtils
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.{ItemArmor, ItemStack}
//import atomicscience.api.IAntiPoisonArmor

/**
  * Author: MachineMuse (Claire Semple)
  * Created: 6:54 AM, 4/28/13
  */
trait ArmorTraits extends ItemArmor with IModularItem
  with ApiaristArmor {
  //with RadiationArmor {

}

@Optional.Interface(iface = "forestry.api.apiculture.IArmorApiarist", modid = "Forestry", striprefs = true)
trait ApiaristArmor extends IArmorApiarist {

  //  @deprecated
  @Optional.Method(modid = "Forestry")
  def protectPlayer(player: EntityPlayer, armor: ItemStack, cause: String, doProtect: Boolean): Boolean = {
    if (ModuleManager.itemHasActiveModule(armor, ApiaristArmorModule.MODULE_APIARIST_ARMOR)) {
      ElectricItemUtils.drainPlayerEnergy(player, ModuleManager.computeModularProperty(armor, ApiaristArmorModule.APIARIST_ARMOR_ENERGY_CONSUMPTION))
      true
    } else false
  }

  @Optional.Method(modid = "Forestry")
  def protectEntity(player: EntityLivingBase, armor: ItemStack, cause: String, doProtect: Boolean): Boolean = {
    if (ModuleManager.itemHasActiveModule(armor, ApiaristArmorModule.MODULE_APIARIST_ARMOR)) {
      ElectricItemUtils.drainPlayerEnergy(player.asInstanceOf[EntityPlayer], ModuleManager.computeModularProperty(armor, ApiaristArmorModule.APIARIST_ARMOR_ENERGY_CONSUMPTION))
      true
    } else false
  }
}

/*
trait RadiationArmor extends ItemArmor with IAntiPoisonArmor {
  def isProtectedFromPoison(itemStack: ItemStack, EntityLivingBase: EntityLivingBase, poisontype: String): Boolean = {
    ModuleManager.itemHasActiveModule(itemStack, HazmatModule.MODULE_HAZMAT)
  }


  def getArmorType:Int = this.armorType

  def onProtectFromPoison(itemStack: ItemStack, EntityLivingBase: EntityLivingBase, poisontype: String) {}
}
*/
