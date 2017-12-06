package net.machinemuse.powersuits.common;

import net.minecraft.util.ResourceLocation;

/*
 * All the constants for the mod in one place
 */
public class MPSConstants {
    public static final String MODID = "powersuits";
    public static final String NAME = "MachineMuse's Modular Powersuits";
    public static final String VERSION = "1.10.0";




    public static final String RESOURCE_PREFIX = MODID + ":";
    public static final String TEXTURE_PREFIX = RESOURCE_PREFIX + "textures/";
    public static final String COMPONENTS_PREFIX = RESOURCE_PREFIX + "component/";
    public static final String SOUND_PREFIX = RESOURCE_PREFIX + "sound/";
    public static final String LANG_PREFIX = RESOURCE_PREFIX + "lang/";
    public static final String SEBK_ARMOR_PATH = TEXTURE_PREFIX + "items/armor/sebkarmor.png";
    public static final String SEBK_ARMORPANTS_PATH = TEXTURE_PREFIX + "items/armor/sebkarmorpants.png";



    public static final ResourceLocation TINKERTABLE_TEXTURE_PATH = new ResourceLocation(MODID, "textures/models/tinkertable_tx.png");




    public static final String ARMOR_TEXTURE_PATH = TEXTURE_PREFIX + "items/armor/diffuse.png";
    public static final String BLANK_ARMOR_MODEL_PATH = TEXTURE_PREFIX + "items/armor/blankarmor.png";
    public static final String SEBK_TOOL_TEXTURE = TEXTURE_PREFIX + "models/tool.png";
    public static final String LIGHTNING_TEXTURE = TEXTURE_PREFIX + "gui/lightning-medium.png";
    public static final String CITIZENJOE_ARMOR_PATH = TEXTURE_PREFIX + "items/armor/joearmor.png";
    public static final String CITIZENJOE_ARMORPANTS_PATH = TEXTURE_PREFIX + "items/armor/joearmorpants.png";
    public static final String GLASS_TEXTURE = TEXTURE_PREFIX + "gui/glass.png";


    /** Config ------------------------------------------------------------------------------------ */
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

    // Energy
    public static final String CONFIG_MAX_ENERGY_LV_CAPACITOR = CONFIG_PREFIX + "maxEnergyLVCapacitor";
    public static final String CONFIG_MAX_ENERGY_MV_CAPACITOR = CONFIG_PREFIX + "maxEnergyMVCapacitor";
    public static final String CONFIG_MAX_ENERGY_HV_CAPACITOR = CONFIG_PREFIX + "maxEnergyHVCapacitor";
    public static final String CONFIG_MAX_ENERGY_EV_CAPACITOR = CONFIG_PREFIX + "maxEnergyEVCapacitor";
    public static final String CONFIG_RF_ENERGY_PER_IC2_EU = CONFIG_PREFIX + "RFEnergyPerIC2EU";

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
    public static final String CONFIG_MODULES = CONFIG_PREFIX + "modules";
    public static final String CONFIG_MODULE_PROPERTY_DOUBLES = CONFIG_PREFIX + "modulePropertyDoubles";




    /** Module Names ------------------------------------------------------------------------------ */
    public static final String CONFIG_MODULE_NAME = CONFIG_PREFIX + ".moduleName.";

    // Armor
    public static final String MODULE_BASIC_PLATING = /* I18n.format(CONFIG_MODULE_NAME  + "ironPlating"); */ "Iron Plating";
    public static final String MODULE_DIAMOND_PLATING = "Diamond Plating";
    public static final String MODULE_ENERGY_SHIELD = "Energy Shield";
    public static final String MODULE_HEAT_SINK = "Heat Sink";


    // Cosmetic
    public static final String MODULE_TRANSPARENT_ARMOR = "Transparent Armor";
    public static final String MODULE_GLOW = "Glow Module";
    public static final String CITIZEN_JOE_STYLE = "Citizen Joe Style";
    public static final String HIGH_POLY_ARMOR = "3D Armor";
    public static final String MODULE_TINT = "Custom Colour Module";


