package net.machinemuse.powersuits.common.config;

import net.machinemuse.powersuits.api.constants.MPSConfigConstants;
import net.machinemuse.powersuits.api.constants.MPSModuleConstants;
import net.minecraftforge.common.config.Config;

import java.util.HashMap;
import java.util.Map;

import static net.machinemuse.numina.api.constants.NuminaConstants.*;
import static net.machinemuse.powersuits.api.constants.MPSModConstants.MODID;

@Config(modid = MODID, name = MPSConfigConstants.CONFIG_FILE)
public class MPSSettings {
    /**
     * The are all client side settings
     */
    public static HUD hud = new HUD();
    public static class HUD {
        //FIXME: This doesn't seem to even be used
        @Config.LangKey(MPSConfigConstants.CONFIG_HUD_USE_HUD)
        @Config.Comment("Use HUD for certain module (Auto Feeder, Compass, Clock, etc.")
        public static boolean useHUDStuff = true;

        @Config.LangKey(MPSConfigConstants.CONFIG_HUD_TOGGLE_MODULE_SPAM)
        @Config.Comment("Chat message when toggling module")
        public boolean toggleModuleSpam = false;


        @Config.LangKey(MPSConfigConstants.CONFIG_HUD_DISPLAY_HUD)
        @Config.Comment("Display HUD")
        public boolean keybindHUDon = true;


        @Config.LangKey(MPSConfigConstants.CONFIG_HUD_KEYBIND_HUD_X)
        @Config.Comment("x position")
        @Config.RangeDouble(min = 0)
        public double keybindHUDx = 8.0;


        @Config.LangKey(MPSConfigConstants.CONFIG_HUD_KEYBIND_HUD_Y)
        @Config.Comment("getY position")
        @Config.RangeDouble(min = 0)
        public double keybindHUDy = 32.0;

        @Config.LangKey(MPSConfigConstants.CONFIG_HUD_USE_GRAPHICAL_METERS)
        @Config.Comment("Use Graphical Meters")
        public static boolean useGraphicalMeters = true;
    }


    /**
     * A mixture of client and server side settings
     */
    public static General general = new General();
    public static class General {
        // Client side settings ------------------------------------------------------
        @Config.LangKey(MPSConfigConstants.CONFIG_GENERAL_USE_24_HOUR_CLOCK)
        @Config.Comment("Use a 24h clock instead of 12h")
        public boolean use24hClock = false;

        @Config.LangKey(MPSConfigConstants.CONFIG_GENERAL_ALLOW_CONFLICTING_KEYBINDS)
        @Config.Comment("Allow Conflicting Keybinds")
        public boolean allowConflictingKeybinds = true;

        // Server side settings -----------------------------------------------
        @Config.LangKey(MPSConfigConstants.CONFIG_GENERAL_USE_OLD_AUTOFEEDER)
        @Config.Comment("Use Old Auto Feeder Method")
        public static boolean useOldAutoFeeder = false;

        @Config.LangKey(MPSConfigConstants.CONFIG_GENERAL_MAX_FLYING_SPEED)
        @Config.Comment("Maximum flight speed (in m/s)")
        public double getMaximumFlyingSpeedmps = 25.0;

        //TODO: eleiminate
        @Config.LangKey(MPSConfigConstants.CONFIG_GENERAL_WEIGHT_LIMIT)
        @Config.Comment("Weight Limit (grams)")
        public static double getWeightCapacity = 25000.0;

        /**
         * The maximum amount of armor contribution allowed per armor piece. Total
         * armor when the full set is worn can never exceed 4 times this amount.
         */
        @Config.LangKey(MPSConfigConstants.CONFIG_GENERAL_GET_MAX_ARMOR_PER_PIECE)
        @Config.Comment("Maximum Armor per Piece")
        @Config.RangeDouble(min = 0, max = 8.0)
        @Config.RequiresWorldRestart
        public double getMaximumArmorPerPiece = 6.0;

        @Config.LangKey(MPSConfigConstants.CONFIG_GENERAL_SALVAGE_CHANCE)
        @Config.Comment("Chance of each item being returned when salvaged")
        @Config.RangeDouble(min = 0, max = 1.0)
        public double getSalvageChance = 0.9;

        //TODO: eleiminate
        @Config.LangKey(MPSConfigConstants.CONFIG_GENERAL_BASE_MAX_HEAT)
        @Config.Comment("ItemModuleBase Heat Cap")
        public double baseMaxHeat = 50.0;

    }

