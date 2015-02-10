package net.machinemuse.utils;

import net.machinemuse.api.IPowerModule;
import net.machinemuse.api.ModuleManager;
import net.machinemuse.api.electricity.ElectricAdapter;
import net.machinemuse.powersuits.common.Config;
import net.machinemuse.powersuits.item.ItemPowerFist;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.StatCollector;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

public abstract class MuseCommonStrings {
    /**
     * String literals as constants to eliminate case sensitivity issues etc.
     */
    public static final String ARMOR_VALUE_PHYSICAL = "Armor (Physical)";
    public static final String ARMOR_VALUE_ENERGY = "Armor (Energy)";
    public static final String ARMOR_ENERGY_CONSUMPTION = "Energy Per Damage";
    public static final String WEIGHT = "Weight";

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
    
    protected static final Map<String, String> CATEGORY_LOCALES;
    static {
        // Static category strings. Implemented as such with plans to switch to unlocalized strings [where/when]ever possible. -- Korynkai 20150209
        Map<String, String> result = new HashMap<String, String>();
        result.put(CATEGORY_ARMOR, StatCollector.translateToLocal("module.category.armor"));
        result.put(CATEGORY_ENERGY, StatCollector.translateToLocal("module.category.energy"));
        result.put(CATEGORY_TOOL, StatCollector.translateToLocal("module.category.tool"));
        result.put(CATEGORY_WEAPON, StatCollector.translateToLocal("module.category.weapon"));
        result.put(CATEGORY_MOVEMENT, StatCollector.translateToLocal("module.category.movement"));
        result.put(CATEGORY_COSMETIC, StatCollector.translateToLocal("module.category.cosmetic"));
        result.put(CATEGORY_VISION, StatCollector.translateToLocal("module.category.vision"));
        result.put(CATEGORY_ENVIRONMENTAL, StatCollector.translateToLocal("module.category.environment"));
        result.put(CATEGORY_SPECIAL, StatCollector.translateToLocal("module.category.special"));
        CATEGORY_LOCALES = Collections.unmodifiableMap(result);
    }
    
    public static String getModuleCategoryLocalString(String category) {
        String locale = CATEGORY_LOCALES.get(category);
        return locale == null ? "" : locale;
    }

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
                currentTipList.add(StatCollector.translateToLocal("module.common.mode") + ":" 
                                + MuseStringUtils.wrapFormatTags(mode, MuseStringUtils.FormatCodes.Red));
            } else {
                currentTipList.add(StatCollector.translateToLocal("module.common.mode.change"));
            }
        }
        ElectricAdapter adapter = ElectricAdapter.wrap(stack);
        if (adapter != null) {
            String energyinfo = StatCollector.translateToLocal("module.common.energy") + ": " 
                    + MuseStringUtils.formatNumberShort(adapter.getCurrentEnergy()) + '/'
                    + MuseStringUtils.formatNumberShort(adapter.getMaxEnergy());
            currentTipList.add(MuseStringUtils.wrapMultipleFormatTags(energyinfo, MuseStringUtils.FormatCodes.Italic.character,
                    MuseStringUtils.FormatCodes.Grey));
        }
        if (Config.doAdditionalInfo()) {
            List<String> installed = MuseCommonStrings.getItemInstalledModules(player, stack);
            if (installed.size() == 0) {
                String message = StatCollector.translateToLocal("module.common.none");
                currentTipList.addAll(MuseStringUtils.wrapStringToLength(message, 30));
            } else {
                currentTipList.add(StatCollector.translateToLocal("module.common.installed") + ":");
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
        for (IPowerModule module : ModuleManager.getValidModulesForItem(player, stack)) {
            if (ModuleManager.tagHasModule(itemTag, module.getDataName())) {
                modules.add(module.getDataName());
            }
        }
        return modules;
    }

}
