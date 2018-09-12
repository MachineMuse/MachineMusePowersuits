package net.machinemuse.powersuits.api.constants;

import static net.machinemuse.powersuits.api.constants.MPSModConstants.MODID;

/**
 * All the unlocalized strings for the config file
 */
public class MPSConfigConstants {
    public static final String CONFIG_FILE = "machinemuse/" +  MODID;
    public static final String CONFIG_PREFIX = "config." + MODID + ".";

    // HUD
    public static final String CONFIG_PREFIX_HUD = CONFIG_PREFIX + "hud.";
    public static final String CONFIG_HUD_USE_HUD = CONFIG_PREFIX_HUD + "useHUD";
    public static final String CONFIG_HUD_TOGGLE_MODULE_SPAM = CONFIG_PREFIX_HUD + "enableModuleSpam";
    public static final String CONFIG_HUD_DISPLAY_HUD = CONFIG_PREFIX_HUD + "DisplayHUD";
    public static final String CONFIG_HUD_KEYBIND_HUD_X = CONFIG_PREFIX_HUD + "Xposition";
    public static final String CONFIG_HUD_KEYBIND_HUD_Y = CONFIG_PREFIX_HUD + "Yposition";
    public static final String CONFIG_HUD_USE_GRAPHICAL_METERS = CONFIG_PREFIX_HUD + "useGraphicalMeters";

    // General
    public static final String CONFIG_PREFIX_GENERAL = CONFIG_PREFIX + "general.";
    public static final String CONFIG_GENERAL_USE_24_HOUR_CLOCK = CONFIG_PREFIX_GENERAL + "use24HrClock";
    public static final String CONFIG_GENERAL_ALLOW_CONFLICTING_KEYBINDS = CONFIG_PREFIX_GENERAL + "allowConflictingKeybinds";
    public static final String CONFIG_GENERAL_USE_OLD_AUTOFEEDER = CONFIG_PREFIX_GENERAL + "useOldAutofeeder";
    public static final String CONFIG_GENERAL_MAX_FLYING_SPEED = CONFIG_PREFIX_GENERAL + "maxFlyingSpeed";
    public static final String CONFIG_GENERAL_WEIGHT_LIMIT = CONFIG_PREFIX_GENERAL + "weightLimit";
    public static final String CONFIG_GENERAL_GET_MAX_ARMOR_PER_PIECE = CONFIG_PREFIX_GENERAL + "getMaximumArmorPerPiece";
    public static final String CONFIG_GENERAL_SALVAGE_CHANCE = CONFIG_PREFIX_GENERAL + "salvageChance";
    public static final String CONFIG_GENERAL_BASE_MAX_HEAT_POWERFIST = CONFIG_PREFIX_GENERAL + "baseMaxHeatPowerfist";
    public static final String CONFIG_GENERAL_BASE_MAX_HEAT_HELMET = CONFIG_PREFIX_GENERAL + "baseMaxHeatHelmet";
    public static final String CONFIG_GENERAL_BASE_MAX_HEAT_CHEST = CONFIG_PREFIX_GENERAL + "baseMaxHeatChest";
    public static final String CONFIG_GENERAL_BASE_MAX_HEAT_LEGS = CONFIG_PREFIX_GENERAL + "baseMaxHeatLegs";
    public static final String CONFIG_GENERAL_BASE_MAX_HEAT_FEET = CONFIG_PREFIX_GENERAL + "baseMaxHeatFeet";


    // Modules
    public static final String CONFIG_MODULES = CONFIG_PREFIX + "module";
    public static final String CONFIG_MODULE_PROPERTY_DOUBLES = CONFIG_PREFIX + "modulePropertyDoubles";
    public static final String CONFIG_MODULE_PROPERTY_INTEGERS = CONFIG_PREFIX + "modulePropertyIntegers";


}
