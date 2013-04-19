package net.machinemuse.powersuits.powermodule.misc;

import java.util.List;

import net.machinemuse.api.*;
import net.machinemuse.api.electricity.ElectricItemUtils;
import net.machinemuse.api.moduletrigger.IPlayerTickModule;
import net.machinemuse.powersuits.item.ItemComponent;
import net.machinemuse.powersuits.powermodule.PowerModuleBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class CoolingSystemModule extends PowerModuleBase implements IPlayerTickModule {
	public static final String COOLING_BONUS = "Cooling Bonus";
	public static final String ENERGY = "Cooling System Energy Consumption";

	public CoolingSystemModule(List<IModularItem> validItems) {
		super(validItems);
		addInstallCost(new ItemStack(Item.eyeOfEnder, 0, 4));
		addInstallCost(MuseItemUtils.copyAndResize(ItemComponent.controlCircuit, 1));
		addTradeoffProperty("Power", COOLING_BONUS, 10, "%");
		addTradeoffProperty("Power", ENERGY, 10, "J/t");
	}

	@Override
	public String getCategory() {
		return MuseCommonStrings.CATEGORY_ENVIRONMENTAL;
	}

	@Override
	public String getName() {
		return "Cooling System";
	}

	@Override
	public String getDescription() {
		return "Cools down heat-producing modules quicker.";
	}

	@Override
	public String getTextureFile() {
		return "coolingsystem";
	}

	@Override
	public void onPlayerTickActive(EntityPlayer player, ItemStack item) {
		double heatBefore = MuseHeatUtils.getPlayerHeat(player);
		MuseHeatUtils.coolPlayer(player, 0.1 * ModuleManager.computeModularProperty(item, COOLING_BONUS));
		double cooling = heatBefore - MuseHeatUtils.getPlayerHeat(player);
		ElectricItemUtils.drainPlayerEnergy(player, cooling * ModuleManager.computeModularProperty(item, ENERGY));
	}

	@Override
	public void onPlayerTickInactive(EntityPlayer player, ItemStack item) {
		// TODO Auto-generated method stub

	}

}
