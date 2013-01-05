package net.machinemuse.powersuits.item;

import java.util.List;

import net.machinemuse.general.MuseStringUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import universalelectricity.core.electricity.ElectricInfo;

public abstract class ModularCommon {
	/**
	 * String literals as constants to eliminate case sensitivity issues etc.
	 */
	public static final String MAXIMUM_ENERGY = "Maximum Energy";
	public static final String CURRENT_ENERGY = "Current Energy";
	public static final String ARMOR_WEIGHT = "Armor Weight";
	public static final String ARMOR_DURABILITY = "Armor Durability";
	public static final String ARMOR_VALUE = "Armor Value";
	
	/**
	 * Tradeoffs for module properties
	 */
	public static final String TRADEOFF_ARMOR_THICKNESS = "Armor Thickness";
	public static final String TRADEOFF_BATTERY_SIZE = "Battery Size";
	
	/**
	 * Module names
	 */
	public static final String SHOVEL = "Shovel";
	public static final String AXE = "Axe";
	public static final String PICKAXE = "Pickaxe";
	public static final String BATTERY_BASIC = "Basic Battery";
	public static final String IRON_SHIELDING = "Iron Shielding";
	public static final String DIAMOND_SHIELDING = "Diamond Shielding";
	
	/**
	 * Categories for modules
	 */
	public static final String CATEGORY_ARMOR = "Armor";
	public static final String CATEGORY_ENERGY = "Energy";
	public static final String CATEGORY_TOOL = "Tool";
	public static final String CATEGORY_WEAPON = "Weapon";
	
	/**
	 * Adds information to the item's tooltip when 'getting' it.
	 * 
	 * @param stack
	 *            The itemstack to get the tooltip for
	 * @param player
	 *            The player (client) viewing the tooltip
	 * @param currentTipList
	 *            A list of strings containing the existing tooltip. When
	 *            passed, it will just contain the name of the item;
	 *            enchantments and lore are appended afterwards.
	 * @param advancedToolTips
	 *            Whether or not the player has 'advanced tooltips' turned on in
	 *            their settings.
	 */
	public static void addInformation(ItemStack stack,
			EntityPlayer player, List currentTipList, boolean advancedToolTips) {
		if (stack.getItem() instanceof ItemPowerTool) {
			String mode = ItemUtils.getStringOrNull(stack, "Tool Mode");
			if (mode != null) {
				currentTipList.add("Mode:" + MuseStringUtils.wrapFormatTags(mode, MuseStringUtils.FormatCodes.Red));
			}
		}
		String energyinfo = "Energy: " + MuseStringUtils.formatNumberShort(getJoules(stack)) + "/"
				+ MuseStringUtils.formatNumberShort(getMaxJoules(stack));
		currentTipList.add(
				MuseStringUtils.wrapMultipleFormatTags(energyinfo, MuseStringUtils.FormatCodes.Italic.character, MuseStringUtils.FormatCodes.Grey)
				);
	}
	
	// ///////////////////////////// //
	// --- UNIVERSAL ELECTRICITY --- //
	// ///////////////////////////// //
	public static double onReceive(double amps, double voltage, ItemStack itemStack) {
		double stored = getJoules(itemStack);
		double provided = ElectricInfo.getJoules(amps, voltage, 1);
		double capacity = getMaxJoules(itemStack) - stored;
		double taken = Math.min(provided, capacity);
		double surplus = provided - taken;
		setJoules(stored + taken, itemStack);
		return surplus;
	}
	
	public static double onUse(double joulesNeeded, ItemStack itemStack) {
		NBTTagCompound itemProperties = ItemUtils.getMuseItemTag(itemStack);
		
		double joulesAvail = getJoules(itemStack);
		double joulesProvided = Math.min(joulesAvail, joulesNeeded);
		
		setJoules(joulesAvail - joulesProvided, itemStack);
		return joulesProvided;
	}
	
	public static double getJoules(ItemStack stack) {
		return ItemUtils.getDoubleOrZero(stack, CURRENT_ENERGY);
	}
	
	public static void setJoules(double joules, ItemStack stack) {
		ItemUtils.setDoubleOrRemove(stack, CURRENT_ENERGY, joules);
	}
	
	public static double getMaxJoules(ItemStack stack) {
		double maxEnergy = 0;
		if (ItemUtils.itemHasModule(stack, BATTERY_BASIC)) {
			maxEnergy += 20000 + getBatterySize(stack, BATTERY_BASIC) * 180000;
		}
		return maxEnergy;
	}
	
	public static double getVoltage() {
		return 120;
	}
	// //////////////////////// //
	// --- OTHER PROPERTIES --- //
	// //////////////////////// //
	public static double getOrSetModuleProperty(ItemStack stack, String moduleName, String propertyName, double defaultValue) {
		NBTTagCompound batteryTag = ItemUtils.getMuseItemTag(stack).getCompoundTag(moduleName);
		if (!batteryTag.hasKey(propertyName)) {
			batteryTag.setDouble(propertyName, defaultValue);
		}
		return batteryTag.getDouble(propertyName);
	}
	
	public static double getBatterySize(ItemStack stack, String batteryType) {
		return getOrSetModuleProperty(stack, batteryType, TRADEOFF_BATTERY_SIZE, 0.1);
	}
	
	public static double getBatteryWeight(ItemStack stack) {
		NBTTagCompound itemTag = ItemUtils.getMuseItemTag(stack);
		double batteryWeight = 0;
		if (ItemUtils.tagHasModule(itemTag, BATTERY_BASIC)) {
			batteryWeight += 100 + getBatterySize(stack, BATTERY_BASIC) * 9900;
		}
		return batteryWeight;
	}
	
	public static double getTotalWeight(ItemStack stack) {
		double weight = 1000;
		weight += getBatteryWeight(stack);
		
		return weight;
	}
	
}
