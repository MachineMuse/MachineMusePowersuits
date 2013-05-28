package net.machinemuse.utils;

import net.machinemuse.api.IPowerModule;
import net.machinemuse.api.ModuleManager;
import net.machinemuse.api.electricity.ElectricAdapter;
import net.machinemuse.powersuits.common.Config;
import net.machinemuse.powersuits.item.ItemPowerFist;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import java.util.LinkedList;
import java.util.List;

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
     * Adds information to the item's tooltip when 'getting' it.
     *
     * @param stack            The itemstack to get the tooltip for
     * @param player           The player (client) viewing the tooltip
     * @param currentTipList   A list of strings containing the existing tooltip. When
     *                         passed, it will just contain the name of the item;
     *                         enchantments and lore are
     *                         appended afterwards.
     * @param advancedToolTips Whether or not the player has 'advanced tooltips' turned on in
     *                         their settings.
     */
    public static void addInformation(ItemStack stack, EntityPlayer player, List currentTipList, boolean advancedToolTips) {
        if (stack.getItem() instanceof ItemPowerFist) {
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
