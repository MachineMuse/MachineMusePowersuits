package net.machinemuse.powersuits.api.constants;

import static net.machinemuse.powersuits.api.constants.MPSModConstants.MODID;

public class MPSConfigConstants {
    public static final String CONFIG_FILE = "machinemuse/" +  MODID;
    public static final String CONFIG_PREFIX = "config." + MODID + ".";
    public static final String CONFIG_TITLE = CONFIG_PREFIX + ".title";

    // General
    public static final String CONFIG_PREFIX_GENERAL = CONFIG_PREFIX + "general.";
    public static final String CONFIG_GENERAL_ALLOW_CONFLICTING_KEYBINDS = CONFIG_PREFIX_GENERAL + "allowConflictingKeybinds";
    public static final String CONFIG_GENERAL_AUTO_EXTRACT_RECIPES = CONFIG_PREFIX_GENERAL + "autoExtractRecipes";
    public static final String CONFIG_GENERAL_GET_MAX_ARMOR_PER_PIECE = CONFIG_PREFIX_GENERAL + "getMaximumArmorPerPiece";
    public static final String CONFIG_GENERAL_USE_24_HOUR_CLOCK = CONFIG_PREFIX_GENERAL + "use24HrClock";
    public static final String CONFIG_GENERAL_MAX_FLYING_SPEED = CONFIG_PREFIX_GENERAL + "maxFlyingSpeed";
    public static final String CONFIG_GENERAL_BASE_MAX_HEAT = CONFIG_PREFIX_GENERAL + "baseMaxHeat";
    public static final String CONFIG_GENERAL_WEIGHT_LIMIT = CONFIG_PREFIX_GENERAL + "weightLimit";
    public static final String CONFIG_GENERAL_SALVAGE_CHANCE = CONFIG_PREFIX_GENERAL + "salvageChance";
    public static final String CONFIG_GENERAL_USE_MOUSE_WHEEL = CONFIG_PREFIX_GENERAL + "useMouseWheel";
    public static final String CONFIG_GENERAL_USE_ADVANCED_ORE_SCANNER_MESSAGE = CONFIG_PREFIX_GENERAL + "useAdvancedOreScannerMessage";
    public static final String CONFIG_GENERAL_USE_OLD_AUTOFEEDER = CONFIG_PREFIX_GENERAL + "useOldAutofeeder";
    public static final String CONFIG_GENERAL_USE_CHEATY_LEATHER = CONFIG_PREFIX_GENERAL + "useCheatyLeather";
    public static final String CONFIG_GENERAL_USE_HUD = CONFIG_PREFIX_GENERAL + "useHUD";

    // Weight
    public static final String CONFIG_PREFIX_WEIGHT = CONFIG_PREFIX + "weight.";
    public static final String CONFIG_GENERAL_MAX_MODULES_POWER_FIST = CONFIG_PREFIX_WEIGHT + "powerfist";
    public static final String CONFIG_GENERAL_MAX_MODULES_ARMOR_HELMET = CONFIG_PREFIX_WEIGHT + "powerArmorHelmet";
    public static final String CONFIG_GENERAL_MAX_MODULES_ARMOR_CHESTPLATE = CONFIG_PREFIX_WEIGHT + "powerArmorChestplate";
    public static final String CONFIG_GENERAL_MAX_MODULES_ARMOR_LEGGINGS = CONFIG_PREFIX_WEIGHT + "powerArmorLeggings";
    public static final String CONFIG_GENERAL_MAX_MODULES_ARMOR_BOOTS = CONFIG_PREFIX_WEIGHT + "powerArmorBoots";

    // Energy
    public static final String CONFIG_PREFIX_ENERGY = CONFIG_PREFIX + "energy.";
    public static final String CONFIG_MAX_ENERGY_LV_CAPACITOR = CONFIG_PREFIX_ENERGY + "maxEnergyLVCapacitor";
    public static final String CONFIG_MAX_ENERGY_MV_CAPACITOR = CONFIG_PREFIX_ENERGY + "maxEnergyMVCapacitor";
    public static final String CONFIG_MAX_ENERGY_HV_CAPACITOR = CONFIG_PREFIX_ENERGY + "maxEnergyHVCapacitor";
    public static final String CONFIG_MAX_ENERGY_EV_CAPACITOR = CONFIG_PREFIX_ENERGY + "maxEnergyEVCapacitor";
    public static final String CONFIG_RF_ENERGY_PER_IC2_EU = CONFIG_PREFIX_ENERGY + "RFEnergyPerIC2EU";

    // Heat
    public static final String CONFIG_PREFIX_HEAT = CONFIG_PREFIX + "heat.";
    public static final String CONFIG_MAX_HEAT_POWERFIST = CONFIG_PREFIX_HEAT + "maxHeatPowerFist";
    public static final String CONFIG_MAX_HEAT_HELMET = CONFIG_PREFIX_HEAT + "maxHeatHelmet";
    public static final String CONFIG_MAX_HEAT_CHESTPLATE = CONFIG_PREFIX_HEAT + "maxHeatChestplate";
    public static final String CONFIG_MAX_HEAT_LEGGINGS = CONFIG_PREFIX_HEAT + "maxHeatLeggings";
    public static final String CONFIG_MAX_HEAT_BOOTS = CONFIG_PREFIX_HEAT + "maxHeatBoots";

    // HUD
    public static final String CONFIG_HUD_TOGGLE_MODULE_SPAM = CONFIG_PREFIX + "enableModuleSpam";
    public static final String CONFIG_HUD_DISPLAY_HUD = CONFIG_PREFIX + "DisplayHUD";
    public static final String CONFIG_HUD_KEYBIND_HUD_X = CONFIG_PREFIX + "Xposition";
    public static final String CONFIG_HUD_KEYBIND_HUD_Y = CONFIG_PREFIX + "Yposition";
    public static final String CONFIG_HUD_USE_GRAPHICAL_METERS = CONFIG_PREFIX + "useGraphicalMeters";

    // Fonts
    public static final String CONFIG_FONT_USE_CUSTOM_FONTS = CONFIG_PREFIX + "useCustomFonts";
    public static final String CONFIG_FONT_DETAIL_MULTIPLIER = CONFIG_PREFIX + "fontDetailMultiplier";
    public static final String CONFIG_FONT_URI = CONFIG_PREFIX + "fontDetailMultiplier";
    public static final String CONFIG_FONT_NAME = CONFIG_PREFIX + "fontName";
    public static final String CONFIG_USE_FONT_ANTI_ALIASING = CONFIG_PREFIX + "useFontAntiAliasing";

    // Graphics
    public static final String CONFIG_GRAPHICS_GLOW_MULTIPLIER = CONFIG_PREFIX + "glowMultiplier";
    public static final String CONFIG_GRAPHICS_USE_SHADERS = CONFIG_PREFIX + "useShaders";

    // Modules
    public static final String CONFIG_MODULES = CONFIG_PREFIX + "module";
    public static final String CONFIG_MODULE_PROPERTY_DOUBLES = CONFIG_PREFIX + "modulePropertyDoubles";
    public static final String CONFIG_MODULE_PROPERTY_INTEGERS = CONFIG_PREFIX + "modulePropertyIntegers";









}