    /**
     * Currently maps need to be initialized and populated at runtime otherwise the values are not read from the config file
     *
     * TODO: move to server config
     */
    public static Modules modules = new Modules();
    public static class Modules {
        @Config.LangKey(MPSConfigConstants.CONFIG_MODULES)
        @Config.Comment("Whether or not specified module is allowed")
        public Map<String, Boolean> allowedModules = new HashMap<String, Boolean>() {{
            // Debug ----------------------------------------------------------------------
            put(MPSModuleConstants.MODULE_DEBUG, true);

            // Armor ----------------------------------------------------------------------
            put(MPSModuleConstants.MODULE_LEATHER_PLATING__DATANAME, true);
            put(MPSModuleConstants.MODULE_IRON_PLATING__DATANAME, true);
            put(MPSModuleConstants.MODULE_DIAMOND_PLATING__DATANAME, true);
            put(MPSModuleConstants.MODULE_ENERGY_SHIELD__DATANAME, true);

            // Cosmetic -------------------------------------------------------------------
            put(MPSModuleConstants.MODULE_TRANSPARENT_ARMOR__DATANAME, true);

            // Energy ---------------------------------------------------------------------
            put(MPSModuleConstants.MODULE_BATTERY_BASIC__DATANAME, true);
            put(MPSModuleConstants.MODULE_BATTERY_ADVANCED__DATANAME, true);
            put(MPSModuleConstants.MODULE_BATTERY_ELITE__DATANAME, true);
            put(MPSModuleConstants.MODULE_BATTERY_ULTIMATE__DATANAME, true);
            put(MPSModuleConstants.MODULE_ADVANCED_SOLAR_GENERATOR__DATANAME, true);
            put(MPSModuleConstants.MODULE_COAL_GEN__DATANAME, true);
            put(MPSModuleConstants.MODULE_KINETIC_GENERATOR__DATANAME, true);
            put(MPSModuleConstants.MODULE_SOLAR_GENERATOR__DATANAME, true);
            put(MPSModuleConstants.MODULE_THERMAL_GENERATOR__DATANAME, true);

            // Environmental --------------------------------------------------------------
            put(MPSModuleConstants.AIRTIGHT_SEAL_MODULE__DATANAME, true);
            put(MPSModuleConstants.MODULE_APIARIST_ARMOR__DATANAME, true);
            put(MPSModuleConstants.MODULE_AUTO_FEEDER__DATANAME, true);
            put(MPSModuleConstants.MODULE_COOLING_SYSTEM__DATANAME, true);
            put(MPSModuleConstants.MODULE_HAZMAT__DATANAME, true);
            put(MPSModuleConstants.MODULE_MOB_REPULSOR__DATANAME, true);
            put(MPSModuleConstants.MODULE_NITROGEN_COOLING_SYSTEM__DATANAME, true);
            put(MPSModuleConstants.MODULE_WATER_ELECTROLYZER__DATANAME, true);
            put(MPSModuleConstants.MODULE_WATER_TANK__DATANAME, true);

            // Movement -------------------------------------------------------------------
            put(MPSModuleConstants.MODULE_BLINK_DRIVE__DATANAME, true);
            put(MPSModuleConstants.MODULE_CLIMB_ASSIST__DATANAME, true);
            put(MPSModuleConstants.MODULE_FLIGHT_CONTROL__DATANAME, true);
            put(MPSModuleConstants.MODULE_GLIDER__DATANAME, true);
            put(MPSModuleConstants.MODULE_JETBOOTS__DATANAME, true);
            put(MPSModuleConstants.MODULE_JETPACK__DATANAME, true);
            put(MPSModuleConstants.MODULE_JUMP_ASSIST__DATANAME, true);
            put(MPSModuleConstants.MODULE_PARACHUTE__DATANAME, true);
            put(MPSModuleConstants.MODULE_SHOCK_ABSORBER__DATANAME, true);
            put(MPSModuleConstants.MODULE_SPRINT_ASSIST__DATANAME, true);
            put(MPSModuleConstants.MODULE_SWIM_BOOST__DATANAME, true);

            // Special --------------------------------------------------------------------
            put(MPSModuleConstants.MODULE_CLOCK__DATANAME, true);
            put(MPSModuleConstants.MODULE_COMPASS__DATANAME, true);
            put(MPSModuleConstants.MODULE_ACTIVE_CAMOUFLAGE__DATANAME, true);
            put(MPSModuleConstants.MODULE_MAGNET__DATANAME, true);

            // Vision ---------------------------------------------------------------------
            put(MPSModuleConstants.BINOCULARS_MODULE__DATANAME, true);
            put(MPSModuleConstants.MODULE_NIGHT_VISION__DATANAME, true);
            put(MPSModuleConstants.MODULE_THAUM_GOGGLES__DATANAME, true);// done via mod compat

            // Tools --------------------------------------------------------------------------------------
            put(MPSModuleConstants.MODULE_AOE_PICK_UPGRADE__DATANAME, true);
            put(MPSModuleConstants.MODULE_APPENG_EC_WIRELESS_FLUID__DATANAME, true);
            put(MPSModuleConstants.MODULE_APPENG_WIRELESS__DATANAME, true);
            put(MPSModuleConstants.MODULE_AQUA_AFFINITY__DATANAME, true);
            put(MPSModuleConstants.MODULE_AXE__DATANAME, true);
            put(MPSModuleConstants.MODULE_CHISEL__DATANAME, true);
            put(MPSModuleConstants.MODULE_DIAMOND_PICK_UPGRADE__DATANAME, true);
            put(MPSModuleConstants.MODULE_DIMENSIONAL_RIFT__DATANAME, true);
            put(MPSModuleConstants.MODULE_FIELD_TINKER__DATANAME, true);
            put(MPSModuleConstants.MODULE_FLINT_AND_STEEL__DATANAME, true);
            put(MPSModuleConstants.MODULE_GRAFTER__DATANAME, true);
            put(MPSModuleConstants.MODULE_HOE__DATANAME, true);
            put(MPSModuleConstants.MODULE_LEAF_BLOWER__DATANAME, true);
            put(MPSModuleConstants.MODULE_LUX_CAPACITOR__DATANAME, true);
            put(MPSModuleConstants.MODULE_FIELD_TELEPORTER__DATANAME, true);
            put(MPSModuleConstants.MODULE_MAD_MODULE__DATANAME, true);
            put(MPSModuleConstants.MODULE_OC_TERMINAL__DATANAME, true);
            put(MPSModuleConstants.MODULE_OMNIPROBE__DATANAME, true);
            put(MPSModuleConstants.MODULE_OMNI_WRENCH__DATANAME, true);
            put(MPSModuleConstants.MODULE_ORE_SCANNER__DATANAME, true);
            put(MPSModuleConstants.MODULE_CM_PSD__DATANAME, true);
            put(MPSModuleConstants.MODULE_PICKAXE__DATANAME, true);
            put(MPSModuleConstants.MODULE_PORTABLE_CRAFTING__DATANAME, true);
            put(MPSModuleConstants.MODULE_REF_STOR_WIRELESS__DATANAME, true);
            put(MPSModuleConstants.MODULE_SCOOP__DATANAME, true);
            put(MPSModuleConstants.MODULE_SHEARS__DATANAME, true);
            put(MPSModuleConstants.MODULE_SHOVEL__DATANAME, true);
            put(MPSModuleConstants.MODULE_TREETAP__DATANAME, true);

            // Weapons ------------------------------------------------------------------------------------
            put(MPSModuleConstants.MODULE_BLADE_LAUNCHER__DATANAME, true);
            put(MPSModuleConstants.MODULE_LIGHTNING__DATANAME, true);
            put(MPSModuleConstants.MODULE_MELEE_ASSIST__DATANAME, true);
            put(MPSModuleConstants.MODULE_PLASMA_CANNON__DATANAME, true);
            put(MPSModuleConstants.MODULE_RAILGUN__DATANAME, true);
        }};

