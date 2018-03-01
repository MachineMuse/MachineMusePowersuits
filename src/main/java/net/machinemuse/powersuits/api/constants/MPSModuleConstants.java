package net.machinemuse.powersuits.api.constants;

import static net.machinemuse.powersuits.api.constants.MPSModConstants.MODID;

public class MPSModuleConstants {
    /**
     * Categories for module
     */
    public static final String CATEGORY_DEBUG = "Debug";
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
     * String literals as constants to eliminate case sensitivity issues etc.
     */
    public static final String ARMOR_VALUE_PHYSICAL = "Armor (Physical)";
    public static final String ARMOR_VALUE_ENERGY = "Armor (Energy)";
    public static final String ARMOR_ENERGY_CONSUMPTION = "Energy Per Damage";
    public static final String WEIGHT = "Weight";

    /**
     * Module UnlocalizedNames
     */
    public static final String MODULE_PREFIX = MODID + ".module.";
    // Debug --------------------------------------------------------------------------------------
    public static final String MODULE_DEBUG = MODULE_PREFIX + "debugModule";

    // Armor --------------------------------------------------------------------------------------
    public static final String MODULE_LEATHER_PLATING = MODULE_PREFIX + "leatherPlating";
    public static final String MODULE_IRON_PLATING = MODULE_PREFIX + "ironPlating";
    public static final String MODULE_DIAMOND_PLATING = MODULE_PREFIX + "diamondPlating";
    public static final String MODULE_ENERGY_SHIELD = MODULE_PREFIX + "energyShield";

    // Cosmetic -----------------------------------------------------------------------------------
    public static final String MODULE_CITIZEN_JOE_STYLE = MODULE_PREFIX + "citizenJoe";//"Citizen Joe Style";
    public static final String MODULE_GLOW = MODULE_PREFIX + "cosmeticGlow";
    public static final String MODULE_HIGH_POLY_ARMOR = MODULE_PREFIX + "3dArmor";
    public static final String MODULE_TINT = MODULE_PREFIX + "tint";
    public static final String MODULE_TRANSPARENT_ARMOR = MODULE_PREFIX + "transparentArmor";

    // Energy -------------------------------------------------------------------------------------
    public static final String MODULE_BATTERY_ADVANCED = MODULE_PREFIX + "advancedBattery";
    public static final String MODULE_ADVANCED_SOLAR_GENERATOR = MODULE_PREFIX + "advSolarGenerator";
    public static final String MODULE_BATTERY_BASIC = MODULE_PREFIX + "basicBattery";
    public static final String MODULE_COAL_GEN = MODULE_PREFIX + "coalGenerator";
    public static final String MODULE_BATTERY_ELITE = MODULE_PREFIX + "eliteBattery";
    public static final String MODULE_KINETIC_GENERATOR = MODULE_PREFIX + "kineticGenerator";
    public static final String MODULE_SOLAR_GENERATOR = MODULE_PREFIX + "solarGenerator";
    public static final String MODULE_THERMAL_GENERATOR = MODULE_PREFIX + "thermalGenerator";
    public static final String MODULE_BATTERY_ULTIMATE = MODULE_PREFIX + "ultimateBattery";

    // Environmental ------------------------------------------------------------------------------
    public static final String AIRTIGHT_SEAL_MODULE = MODULE_PREFIX + "airtightSeal";
    public static final String MODULE_APIARIST_ARMOR = MODULE_PREFIX + "apiaristArmor";
    public static final String MODULE_AUTO_FEEDER = MODULE_PREFIX + "autoFeeder";
    public static final String MODULE_COOLING_SYSTEM = MODULE_PREFIX + "coolingSystem";
    public static final String MODULE_HAZMAT = MODULE_PREFIX + "hazmat";
    public static final String MODULE_HEAT_SINK = MODULE_PREFIX + "heatSink";
    public static final String MODULE_MECH_ASSISTANCE = MODULE_PREFIX + "mechAssistance";
    public static final String MODULE_MOB_REPULSOR = MODULE_PREFIX + "mobRepulsor";
    public static final String MODULE_NITROGEN_COOLING_SYSTEM = MODULE_PREFIX + "nitrogenCoolingSystem";;
    public static final String MODULE_WATER_ELECTROLYZER = MODULE_PREFIX + "waterElectrolyzer";
    public static final String MODULE_WATER_TANK = MODULE_PREFIX + "waterTank";

