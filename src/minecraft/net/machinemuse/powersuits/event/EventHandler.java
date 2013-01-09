package net.machinemuse.powersuits.event;

import net.machinemuse.powersuits.item.ItemPowerArmor;
import net.machinemuse.powersuits.item.ItemPowerTool;
import net.machinemuse.powersuits.item.ItemUtils;
import net.machinemuse.powersuits.item.ModularCommon;
import net.machinemuse.powersuits.powermodule.ModuleManager;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.living.LivingEvent.LivingJumpEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;

public class EventHandler {
	@ForgeSubscribe
	public void handleHarvestCheck(PlayerEvent.HarvestCheck event) {
		EntityPlayer player = event.entityPlayer;
		Block block = event.block;
		ItemStack stack = player.inventory.getCurrentItem();
		if (stack != null && stack.getItem() instanceof ItemPowerTool && ItemPowerTool.canHarvestBlock(stack, block, 0)) {
			event.success = true;
		}
	}

	@ForgeSubscribe
	public void handleLivingJumpEvent(LivingJumpEvent event) {
		if (event.entityLiving instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) event.entityLiving;
			ItemStack stack = player.getCurrentArmor(1);
			if (stack != null && stack.getItem() instanceof ItemPowerArmor && ItemUtils.itemHasModule(stack, ModularCommon.MODULE_JUMP_ASSIST)) {
				double jumpAssist = ModuleManager.computeModularProperty(stack, ModularCommon.JUMP_MULTIPLIER);
				double drain = ModuleManager.computeModularProperty(stack, ModularCommon.JUMP_ENERGY_CONSUMPTION);
				double avail = ItemUtils.getPlayerEnergy(player);
				if (drain < avail) {
					ItemUtils.drainPlayerEnergy(player, drain);
					player.motionX *= jumpAssist;
					player.motionY *= jumpAssist;
					player.motionZ *= jumpAssist;
				}
			}

		}
	}
}
