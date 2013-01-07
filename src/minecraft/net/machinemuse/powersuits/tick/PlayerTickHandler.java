/**
 * 
 */
package net.machinemuse.powersuits.tick;

import java.util.EnumSet;
import java.util.List;

import net.machinemuse.powersuits.common.Config;
import net.machinemuse.powersuits.common.MuseLogger;
import net.machinemuse.powersuits.item.IModularItem;
import net.machinemuse.powersuits.item.ItemUtils;
import net.machinemuse.powersuits.item.ModularCommon;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.ITickHandler;
import cpw.mods.fml.common.TickType;
import cpw.mods.fml.relauncher.Side;

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
	@Override public void tickStart(EnumSet<TickType> type, Object... tickData) {
		EntityPlayer player = toPlayer(tickData[0]);
		double totalEnergy = ItemUtils.getPlayerEnergy(player);
		double totalWeight = 0;
		double weightCapacity = 25000;
		Side side = FMLCommonHandler.instance().getEffectiveSide();
		
		for (ItemStack stack : ItemUtils.getModularItemsInInventory(player)) {
			totalWeight += ModularCommon.getTotalWeight(stack);
		}
		if (player.isSprinting()) {
			// idk why this is the pants slot
			ItemStack pants = player.getCurrentArmor(1);
			if (pants != null && pants.getItem() instanceof IModularItem && ItemUtils.itemHasModule(pants, ModularCommon.MODULE_SPRINT_ASSIST)) {
				double sprintCost = Config.computeModularProperty(pants, ModularCommon.SPRINT_ENERGY_CONSUMPTION);
				if (sprintCost < totalEnergy) {
					totalEnergy -= sprintCost;
					ItemUtils.drainPlayerEnergy(player, sprintCost);
					double sprintBoost = Config.computeModularProperty(pants, ModularCommon.SPRINT_SPEED_MULTIPLIER);
					player.landMovementFactor *= sprintBoost;
					player.jumpMovementFactor *= sprintBoost;
					double movement = Math.round(Math.sqrt(player.motionX * player.motionX + player.motionZ * player.motionZ) * 100.0F);
					double exhaustionComp = Config.computeModularProperty(pants, ModularCommon.SPRINT_FOOD_COMPENSATION);
					player.addExhaustion((float) (-0.0001 * movement * exhaustionComp));
				}
			}
		}
		if (totalWeight > weightCapacity) {
			player.motionX *= weightCapacity / totalWeight;
			player.motionZ *= weightCapacity / totalWeight;
		}
	}
	@Override public void tickEnd(EnumSet<TickType> type, Object... tickData) {
		EntityPlayer player = toPlayer(tickData[0]);
		List<ItemStack> stacks = ItemUtils
				.getModularItemsInInventory(player.inventory);
		
	}
	
	public static World toWorld(Object data) {
		World world = null;
		try {
			world = (World) data;
		} catch (ClassCastException e) {
			MuseLogger.logDebug(
					"MMMPS: Player tick handler received invalid World object");
			e.printStackTrace();
		}
		return world;
	}
	
	public static EntityPlayer toPlayer(Object data) {
		EntityPlayer player = null;
		try {
			player = (EntityPlayer) data;
		} catch (ClassCastException e) {
			MuseLogger
					.logDebug(
					"MMMPS: Player tick handler received invalid Player object");
			e.printStackTrace();
		}
		return player;
	}
	
	/**
	 * Type of tick handled by this handler
	 */
	@Override public EnumSet<TickType> ticks() {
		return EnumSet.of(TickType.PLAYER);
	}
	
	/**
	 * Profiling label for this handler
	 */
	@Override public String getLabel() {
		return "MMMPS: Player Tick";
	}
	
}
