package net.machinemuse.api

import buildcraft.api.tools.IToolWrench
import cofh.api.item.IToolHammer
import cpw.mods.fml.common.FMLCommonHandler
import cpw.mods.fml.common.Optional
import crazypants.enderio.TileEntityEio
import crazypants.enderio.api.tool.ITool
import mods.railcraft.api.core.items.IToolCrowbar
import net.machinemuse.numina.general.MuseLogger
import net.machinemuse.powersuits.powermodule.tool.{MFFSFieldTeleporterModule, OmniWrenchModule}
import net.machinemuse.utils.ElectricItemUtils
import net.minecraft.block.Block
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.item.EntityMinecart
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraftforge.common.util.ForgeDirection
import powercrystals.minefactoryreloaded.api.IMFRHammer

/**
 * Author: MachineMuse (Claire Semple)
 * Created: 5:06 PM, 29/04/13
 */

@Optional.InterfaceList(Array(
    new Optional.Interface(iface = "net.machinemuse.api.ModularCrowbar", modid = "Railcraft", striprefs = true),
    new Optional.Interface(iface = "net.machinemuse.api.EnderIOTool", modid = "EnderIO", striprefs = true),
    new Optional.Interface(iface = "net.machinemuse.api.ModularCrescentHammer", modid = "CoFHCore", striprefs = true),
    new Optional.Interface(iface = "net.machinemuse.api.ModularWrench", modid = "BuildCraft|Core", striprefs = true),
    new Optional.Interface(iface = "net.machinemuse.api.ModularHammer", modid = "MineFactoryReloaded", striprefs = true)
))
trait OmniWrench
  extends ModularWrench
  with ModularCrowbar
  with EnderIOTool
  with ModularHammer
  with ModularCrescentHammer
  /*with ForceFieldManipulator*/ {

}

// Railcraft Crowbar
@Optional.Interface(iface = "mods.railcraft.api.core.items.IToolCrowbar", modid = "Railcraft", striprefs = true)
trait ModularCrowbar extends IToolCrowbar {
  def canWhack(player: EntityPlayer, crowbar: ItemStack, x: Int, y: Int, z: Int): Boolean = {
    if (crowbar != null && crowbar.getItem.isInstanceOf[IModularItem]) {
      return ModuleManager.itemHasActiveModule(crowbar, OmniWrenchModule.MODULE_OMNI_WRENCH)
    } else {
      return false
    }
  }

  def onWhack(player: EntityPlayer, crowbar: ItemStack, x: Int, y: Int, z: Int) {
    player.swingItem
  }

  def canLink(player: EntityPlayer, crowbar: ItemStack, cart: EntityMinecart): Boolean = {
    if (crowbar != null && crowbar.getItem.isInstanceOf[IModularItem]) {
      return ModuleManager.itemHasActiveModule(crowbar, OmniWrenchModule.MODULE_OMNI_WRENCH)
    } else {
      return false
    }
  }

  def onLink(player: EntityPlayer, crowbar: ItemStack, cart: EntityMinecart) {
    player.swingItem
  }

  def canBoost(player: EntityPlayer, crowbar: ItemStack, cart: EntityMinecart): Boolean = {
    if (crowbar != null && crowbar.getItem.isInstanceOf[IModularItem]) {
      return ModuleManager.itemHasActiveModule(crowbar, OmniWrenchModule.MODULE_OMNI_WRENCH)
    } else {
      return false
    }
  }

  def onBoost(player: EntityPlayer, crowbar: ItemStack, cart: EntityMinecart) {
    player.swingItem
  }
}

