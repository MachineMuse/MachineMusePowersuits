package net.machinemuse.api.electricity;

import ic2.api.IElectricItem;
import net.minecraft.item.ItemStack;
import universalelectricity.core.item.IItemElectric;

public abstract class ElectricAdapter {
	public static ElectricAdapter wrap(ItemStack stack) {
		if (stack == null) {
			return null;
		}
		if (stack.getItem() instanceof IItemElectric) {
			return new UEElectricAdapter(stack);
		} else if (stack.getItem() instanceof IElectricItem) {
			return new IC2ElectricAdapter(stack);
			// } else if (stack.getItem() instanceof IChargeableItem) {
			// return new TEElectricAdapter(stack);
			// } else if (stack.getItem() instanceof IXynergyItem) {
			// return new XyElectricAdapter(stack);
		} else {
			return null;
		}
	}

	/**
	 * Call to get the energy of an item
	 * 
	 * @param stack
	 *            ItemStack to set
	 * @return Current energy level
	 */
	public abstract double getCurrentEnergy();

	/**
	 * Call to set the energy of an item
	 * 
	 * @param stack
	 *            ItemStack to set
	 * @return Maximum energy level
	 */
	public abstract double getMaxEnergy();

	/**
	 * Call to drain energy from an item
	 * 
	 * @param stack
	 *            ItemStack being requested for energy
	 * @param requested
	 *            Amount of energy to drain
	 * @return Amount of energy successfully drained
	 */
	public abstract double drainEnergy(double requested);

	/**
	 * Call to give energy to an item
	 * 
	 * @param stack
	 *            ItemStack being requested for energy
	 * @param provided
	 *            Amount of energy to drain
	 * @return Amount of energy used
	 */
	public abstract double giveEnergy(double provided);
}
