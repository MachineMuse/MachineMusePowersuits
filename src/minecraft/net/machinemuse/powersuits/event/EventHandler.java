package net.machinemuse.powersuits.event;

import net.machinemuse.powersuits.common.Config;
import net.machinemuse.powersuits.item.ItemPowerArmor;
import net.machinemuse.powersuits.item.ItemPowerTool;
import net.machinemuse.powersuits.item.ItemUtils;
import net.machinemuse.powersuits.item.ModularCommon;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.living.LivingEvent.LivingJumpEvent;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;

public class EventHandler {
	@ForgeSubscribe public void handleHarvestCheck(PlayerEvent.HarvestCheck event) {
		EntityPlayer player = event.entityPlayer;
		Block block = event.block;
		ItemStack stack = player.inventory.getCurrentItem();
		if (stack.getItem() instanceof ItemPowerTool && ItemPowerTool.canHarvestBlock(stack, block, 0)) {
			event.success = true;
		}
	}
	
	@ForgeSubscribe public void handleLivingJumpEvent(LivingJumpEvent event) {
		if (event.entityLiving instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) event.entityLiving;
			ItemStack stack = player.getCurrentArmor(1);
			if (stack != null && stack.getItem() instanceof ItemPowerArmor && ItemUtils.itemHasModule(stack, ModularCommon.MODULE_JUMP_ASSIST)) {
				double jumpAssist = Config.computeModularProperty(stack, ModularCommon.JUMP_MULTIPLIER);
				double drain = Config.computeModularProperty(stack, ModularCommon.JUMP_ENERGY_CONSUMPTION);
				double avail = ItemUtils.getPlayerEnergy(player);
				if (drain < avail) {
					ItemUtils.drainPlayerEnergy(player, drain);
					player.motionY *= jumpAssist;
				}
			}
			
		}
	}
	@ForgeSubscribe public void handleFallEvent(LivingFallEvent event) {
		if (event.entityLiving instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) event.entityLiving;
			ItemStack boots = player.getCurrentArmor(0);
			if (boots != null) {
				if (ItemUtils.itemHasModule(boots, ModularCommon.MODULE_SHOCK_ABSORBER)) {
					double distanceAbsorb = event.distance * Config.computeModularProperty(boots, ModularCommon.SHOCK_ABSORB_MULTIPLIER);
					
					double drain = distanceAbsorb * distanceAbsorb * 0.05
							* Config.computeModularProperty(boots, ModularCommon.SHOCK_ABSORB_ENERGY_CONSUMPTION);
					double avail = ItemUtils.getPlayerEnergy(player);
					if (drain < avail) {
						ItemUtils.drainPlayerEnergy(player, drain);
						event.distance -= distanceAbsorb;
					}
				}
			}
			
		}
	}
}
