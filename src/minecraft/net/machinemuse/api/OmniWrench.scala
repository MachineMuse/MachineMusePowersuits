package net.machinemuse.api

import mods.railcraft.api.core.items.IToolCrowbar
import net.machinemuse.utils.{ElectricItemUtils, MuseItemUtils}
import net.machinemuse.powersuits.powermodule.tool.{MFFSFieldTeleporterModule, OmniWrenchModule}
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.entity.item.EntityMinecart
import buildcraft.api.tools.IToolWrench
import universalelectricity.prefab.implement.IToolConfigurator
import cpw.mods.fml.common.FMLCommonHandler
import mods.mffs.api.IFieldTeleporter
import powercrystals.minefactoryreloaded.api.IToolHammerAdvanced

/**
 * Author: MachineMuse (Claire Semple)
 * Created: 5:06 PM, 29/04/13
 */
trait OmniWrench
  extends ModularWrench
  with ModularCrowbar
  with ModularHammer
  with ForceFieldManipulator {

}

trait ModularCrowbar extends IToolCrowbar {
  def canWhack(player: EntityPlayer, crowbar: ItemStack, x: Int, y: Int, z: Int): Boolean = {
    return MuseItemUtils.itemHasActiveModule(crowbar, OmniWrenchModule.MODULE_OMNI_WRENCH)
  }

  def onWhack(player: EntityPlayer, crowbar: ItemStack, x: Int, y: Int, z: Int) {
    player.swingItem
  }

  def canLink(player: EntityPlayer, crowbar: ItemStack, cart: EntityMinecart): Boolean = {
    return MuseItemUtils.itemHasActiveModule(crowbar, OmniWrenchModule.MODULE_OMNI_WRENCH)
  }

  def onLink(player: EntityPlayer, crowbar: ItemStack, cart: EntityMinecart) {
    player.swingItem
  }

  def canBoost(player: EntityPlayer, crowbar: ItemStack, cart: EntityMinecart): Boolean = {
    return MuseItemUtils.itemHasActiveModule(crowbar, OmniWrenchModule.MODULE_OMNI_WRENCH)
  }

  def onBoost(player: EntityPlayer, crowbar: ItemStack, cart: EntityMinecart) {
    player.swingItem
  }
}


trait ModularWrench
  extends IToolWrench // Buildcraft wrench
  with IToolConfigurator {
  // Universal Electricity wrench

  def canWrench(player: EntityPlayer, x: Int, y: Int, z: Int): Boolean = {
    if (player.getCurrentEquippedItem != null && player.getCurrentEquippedItem.getItem.isInstanceOf[IModularItem]) {
      return MuseItemUtils.itemHasActiveModule(player.getCurrentEquippedItem, OmniWrenchModule.MODULE_OMNI_WRENCH)
    }
    return false
  }

  def wrenchUsed(player: EntityPlayer, x: Int, y: Int, z: Int) {
  }
}

// MFR wrench
trait ModularHammer extends IToolHammerAdvanced {
  def isActive(stack: ItemStack): Boolean = MuseItemUtils.itemHasActiveModule(stack, OmniWrenchModule.MODULE_OMNI_WRENCH)
}

trait ForceFieldManipulator extends IFieldTeleporter {
  def canFieldTeleport(player: EntityPlayer, stack: ItemStack, teleportCost: Int): Boolean = {
    if (MuseItemUtils.itemHasModule(stack, MFFSFieldTeleporterModule.MODULE_FIELD_TELEPORTER)) {
      if (ElectricItemUtils.getPlayerEnergy(player) > ModuleManager.computeModularProperty(stack, MFFSFieldTeleporterModule.FIELD_TELEPORTER_ENERGY_CONSUMPTION)) {
        return true
      }
      else if (FMLCommonHandler.instance.getEffectiveSide.isServer) {
        player.sendChatToPlayer("[Field Security] Could not teleport through forcefield. 20,000J is required to teleport.")
      }
    }
    return false
  }

  def onFieldTeleportSuccess(player: EntityPlayer, stack: ItemStack, teleportCost: Int) {
    ElectricItemUtils.drainPlayerEnergy(player, ModuleManager.computeModularProperty(stack, MFFSFieldTeleporterModule.FIELD_TELEPORTER_ENERGY_CONSUMPTION))
  }

  def onFieldTeleportFailed(player: EntityPlayer, stack: ItemStack, teleportCost: Int) {
  }
}