package net.machinemuse.powersuits.api.constants;

public class MPSModuleConstants {
    /**
     * String literals as constants to eliminate case sensitivity issues etc.
     */
    // Labels -------------------------------------------------------------------------------------


    /**
     * These are used in the module NBT tags and label settings... prefix with  MODULE_TRADEOFF_PREFIX before translating
     */
    public static final String SLOT_POINTS = "slotPoints";
    public static final String MODULE_TRADEOFF_PREFIX = "powersuits.tradeoff.";
    public static final String ARMOR_POINTS = "armorPoints";
    public static final String ARMOR_VALUE_PHYSICAL = "armorPhysical";
    public static final String ARMOR_VALUE_ENERGY = "armorEnergy";
    public static final String ARMOR_ENERGY_CONSUMPTION = "armorEnergyPerDamage";
    public static final String MODULE_FIELD_STRENGTH = "fieldStrength";
    public static final String ENERGY_PER_COAL = "energyPerCoal";
    public static final String HEAT_GENERATION = "heatGeneration";
    public static final String MAX_COAL_STORAGE = "maxStorageAmount";
    public static final String KINETIC_ENERGY_GENERATION = "energyPerBlock";
    public static final String KINETIC_ENERGY_MOVEMENT_RESISTANCE = "movementResistance";
    public static final String ENERGY_GENERATED = "energyGenerated";
    public static final String SOLAR_HEAT_GENERATION_DAY = "daytimeHeatGen";
    public static final String SOLAR_HEAT_GENERATION_NIGHT = "nightTimeHeatGen";
    public static final String SOLAR_ENERGY_GENERATION_DAY = "daytimeEnergyGen";
    public static final String SOLAR_ENERGY_GENERATION_NIGHT = "nightTimeEnergyGen";
    public static final String THERMAL_ENERGY_GENERATION = "thermalEnergyGen";
    public static final String APIARIST_ARMOR_ENERGY_CONSUMPTION = "apiaristArmorEnergyCon";
    public static final String EATING_ENERGY_CONSUMPTION = "eatingEnergyCon";
    public static final String EATING_EFFICIENCY = "autoFeederEfficiency";
    public static final String BASIC_COOLING_POWER = "basicCoolingPower";
    public static final String COOLING_BONUS = "coolingBonus";
    public static final String BASIC_COOLING_SYSTEM_ENERGY_CONSUMPTION = "coolingSystemEnergyCon";
    public static final String MOB_REPULSOR_ENERGY_CONSUMPTION = "repulsorEnergyCon";
    public static final String ADVANCED_COOLING_SYSTEM_ENERGY_CONSUMPTION = "advCoolSysEnergyCon";
    public static final String WATERBREATHING_ENERGY_CONSUMPTION = "joltEnergy";
    public static final String BLINK_DRIVE_ENERGY_CONSUMPTION = "blinkDriveEnergyCon";
    public static final String BLINK_DRIVE_RANGE = "blinkDriveRange";
    public static final String FLIGHT_VERTICALITY = "yLookRatio";
    public static final String JETBOOTS_ENERGY_CONSUMPTION = "jetBootsEnergyCon";
    public static final String JETBOOTS_THRUST = "jetbootsThrust";
    public static final String JETPACK_ENERGY_CONSUMPTION = "jetpackEnergyCon";
    public static final String JETPACK_THRUST = "jetpackThrust";
    public static final String JUMP_ENERGY_CONSUMPTION = "jumpEnergyCon";
    public static final String JUMP_MULTIPLIER = "jumpBoost";
    public static final String JUMP_FOOD_COMPENSATION = "jumpExhaustComp";
    public static final String SHOCK_ABSORB_MULTIPLIER = "distanceRed";
    public static final String SHOCK_ABSORB_ENERGY_CONSUMPTION = "impactEnergyCon";
    public static final String SPRINT_ENERGY_CONSUMPTION = "sprintEnergyCon";
    public static final String SPRINT_SPEED_MULTIPLIER = "sprintSpeedMult";
    public static final String SPRINT_FOOD_COMPENSATION = "sprintExComp";
    public static final String WALKING_ENERGY_CONSUMPTION = "walkingEnergyCon";
    public static final String WALKING_SPEED_MULTIPLIER = "walkingSpeedMult";
    public static final String SWIM_BOOST_AMOUNT = "underwaterMovBoost";
    public static final String SWIM_BOOST_ENERGY_CONSUMPTION = "swimBoostEnergyCon";
    public static final String MAGNET_RADIUS = "magnetRadius";
    public static final String FOV = "fieldOfView";
    public static final String FIELD_OF_VIEW = "fOVMult";
    public static final String AQUA_AFFINITY_ENERGY_CONSUMPTION = "underWaterEnergyCon";
    public static final String UNDERWATER_HARVEST_SPEED = "underWaterHarvSpeed";
    public static final String AXE_ENERGY_CONSUMPTION = "axeEnergyCon";
    public static final String AXE_HARVEST_SPEED = "axeHarvSpd";
    public static final String CHISEL_HARVEST_SPEED = "chiselHarvSpd";
    public static final String CHISEL_ENERGY_CONSUMPTION = "chiselEnergyCon";
    public static final String IGNITION_ENERGY_CONSUMPTION = "ignitEnergyCon";
    public static final String GRAFTER_ENERGY_CONSUMPTION = "grafterEnergyCon";
    public static final String GRAFTER_HEAT_GENERATION = "grafterHeatGen";
    public static final String HOE_ENERGY_CONSUMPTION = "hoeEnergyCon";
    public static final String HOE_SEARCH_RADIUS = "hoeSearchRad";
    public static final String LEAF_BLOWER_RADIUS = "radius";
    public static final String LUX_CAPACITOR_ENERGY_CONSUMPTION = "luxCapEnergyCon";
    public static final String LUX_CAPACITOR_RED_HUE = "luxCapRed";
    public static final String LUX_CAPACITOR_BLUE_HUE = "luxCapBlue";
    public static final String LUX_CAPACITOR_GREEN_HUE = "luxCapGreen";
    public static final String PICKAXE_HARVEST_SPEED = "pickHarvSpd";
    public static final String PICKAXE_ENERGY_CONSUMPTION = "pickaxeEnergyCon";
    public static final String SCOOP_HARVEST_SPEED = "scoopHarSpd";
    public static final String SCOOP_ENERGY_CONSUMPTION = "scoopEnergyCon";
    public static final String SHEARING_ENERGY_CONSUMPTION = "shearEnergyCon";
    public static final String SHEARING_HARVEST_SPEED = "shearHarvSpd";
    public static final String SHOVEL_HARVEST_SPEED = "shovelHarvSpd";
    public static final String SHOVEL_ENERGY_CONSUMPTION = "shovelEnergyCon";
    public static final String BLADE_ENERGY = "spinBladeEnergyCon";
    public static final String BLADE_DAMAGE = "spinBladeDam";
    public static final String ENERGY_CONSUMPTION = "energyCon";
    public static final String HEAT_EMISSION = "heatEmission";
    public static final String PUNCH_ENERGY = "punchEnergyCon";
    public static final String PUNCH_DAMAGE = "meleeDamage";
    public static final String PUNCH_KNOCKBACK = "meleeKnockback";
    public static final String PLASMA_CANNON_ENERGY_PER_TICK = "plasmaEnergyPerTick";
    public static final String PLASMA_CANNON_DAMAGE_AT_FULL_CHARGE = "plasmaDamage";
    public static final String PLASMA_CANNON_EXPLOSIVENESS = "plasmaExplosiveness";
    public static final String RAILGUN_TOTAL_IMPULSE = "railgunTotalImpulse";
    public static final String RAILGUN_ENERGY_COST = "railgunEnergyCost;";
    public static final String RAILGUN_HEAT_EMISSION = "railgunHeatEm";
    public static final String TIMER = "cooldown";
    public static final String POWER = "power";
    public static final String VOLTAGE = "voltage";
    public static final String IMPACT = "impact";
    public static final String CARRY_THROUGH = "carryThrough";
    public static final String AMPERAGE = "amperage";
    public static final String CREEPER = "creeper";
    public static final String ADVANCED_COOLING_POWER = "advancedCoolingPower";
    public static final String OVERCLOCK = "overclock";
    public static final String FIELD_TELEPORTER_ENERGY_CONSUMPTION = "fieldTelEnergyCon";
    public static final String THRUST = "thrust";
    public static final String RANGE = "range";
    public static final String RADIUS = "radius";
    public static final String DIAMETER ="diameter";
    public static final String EFFICIENCY = "efficiency";
    public static final String RED = "red";
    public static final String GREEN = "green";
    public static final String BLUE = "blue";
    public static final String SPRINT_ASSIST = "sprintAssist";
    public static final String COMPENSATION = "compensation";
    public static final String WALKING_ASSISTANCE = "walkingAssist";
    public static final String VERTICALITY = "vertically";
    public static final String AOE_ENERGY_CONSUMPTION = "aoeEnergyCon";
    public static final String AOE_MINING_RADIUS = "aoeMiningDiameter";
    public static final String FORTUNE_ENERGY_CONSUMPTION = "fortuneEnCon";
    public static final String ENCHANTMENT_LEVEL ="enchLevel";
    public static final String FORTUNE_ENCHANTMENT_LEVEL ="fortuneLevel";
    public static final String SILK_TOUCH_ENERGY_CONSUMPTION = "silkTouchEnCon";

