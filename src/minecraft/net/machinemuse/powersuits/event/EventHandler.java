package net.machinemuse.powersuits.event;

import net.machinemuse.api.ElectricItemUtils;
import net.machinemuse.api.IModularItem;
import net.machinemuse.api.ModuleManager;
import net.machinemuse.api.MuseCommonStrings;
import net.machinemuse.api.MuseItemUtils;
import net.machinemuse.powersuits.item.ItemPowerTool;
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
		double energy = ElectricItemUtils.getPlayerEnergy(player);
		int meta = event.metadata;
		ItemStack stack = player.getCurrentEquippedItem();
		if (stack != null && stack.getItem() instanceof ItemPowerTool) {
			if (ItemPowerTool.useIronPickaxe(stack, block, meta)
					&& energy > ModuleManager.computeModularProperty(stack, MuseCommonStrings.PICKAXE_ENERGY_CONSUMPTION)) {

				harvestSpeed *= ModuleManager.computeModularProperty(stack, MuseCommonStrings.PICKAXE_HARVEST_SPEED);

			} else if (ItemPowerTool.useIronShovel(stack, block, meta)
					&& energy > ModuleManager.computeModularProperty(stack, MuseCommonStrings.SHOVEL_ENERGY_CONSUMPTION)) {

				harvestSpeed *= ModuleManager.computeModularProperty(stack, MuseCommonStrings.SHOVEL_HARVEST_SPEED);

			} else if (ItemPowerTool.useIronAxe(stack, block, meta)
					&& energy > ModuleManager.computeModularProperty(stack, MuseCommonStrings.AXE_ENERGY_CONSUMPTION)) {

				harvestSpeed *= ModuleManager.computeModularProperty(stack, MuseCommonStrings.AXE_HARVEST_SPEED);

			} else if (ItemPowerTool.useDiamondPickaxe(stack, block, meta)
					&& energy > ModuleManager.computeModularProperty(stack, MuseCommonStrings.PICKAXE_ENERGY_CONSUMPTION)) {

				harvestSpeed *= ModuleManager.computeModularProperty(stack, MuseCommonStrings.PICKAXE_HARVEST_SPEED);

			} else {

				harvestSpeed = 1;

			}
			if (harvestSpeed > 1 && player.isInsideOfMaterial(Material.water)
					&& MuseItemUtils.itemHasActiveModule(stack, MuseCommonStrings.MODULE_AQUA_AFFINITY)
					&& energy > ModuleManager.computeModularProperty(stack, MuseCommonStrings.AQUA_AFFINITY_ENERGY_CONSUMPTION)) {
				harvestSpeed *= 5 * ModuleManager.computeModularProperty(stack, MuseCommonStrings.UNDERWATER_HARVEST_SPEED);
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
					&& MuseItemUtils.itemHasActiveModule(helmet, MuseCommonStrings.MODULE_WATER_ELECTROLYZER)) {
				double energy = ElectricItemUtils.getPlayerEnergy(player);
				double energyConsumption = ModuleManager.computeModularProperty(helmet, MuseCommonStrings.WATERBREATHING_ENERGY_CONSUMPTION);
				if (energy > energyConsumption && player.getAir() < 10) {
					ElectricItemUtils.drainPlayerEnergy(player, energyConsumption);
					player.setAir(300);
				}
			}
		}
	}

}