        @Config.LangKey(MPSConfigConstants.CONFIG_MODULE_PROPERTY_DOUBLES)
        @Config.Comment("Value of specified property")
        public Map<String, Double> propertyDouble = new HashMap<String, Double>() {{

            }};


        @Config.LangKey(MPSConfigConstants.CONFIG_MODULE_PROPERTY_INTEGERS)
        @Config.Comment("Value of specified property")
        public Map<String, Integer> propertyInteger = new HashMap<String, Integer>() {{

        }};

    }

    //TODO: eleiminate; This whole thing is just temporary, I think :P
    public static Energy energy = new Energy();
    public static class Energy {
        // 1 RF = 0.1 MJ
        @Config.LangKey("mekRatio")
        @Config.Comment("Energy per Mekanism MJ")
        public double mekRatio = 0.1D;

        // 1 RF = 0.25 EU
        @Config.LangKey("ic2Ratio")
        @Config.Comment("Energy per IC2 EU")
        public double ic2Ratio = 0.25D;

        // 1 RF = 1 RS
        @Config.LangKey("refinedStorageRatio")
        @Config.Comment("Energy per RS")
        public double refinedStorageRatio = 1D;

        // 1 rf = 0.5 AE
        @Config.LangKey("ae2Ratio")
        @Config.Comment("Energy per AE")
        public double ae2Ratio = 0.5D;

        // (100KJ or 1M-RF)
        @Config.LangKey(CONFIG_TIER_1_ENERGY_LVL)
        @Config.Comment("Maximum amount of RF energy for Tier 1.")
        @Config.RangeInt(min = 0)
        public static int maxTier1 = (int) (1 * Math.pow(10, 6));

        // advanced capacitor (500KJ or 5M-RF)
        @Config.LangKey(CONFIG_TIER_2_ENERGY_LVL)
        @Config.Comment("Maximum amount of RF energy for Tier 2.")
        @Config.RangeInt(min = 0)
        public static int maxTier2 = (int) (5 * Math.pow(10, 6));

        // elite capacitor (5MJ or 50M-RF)
        @Config.LangKey(CONFIG_TIER_3_ENERGY_LVL)
        @Config.Comment("Maximum amount of RF energy for Tier 3.")
        @Config.RangeInt(min = 0)
        public static int maxTier3 = (int) (5 * Math.pow(10, 7));

        // ultimate capacitor (10MJ or 100M-RF)
        @Config.LangKey(CONFIG_TIER_4_ENERGY_LVL)
        @Config.Comment("Maximum amount of RF energy for Tier 4.")
        @Config.RangeInt(min = 0)
        public static int maxTier4 = (int) (1 * Math.pow(10, 8));
    }
}