    /**
     * Module UnlocalizedNames
     */
    // Debug --------------------------------------------------------------------------------------
    public static final String MODULE_DEBUG = "debugModule";
    // Armor --------------------------------------------------------------------------------------

    public static final String MODULE_LEATHER_PLATING__DATANAME = "leatherPlating";
    public static final String MODULE_IRON_PLATING__DATANAME = "ironPlating";
    public static final String MODULE_DIAMOND_PLATING__DATANAME = "diamondPlating";
    public static final String MODULE_ENERGY_SHIELD__DATANAME = "energyShield";

    // Cosmetic -----------------------------------------------------------------------------------
    public static final String MODULE_TRANSPARENT_ARMOR__DATANAME = "transparentArmor";

    // Energy -------------------------------------------------------------------------------------
    public static final String MODULE_BATTERY_BASIC__DATANAME = "basicBattery";
    public static final String MODULE_BATTERY_ADVANCED__DATANAME = "advancedBattery";
    public static final String MODULE_BATTERY_ELITE__DATANAME = "eliteBattery";
    public static final String MODULE_BATTERY_ULTIMATE__DATANAME = "ultimateBattery";
    public static final String MODULE_SOLAR_GENERATOR__DATANAME = "solarGenerator";
    public static final String MODULE_ADVANCED_SOLAR_GENERATOR__DATANAME = "advSolarGenerator";
    public static final String MODULE_COAL_GEN__DATANAME = "coalGenerator";
    public static final String MODULE_KINETIC_GENERATOR__DATANAME = "kineticGenerator";
    public static final String MODULE_THERMAL_GENERATOR__DATANAME = "thermalGenerator";

