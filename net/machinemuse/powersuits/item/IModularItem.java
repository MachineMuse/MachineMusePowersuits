package net.machinemuse.powersuits.item;

import java.util.List;

import net.machinemuse.powersuits.common.Config;

/**
 * Interface for ItemPowerArmor and ItemPowerTool to share.
 * 
 * @author MachineMuse
 */
public interface IModularItem {

	public static final String MAXIMUM_ENERGY = "Maximum Energy";
	public static final String CURRENT_ENERGY = "Current Energy";
	public static final String BATTERY_WEIGHT = "Battery Weight";
	public static final String ARMOR_WEIGHT = "Armor Weight";
	public static final String ARMOR_DURABILITY = "Armor Durability";
	public static final String ARMOR_VALUE = "Armor Value";
	public static final String PASSIVE_SHIELDING = "Passive Shielding";

	public Config.Items getItemType();

	public List<String> getValidProperties();

}