    // Energy
    public static final String MODULE_BATTERY_BASIC = "Basic Battery";
    public static final String MODULE_BATTERY_ADVANCED = "Advanced Battery";
    public static final String MODULE_BATTERY_ELITE = "Elite Battery";
    public static final String MODULE_BATTERY_ULTIMATE = "Ultimate Battery";


    // PowerFist
    public static final String MODULE_AXE = "Axe";
    public static final String MODULE_PICKAXE = "Pickaxe";
    public static final String MODULE_DIAMOND_PICK_UPGRADE = "Diamond Drill Upgrade";
    public static final String MODULE_SHOVEL = "Shovel";
    public static final String MODULE_SHEARS = "Shears";
    public static final String MODULE_HOE = "Rototiller";
    public static final String MODULE_LUX_CAPACITOR = "Lux Capacitor";
    public static final String MODULE_FIELD_TINKER = "Field Tinker Module";
    public static final String MODULE_MELEE_ASSIST = "Melee Assist";
    public static final String MODULE_PLASMA_CANNON = "Plasma Cannon";
    public static final String MODULE_RAILGUN = "Railgun";
    public static final String MODULE_BLADE_LAUNCHER = "Blade Launcher";
    public static final String MODULE_BLINK_DRIVE = "Blink Drive";
    public static final String MODULE_AQUA_AFFINITY = "Aqua Affinity";
    public static final String MODULE_PORTABLE_CRAFTING = "In-Place Assembler";
    public static final String MODULE_ORE_SCANNER = "Ore Scanner";
    public static final String MODULE_LEAF_BLOWER = "Leaf Blower";
    public static final String MODULE_FLINT_AND_STEEL = "Flint and Steel";
    public static final String MODULE_LIGHTNING = "Lightning Summoner";
    public static final String MODULE_DIMENSIONAL_RIFT = "Dimensional Tear Generator";


    // Head
    public static final String MODULE_WATER_ELECTROLYZER = "Water Electrolyzer";
    public static final String MODULE_NIGHT_VISION = "Night Vision";
    public static final String BINOCULARS_MODULE = "Binoculars";
    public static final String MODULE_FLIGHT_CONTROL = "Flight Control";
    public static final String MODULE_SOLAR_GENERATOR = "Solar Generator";
    public static final String MODULE_AUTO_FEEDER = "Auto-Feeder";
    public static final String MODULE_CLOCK = "Clock";
    public static final String MODULE_COMPASS = "Compass";
    public static final String MODULE_ADVANCED_SOLAR_GENERATOR = "Advanced Solar Generator";


    // Torso
    public static final String MODULE_PARACHUTE = "Parachute";
    public static final String MODULE_GLIDER = "Glider";
    public static final String MODULE_JETPACK = "Jetpack";
    public static final String MODULE_ACTIVE_CAMOUFLAGE = "Active Camouflage";
    public static final String MODULE_COOLING_SYSTEM = "Cooling System";
    public static final String MODULE_MAGNET = "Magnet";
    public static final String MODULE_THERMAL_GENERATOR = "Thermal Generator";
    public static final String MODULE_MOB_REPULSOR = "Mob Repulsor";
    public static final String MODULE_WATER_TANK = "Water Tank";
    public static final String MODULE_NITROGEN_COOLING_SYSTEM = "Liquid Nitrogen Cooling System";
    public static final String MODULE_MECH_ASSISTANCE = "Mechanical Assistance";
    public static final String MODULE_COAL_GEN = "Coal Generator";

    // Legs
    public static final String MODULE_SPRINT_ASSIST = "Sprint Assist";
    public static final String MODULE_JUMP_ASSIST = "Jump Assist";
    public static final String MODULE_SWIM_BOOST = "Swim Boost";
    public static final String MODULE_KINETIC_GENERATOR = "Kinetic Generator";
    public static final String MODULE_CLIMB_ASSIST = "Uphill Step Assist";


    // Feet
    public static final String MODULE_JETBOOTS = "Jet Boots";
    public static final String MODULE_SHOCK_ABSORBER = "Shock Absorber";
}
