package net.machinemuse.api.electricity;

import ic2.api.ElectricItem;
import ic2.api.IElectricItem;
import net.machinemuse.api.ModuleManager;
import net.machinemuse.powersuits.common.ModCompatability;
import net.minecraft.item.ItemStack;

public class IC2ElectricAdapter extends ElectricAdapter {

	public static final String IC2_TIER = "IC2 Tier";
	protected ItemStack stack;
	protected IElectricItem item;

	public IC2ElectricAdapter(ItemStack stack) {
		this.stack = stack;
		this.item = (IElectricItem) stack.getItem();
	}

	@Override
	public double getCurrentEnergy() {
		return museEnergyFromEU(ElectricItem.discharge(stack, Integer.MAX_VALUE, getTier(), true, true));
	}

	@Override
	public double getMaxEnergy() {
		return museEnergyFromEU(item.getMaxCharge(stack));
	}

	@Override
	public double drainEnergy(double requested) {
		double requestedEU = museEnergyToEU(requested);
		double drainedEU = ElectricItem.discharge(stack, (int) requestedEU, getTier(), true, false);
		return museEnergyFromEU(drainedEU);
	}

	@Override
	public double giveEnergy(double provided) {
		double providedEU = museEnergyToEU(provided);
		double givenEU = ElectricItem.charge(stack, (int) providedEU, getTier(), true, false);
		return museEnergyFromEU(givenEU);
	}

	public int getTier() {
		return getTier(stack);
	}

	public static int getTier(ItemStack stack) {
		double supposedTier = ModuleManager.computeModularProperty(stack, IC2_TIER);
		return (int) supposedTier;
	}

	public static double museEnergyToEU(double museEnergy) {
		return museEnergy / ModCompatability.getIC2Ratio();
	}

	public static double museEnergyFromEU(double eu) {
		return eu * ModCompatability.getIC2Ratio();
	}

}
