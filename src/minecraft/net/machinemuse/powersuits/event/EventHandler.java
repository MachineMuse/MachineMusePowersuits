package net.machinemuse.powersuits.event;

import net.machinemuse.powersuits.item.IModularItem;
import net.machinemuse.powersuits.item.ItemPowerTool;
import net.machinemuse.powersuits.item.ItemUtils;
import net.machinemuse.powersuits.item.ModularCommon;
import net.machinemuse.powersuits.powermodule.ModuleManager;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;

public class EventHandler {
	@ForgeSubscribe
	public void handleHarvestCheck(PlayerEvent.HarvestCheck event) {
		EntityPlayer player = event.entityPlayer;
		Block block = event.block;
		ItemStack stack = player.inventory.getCurrentItem();
		if (stack != null && stack.getItem() instanceof ItemPowerTool && ItemPowerTool.canHarvestBlock(stack, block, 0, player)) {
			event.success = true;
		}
	}

	@ForgeSubscribe
	public void handleBreakSpeed(PlayerEvent.BreakSpeed event) {
		Block block = event.block;
		EntityPlayer player = event.entityPlayer;
		double harvestSpeed = event.newSpeed;
		double energy = ItemUtils.getPlayerEnergy(player);
		int meta = event.metadata;
		ItemStack stack = player.getCurrentEquippedItem();
		if (stack != null && stack.getItem() instanceof ItemPowerTool) {
			if (ItemPowerTool.useIronPickaxe(stack, block, meta)
					&& energy > ModuleManager.computeModularProperty(stack, ModularCommon.PICKAXE_ENERGY_CONSUMPTION)) {

				harvestSpeed *= ModuleManager.computeModularProperty(stack, ModularCommon.PICKAXE_HARVEST_SPEED);

			} else if (ItemPowerTool.useIronShovel(stack, block, meta)
					&& energy > ModuleManager.computeModularProperty(stack, ModularCommon.SHOVEL_ENERGY_CONSUMPTION)) {

				harvestSpeed *= ModuleManager.computeModularProperty(stack, ModularCommon.SHOVEL_HARVEST_SPEED);

			} else if (ItemPowerTool.useIronAxe(stack, block, meta)
					&& energy > ModuleManager.computeModularProperty(stack, ModularCommon.AXE_ENERGY_CONSUMPTION)) {

				harvestSpeed *= ModuleManager.computeModularProperty(stack, ModularCommon.AXE_HARVEST_SPEED);

			} else if (ItemPowerTool.useDiamondPickaxe(stack, block, meta)
					&& energy > ModuleManager.computeModularProperty(stack, ModularCommon.PICKAXE_ENERGY_CONSUMPTION)) {

				harvestSpeed *= ModuleManager.computeModularProperty(stack, ModularCommon.PICKAXE_HARVEST_SPEED);

			} else {

				harvestSpeed = 1;

			}
			if (harvestSpeed > 1
					&& player.isInsideOfMaterial(Material.water)
					&& ItemUtils.itemHasActiveModule(stack, ModularCommon.MODULE_AQUA_AFFINITY)
					&& energy > ModuleManager.computeModularProperty(stack, ModularCommon.AQUA_AFFINITY_ENERGY_CONSUMPTION)) {
				harvestSpeed *= 5 * ModuleManager.computeModularProperty(stack, ModularCommon.UNDERWATER_HARVEST_SPEED);
			}
			event.newSpeed = (float) harvestSpeed;
		}

	}

	@ForgeSubscribe
	public void handleLivingUpdateEvent(LivingEvent.LivingUpdateEvent event) {
		if (event.entityLiving instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) event.entityLiving;
			ItemStack helmet = player.getCurrentArmor(3);
			if (helmet != null && helmet.getItem() instanceof IModularItem
					&& ItemUtils.itemHasActiveModule(helmet, ModularCommon.MODULE_WATER_ELECTROLYZER)) {
				double energy = ItemUtils.getPlayerEnergy(player);
				double energyConsumption = ModuleManager.computeModularProperty(helmet, ModularCommon.WATERBREATHING_ENERGY_CONSUMPTION);
				if (energy > energyConsumption && player.getAir() < 10) {
					ItemUtils.drainPlayerEnergy(player, energyConsumption);
					player.setAir(300);
				}
			}
			ItemStack legs = player.getCurrentArmor(1);
			if (legs != null && legs.getItem() instanceof IModularItem
					&& ItemUtils.itemHasActiveModule(legs,
							ModularCommon.MODULE_CLIMB_ASSIST)) {
				player.stepHeight = 1.01F;
			} else if (player.stepHeight == 1.01F) {
				player.stepHeight = 0F;
			}
		}
	}

}