    // Movement -----------------------------------------------------------------------------------
    public static final String MODULE_BLINK_DRIVE = MODULE_PREFIX + "blinkDrive";
    public static final String MODULE_CLIMB_ASSIST = MODULE_PREFIX + "climbAssist";
    public static final String MODULE_FLIGHT_CONTROL = MODULE_PREFIX + "flightControl";
    public static final String MODULE_GLIDER =  MODULE_PREFIX + "glider";
    public static final String MODULE_JETBOOTS = MODULE_PREFIX + "jetBoots";
    public static final String MODULE_JETPACK = MODULE_PREFIX + "jetpack";
    public static final String MODULE_JUMP_ASSIST = MODULE_PREFIX + "jumpAssist";
    public static final String MODULE_PARACHUTE = MODULE_PREFIX + "parachute";
    public static final String MODULE_SHOCK_ABSORBER = MODULE_PREFIX + "shockAbsorber";
    public static final String MODULE_SPRINT_ASSIST = MODULE_PREFIX + "sprintAssist";
    public static final String MODULE_SWIM_BOOST = MODULE_PREFIX + "swimAssist";

    // Special ------------------------------------------------------------------------------------
    public static final String MODULE_CLOCK = MODULE_PREFIX + "clock";
    public static final String MODULE_COMPASS = MODULE_PREFIX + "compass";
    public static final String MODULE_ACTIVE_CAMOUFLAGE = MODULE_PREFIX + "invisibility";
    public static final String MODULE_MAGNET =  MODULE_PREFIX + "magnet";

    // Vision -------------------------------------------------------------------------------------
    public static final String BINOCULARS_MODULE = MODULE_PREFIX + "binoculars";
    public static final String MODULE_NIGHT_VISION = MODULE_PREFIX + "nightVision";
    public static final String MODULE_THAUM_GOGGLES = MODULE_PREFIX + "aurameter";

    // Tools --------------------------------------------------------------------------------------
    public static final String MODULE_AOE_PICK_UPGRADE = MODULE_PREFIX + "aoePickUpgrade";
    public static final String MODULE_APPENG_EC_WIRELESS_FLUID = MODULE_PREFIX + "appengECWirelessFluid";
    public static final String MODULE_APPENG_WIRELESS = MODULE_PREFIX + "appengWireless";
    public static final String MODULE_AQUA_AFFINITY = MODULE_PREFIX + "aquaAffinity";
    public static final String MODULE_AXE = MODULE_PREFIX + "axe";
    public static final String MODULE_CHISEL = MODULE_PREFIX + "chisel";
    public static final String MODULE_DIAMOND_PICK_UPGRADE = MODULE_PREFIX + "diamondPickUpgrade";
    public static final String MODULE_DIMENSIONAL_RIFT = MODULE_PREFIX + "dimRiftGen";
    public static final String MODULE_FIELD_TINKER = MODULE_PREFIX + "fieldTinkerer";
    public static final String MODULE_FLINT_AND_STEEL = MODULE_PREFIX + "flintAndSteel";
    public static final String MODULE_GRAFTER = MODULE_PREFIX + "grafter";
    public static final String MODULE_HOE = MODULE_PREFIX + "hoe";
    public static final String MODULE_LEAF_BLOWER = MODULE_PREFIX + "leafBlower";
    public static final String MODULE_LUX_CAPACITOR = MODULE_PREFIX + "luxCapacitor";
    public static final String MODULE_FIELD_TELEPORTER = MODULE_PREFIX + "mffsFieldTeleporter";
    public static final String MODULE_OC_TERMINAL = MODULE_PREFIX + "ocTerminal";
    public static final String MODULE_OMNIPROBE =  MODULE_PREFIX + "omniProbe";
    public static final String MODULE_OMNI_WRENCH = MODULE_PREFIX + "omniwrench";
    public static final String MODULE_ORE_SCANNER = MODULE_PREFIX + "oreScanner";
    public static final String MODULE_CM_PSD = MODULE_PREFIX + "cmPSD";//"Personal Shrinking Device";
    public static final String MODULE_PICKAXE = MODULE_PREFIX + "pickaxe";
    public static final String MODULE_PORTABLE_CRAFTING = MODULE_PREFIX + "portableCraftingTable";
    public static final String MODULE_REF_STOR_WIRELESS = MODULE_PREFIX + "refinedStorageWirelessGrid";//"Refined Storage Wireless Grid";
    public static final String MODULE_SCOOP = MODULE_PREFIX + "scoop";
    public static final String MODULE_SHEARS = MODULE_PREFIX + "shears";
    public static final String MODULE_SHOVEL = MODULE_PREFIX + "shovel";
    public static final String MODULE_TREETAP = MODULE_PREFIX + "treetap";

    // Weapons ------------------------------------------------------------------------------------
    public static final String MODULE_BLADE_LAUNCHER = MODULE_PREFIX + "bladeLauncher";
    public static final String MODULE_LIGHTNING = MODULE_PREFIX + "lightningSummoner";
    public static final String MODULE_MELEE_ASSIST =  "meleeAssist";
    public static final String MODULE_PLASMA_CANNON = MODULE_PREFIX + "plasmaCannon";
    public static final String MODULE_RAILGUN = MODULE_PREFIX + "railgun";
}
