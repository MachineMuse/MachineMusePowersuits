package net.machinemuse.powersuits.item;

import net.machinemuse.api.ElectricItemUtils;
import net.machinemuse.powersuits.common.ModCompatability;
import net.minecraft.block.Block;
import net.minecraft.item.EnumToolMaterial;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import universalelectricity.core.electricity.ElectricityPack;
import universalelectricity.core.item.IItemElectric;

public class ItemElectricTool extends ItemTool

implements IItemElectric // Universal Electricity

// ICustomElectricItem, // Industrial Craft 2

// IEMPItem, // for ICBM EMP interfacing

// IChargeableItem // for Thermal Expansion
{

	protected ItemElectricTool(int par1, int par2, EnumToolMaterial par3EnumToolMaterial, Block[] par4ArrayOfBlock) {
		super(par1, par2, par3EnumToolMaterial, par4ArrayOfBlock);
	}

	// //////////// //
	// --- ICBM --- //
	// //////////// //
	// @Override
	// public void onEMP(ItemStack itemStack, Entity entity, IExplosive empExplosive) {
	// ElectricItemUtils.onEMP(itemStack, entity, empExplosive);
	// }

	// ///////////////////////// //
	// --- Thermal Expansion --- //
	// ///////////////////////// //

	/**
	 * Adds energy to an item. Returns the quantity of energy that was accepted. This should always return 0 if the item cannot be externally charged.
	 * 
	 * @param theItem
	 *            ItemStack to be charged.
	 * @param energy
	 *            Maximum amount of energy to be sent into the item.
	 * @param doReceive
	 *            If false, the charge will only be simulated.
	 * @return Amount of energy that was accepted by the item.
	 */
	public float receiveEnergy(ItemStack stack, float energy, boolean doReceive) {
		double offeredJoules = energy * ModCompatability.getBCRatio();
		double missingJoules = ElectricItemUtils.getMaxJoules(stack) - ElectricItemUtils.getJoules(stack);
		double transferredJoules = Math.min(offeredJoules, missingJoules);
		ElectricItemUtils.charge(transferredJoules, stack);
		return (float) (transferredJoules / ModCompatability.getBCRatio());
	}

	/**
	 * Removes energy from an item. Returns the quantity of energy that was removed. This should always return 0 if the item cannot be externally
	 * discharged.
	 * 
	 * @param theItem
	 *            ItemStack to be discharged.
	 * @param energy
	 *            Maximum amount of energy to be removed from the item.
	 * @param doTransfer
	 *            If false, the discharge will only be simulated.
	 * @return Amount of energy that was removed from the item.
	 */
	public float transferEnergy(ItemStack stack, float energy, boolean doTransfer) {
		double requestedJoules = energy * ModCompatability.getBCRatio();
		double availableJoules = ElectricItemUtils.getJoules(stack);
		double transferredJoules = Math.min(requestedJoules, availableJoules);
		ElectricItemUtils.discharge(transferredJoules, stack);
		return (float) (transferredJoules / ModCompatability.getBCRatio());
	}

	/**
	 * Get the amount of energy currently stored in the item.
	 */
	public float getEnergyStored(ItemStack stack) {
		return (float) (ModCompatability.getBCRatio() * ElectricItemUtils.getJoules(stack));
	}

	/**
	 * Get the max amount of energy that can be stored in the item.
	 */
	public float getMaxEnergyStored(ItemStack stack) {
		return (float) (ModCompatability.getBCRatio() * ElectricItemUtils.getMaxJoules(stack));
	}

	// //////////////////////////////////////// //
	// --- INDUSTRIAL CRAFT 2 COMPATABILITY --- //
	// //////////////////////////////////////// //
	public int charge(ItemStack stack, int amount, int tier, boolean ignoreTransferLimit, boolean simulate) {
		double joulesProvided = ModCompatability.joulesFromEU(amount);
		double currentJoules = ElectricItemUtils.getJoules(stack);
		double surplus = ElectricItemUtils.charge(joulesProvided, stack);
		if (simulate) {
			ElectricItemUtils.setJoules(currentJoules, stack);
		}
		return ModCompatability.joulesToEU(joulesProvided - surplus);
	}

	public int discharge(ItemStack stack, int amount, int tier, boolean ignoreTransferLimit, boolean simulate) {

		double joulesRequested = ModCompatability.joulesFromEU(amount);
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

	// /////////////////////////////////////////// //
	// --- UNIVERSAL ELECTRICITY COMPATABILITY --- //
	// /////////////////////////////////////////// //
	/**
	 * Called when this item receives electricity; being charged.
	 * 
	 * @return The amount of electricity that was added to the electric item.
	 */
	public ElectricityPack onReceive(ElectricityPack electricityPack, ItemStack itemStack) {
		return null;
	}

	/**
	 * Called when something requests electricity from this item; being decharged.
	 * 
	 * @return - The amount of electricity that was removed from the electric item.
	 */
	public ElectricityPack onProvide(ElectricityPack ep, ItemStack itemStack) {
		return null;
	}

	/**
	 * @return How much electricity does this item want to receive/take? This will affect the speed in which items get charged per tick.
	 */
	public ElectricityPack getReceiveRequest(ItemStack itemStack) {
		return new ElectricityPack(Math.sqrt(getMaxJoules(itemStack)), getVoltage(itemStack));
	}

	/**
	 * 
	 * @return How much electricity does this item want to provide/give out? This will affect the speed in which items get decharged per tick.
	 */
	public ElectricityPack getProvideRequest(ItemStack itemStack) {
		return new ElectricityPack(Math.sqrt(getMaxJoules(itemStack)), getVoltage(itemStack));
	}

	@Override
	public double getJoules(ItemStack itemStack) {
		return ElectricItemUtils.getJoules(itemStack);
	}

	@Override
	public void setJoules(double joules, ItemStack itemStack) {
		ElectricItemUtils.setJoules(joules, itemStack);
	}

	@Override
	public double getMaxJoules(ItemStack itemStack) {
		return ElectricItemUtils.getMaxJoules(itemStack);
	}

	@Override
	public double getVoltage(ItemStack itemStack) {
		return ElectricItemUtils.getVoltage(itemStack);
	}

	public void setJoules(double joules, Object... data) {
	}

	// public boolean canReceiveElectricity() {
	// return true;
	// }
	//
	// public boolean canProduceElectricity() {
	// return true;
	// }

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

}
