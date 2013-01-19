package net.machinemuse.powersuits.event;

import java.util.HashMap;
import java.util.Map;

import net.machinemuse.powersuits.common.MuseLogger;
import net.machinemuse.powersuits.item.ItemPowerArmor;
import net.machinemuse.powersuits.item.ItemUtils;
import net.machinemuse.powersuits.item.ModularCommon;
import net.machinemuse.powersuits.network.MusePacket;
import net.machinemuse.powersuits.network.packets.MusePacketPlayerUpdate;
import net.machinemuse.powersuits.powermodule.ModuleManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.living.LivingEvent.LivingJumpEvent;
import net.minecraftforge.event.entity.living.LivingFallEvent;

public class MovementManager {
	public static Map<EntityPlayer, Double> playerJumpMultipliers = new HashMap();

	public static double getPlayerJumpMultiplier(EntityPlayer player) {
		if (playerJumpMultipliers.containsKey(player)) {
			return playerJumpMultipliers.get(player);
		} else {
			return 0;
		}
	}

	public static void setPlayerJumpTicks(EntityPlayer player, double number) {
		playerJumpMultipliers.put(player, number);
	}

	@ForgeSubscribe
	public void handleLivingJumpEvent(LivingJumpEvent event) {
		event.entityLiving.setJumping(true);
		if (event.entityLiving instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) event.entityLiving;
			ItemStack stack = player.getCurrentArmor(1);
			if (stack != null && stack.getItem() instanceof ItemPowerArmor && ItemUtils.itemHasModule(stack, ModularCommon.MODULE_JUMP_ASSIST)) {
				double jumpAssist = ModuleManager.computeModularProperty(stack, ModularCommon.JUMP_MULTIPLIER) * 2;
				double drain = ModuleManager.computeModularProperty(stack, ModularCommon.JUMP_ENERGY_CONSUMPTION);
				double avail = ItemUtils.getPlayerEnergy(player);
				if (drain < avail) {
					ItemUtils.drainPlayerEnergy(player, drain);
					setPlayerJumpTicks(player, jumpAssist);
					double jumpCompensationRatio = ModuleManager.computeModularProperty(stack, ModularCommon.JUMP_FOOD_COMPENSATION);
					MuseLogger.logDebug("Jumped");
					if (player.isSprinting()) {
						player.getFoodStats().addExhaustion((float) (-0.8 * jumpCompensationRatio));
					} else {
						player.getFoodStats().addExhaustion((float) (-0.2 * jumpCompensationRatio));
					}

				}
			}

		}
	}

	@ForgeSubscribe
	public void handleFallEvent(LivingFallEvent event) {
		if (event.entityLiving instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) event.entityLiving;
			ItemStack boots = player.getCurrentArmor(0);
			if (boots != null) {
				if (ItemUtils.itemHasModule(boots, ModularCommon.MODULE_SHOCK_ABSORBER)) {
					double distanceAbsorb = event.distance * ModuleManager.computeModularProperty(boots, ModularCommon.SHOCK_ABSORB_MULTIPLIER);

					double drain = distanceAbsorb * distanceAbsorb * 0.05
							* ModuleManager.computeModularProperty(boots, ModularCommon.SHOCK_ABSORB_ENERGY_CONSUMPTION);
					double avail = ItemUtils.getPlayerEnergy(player);
					if (drain < avail) {
						ItemUtils.drainPlayerEnergy(player, drain);
						event.distance -= distanceAbsorb;
					}
				}
			}
		}
	}

	/**
	 * Gravity, in meters per tick per tick.
	 */
	public static final double DEFAULT_GRAVITY = -0.0784000015258789;

	public static double computeFallHeightFromVelocity(double velocity) {
		double ticks = velocity / DEFAULT_GRAVITY;
		double distance = -0.5 * DEFAULT_GRAVITY * ticks * ticks;
		return distance;
	}
}