    // Environmental ------------------------------------------------------------------------------
    public static final String MODULE_BASIC_COOLING_SYSTEM__DATANAME = "basicCoolingSystem";
    public static final String MODULE_ADVANCED_COOLING_SYSTEM__DATANAME = "advancedCoolingSystem";
    public static final String MODULE_AIRTIGHT_SEAL__DATANAME = "airtightSeal";
    public static final String MODULE_APIARIST_ARMOR__DATANAME = "apiaristArmor";
    public static final String MODULE_HAZMAT__DATANAME = "hazmat";
    public static final String MODULE_AUTO_FEEDER__DATANAME = "autoFeeder";
    public static final String MODULE_MOB_REPULSOR__DATANAME = "mobRepulsor";
    public static final String MODULE_WATER_ELECTROLYZER__DATANAME = "waterElectrolyzer";

    // Movement -----------------------------------------------------------------------------------
    public static final String MODULE_BLINK_DRIVE__DATANAME = "blinkDrive";
    public static final String MODULE_CLIMB_ASSIST__DATANAME = "climbAssist";
    public static final String MODULE_FLIGHT_CONTROL__DATANAME = "flightControl";
    public static final String MODULE_GLIDER__DATANAME = "glider";
    public static final String MODULE_JETBOOTS__DATANAME = "jetBoots";
    public static final String MODULE_JETPACK__DATANAME = "jetpack";
    public static final String MODULE_JUMP_ASSIST__DATANAME = "jumpAssist";
    public static final String MODULE_PARACHUTE__DATANAME = "parachute";
    public static final String MODULE_SHOCK_ABSORBER__DATANAME = "shockAbsorber";
    public static final String MODULE_SPRINT_ASSIST__DATANAME = "sprintAssist";
    public static final String MODULE_SWIM_BOOST__DATANAME = "swimAssist";

