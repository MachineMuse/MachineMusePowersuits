package net.machinemuse.powersuits.item;

import java.util.List;

import net.machinemuse.general.StringUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import universalelectricity.core.electricity.ElectricInfo;

public abstract class ModularItemCommon {
	/**
	 * String literals as constants to eliminate case sensitivity issues etc.
	 */
	public static final String MAXIMUM_ENERGY = "Maximum Energy";
	public static final String CURRENT_ENERGY = "Current Energy";
	public static final String BATTERY_WEIGHT = "Battery Weight";
	public static final String ARMOR_WEIGHT = "Armor Weight";
	public static final String ARMOR_DURABILITY = "Armor Durability";
	public static final String ARMOR_VALUE = "Armor Value";
	public static final String PASSIVE_SHIELDING = "Passive Shielding";

	public static void addInformation(ItemStack stack,
			EntityPlayer player, List currentTipList, boolean advancedToolTips) {
		String energyinfo = "Energy: "
				+ StringUtils.formatNumberShort(getJoules(stack)) + "/"
				+ StringUtils.formatNumberShort(getMaxJoules(stack));
		currentTipList.add(
				StringUtils.wrapMultipleFormatTags(energyinfo,
						StringUtils.FormatCodes.Italic.character,
						StringUtils.FormatCodes.DarkGrey));
	}

	// ///////////////////////////// //
	// --- UNIVERSAL ELECTRICITY --- //
	// ///////////////////////////// //
	public static double onReceive(double amps, double voltage,
			ItemStack itemStack) {
		double stored = getJoules(itemStack);
		double receivable = ElectricInfo.getJoules(amps, voltage, 1);
		double received = Math.min(receivable,
				getMaxJoules(itemStack) - stored);
		setJoules(stored + received, itemStack);
		return receivable - received;
	}

	public static double onUse(double joulesNeeded, ItemStack itemStack) {
		NBTTagCompound itemProperties = ItemUtils
				.getItemModularProperties(itemStack);

		double joulesAvail = getJoules(itemStack);
		double joulesProvided = Math.min(joulesAvail, joulesNeeded);

		setJoules(joulesAvail - joulesProvided, itemStack);
		return joulesProvided;
	}

	public static double getJoules(ItemStack stack) {
		return ItemUtils.getDoubleOrZero(stack,
				CURRENT_ENERGY);
	}

	public static void setJoules(double joules, ItemStack stack) {
		ItemUtils.setDoubleOrRemove(stack, ModularItemCommon.CURRENT_ENERGY,
				joules);
	}

	public static double getMaxJoules(ItemStack stack) {
		return ItemUtils.getDoubleOrZero(stack,
				ModularItemCommon.MAXIMUM_ENERGY);
	}

	public static double getVoltage() {
		return 120;
	}

}
