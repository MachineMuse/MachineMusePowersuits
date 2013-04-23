/**
 * 
 */
package net.machinemuse.powersuits.tick;

import java.util.EnumSet;
import java.util.List;

import net.machinemuse.api.*;
import net.machinemuse.api.electricity.ElectricItemUtils;
import net.machinemuse.api.moduletrigger.IPlayerTickModule;
import net.machinemuse.general.MuseLogger;
import net.machinemuse.general.MuseMathUtils;
import net.machinemuse.powersuits.common.Config;
import net.machinemuse.powersuits.control.PlayerInputMap;
import net.machinemuse.powersuits.event.MovementManager;
import net.machinemuse.powersuits.powermodule.movement.FlightControlModule;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import cpw.mods.fml.common.ITickHandler;
import cpw.mods.fml.common.TickType;

/**
 * Tick handler for Player update step. tickStart() is queued before the entity
 * is updated, and tickEnd() is queued afterwards.
 * 
 * Player update step: "Called to update the entity's position/logic."
 * 
 * tickData: EntityPlayer of the entity being updated.
 * 
 * @author MachineMuse
 */
public class PlayerTickHandler implements ITickHandler {

	@Override
	public void tickStart(EnumSet<TickType> type, Object... tickData) {
        EntityPlayer player = MusePlayerUtils.toPlayer(tickData[0]);
		handle(player);

	}

	// int gliderTicker = 0, swimTicker = 0;

	public void handle(EntityPlayer player) {
		List<ItemStack> modularItemsEquipped = MuseItemUtils.modularItemsEquipped(player);

		double totalWeight = MuseItemUtils.getPlayerWeight(player);
		double weightCapacity = 25000;

		// double totalEnergyDrain = 0;

        for (ItemStack stack : modularItemsEquipped) {
            if (stack.getTagCompound().hasKey("ench")) {
                stack.getTagCompound().removeTag("ench");
            }
        }

		boolean foundItem = modularItemsEquipped.size()>0;
        if (foundItem) {
            for (IPlayerTickModule module : ModuleManager.getPlayerTickModules()) {
                for (ItemStack itemStack : modularItemsEquipped) {
                    if (module.isValidForItem(itemStack, player)) {
                        if (MuseItemUtils.itemHasActiveModule(itemStack, module.getName())) {
                            module.onPlayerTickActive(player, itemStack);
                        } else {
                            module.onPlayerTickInactive(player, itemStack);
                        }
                    }
                }
            }
			player.fallDistance = (float) MovementManager.computeFallHeightFromVelocity(MuseMathUtils.clampDouble(player.motionY, -1000.0, 0.0));

			// Weight movement penalty
			if (totalWeight > weightCapacity) {
				player.motionX *= weightCapacity / totalWeight;
				player.motionZ *= weightCapacity / totalWeight;
			}
            MuseHeatUtils.coolPlayer(player, MusePlayerUtils.getPlayerCoolingBasedOnMaterial(player));
            double maxHeat = MuseHeatUtils.getMaxHeat(player);
            double currHeat = MuseHeatUtils.getPlayerHeat(player);
            if (currHeat > maxHeat) {
                player.attackEntityFrom(MuseHeatUtils.overheatDamage, (int) Math.sqrt(currHeat - maxHeat) / 4);
                player.setFire(1);
            } else {
                player.extinguish();
            }
        }
	}

	@Override
	public void tickEnd(EnumSet<TickType> type, Object... tickData) {
		EntityPlayer player = MusePlayerUtils.toPlayer(tickData[0]);
		List<ItemStack> stacks = MuseItemUtils.getModularItemsInInventory(player.inventory);

	}

	public static World toWorld(Object data) {
		World world = null;
		try {
			world = (World) data;
		} catch (ClassCastException e) {
			MuseLogger.logError("MMMPS: Player tick handler received invalid World object");
			e.printStackTrace();
		}
		return world;
	}

	/**
	 * Type of tick handled by this handler
	 */
	@Override
	public EnumSet<TickType> ticks() {
		return EnumSet.of(TickType.PLAYER);
	}

	/**
	 * Profiling label for this handler
	 */
	@Override
	public String getLabel() {
		return "MMMPS: Player Tick";
	}

}
