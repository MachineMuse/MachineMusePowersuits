package net.machinemuse.powersuits.item;

import net.machinemuse.api.ElectricItemUtils;
import net.machinemuse.powersuits.common.ModCompatability;
import net.minecraft.item.EnumArmorMaterial;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;

public class ItemElectricArmor extends ItemArmor
/**
 * implements IItemElectric, // Universal Electricity ICustomElectricItem, // Industrial Craft 2 IEMPItem, // for ICBM EMP interfacing
 * IAntiPoisonArmor, // for atomic science hazmat suits IChargeableItem // for Thermal Expansion
 **/

{
	public ItemElectricArmor(int par1, EnumArmorMaterial par2EnumArmorMaterial, int par3, int par4) {
		super(par1, par2EnumArmorMaterial, par3, par4);
	}

	// //////////////////////////////////////////////
	// --- UNIVERSAL ELECTRICITY COMPATABILITY ---//
	// //////////////////////////////////////////////
	public double onReceive(double amps, double voltage, ItemStack itemStack) {
		double amount = 1;// ElectricInfo.getJoules(amps, voltage, 1);
		return ElectricItemUtils.charge(amount, itemStack);
	}

	public double onUse(double joulesNeeded, ItemStack itemStack) {
		return ElectricItemUtils.discharge(joulesNeeded, itemStack);
	}

	public double getJoules(Object... data) {
		return ElectricItemUtils.getJoules(getAsStack(data));
	}

	public void setJoules(double joules, Object... data) {
		ElectricItemUtils.setJoules(joules, getAsStack(data));
	}

	public double getMaxJoules(Object... data) {
		return ElectricItemUtils.getMaxJoules(getAsStack(data));
	}

	public double getVoltage(Object... data) {
		return ElectricItemUtils.getVoltage(getAsStack(data));
	}

	public boolean canReceiveElectricity() {
		return true;
	}

	public boolean canProduceElectricity() {
		return true;
	}

	/**
	 * Helper function to deal with UE's use of varargs
	 */
	private ItemStack getAsStack(Object[] data) {
		if (data[0] instanceof ItemStack) {
			return (ItemStack) data[0];
		} else {
			throw new IllegalArgumentException("MusePowerSuits: Invalid ItemStack passed via UE interface");
		}
	}

	// //////////////////////////////////////// //
	// --- INDUSTRIAL CRAFT 2 COMPATABILITY --- //
	// //////////////////////////////////////// //
	public int charge(ItemStack stack, int amount, int tier, boolean ignoreTransferLimit, boolean simulate) {
		double joulesProvided = ModCompatability.joulesFromEU(amount);
		double maxJoules = ElectricItemUtils.getMaxJoules(stack);
		if (!ignoreTransferLimit && (joulesProvided > maxJoules / 200.0)) {
			joulesProvided = maxJoules / 200.0;
		}
		double currentJoules = ElectricItemUtils.getJoules(stack);
		double surplus = ElectricItemUtils.charge(joulesProvided, stack);
		if (simulate) {
			ElectricItemUtils.setJoules(currentJoules, stack);
		}

		return ModCompatability.joulesToEU(joulesProvided - surplus);
	}

	public int discharge(ItemStack stack, int amount, int tier, boolean ignoreTransferLimit, boolean simulate) {
		double joulesRequested = ModCompatability.joulesFromEU(amount);
		double maxJoules = ElectricItemUtils.getMaxJoules(stack);
		if (!ignoreTransferLimit && (joulesRequested > maxJoules / 200.0)) {
			joulesRequested = maxJoules / 200.0;
		}
		double currentJoules = ElectricItemUtils.getJoules(stack);
		double givenJoules = ElectricItemUtils.discharge(joulesRequested, stack);
		if (simulate) {
			ElectricItemUtils.setJoules(currentJoules, stack);
		}
		return ModCompatability.joulesToEU(givenJoules);
	}

	public boolean canUse(ItemStack stack, int amount) {
		double joulesRequested = ModCompatability.joulesFromEU(amount);
		double currentJoules = ElectricItemUtils.getJoules(stack);
		if (currentJoules > joulesRequested) {
			return true;
		} else {
			return false;
		}
	}

	public boolean canShowChargeToolTip(ItemStack itemStack) {
		return false;
	}

	public boolean canProvideEnergy() {
		return true;
	}

	public int getChargedItemId() {
		return this.itemID;
	}

	public int getEmptyItemId() {
		return this.itemID;
	}

	public int getMaxCharge() {
		return 1;
	}

	public int getTier() {
		return 1;
	}

	public int getTransferLimit() {
		return 0;
	}

	// //////////// //
	// --- ICBM --- //
	// //////////// //
	// public void onEMP(ItemStack itemStack, Entity entity, IExplosive empExplosive) {
	// ElectricItemUtils.onEMP(itemStack, entity, empExplosive);
	// }

	// ////////////////////// //
	// --- Atomic Science --- //
	// ////////////////////// //
	// public boolean isProtectedFromPoison(ItemStack itemStack, EntityLiving entityLiving, Poison type) {
	// return MuseItemUtils.itemHasModule(itemStack, MuseCommonStrings.MODULE_HAZMAT);
	// }

	// public void onProtectFromPoison(ItemStack itemStack, EntityLiving entityLiving, Poison type) {
	// }

	// ///////////////////////// //
	// --- Thermal Expansion --- //
	// ///////////////////////// //
	public float receiveEnergy(ItemStack theItem, float energy, boolean doReceive) {
		// TODO Auto-generated method stub
		return 0;
	}

	public float transferEnergy(ItemStack theItem, float energy, boolean doTransfer) {
		// TODO Auto-generated method stub
		return 0;
	}

	public float getEnergyStored(ItemStack theItem) {
		// TODO Auto-generated method stub
		return 0;
	}

	public float getMaxEnergyStored(ItemStack theItem) {
		// TODO Auto-generated method stub
		return 0;
	}
}
