package net.machinemuse.powersuits.item;

import ic2.api.ICustomElectricItem;
import net.machinemuse.api.ModuleManager;
import net.machinemuse.api.MuseItemUtils;
import net.machinemuse.api.electricity.ElectricItemUtils;
import net.machinemuse.api.electricity.IC2ElectricAdapter;
import net.machinemuse.api.electricity.UEElectricAdapter;
import net.minecraft.item.EnumArmorMaterial;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import universalelectricity.core.electricity.ElectricityPack;
import universalelectricity.core.item.IItemElectric;

public class ItemElectricArmor extends ItemArmor
		implements
		IItemElectric,
		ICustomElectricItem
/**
 * implements IItemElectric, // Universal Electricity ICustomElectricItem, // Industrial Craft 2 IEMPItem, // for ICBM EMP interfacing
 * IAntiPoisonArmor, // for atomic science hazmat suits IChargeableItem // for Thermal Expansion
 **/

{
	public ItemElectricArmor(int par1, EnumArmorMaterial par2EnumArmorMaterial, int par3, int par4) {
		super(par1, par2EnumArmorMaterial, par3, par4);
	}

	/**
	 * Call to get the energy of an item
	 * 
	 * @param stack
	 *            ItemStack to set
	 * @return Current energy level
	 */
	public double getCurrentEnergy(ItemStack stack) {
		return MuseItemUtils.getDoubleOrZero(stack, ElectricItemUtils.CURRENT_ENERGY);
	}

	/**
	 * Call to set the energy of an item
	 * 
	 * @param stack
	 *            ItemStack to set
	 * @return Maximum energy level
	 */
	public double getMaxEnergy(ItemStack stack) {
		return ModuleManager.computeModularProperty(stack, ElectricItemUtils.MAXIMUM_ENERGY);
	}

	/**
	 * Call to set the energy of an item
	 * 
	 * @param stack
	 *            ItemStack to set
	 * @param energy
	 *            Level to set it to
	 */
	public void setCurrentEnergy(ItemStack stack, double energy) {
		MuseItemUtils.setDoubleOrRemove(stack, ElectricItemUtils.CURRENT_ENERGY, energy);
	}

	/**
	 * Call to drain energy from an item
	 * 
	 * @param stack
	 *            ItemStack being requested for energy
	 * @param requested
	 *            Amount of energy to drain
	 * @return Amount of energy successfully drained
	 */
	public double drainEnergyFrom(ItemStack stack, double requested) {
		double available = getCurrentEnergy(stack);
		if (available > requested) {
			setCurrentEnergy(stack, available - requested);
			return requested;
		} else {
			setCurrentEnergy(stack, 0);
			return available;
		}
	}

	/**
	 * Call to give energy to an item
	 * 
	 * @param stack
	 *            ItemStack being requested for energy
	 * @param provided
	 *            Amount of energy to drain
	 * @return Amount of energy used
	 */
	public double giveEnergyTo(ItemStack stack, double provided) {
		double available = getCurrentEnergy(stack);
		double max = getMaxEnergy(stack);
		if (available + provided < max) {
			setCurrentEnergy(stack, available + provided);
			return provided;
		} else {
			setCurrentEnergy(stack, max);
			return max - available;
		}
	}

	@Override
	public double getJoules(ItemStack itemStack) {
		return UEElectricAdapter.museEnergyToJoules(getCurrentEnergy(itemStack));
	}

	@Override
	public void setJoules(double joules, ItemStack itemStack) {
		setCurrentEnergy(itemStack, UEElectricAdapter.museEnergyFromJoules(joules));
	}

	@Override
	public double getMaxJoules(ItemStack itemStack) {
		return UEElectricAdapter.museEnergyToJoules(getMaxEnergy(itemStack));
	}

	@Override
	public double getVoltage(ItemStack itemStack) {
		return 120;
	}

	@Override
	public ElectricityPack onReceive(ElectricityPack electricityPack, ItemStack itemStack) {
		double energyReceiving = UEElectricAdapter.museEnergyFromElectricityPack(electricityPack);
		double energyConsumed = giveEnergyTo(itemStack, energyReceiving);
		ElectricityPack packConsumed = UEElectricAdapter.museEnergyToElectricityPack(energyConsumed, getVoltage(itemStack));
		return packConsumed;
	}

	@Override
	public ElectricityPack onProvide(ElectricityPack electricityPack, ItemStack itemStack) {
		double energyRequested = UEElectricAdapter.museEnergyFromElectricityPack(electricityPack);
		double energyGiven = drainEnergyFrom(itemStack, energyRequested);
		ElectricityPack packGiven = UEElectricAdapter.museEnergyToElectricityPack(energyGiven, getVoltage(itemStack));
		return packGiven;
	}

	@Override
	public ElectricityPack getReceiveRequest(ItemStack itemStack) {
		return UEElectricAdapter.museEnergyToElectricityPack(getMaxEnergy(itemStack) - getCurrentEnergy(itemStack), getVoltage(itemStack));
	}

	@Override
	public ElectricityPack getProvideRequest(ItemStack itemStack) {
		return UEElectricAdapter.museEnergyToElectricityPack(getMaxEnergy(itemStack) - getCurrentEnergy(itemStack), getVoltage(itemStack));
	}

	@Override
	public boolean canProvideEnergy(ItemStack itemStack) {
		return true;
	}

	@Override
	public int getChargedItemId(ItemStack itemStack) {
		return itemStack.itemID;
	}

	@Override
	public int getEmptyItemId(ItemStack itemStack) {
		return itemStack.itemID;
	}

	@Override
	public int getMaxCharge(ItemStack itemStack) {
		return (int) IC2ElectricAdapter.museEnergyToEU(getCurrentEnergy(itemStack));
	}

	@Override
	public int getTier(ItemStack itemStack) {
		return IC2ElectricAdapter.getTier(itemStack);
	}

	@Override
	public int getTransferLimit(ItemStack itemStack) {
		return (int) IC2ElectricAdapter.museEnergyToEU(Math.sqrt(getMaxEnergy(itemStack)));
	}

	@Override
	public int charge(ItemStack itemStack, int amount, int tier, boolean ignoreTransferLimit, boolean simulate) {
		double current = getCurrentEnergy(itemStack);
		double given = giveEnergyTo(itemStack, IC2ElectricAdapter.museEnergyFromEU(amount));
		if (simulate) {
			setCurrentEnergy(itemStack, current);
		}
		return (int) IC2ElectricAdapter.museEnergyToEU(given);
	}

	@Override
	public int discharge(ItemStack itemStack, int amount, int tier, boolean ignoreTransferLimit, boolean simulate) {
		double current = getCurrentEnergy(itemStack);
		double taken = drainEnergyFrom(itemStack, IC2ElectricAdapter.museEnergyFromEU(amount));
		if (simulate) {
			setCurrentEnergy(itemStack, current);
		}
		return (int) IC2ElectricAdapter.museEnergyToEU(taken);
	}

	@Override
	public boolean canUse(ItemStack itemStack, int amount) {
		double requested = IC2ElectricAdapter.museEnergyFromEU(amount);
		return requested < getCurrentEnergy(itemStack);
	}

	@Override
	public boolean canShowChargeToolTip(ItemStack itemStack) {
		return false;
	}

}