// EnderIO Tool
@Optional.Interface(iface = "crazypants.enderio.api.tool.ITool", modid = "EnderIO", striprefs = true)
trait EnderIOTool
	extends ITool {
		def canUse(stack: ItemStack, player: EntityPlayer, x: Int, y: Int, z: Int): Boolean = {
			MuseLogger.logDebug("EnderIO canUse called")
      return player.getEntityWorld.getTileEntity(x, y, z).isInstanceOf[TileEntityEio]
		}

		def used(stack: ItemStack, player: EntityPlayer, x: Int, y: Int, z: Int) {
			val item = player.getHeldItem
			if ( item != null && item.getItem.isInstanceOf[IModularItem]) {
				val t = player.getEntityWorld.getTileEntity(x, y, z)
				val b = player.getEntityWorld.getBlock(x, y, z)
				if (t.isInstanceOf[TileEntityEio] && MuseItemTag.getMuseItemTag(item).getBoolean("eioManipulateConduit")) {
					MuseLogger.logDebug("Conduit manipulated...")
					if (player.isSneaking) {
            b.removedByPlayer(player.getEntityWorld, player, x, y, z, true)
          } else {
            b.rotateBlock(player.getEntityWorld, x, y, z, ForgeDirection.getOrientation(t.getBlockMetadata))
          }
          player.swingItem
				}
			}
		}

		def shouldHideFacades(stack: ItemStack, player: EntityPlayer): Boolean = {
			val item = player.getHeldItem
			if (item != null && item.getItem.isInstanceOf[IModularItem]) {
				//MuseLogger.logDebug(MuseItemTag.getMuseItemTag(item).toString)
    		return MuseItemTag.getMuseItemTag(item).getBoolean("eioFacadeTransparency")
			} else {
				return false
			}
		}
	}

// CoFH Hammer
@Optional.Interface(iface = "cofh.api.item.IToolHammer", modid = "CoFHCore", striprefs = true)
trait ModularCrescentHammer
  extends IToolHammer {
    def isUsable(item: ItemStack, user: EntityLivingBase, x: Int, y: Int, z: Int): Boolean = {
      return ModuleManager.itemHasActiveModule(item, OmniWrenchModule.MODULE_OMNI_WRENCH)
    }

    def toolUsed(item: ItemStack, user: EntityLivingBase, x: Int, y: Int, z: Int) = {
    }
}

// Buildcraft Wrench
@Optional.Interface(iface = "buildcraft.api.tools.IToolWrench", modid = "BuildCraft|Core", striprefs = true)
trait ModularWrench
  extends IToolWrench {

  def canWrench(player: EntityPlayer, x: Int, y: Int, z: Int): Boolean = {
    if (player.getCurrentEquippedItem != null && player.getCurrentEquippedItem.getItem.isInstanceOf[IModularItem]) {
      return ModuleManager.itemHasActiveModule(player.getCurrentEquippedItem, OmniWrenchModule.MODULE_OMNI_WRENCH)
    }
    return false
  }

  def wrenchUsed(player: EntityPlayer, x: Int, y: Int, z: Int) {
    player.swingItem
  }
}

// MFR Hammer
@Optional.Interface(iface = "powercrystals.minefactoryreloaded.api.IMFRHammer", modid = "MineFactoryReloaded", striprefs = true)
trait ModularHammer extends IMFRHammer {
}

// Considering implementing EnderIO ITool here, but the NBT method is thoroughly tested and works properly right now. - 2014-12-01 Korynkai

// MFFS ForceField Manipulator -- Requires calclavia reimplementation. - 2014-12-01 Korynkai
/*trait ForceFieldManipulator extends IFieldTeleporter {
  def canFieldTeleport(player: EntityPlayer, stack: ItemStack, teleportCost: Int): Boolean = {
    if (ModuleManager.itemHasModule(stack, MFFSFieldTeleporterModule.MODULE_FIELD_TELEPORTER)) {
      if (ElectricItemUtils.getPlayerEnergy(player) > ModuleManager.computeModularProperty(stack, MFFSFieldTeleporterModule.FIELD_TELEPORTER_ENERGY_CONSUMPTION)) {
        return true
      }
      else if (FMLCommonHandler.instance.getEffectiveSide.isServer) {
        player.sendChatToPlayer(ChatMessageComponent.createFromText("[Field Security] Could not teleport through forcefield. 20,000J is required to teleport."))
      }
    }
    return false
  }*/

  /*def onFieldTeleportSuccess(player: EntityPlayer, stack: ItemStack, teleportCost: Int) {
    ElectricItemUtils.drainPlayerEnergy(player, ModuleManager.computeModularProperty(stack, MFFSFieldTeleporterModule.FIELD_TELEPORTER_ENERGY_CONSUMPTION))
  }

  def onFieldTeleportFailed(player: EntityPlayer, stack: ItemStack, teleportCost: Int) {
  }
}*/
