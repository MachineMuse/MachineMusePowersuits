package net.machinemuse.api.electricity;

import net.machinemuse.powersuits.common.ModCompatability;
import net.minecraft.item.ItemStack;
import thermalexpansion.api.item.IChargeableItem;

public class TEElectricAdapter extends ElectricAdapter {

	protected ItemStack stack;
	protected IChargeableItem item;

	public TEElectricAdapter(ItemStack stack) {
		this.stack = stack;
		this.item = (IChargeableItem) stack.getItem();
	}

	@Override
	public double getCurrentEnergy() {
		return museEnergyFromMJ(item.getEnergyStored(stack));
	}

	@Override
	public double getMaxEnergy() {
		return museEnergyFromMJ(item.getMaxEnergyStored(stack));
	}

	@Override
	public double drainEnergy(double requested) {
		float requestedMJ = (float) museEnergyToMJ(requested);
		float receivedMJ = item.transferEnergy(stack, requestedMJ, true);
		return museEnergyFromMJ(receivedMJ);
	}

	@Override
	public double giveEnergy(double provided) {
		float providedMJ = (float) museEnergyToMJ(provided);
		float takenMJ = item.receiveEnergy(stack, providedMJ, true);
		return museEnergyFromMJ(takenMJ);
	}

	public static double museEnergyToMJ(double museEnergy) {
		return museEnergy / ModCompatability.getBCRatio();
	}

	public static double museEnergyFromMJ(double mj) {
		return mj * ModCompatability.getBCRatio();
	}
}
