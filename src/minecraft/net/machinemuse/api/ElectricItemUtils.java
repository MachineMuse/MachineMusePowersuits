package net.machinemuse.api;

import icbm.api.explosion.IExplosive;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import universalelectricity.core.implement.IItemElectric;

public class ElectricItemUtils {

	public static final String MAXIMUM_ENERGY = "Maximum Energy";
	public static final String CURRENT_ENERGY = "Current Energy";

	public static double getPlayerEnergy(EntityPlayer player) {
		double avail = 0;
		for (ItemStack stack : electricItemsEquipped(player)) {
			avail += ((IItemElectric) stack.getItem()).getJoules(stack);
		}
		return avail;
	}

	private static List<ItemStack> electricItemsEquipped(EntityPlayer player) {
		List<ItemStack> electrics = new ArrayList(5);
		ItemStack[] equipped = MuseItemUtils.itemsEquipped(player);
		for (ItemStack stack : equipped) {
			if (stack != null && stack.getItem() instanceof IItemElectric) {
				electrics.add(stack);
			}
		}
		return electrics;
	}

	public static void drainPlayerEnergy(EntityPlayer player, double drainAmount) {
		for (ItemStack stack : electricItemsEquipped(player)) {
			if (stack != null) {
				IItemElectric item = (IItemElectric) (stack.getItem());
				double joules = item.getJoules(stack);
				if (joules > drainAmount) {
					item.onUse(drainAmount, stack);
					break;
				} else {
					drainAmount -= joules;
					item.onUse(joules, stack);
				}
			}
		}
	}

	public static double getMaxEnergy(EntityPlayer player) {
		double max = 0;
		for (ItemStack stack : electricItemsEquipped(player)) {
			max += ((IItemElectric) stack.getItem()).getMaxJoules(stack);
		}
		return max;
	}

	public static void givePlayerEnergy(EntityPlayer player, double joulesToGive) {
		for (ItemStack stack : electricItemsEquipped(player)) {
			if (stack != null) {
				IItemElectric item = (IItemElectric) (stack.getItem());
				double missingjoules = item.getMaxJoules(stack) - item.getJoules(stack);
				if (missingjoules > joulesToGive) {
					item.onUse(-joulesToGive, stack);
					break;
				} else {
					joulesToGive -= missingjoules;
					item.onUse(-missingjoules, stack);
				}
			}
		}
	}

	// ///////////////////////////// //
	// --- UNIVERSAL ELECTRICITY --- //
	// ///////////////////////////// //

	/**
	 * Provide energy to an item.
	 * 
	 * @param joulesNeeded
	 *            Amount to request (in UE Joules).
	 * @param itemStack
	 *            Itemstack to request the energy from.
	 * @return Amount of joules provided by the item.
	 */
	public static double charge(double amount, ItemStack itemStack) {
		double stored = getJoules(itemStack);
		double capacity = getMaxJoules(itemStack) - stored;
		double taken = Math.min(amount, capacity);
		double surplus = amount - taken;
		setJoules(stored + taken, itemStack);
		return surplus;
	}

	/**
	 * Request energy from this item.
	 * 
	 * @param joulesNeeded
	 *            Amount to request (in UE Joules).
	 * @param itemStack
	 *            Itemstack to request the energy from.
	 * @return Amount of joules provided by the item.
	 */
	public static double discharge(double joulesNeeded, ItemStack itemStack) {
		NBTTagCompound itemProperties = MuseItemUtils.getMuseItemTag(itemStack);

		double joulesAvail = getJoules(itemStack);
		double joulesProvided = Math.min(joulesAvail, joulesNeeded);

		setJoules(joulesAvail - joulesProvided, itemStack);
		return joulesProvided;
	}

	public static double getJoules(ItemStack stack) {
		double joules = MuseItemUtils.getDoubleOrZero(stack, CURRENT_ENERGY);
		double maxJoules = getMaxJoules(stack);
		if (joules > maxJoules) {
			joules = maxJoules;
			setJoules(joules, stack);
		}
		return joules;
	}

	public static void setJoules(double joules, ItemStack stack) {
		MuseItemUtils.setDoubleOrRemove(stack, CURRENT_ENERGY, joules);
	}

	public static double getMaxJoules(ItemStack stack) {
		double maxJoules = ModuleManager.computeModularProperty(stack, MAXIMUM_ENERGY);
		return maxJoules;
	}

	public static double getVoltage(ItemStack itemStack) {
		return 120;
	}

	public static void onEMP(ItemStack itemStack, Entity entity, IExplosive empExplosive) {
		setJoules(0, itemStack);
	}
}
