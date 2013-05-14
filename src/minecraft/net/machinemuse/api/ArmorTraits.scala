package net.machinemuse.api

import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.machinemuse.utils.{ElectricItemUtils, MuseItemUtils}
import net.machinemuse.powersuits.powermodule.armor.{HazmatModule, ApiaristArmorModule}
import forestry.api.apiculture.IArmorApiarist
import net.minecraft.entity.EntityLiving
import atomicscience.api.poison.Poison
import atomicscience.api.IAntiPoisonArmor

/**
 * Author: MachineMuse (Claire Semple)
 * Created: 6:54 AM, 4/28/13
 */
trait ArmorTraits extends IModularItem
with ApiaristArmor
with RadiationArmor {

}

trait ApiaristArmor extends IArmorApiarist {
  def protectPlayer(player: EntityPlayer, armor: ItemStack, cause: String, doProtect: Boolean): Boolean = {
    if (MuseItemUtils.itemHasActiveModule(armor, ApiaristArmorModule.MODULE_APIARIST_ARMOR)) {
      ElectricItemUtils.drainPlayerEnergy(player, ModuleManager.computeModularProperty(armor, ApiaristArmorModule.APIARIST_ARMOR_ENERGY_CONSUMPTION))
      true
    } else false
  }
}

trait RadiationArmor extends IAntiPoisonArmor {
  def isProtectedFromPoison(itemStack: ItemStack, entityLiving: EntityLiving, `type`: Poison): Boolean = {
    MuseItemUtils.itemHasActiveModule(itemStack, HazmatModule.MODULE_HAZMAT)
  }

  def onProtectFromPoison(itemStack: ItemStack, entityLiving: EntityLiving, `type`: Poison) {}
}