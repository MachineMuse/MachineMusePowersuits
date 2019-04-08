package net.machinemuse.powersuits.constants;

import net.machinemuse.powersuits.basemod.ModularPowersuits;

public class MPSConfigConstants {
    public static final String CONFIG_PREFIX = "config." + ModularPowersuits.MODID + ".";

    // HUD ----------------------------------------------------------------------------------------
    public static final String CONFIG_PREFIX_HUD = CONFIG_PREFIX + "hud.";
    public static final String CONFIG_HUD_TOGGLE_MODULE_SPAM = CONFIG_PREFIX_HUD + "enableModuleSpam";
    public static final String CONFIG_HUD_DISPLAY_HUD = CONFIG_PREFIX_HUD + "DisplayHUD";
    public static final String CONFIG_HUD_KEYBIND_HUD_X = CONFIG_PREFIX_HUD + "Xposition";
    public static final String CONFIG_HUD_KEYBIND_HUD_Y = CONFIG_PREFIX_HUD + "Yposition";
    public static final String CONFIG_HUD_USE_GRAPHICAL_METERS = CONFIG_PREFIX_HUD + "useGraphicalMeters";
    public static final String CONFIG_HUD_USE_24_HOUR_CLOCK = CONFIG_PREFIX_HUD + "use24HrClock";

    // Cosmetics ----------------------------------------------------------------------------------
    public static final String CONFIG_PREFIX_COSMETIC = CONFIG_PREFIX + "cosmetic.";
    public static final String CONFIG_COSMETIC_USE_LEGACY_COSMETIC_SYSTEM = CONFIG_PREFIX_COSMETIC + "useLegacyCosmeticSystem";
    public static final String CONFIG_COSMETIC_ALLOW_HIGH_POLLY_ARMOR_MODELS = CONFIG_PREFIX_COSMETIC + "allowHighPollyArmorModuels";
    public static final String CONFIG_COSMETIC_ALLOW_POWER_FIST_CUSTOMIZATOIN = CONFIG_PREFIX_COSMETIC + "allowPowerFistCustomization";

    // Modules
    public static final String CONFIG_MODULES = CONFIG_PREFIX + "module.";

    // Modules - Energy Storage
    public static final String CONFIG_MODULES_ENERGY_STORAGE = CONFIG_MODULES + "energyStorage.";
    public static final String CONFIG_MODULES_ENERGY_STORAGE_BASIC_BATTERY_MAX_ENERGY = CONFIG_MODULES_ENERGY_STORAGE + "basicBatteryMaxEnergy";
    public static final String CONFIG_MODULES_ENERGY_STORAGE_BASIC_BATTERY_MAX_TRAMSFER = CONFIG_MODULES_ENERGY_STORAGE + "basicBatteryMaxTransfer";
    public static final String CONFIG_MODULES_ENERGY_STORAGE_ADVANCED__MAX_ENERGY = CONFIG_MODULES_ENERGY_STORAGE + "advancedBatteryMaxEnergy";
    public static final String CONFIG_MODULES_ENERGY_STORAGE_ADVANCED_MAX_TRAMSFER = CONFIG_MODULES_ENERGY_STORAGE + "advancedBatteryMaxTransfer";
    public static final String CONFIG_MODULES_ENERGY_STORAGE_ELITE__MAX_ENERGY = CONFIG_MODULES_ENERGY_STORAGE + "eltiteBatteryMaxEnergy";
    public static final String CONFIG_MODULES_ENERGY_STORAGE_ELITE_MAX_TRAMSFER = CONFIG_MODULES_ENERGY_STORAGE + "eltiteBatteryMaxTransfer";
    public static final String CONFIG_MODULES_ENERGY_STORAGE_ULTIMATE__MAX_ENERGY = CONFIG_MODULES_ENERGY_STORAGE + "ultimateBatteryMaxEnergy";
    public static final String CONFIG_MODULES_ENERGY_STORAGE_ULTIMATE_MAX_TRAMSFER = CONFIG_MODULES_ENERGY_STORAGE + "ultimateBatteryMaxTransfer";


    // General
    public static final String CONFIG_PREFIX_GENERAL = CONFIG_PREFIX + "general.";
    public static final String CONFIG_GENERAL_ALLOW_CONFLICTING_KEYBINDS = CONFIG_PREFIX_GENERAL + "allowConflictingKeybinds";




    public static final String CONFIG_MODULE_PROPERTY_DOUBLES = CONFIG_PREFIX + "modulePropertyDoubles";
    public static final String CONFIG_MODULE_PROPERTY_INTEGERS = CONFIG_PREFIX + "modulePropertyIntegers";






}
