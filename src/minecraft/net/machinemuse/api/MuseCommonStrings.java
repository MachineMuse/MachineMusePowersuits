package net.machinemuse.api;

import java.util.LinkedList;
import java.util.List;

import net.machinemuse.api.electricity.ElectricAdapter;
import net.machinemuse.general.MuseStringUtils;
import net.machinemuse.powersuits.common.Config;
import net.machinemuse.powersuits.item.ItemPowerGauntlet;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public abstract class MuseCommonStrings {
	/**
	 * String literals as constants to eliminate case sensitivity issues etc.
	 */
	public static final String ARMOR_VALUE_PHYSICAL = "Armor (Physical)";
	public static final String ARMOR_VALUE_ENERGY = "Armor (Energy)";
	public static final String ARMOR_ENERGY_CONSUMPTION = "Energy Per Damage";
	public static final String WEIGHT = "Weight";

	/**
	 * Module names
	 */
	public static final String MODULE_ANTIGRAVITY = "Antigravity Drive";
	public static final String MODULE_HAZMAT = "Radiation Shielding";

	/**
	 * Categories for modules
	 */
	public static final String CATEGORY_ARMOR = "Armor";
	public static final String CATEGORY_ENERGY = "Energy";
	public static final String CATEGORY_TOOL = "Tool";
	public static final String CATEGORY_WEAPON = "Weapon";
	public static final String CATEGORY_MOVEMENT = "Movement";
	public static final String CATEGORY_COSMETIC = "Cosmetic";
	public static final String CATEGORY_VISION = "Vision";
	public static final String CATEGORY_ENVIRONMENTAL = "Environment";
	public static final String CATEGORY_SPECIAL = "Special";

	/**
	 * Sounds
	 */
	private static final String SOUND_RESOURCE_LOCATION = "resources/machinemuse/sound/";
	private static final String SOUND_PREFIX = "resources.machinemuse.sound.";

	public static String[] soundFiles = { SOUND_RESOURCE_LOCATION + "Glider.ogg", SOUND_RESOURCE_LOCATION + "GUIInstall.ogg",
			SOUND_RESOURCE_LOCATION + "GUISelect.ogg", SOUND_RESOURCE_LOCATION + "JetBoots.ogg", SOUND_RESOURCE_LOCATION + "Jetpack.ogg",
			SOUND_RESOURCE_LOCATION + "JumpAssist.ogg", SOUND_RESOURCE_LOCATION + "SwimAssist.ogg",
			SOUND_RESOURCE_LOCATION + "WaterElectrolyzer.ogg", };
	public static final String SOUND_GLIDER = SOUND_PREFIX + "Glider";
	public static final String SOUND_GUI_INSTALL = SOUND_PREFIX + "GUIInstall";
	public static final String SOUND_GUI_SELECT = SOUND_PREFIX + "GUISelect";
	public static final String SOUND_JET_BOOTS = SOUND_PREFIX + "JetBoots";
	public static final String SOUND_JETPACK = SOUND_PREFIX + "Jetpack";
	public static final String SOUND_JUMP_ASSIST = SOUND_PREFIX + "JumpAssist";
	public static final String SOUND_SWIM_ASSIST = SOUND_PREFIX + "SwimAssist";
	public static final String SOUND_WATER_ELECTROLYZER = SOUND_PREFIX + "WaterElectrolyzer";

	/**
	 * Adds information to the item's tooltip when 'getting' it.
	 * 
	 * @param stack
	 *            The itemstack to get the tooltip for
	 * @param player
	 *            The player (client) viewing the tooltip
	 * @param currentTipList
	 *            A list of strings containing the existing tooltip. When passed, it will just contain the name of the item; enchantments and lore are
	 *            appended afterwards.
	 * @param advancedToolTips
	 *            Whether or not the player has 'advanced tooltips' turned on in their settings.
	 */
	public static void addInformation(ItemStack stack, EntityPlayer player, List currentTipList, boolean advancedToolTips) {
		if (stack.getItem() instanceof ItemPowerGauntlet) {
			String mode = MuseItemUtils.getStringOrNull(stack, "Mode");
			if (mode != null) {
				currentTipList.add("Mode:" + MuseStringUtils.wrapFormatTags(mode, MuseStringUtils.FormatCodes.Red));
			} else {
				currentTipList.add("Change modes: Sneak+mousewheel.");
			}
		}
		ElectricAdapter adapter = ElectricAdapter.wrap(stack);
		if (adapter != null) {
			String energyinfo = "Energy: " + MuseStringUtils.formatNumberShort(adapter.getCurrentEnergy()) + '/'
					+ MuseStringUtils.formatNumberShort(adapter.getMaxEnergy());
			currentTipList.add(MuseStringUtils.wrapMultipleFormatTags(energyinfo, MuseStringUtils.FormatCodes.Italic.character,
					MuseStringUtils.FormatCodes.Grey));
		}
		// try {
		// if (ModCompatability.isAndrewAddonsLoaded() && stack.getItem() instanceof ItemPowerArmorHead && MuseItemUtils.getFoodLevel(stack) > 0
		// && MuseItemUtils.itemHasModule(stack, "Auto-Feeder")) {
		// currentTipList.add(MuseStringUtils.wrapMultipleFormatTags("Food level: " + MuseItemUtils.getFoodLevel(stack),
		// MuseStringUtils.FormatCodes.Italic.character, MuseStringUtils.FormatCodes.Grey));
		// }
		// } catch (Exception e) {
		// }
		if (Config.doAdditionalInfo()) {
			List<String> installed = MuseCommonStrings.getItemInstalledModules(player, stack);
			if (installed.size() == 0) {
				String message = "No installed modules! This item is useless until you add some modules at a Tinker Table.";
				currentTipList.addAll(MuseStringUtils.wrapStringToLength(message, 30));
			} else {
				currentTipList.add("Installed Modules:");
				currentTipList.addAll(installed);
			}
		} else {
			currentTipList.add(Config.additionalInfoInstructions());
		}
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
		return ModuleManager.computeModularProperty(stack, MuseCommonStrings.WEIGHT);
	}

	public static List<String> getItemInstalledModules(EntityPlayer player, ItemStack stack) {
		NBTTagCompound itemTag = MuseItemUtils.getMuseItemTag(stack);
		List<String> modules = new LinkedList();
		for (IPowerModule module : MuseItemUtils.getValidModulesForItem(player, stack)) {
			if (MuseItemUtils.tagHasModule(itemTag, module.getName())) {
				modules.add(module.getName());
			}
		}
		return modules;
	}

}
