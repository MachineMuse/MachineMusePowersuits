package net.machinemuse.powersuits.event;

import net.machinemuse.powersuits.item.ItemUtils;
import net.machinemuse.powersuits.item.ModularCommon;
import net.machinemuse.powersuits.powermodule.ModuleManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.living.LivingFallEvent;

public class FallManager {
	@ForgeSubscribe
	public void handleFallEvent(LivingFallEvent event) {
		event.distance = (float) computeFallHeightFromVelocity(event.entity.motionY);
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
	public static double DEFAULT_GRAVITY = -0.0784000015258789;

	public static double computeFallHeightFromVelocity(double velocity) {
		double ticks = velocity / DEFAULT_GRAVITY;
		double distance = 0.5 * velocity * ticks * ticks;
		return distance;
	}
}
