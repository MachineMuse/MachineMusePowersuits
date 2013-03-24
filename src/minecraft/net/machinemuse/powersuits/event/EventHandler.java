package net.machinemuse.powersuits.event;

import net.machinemuse.api.IModularItem;
import net.machinemuse.api.ModuleManager;
import net.machinemuse.api.MuseItemUtils;
import net.machinemuse.api.electricity.ElectricItemUtils;
import net.machinemuse.api.moduletrigger.IBlockBreakingModule;
import net.machinemuse.powersuits.item.ItemPowerGauntlet;
import net.machinemuse.powersuits.powermodule.misc.WaterElectrolyzerModule;
import net.minecraft.block.Block;
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
		if (stack != null && stack.getItem() instanceof ItemPowerGauntlet && ItemPowerGauntlet.canHarvestBlock(stack, block, 0, player)) {
			event.success = true;
		}
	}

	@ForgeSubscribe
	public void handleBreakSpeed(PlayerEvent.BreakSpeed event) {
		Block block = event.block;
		EntityPlayer player = event.entityPlayer;
		int meta = event.metadata;
		event.newSpeed = event.originalSpeed;
		ItemStack stack = player.getCurrentEquippedItem();
		if (stack != null && stack.getItem() instanceof IModularItem) {
			for (IBlockBreakingModule module : ModuleManager.getBlockBreakingModules()) {
				if (MuseItemUtils.itemHasActiveModule(stack, module.getName()) && module.canHarvestBlock(stack, block, meta, player)) {
					if (event.newSpeed == 0) {
						event.newSpeed = 1;
					}
					module.handleBreakSpeed(event);
				}
			}
		}

	}

	@ForgeSubscribe
	public void handleLivingUpdateEvent(LivingEvent.LivingUpdateEvent event) {
		if (event.entityLiving instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) event.entityLiving;
			ItemStack helmet = player.getCurrentArmor(3);
			if (helmet != null && helmet.getItem() instanceof IModularItem
					&& MuseItemUtils.itemHasActiveModule(helmet, WaterElectrolyzerModule.MODULE_WATER_ELECTROLYZER)) {
				double energy = ElectricItemUtils.getPlayerEnergy(player);
				double energyConsumption = ModuleManager.computeModularProperty(helmet, WaterElectrolyzerModule.WATERBREATHING_ENERGY_CONSUMPTION);
				if (energy > energyConsumption && player.getAir() < 10) {
					ElectricItemUtils.drainPlayerEnergy(player, energyConsumption);
					player.setAir(300);
				}
			}
		}
	}

}
