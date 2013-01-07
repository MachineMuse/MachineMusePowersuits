package net.machinemuse.powersuits.item;

import java.util.List;

import net.machinemuse.general.MuseStringUtils;
import net.machinemuse.powersuits.common.Config;
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
	public static final String ARMOR_VALUE_PHYSICAL = "Armor (Physical)";
	public static final String ARMOR_VALUE_ENERGY = "Armor (Energy)";
	public static final String ARMOR_ENERGY_CONSUMPTION = "Energy Per Damage";
	public static final String WEIGHT = "Weight";
	public static final String SHOVEL_HARVEST_SPEED = "Shovel Harvest Speed";
	public static final String AXE_HARVEST_SPEED = "Axe Harvest Speed";
	public static final String PICKAXE_HARVEST_SPEED = "Pickaxe Harvest Speed";
	public static final String SHOVEL_ENERGY_CONSUMPTION = "Shovel Energy Consumption";
	public static final String AXE_ENERGY_CONSUMPTION = "Axe Energy Consumption";
	public static final String PICKAXE_ENERGY_CONSUMPTION = "Pickaxe Energy Consumption";
	public static final String BATTERY_WEIGHT = "Battery Weight";
	public static final String SPRINT_ENERGY_CONSUMPTION = "Sprint Energy Consumption";
	public static final String SPRINT_SPEED_MULTIPLIER = "Sprint Speed Multiplier";
	public static final String SPRINT_FOOD_COMPENSATION = "Exhaustion Compensation";
	public static final String JUMP_ENERGY_CONSUMPTION = "Jump Energy Consumption";
	public static final String JUMP_MULTIPLIER = "Jump Multiplier";
	public static final String SHOCK_ABSORB_MULTIPLIER = "Distance Reduction";
	public static final String SHOCK_ABSORB_ENERGY_CONSUMPTION = "Energy consumption";
	
	/**
	 * Module names
	 */
	public static final String MODULE_SHOVEL = "Shovel";
	public static final String MODULE_AXE = "Axe";
	public static final String MODULE_PICKAXE = "Pickaxe";
	public static final String MODULE_BATTERY_BASIC = "Basic Battery";
	public static final String MODULE_IRON_PLATING = "Iron Plating";
	public static final String MODULE_DIAMOND_PLATING = "Diamond Plating";
	public static final String MODULE_ENERGY_SHIELD = "Energy Shield";
	public static final String MODULE_DIAMOND_PICK_UPGRADE = "Diamond Drill Upgrade";
	public static final String MODULE_SPRINT_ASSIST = "Sprint Assist";
	public static final String MODULE_JUMP_ASSIST = "Jump Assist";
	public static final String MODULE_SHOCK_ABSORBER = "Shock Absorber";
	
	/**
	 * Categories for modules
	 */
	public static final String CATEGORY_ARMOR = "Armor";
	public static final String CATEGORY_ENERGY = "Energy";
	public static final String CATEGORY_TOOL = "Tool";
	public static final String CATEGORY_WEAPON = "Weapon";
	public static final String CATEGORY_MOVEMENT = "Movement";
	
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
		double maxJoules = Config.computeModularProperty(stack, ModularCommon.MAXIMUM_ENERGY);
		if (getJoules(stack) > maxJoules) {
			setJoules(maxJoules, stack);
		}
		return maxJoules;
	}
	
	public static double getVoltage() {
		return 120;
	}
	// //////////////////////// //
	// --- OTHER PROPERTIES --- //
	// //////////////////////// //
	public static double getOrSetModuleProperty(NBTTagCompound moduleTag, String propertyName, double defaultValue) {
		if (!moduleTag.hasKey(propertyName)) {
			moduleTag.setDouble(propertyName, defaultValue);
		}
		return moduleTag.getDouble(propertyName);
	}
	public static double getTotalWeight(ItemStack stack) {
		return Config.computeModularProperty(stack, ModularCommon.WEIGHT);
	}
}