    // Special ------------------------------------------------------------------------------------
    public static final String MODULE_CLOCK__DATANAME = "clock";
    public static final String MODULE_COMPASS__DATANAME = "compass";
    public static final String MODULE_ACTIVE_CAMOUFLAGE__DATANAME = "invisibility";
    public static final String MODULE_MAGNET__DATANAME = "magnet";

    // Vision -------------------------------------------------------------------------------------
    public static final String BINOCULARS_MODULE__DATANAME = "binoculars";
    public static final String MODULE_NIGHT_VISION__DATANAME = "nightVision";
    public static final String MODULE_THAUM_GOGGLES__DATANAME = "aurameter";

    // Mining Enhancements ------------------------------------------------------------------------
    public static final String MODULE_AOE_PICK_UPGRADE__DATANAME = "aoePickUpgrade";
    public static final String MODULE_SILK_TOUCH__DATANAME = "silk_touch";
    public static final String MODULE_MAD__DATANAME = "madModule";
    public static final String MODULE_FORTUNE_DATANAME= "fortuneModule";

    // Tools --------------------------------------------------------------------------------------
    public static final String MODULE_APPENG_EC_WIRELESS_FLUID__DATANAME = "appengECWirelessFluid";
    public static final String MODULE_APPENG_WIRELESS__DATANAME = "appengWireless";
    public static final String MODULE_AQUA_AFFINITY__DATANAME = "aquaAffinity";
    public static final String MODULE_AXE__DATANAME = "axe";
    public static final String MODULE_CHISEL__DATANAME = "chisel";
    public static final String MODULE_DIAMOND_PICK_UPGRADE__DATANAME = "diamondPickUpgrade";
    public static final String MODULE_DIMENSIONAL_RIFT__DATANAME = "dimRiftGen";
    public static final String MODULE_FIELD_TINKER__DATANAME = "fieldTinkerer";
    public static final String MODULE_FLINT_AND_STEEL__DATANAME = "flintAndSteel";
    public static final String MODULE_GRAFTER__DATANAME = "grafter";
    public static final String MODULE_HOE__DATANAME = "hoe";
    public static final String MODULE_LEAF_BLOWER__DATANAME = "leafBlower";
    public static final String MODULE_LUX_CAPACITOR__DATANAME = "luxCapacitor";
    public static final String MODULE_FIELD_TELEPORTER__DATANAME = "mffsFieldTeleporter";
    public static final String MODULE_OC_TERMINAL__DATANAME = "ocTerminal";
    public static final String MODULE_PORTABLE_CRAFTING__DATANAME = "portableCraftingTable";
    public static final String MODULE_OMNIPROBE__DATANAME = "omniProbe";
    public static final String MODULE_OMNI_WRENCH__DATANAME = "omniwrench";
    public static final String MODULE_ORE_SCANNER__DATANAME = "oreScanner";
    public static final String MODULE_CM_PSD__DATANAME = "cmPSD";//"Personal Shrinking Device";
    public static final String MODULE_PICKAXE__DATANAME = "pickaxe";
    public static final String MODULE_REF_STOR_WIRELESS__DATANAME = "refinedStorageWirelessGrid";//"Refined Storage Wireless Grid";
    public static final String MODULE_SCOOP__DATANAME = "scoop";
    public static final String MODULE_SHEARS__DATANAME = "shears";
    public static final String MODULE_SHOVEL__DATANAME = "shovel";
    public static final String MODULE_TREETAP__DATANAME = "treetap";

    // Weapons ------------------------------------------------------------------------------------
    public static final String MODULE_BLADE_LAUNCHER__DATANAME = "bladeLauncher";
    public static final String MODULE_LIGHTNING__DATANAME = "lightningSummoner";
    public static final String MODULE_MELEE_ASSIST__DATANAME = "meleeAssist";
    public static final String MODULE_PLASMA_CANNON__DATANAME = "plasmaCannon";
    public static final String MODULE_RAILGUN__DATANAME = "railgun";
}