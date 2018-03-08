package net.machinemuse.powersuits.common.config;


import com.google.gson.*;
import jline.internal.Nullable;
import net.machinemuse.numina.api.module.IModule;
import net.machinemuse.numina.api.module.ModuleManager;
import net.machinemuse.numina.utils.MuseLogger;
import net.machinemuse.powersuits.api.constants.MPSConfigConstants;
import net.machinemuse.powersuits.api.constants.MPSModuleConstants;
import net.machinemuse.powersuits.api.constants.MPSResourceConstants;
import net.machinemuse.powersuits.client.render.modelspec.ModelSpecXMLReader;
import net.machinemuse.powersuits.common.InstallCost;
import net.machinemuse.powersuits.common.MPSItems;
import net.machinemuse.powersuits.common.ModCompatibility;
import net.machinemuse.powersuits.proxy.CommonProxy;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreDictionary;

import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static net.machinemuse.powersuits.api.constants.MPSModConstants.MODID;

//@Config(modid = MODID, name = MPSConfigConstants.CONFIG_FILE, category = "")
//@Config.LangKey(MPSConfigConstants.CONFIG_FILE)

@Config(modid = MODID, name = MPSConfigConstants.CONFIG_FILE)
public class MPSSettings {
    private MPSSettings() {}

    public static General general = new General();
    public static class General {
        // General
        @Config.LangKey(MPSConfigConstants.CONFIG_GENERAL_ALLOW_CONFLICTING_KEYBINDS)
        @Config.Comment("Allow Conflicting Keybinds")
//        @Config.RequiresWorldRestart
        public boolean allowConflictingKeybinds = true;

        @Config.LangKey(MPSConfigConstants.CONFIG_GENERAL_AUTO_EXTRACT_RECIPES)
        @Config.Comment("Auto-extract recipes")
//        @Config.RequiresWorldRestart
        public boolean autoExtractRecipes = false;

        /**
         * The maximum amount of armor contribution allowed per armor piece. Total
         * armor when the full set is worn can never exceed 4 times this amount.
         */
        @Config.LangKey(MPSConfigConstants.CONFIG_GENERAL_GET_MAX_ARMOR_PER_PIECE)
        @Config.Comment("Maximum Armor per Piece")
        @Config.RangeDouble(min = 0, max = 8.0)
//        @Config.RequiresWorldRestart
        public double getMaximumArmorPerPiece = 6.0;


        @Config.LangKey(MPSConfigConstants.CONFIG_GENERAL_USE_24_HOUR_CLOCK)
        @Config.Comment("Use a 24h clock instead of 12h")
        public boolean use24hClock = false;


        @Config.LangKey(MPSConfigConstants.CONFIG_GENERAL_MAX_FLYING_SPEED)
        @Config.Comment("Maximum flight speed (in m/s)")
        public double getMaximumFlyingSpeedmps = 25.0;


        //FIXME
        @Config.LangKey(MPSConfigConstants.CONFIG_GENERAL_BASE_MAX_HEAT)
        @Config.Comment("ItemModuleBase Heat Cap")
        public double baseMaxHeat = 50.0;


        @Config.LangKey(MPSConfigConstants.CONFIG_GENERAL_WEIGHT_LIMIT)
        @Config.Comment("Weight Limit (grams)")
        public static double getWeightCapacity = 25000.0;


        @Config.LangKey(MPSConfigConstants.CONFIG_GENERAL_SALVAGE_CHANCE)
        @Config.Comment("Chance of each item being returned when salvaged")
        @Config.RangeDouble(min = 0, max = 1.0)
        public double getSalvageChance = 0.9;


        @Config.LangKey(MPSConfigConstants.CONFIG_GENERAL_USE_ADVANCED_ORE_SCANNER_MESSAGE)
        @Config.Comment("Use Detailed Ore Scanner Message")
        public static boolean useAdvancedOreScannerMessage = true;


        @Config.LangKey(MPSConfigConstants.CONFIG_GENERAL_USE_OLD_AUTOFEEDER)
        @Config.Comment("Use Old Auto Feeder Method")
        public static boolean useOldAutoFeeder = false;


        @Config.LangKey(MPSConfigConstants.CONFIG_GENERAL_USE_CHEATY_LEATHER)
        @Config.Comment("Use Cheaty Leather Recipe (Requires Thermal Expansion)")
        public static boolean useCheatyLeatherRecipe = true;


        @Config.LangKey(MPSConfigConstants.CONFIG_GENERAL_USE_HUD)
        @Config.Comment("Use HUD for certain module (Auto Feeder, Compass, Clock, etc.")
        public static boolean useHUDStuff = true;
    }

    public static final Inventory inventory = new Inventory();
    public static  class Inventory {
        @Config.LangKey(MPSConfigConstants.CONFIG_GENERAL_MAX_MODULES_POWER_FIST)
        @Config.Comment("Module Count Limit")
        public static int maxModuleCountPowerFist = 30;

        @Config.LangKey(MPSConfigConstants.CONFIG_GENERAL_MAX_MODULES_ARMOR_HELMET)
        @Config.Comment("Weight Limit (grams)")
        public static int maxModuleCountArmorHelmet = 30;

        @Config.LangKey(MPSConfigConstants.CONFIG_GENERAL_MAX_MODULES_ARMOR_CHESTPLATE)
        @Config.Comment("Weight Limit (grams)")
        public static int maxModuleCountArmorChest = 30;

        @Config.LangKey(MPSConfigConstants.CONFIG_GENERAL_MAX_MODULES_ARMOR_LEGGINGS)
        @Config.Comment("Weight Limit (grams)")
        public static int maxModuleCountArmorLegs = 30;

        @Config.LangKey(MPSConfigConstants.CONFIG_GENERAL_MAX_MODULES_ARMOR_BOOTS)
        @Config.Comment("Weight Limit (grams)")
        public static int maxModuleCountArmorFeet = 30;
    }

    /*
    1 MJ = 25 J = 10 RF = 2.5 EU
    1 J = 0.04 MJ = 0.4 RF = 0.1 EU
    1 RF = 0.1 MJ = 2.5 J = 0.25 EU
    1 EU = 10 J = 0.4 MJ = 4 RF
     */
//    @Config.RequiresWorldRestart
    public static final Energy energy = new Energy();
    public static class Energy {
        // basic capacitor (100KJ or 1M-RF)
        @Config.LangKey(MPSConfigConstants.CONFIG_MAX_ENERGY_BASIC_BATTERY)
        @Config.Comment("Amount of RF energy that the Basic Battery can store.")
        @Config.RangeInt(min = 0)
        public int maxEnergyBasicBattery = (int) (1 * Math.pow(10, 6));

        // advanced capacitor (500KJ or 5M-RF)
        @Config.LangKey(MPSConfigConstants.CONFIG_MAX_ENERGY_ADVANCED_BATTERY)
        @Config.Comment("Amount of RF energy that the Advanced Battery can store.")
        @Config.RangeInt(min = 0)
        public int maxEnergyAdvancedBattery = (int) (5 * Math.pow(10, 6));

        // elite capacitor (5MJ or 50M-RF)
        @Config.LangKey(MPSConfigConstants.CONFIG_MAX_ENERGY_ELITE_BATTERY)
        @Config.Comment("Amount of RF energy that the Elite Battery can store.")
        @Config.RangeInt(min = 0)
        public int maxEnergyEliteBattery = (int) (5 * Math.pow(10, 7));

        // ultimate capacitor (10MJ or 100M-RF)
        @Config.LangKey(MPSConfigConstants.CONFIG_MAX_ENERGY_ULTIMATE_BATTERY)
        @Config.Comment("Amount of RF energy that the Ultimate Battery can store.")
        @Config.RangeInt(min = 0)
        public int maxEnergyUltimateBattery = (int) (1 * Math.pow(10, 8));


        @Config.LangKey(MPSConfigConstants.CONFIG_RF_ENERGY_PER_IC2_EU)
        @Config.Comment("RF Energy per IC2 EU")
//        @Config.RangeDouble(min = 0)
        public double rfEnergyPerIC2EU = 0.25;
    }

    /**
     * Mass of equipment (best guess)
     *
     * Power Fist... 15Kg
     * Helmet....... 10kG
     * ChestPlate .. 100Kg
     * Leggings .... 40Kg
     * Boots........ 10Kg
     *
     * Max temperature 60 Degrees Celsius
     * 1J change 1Kg 1C degree
     *
     *
     */
//    @Config.RequiresWorldRestart
    public static final Heat heat = new Heat();
    public static class Heat {
        private static final int mJ = 1000;
        // Note: all heat in mJ (millijoules) 1000 mJ = 1J
        // maximum temperature before causing injury
        static final int MAX_TEMPERATURE = (60 * mJ);
        // based on Power Fist with mass of 15Kg
        @Config.LangKey(MPSConfigConstants.CONFIG_MAX_HEAT_POWERFIST)
        @Config.Comment("Maximum heat in milliJoules before causing injury.")
        @Config.RangeInt(min = 0)
        public int maxHeatPowerFist = MAX_TEMPERATURE * 15;

        // based on Power Armor Helmet with mass of 10Kg
        @Config.LangKey(MPSConfigConstants.CONFIG_MAX_HEAT_HELMET)
        @Config.Comment("Maximum heat in milliJoules before causing injury.")
        @Config.RangeInt(min = 0)
        public int maxHeatPowerArmorHelmet = MAX_TEMPERATURE * 10;

        // based on Power Armor Chestplate with mass of 100Kg
        @Config.LangKey(MPSConfigConstants.CONFIG_MAX_HEAT_CHESTPLATE)
        @Config.Comment("Maximum heat in milliJoules before causing injury.")
        @Config.RangeInt(min = 0)
        public int maxHeatPowerArmorChestplate = MAX_TEMPERATURE * 100;

        // based on Power Armor Leggings with mass of 40Kg
        @Config.LangKey(MPSConfigConstants.CONFIG_MAX_HEAT_LEGGINGS)
        @Config.Comment("Maximum heat in milliJoules before causing injury.")
        @Config.RangeInt(min = 0)
        public int maxHeatPowerArmorLeggings = MAX_TEMPERATURE * 40;

        // based on Power Armor Helmet with mass of 40Kg
        @Config.LangKey(MPSConfigConstants.CONFIG_MAX_HEAT_BOOTS)
        @Config.Comment("Maximum heat in milliJoules before causing injury.")
        public int maxHeatPowerArmorBoots = MAX_TEMPERATURE * 10;
    }

    public static HUD hud = new HUD();
    public static class HUD {
        @Config.LangKey(MPSConfigConstants.CONFIG_HUD_TOGGLE_MODULE_SPAM)
        @Config.Comment("Chat message when toggling module")
//        @Config.RequiresWorldRestart
        public boolean toggleModuleSpam = false;


        @Config.LangKey(MPSConfigConstants.CONFIG_HUD_DISPLAY_HUD)
        @Config.Comment("Display HUD")
//        @Config.RequiresWorldRestart
        public boolean keybindHUDon = true;


        @Config.LangKey(MPSConfigConstants.CONFIG_HUD_KEYBIND_HUD_X)
        @Config.Comment("x position")
        @Config.RangeDouble(min = 0)
//        @Config.RequiresWorldRestart
        public double keybindHUDx = 8.0;


        @Config.LangKey(MPSConfigConstants.CONFIG_HUD_KEYBIND_HUD_Y)
        @Config.Comment("y position")
        @Config.RangeDouble(min = 0)
//        @Config.RequiresWorldRestart
        public double keybindHUDy = 32.0;

        @Config.LangKey(MPSConfigConstants.CONFIG_HUD_USE_GRAPHICAL_METERS)
        @Config.Comment("Use Graphical Meters")
//        @Config.RequiresWorldRestart
        public static boolean useGraphicalMeters = true;

    }

    public static final Graphics graphics = new Graphics();
    public static class Graphics {
        @Config.LangKey(MPSConfigConstants.CONFIG_GRAPHICS_GLOW_MULTIPLIER)
        @Config.Comment("Bloom Multiplier")
        public int glowMultiplier = 3;

        @Config.LangKey(MPSConfigConstants.CONFIG_GRAPHICS_USE_SHADERS)
        @Config.Comment("Use Pixel/Vertex Shaders")
        public boolean useShaders = true;
    }

    public static Font font = new Font();
    public static class Font {
        @Config.LangKey(MPSConfigConstants.CONFIG_FONT_USE_CUSTOM_FONTS)
        @Config.Comment("Use Custom Font Engine")
        public boolean useCustomFonts = true;


        @Config.LangKey(MPSConfigConstants.CONFIG_FONT_DETAIL_MULTIPLIER)
        @Config.Comment("Font Detail Multiplier")
        public double fontDetail = 4;


        @Config.LangKey(MPSConfigConstants.CONFIG_FONT_URI)
        @Config.Comment("Font URI")
        public String fontURI = MPSResourceConstants.RESOURCE_PREFIX + "fonts/cra.ttf";


        @Config.LangKey(MPSConfigConstants.CONFIG_FONT_NAME)
        @Config.Comment("Native Font Name (Overrides URI)")
        public String fontName = "";

        @Config.LangKey(MPSConfigConstants.CONFIG_USE_FONT_ANTI_ALIASING)
        @Config.Comment("Font Anti-Aliasing")
        public boolean fontAntiAliasing = false;
    }

    /**
     * Model Settings
     */
    public static ModelConfig modelconfig = new ModelConfig();
    public static class ModelConfig{
        @Config.Comment("Use this to enable model transform mapping from keyboard")
        public boolean modelSetup = false;

        @Config.Comment("Enable high polly armor models")
        public boolean allowHighPollyArmorModels = false;

        @Config.Comment("Enable custom high polly armor models")
        public boolean allowCustomHighPollyArmor = false;

        // TODO: ? this would use boxes but ... meh
        @Config.Comment("Enable high polly PowerFist models")
        public boolean allowHighPollyPowerFistModels = false;

        @Config.Comment("Enable custom PowerFist models")
        public boolean allowCustomPowerFistModels = false;

        @Config.Comment("Enable custom High Polly PowerFist models")
        public boolean allowCustomHighPollyPowerFistModels = false;
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
        public Map<String, Boolean> allowedModules = //new HashMap<>();
                new HashMap<String, Boolean>() {{
                    // Debug ----------------------------------------------------------------------
                    put("item." + MPSModuleConstants.MODULE_DEBUG, true);

                    // Armor ----------------------------------------------------------------------
                    put("item." + MPSModuleConstants.MODULE_LEATHER_PLATING, true);
                    put("item." + MPSModuleConstants.MODULE_IRON_PLATING, true);
                    put("item." + MPSModuleConstants.MODULE_DIAMOND_PLATING, true);
                    put("item." + MPSModuleConstants.MODULE_ENERGY_SHIELD, true );

                    // Cosmetic -------------------------------------------------------------------
                    put("item." + MPSModuleConstants.MODULE_CITIZEN_JOE_STYLE, true );
                    put("item." + MPSModuleConstants.MODULE_GLOW, true );
                    put("item." + MPSModuleConstants.MODULE_HIGH_POLY_ARMOR, true );
                    put("item." + MPSModuleConstants.MODULE_TINT, true );
                    put("item." + MPSModuleConstants.MODULE_TRANSPARENT_ARMOR, true );

                    // Energy ---------------------------------------------------------------------
                    put("item." + MPSModuleConstants.MODULE_BATTERY_ADVANCED, true );
                    put("item." + MPSModuleConstants.MODULE_ADVANCED_SOLAR_GENERATOR, true );
                    put("item." + MPSModuleConstants.MODULE_BATTERY_BASIC, true );
                    put("item." + MPSModuleConstants.MODULE_COAL_GEN, true );
                    put("item." + MPSModuleConstants.MODULE_BATTERY_ELITE, true );
                    put("item." + MPSModuleConstants.MODULE_KINETIC_GENERATOR, true );
                    put("item." + MPSModuleConstants.MODULE_SOLAR_GENERATOR, true );
                    put("item." + MPSModuleConstants.MODULE_THERMAL_GENERATOR, true );
                    put("item." + MPSModuleConstants.MODULE_BATTERY_ULTIMATE, true );

                    // Environmental --------------------------------------------------------------
                    put("item." + MPSModuleConstants.AIRTIGHT_SEAL_MODULE, true );
                    put("item." + MPSModuleConstants.MODULE_APIARIST_ARMOR, true );
                    put("item." + MPSModuleConstants.MODULE_AUTO_FEEDER, true );
                    put("item." + MPSModuleConstants.MODULE_COOLING_SYSTEM, true );
                    put("item." + MPSModuleConstants.MODULE_HAZMAT, true );
                    put("item." + MPSModuleConstants.MODULE_HEAT_SINK, true );
                    put("item." + MPSModuleConstants.MODULE_MECH_ASSISTANCE, true );
                    put("item." + MPSModuleConstants.MODULE_MOB_REPULSOR, true );
                    put("item." + MPSModuleConstants.MODULE_NITROGEN_COOLING_SYSTEM, true );
                    put("item." + MPSModuleConstants.MODULE_WATER_ELECTROLYZER, true );
                    put("item." + MPSModuleConstants.MODULE_WATER_TANK, true );

                    // Movement -------------------------------------------------------------------
                    put("item." + MPSModuleConstants.MODULE_BLINK_DRIVE, true );
                    put("item." + MPSModuleConstants.MODULE_CLIMB_ASSIST, true );
                    put("item." + MPSModuleConstants.MODULE_FLIGHT_CONTROL, true );
                    put("item." + MPSModuleConstants.MODULE_GLIDER, true );
                    put("item." + MPSModuleConstants.MODULE_JETBOOTS, true );
                    put("item." + MPSModuleConstants.MODULE_JETPACK, true );
                    put("item." + MPSModuleConstants.MODULE_JUMP_ASSIST, true );
                    put("item." + MPSModuleConstants.MODULE_PARACHUTE, true );
                    put("item." + MPSModuleConstants.MODULE_SHOCK_ABSORBER, true );
                    put("item." + MPSModuleConstants.MODULE_SPRINT_ASSIST, true );
                    put("item." + MPSModuleConstants.MODULE_SWIM_BOOST, true );

                    // Special --------------------------------------------------------------------
                    put("item." + MPSModuleConstants.MODULE_CLOCK, true );
                    put("item." + MPSModuleConstants.MODULE_COMPASS, true );
                    put("item." + MPSModuleConstants.MODULE_ACTIVE_CAMOUFLAGE, true );
                    put("item." + MPSModuleConstants.MODULE_MAGNET, true );

                    // Vision ---------------------------------------------------------------------
                    put("item." + MPSModuleConstants.BINOCULARS_MODULE, true );
                    put("item." + MPSModuleConstants.MODULE_NIGHT_VISION, true ); // done via mod compat
                    put("item." + MPSModuleConstants.MODULE_THAUM_GOGGLES, true );

                    // Tools --------------------------------------------------------------------------------------
                    put("item." + MPSModuleConstants.MODULE_AOE_PICK_UPGRADE, true );
                    put("item." + MPSModuleConstants.MODULE_APPENG_EC_WIRELESS_FLUID, true );
                    put("item." + MPSModuleConstants.MODULE_APPENG_WIRELESS, true );
                    put("item." + MPSModuleConstants.MODULE_AQUA_AFFINITY, true );
                    put("item." + MPSModuleConstants.MODULE_AXE, true );
                    put("item." + MPSModuleConstants.MODULE_CHISEL, true );
                    put("item." + MPSModuleConstants.MODULE_DIAMOND_PICK_UPGRADE, true );
                    put("item." + MPSModuleConstants.MODULE_DIMENSIONAL_RIFT, true );
                    put("item." + MPSModuleConstants.MODULE_FIELD_TINKER, true );
                    put("item." + MPSModuleConstants.MODULE_FLINT_AND_STEEL, true );
                    put("item." + MPSModuleConstants.MODULE_GRAFTER, true );
                    put("item." + MPSModuleConstants.MODULE_HOE, true );
                    put("item." + MPSModuleConstants.MODULE_LEAF_BLOWER, true );
                    put("item." + MPSModuleConstants.MODULE_LUX_CAPACITOR, true );
                    put("item." + MPSModuleConstants.MODULE_FIELD_TELEPORTER, true );
                    put("item." + MPSModuleConstants.MODULE_OC_TERMINAL, true );
                    put("item." + MPSModuleConstants.MODULE_OMNIPROBE, true );
                    put("item." + MPSModuleConstants.MODULE_OMNI_WRENCH, true );
                    put("item." + MPSModuleConstants.MODULE_ORE_SCANNER, true );
                    put("item." + MPSModuleConstants.MODULE_CM_PSD, true );
                    put("item." + MPSModuleConstants.MODULE_PICKAXE, true );
                    put("item." + MPSModuleConstants.MODULE_PORTABLE_CRAFTING, true );
                    put("item." + MPSModuleConstants.MODULE_REF_STOR_WIRELESS, true );
                    put("item." + MPSModuleConstants.MODULE_SCOOP, true );
                    put("item." + MPSModuleConstants.MODULE_SHEARS, true );
                    put("item." + MPSModuleConstants.MODULE_SHOVEL, true );
                    put("item." + MPSModuleConstants.MODULE_TREETAP, true );

                    // Weapons ------------------------------------------------------------------------------------
                    put("item." + MPSModuleConstants.MODULE_BLADE_LAUNCHER , true );
                    put("item." + MPSModuleConstants.MODULE_LIGHTNING, true );
                    put("item." + MPSModuleConstants.MODULE_MELEE_ASSIST, true );
                    put("item." + MPSModuleConstants.MODULE_PLASMA_CANNON, true );
                    put("item." + MPSModuleConstants.MODULE_RAILGUN, true );
                }};


        public boolean getModuleAllowedorDefault(String name, boolean allowed) {
            if (!allowedModules.containsKey(name))
                allowedModules.put(name, allowed);
            return allowedModules.get(name);
        }





        public boolean isModuleAllowed(String name) {
            if (!allowedModules.containsKey(name))
                allowedModules.put(name, true);
            return allowedModules.get(name);
        }


        @Config.LangKey(MPSConfigConstants.CONFIG_MODULE_PROPERTY_DOUBLES)
        @Config.Comment("Value of specified property")
        public Map<String, Double> propertyDouble = new HashMap<>();

        public double getPropertyDoubleOrDefault(String name, double value) {
            System.out.println("property: " + name);
            System.out.println("value: " + value);

            if (propertyDouble.isEmpty() ||!propertyDouble.containsKey(name))
                propertyDouble.put(name, value);
            return propertyDouble.get(name);
        }

        public double getPropertyDoubleOr(String name) {
            if (propertyDouble.isEmpty() ||!propertyDouble.containsKey(name))
                propertyDouble.put(name, 1.0);
            return propertyDouble.get(name);
        }

        @Config.LangKey(MPSConfigConstants.CONFIG_MODULE_PROPERTY_INTEGERS)
        @Config.Comment("Value of specified property")
        public Map<String, Integer> propertyInteger = new HashMap<>();

        public int getPropertyIntegerOrDefault(String name, int value) {
            if (propertyInteger.isEmpty() ||!propertyInteger.containsKey(name))
                propertyInteger.put(name, value);
            return propertyInteger.get(name);
        }
    }

    private static MPSServerSettings serverSettings;
    public static void setServerSettings(@Nullable final MPSServerSettings serverSettings) {
        MPSSettings.serverSettings = serverSettings;
    }

    @Nullable
    public static final MPSServerSettings getServerSettings() {
        return serverSettings;
    }


//========================================================




    public static void addModule(IModule module) {
//        if(module.isEmpty())
//            module.put(module.getDataName(), true);
//        else if(!module.keySet().contains(module.getDataName()))
//            module.put(module.getDataName(), true);
//        else
        ModuleManager.getInstance().addModule(new ItemStack((Item)module));
//        try {
//            getOrDefaultModuleAllowed(module.getDataName(), true);
//        } catch (Exception e) {
//            System.out.println("failed to add module: " + e.getMessage());
//        }
    }

    /**
     * Load all the module in the config file into memory. Eventually. For now,
     * they are hardcoded.
     */
    public static void loadPowerModules() {
        MPSItems mpsItems = MPSItems.getInstance();
        // Debug ----------------------------------------------------------------------------------
        addModule((IModule) mpsItems.module_debug);

        // Armor ----------------------------------------------------------------------------------
        addModule((IModule) mpsItems.module_leather_plating);
        addModule((IModule) mpsItems.module_iron_plating);
        addModule((IModule) mpsItems.module_diamond_plating);

        // Skip these
        // Cosmetic -------------------------------------------------------------------------------
        addModule((IModule) mpsItems.module_citizen_joe);
        addModule((IModule) mpsItems.module_cosmetic_glow);
        addModule((IModule) mpsItems.module_3d_armor);
        addModule((IModule) mpsItems.module_tint);
        addModule((IModule) mpsItems.module_transparent_armor);

        // Energy ---------------------------------------------------------------------------------
        addModule((IModule) mpsItems.module_basic_battery);
        addModule((IModule) mpsItems.module_advanced_battery);
        addModule((IModule) mpsItems.module_elite_battery);
        addModule((IModule) mpsItems.module_ultimate_battery);
        addModule((IModule) mpsItems.module_solar_generator);
        addModule((IModule) mpsItems.module_advanced_generator);
        addModule((IModule) mpsItems.module_coal_generator);
        addModule((IModule) mpsItems.module_kinetic_generator);
        addModule((IModule) mpsItems.module_stirling_generator);

        // Environmental --------------------------------------------------------------------------
        addModule((IModule) mpsItems.module_autofeeder);
//        addModule((IModule) mpsItems.module_airtight_seal); // mod compat
//        addModule((IModule) mpsItems.module_apiarist_armor); // mod compat
        addModule((IModule) mpsItems.module_cooling_system);
//        addModule((IModule) mpsItems.module_hazmat); // done via mod compat
        addModule((IModule) mpsItems.module_mechanical_assistance);
        addModule((IModule) mpsItems.module_mob_repulsor);
        addModule((IModule) mpsItems.module_heat_sink);
        addModule((IModule) mpsItems.module_nitrogen_cooling_system);
        addModule((IModule) mpsItems.module_water_electrolyzer);
        addModule((IModule) mpsItems.module_water_tank);

        // Movement -------------------------------------------------------------------------------
        addModule((IModule) mpsItems.module_blink_drive);
        addModule((IModule) mpsItems.module_climb_assist);
        addModule((IModule) mpsItems.module_flight_control);
        addModule((IModule) mpsItems.module_glider);
        addModule((IModule) mpsItems.module_jet_boots);
        addModule((IModule) mpsItems.module_jetpack);
        addModule((IModule) mpsItems.module_jump_assist);
        addModule((IModule) mpsItems.module_parachute);
        addModule((IModule) mpsItems.module_shock_absorber);
        addModule((IModule) mpsItems.module_sprint_assist);
        addModule((IModule) mpsItems.module_swim_boost);

        // Special --------------------------------------------------------------------------------
        addModule((IModule) mpsItems.module_clock);
        addModule((IModule) mpsItems.module_compass);
        addModule((IModule) mpsItems.module_invisibility);
        addModule((IModule) mpsItems.module_magnet);

        // Vision ---------------------------------------------------------------------------------
        addModule((IModule) mpsItems.module_binnoculars);
//        addModule((IModule) mpsItems.module_night_vision); // done via mod compat
        addModule((IModule) mpsItems.module_aurameter);

        // Tools ----------------------------------------------------------------------------------
        addModule((IModule) mpsItems.module_pickaxe);
        addModule((IModule) mpsItems.module_diamond_pick_upgrade);
        addModule((IModule) mpsItems.module_aoe_pick_upgrade);
        addModule((IModule) mpsItems.module_axe);
        addModule((IModule) mpsItems.module_shears);
        addModule((IModule) mpsItems.module_shovel);
//        addModule((IModule) mpsItems.module_appeng_ec_wireless_fluid); // mod compat
//        addModule((IModule) mpsItems.module_appeng_wireless); // mod compat
        addModule((IModule) mpsItems.module_aqua_affinity);
//        addModule((IModule) mpsItems.module_chisel); // mod compat
        addModule((IModule) mpsItems.module_dim_rift_gen);
        addModule((IModule) mpsItems.module_field_tinkerer);
        addModule((IModule) mpsItems.module_flint_and_steel);
        addModule((IModule) mpsItems.module_grafter);
        addModule((IModule) mpsItems.module_hoe);
        addModule((IModule) mpsItems.module_leafblower);
        addModule((IModule) mpsItems.module_luxcaplauncher);
        addModule((IModule) mpsItems.module_mffsfieldteleporter);
//    module_octerminal);
//        addModule((IModule) mpsItems.module_omniprobe); // mod compat
//        addModule((IModule) mpsItems.module_omniwrench); // done via mod compat
        addModule((IModule) mpsItems.module_ore_scanner);
//        addModule((IModule) mpsItems.module_cmpsd); // mod compat
        addModule((IModule) mpsItems.module_portable_crafting_table);
//        addModule((IModule) mpsItems.module_refinedstoragewirelessgrid); // mod compat
//        addModule((IModule) mpsItems.module_scoop); // mod compat
        addModule((IModule) mpsItems.module_tree_tap);
        addModule((IModule) mpsItems.module_refinedstoragewirelessgrid);
        addModule((IModule) mpsItems.module_scoop);
//        addModule((IModule) mpsItems.module_tree_tap); // done via mod compat

        // Weapon ---------------------------------------------------------------------------------
        addModule((IModule) mpsItems.module_blade_launcher);
        addModule((IModule) mpsItems.module_lightning);
        addModule((IModule) mpsItems.module_melee_assist);
        addModule((IModule) mpsItems.module_plasma_cannon);
        addModule((IModule) mpsItems.module_railgun);
    }

    public static void addCustomInstallCosts() {
        try {
            File installCostFile = new File(configFolder, "custominstallcosts.json");
            Gson gson = new Gson();
            if (installCostFile.exists()) {
                DataInputStream is = new DataInputStream(new FileInputStream(installCostFile));
                byte[] bytes = new byte[(int) installCostFile.length()];
                is.readFully(bytes);
                String string = Charset.defaultCharset().decode(ByteBuffer.wrap(bytes)).toString();
                is.close();

                MuseLogger.logDebug(string);
                InstallCost[] costs = (InstallCost[])gson.fromJson(string, (Class)InstallCost[].class);
                for(InstallCost cost: costs) {
                    String moduleName = cost.moduleName;
//                    Item item = GameRegistry.findItem(cost.modId, cost.itemName);
                    Item item = Item.REGISTRY.getObject(new ResourceLocation(cost.modId, cost.itemName));

                    if(item != null) {
                        int metadata = (cost.itemMetadata == null) ? 0 : cost.itemMetadata;
                        int quantity = (cost.itemQuantity == null) ? 1 : cost.itemQuantity;
                        ItemStack stack = new ItemStack(item, quantity, metadata);
                        if(stack != null) {
                            ModuleManager.getInstance().addCustomInstallCost(moduleName, stack);
                        } else {
                            MuseLogger.logError("Invalid Itemstack in custom install cost. Module [" + cost.moduleName + "], item [" + cost.itemName + "]");
                        }
                    } else {
                        MuseLogger.logError("Invalid Item in custom install cost. Module [" + cost.moduleName + "], item [" + cost.itemName + "]");
                    }
                }
            } else {
                installCostFile.createNewFile();
                InstallCost examplecost = new InstallCost();
                examplecost.moduleName = "Shock Absorber";
                examplecost.itemName = "wool";
                examplecost.modId = "minecraft";
                examplecost.itemQuantity = 2;
                examplecost.itemMetadata = 0;
                InstallCost examplecost2 = new InstallCost();
                examplecost2.moduleName = "Shock Absorber";
                examplecost2.itemName = "powerArmorComponent";
                examplecost2.modId = "powersuits";
                examplecost2.itemQuantity = 2;
                examplecost2.itemMetadata = 2;
                InstallCost[] output = { examplecost, examplecost2 };
                String json = gson.toJson((Object)output);
                PrintWriter dest = new PrintWriter(installCostFile);
                dest.write(json);
                dest.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    public static Map<Map<ResourceLocation, Integer>, Integer> getOreValues() {
        return readOreValues();
    }










    static File configFolder = null;
    static File recipeFolder = null;
    static File modelSpecFolder = null;

    public static void setConfigFolderBase(FMLPreInitializationEvent event) {
        configFolder = new File(event.getModConfigurationDirectory().getAbsolutePath() + "/machinemuse");
        recipeFolder = new File(configFolder.getAbsolutePath().concat("/recipes/"));
        modelSpecFolder = new File(configFolder.getAbsolutePath().concat("/modelspec/"));
    }

    public static void copyRecipe(String inFile) {
        copyFile(inFile, new StringBuilder(recipeFolder.getAbsolutePath()).append(inFile).toString());
    }

    public static void copyFile(String inFile, String outFile) {
        InputStream src = MPSSettings.class.getResourceAsStream(inFile);
        File dest = new File(outFile);
        if(!dest.exists()) {
            try {
                Files.copy(src, dest.toPath());
            } catch (Exception e) {
                MuseLogger.logException("failed to copy file", e);
            }
        }
    }

    /*
     * Ore Value file parser for the OreScannerModule
     */
    private static Map<Map<ResourceLocation, Integer>, Integer> readOreValues() {
        Map<Map<ResourceLocation, Integer>, Integer> oreValues = new HashMap<>();
        String oreValuesFileName = "oreValues.json";
        try {
            File oreValuesFile = new File(configFolder, oreValuesFileName);
            Gson gson = new Gson();
            // if file does not exist, extract it
            if (!oreValuesFile.exists()) {
                InputStream src = CommonProxy.class.getClassLoader().getResourceAsStream(oreValuesFileName);
                try {
                    Files.copy(src, oreValuesFile.toPath());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (oreValuesFile.exists()) {
                DataInputStream is = new DataInputStream(new FileInputStream(oreValuesFile));
                byte[] bytes = new byte[(int) oreValuesFile.length()];
                is.readFully(bytes);
                String string = Charset.defaultCharset().decode(ByteBuffer.wrap(bytes)).toString();
                is.close();
                JsonParser parser = new JsonParser();
                JsonArray ja = (JsonArray) parser.parse(string);
                Map<ResourceLocation, Integer> regNameMeta;
                for (JsonElement jo : ja) {
                    JsonObject j = (JsonObject) jo;
                    int value;
                    int meta;

                    // every entry should have a value
                    value = j.get("value").getAsInt();

                    // check if this is an oredict entry
                    JsonElement oredictName = j.get("oredictName");
                    if (oredictName != null) {
                        NonNullList<ItemStack> stacks = OreDictionary.getOres(oredictName.getAsString());
                        for (ItemStack itemStack : stacks) {
                            regNameMeta = new HashMap<>();
                            meta = itemStack.getItemDamage();
                            ResourceLocation regName = itemStack.getItem().getRegistryName();
                            regNameMeta.put(regName, meta);
                            oreValues.put(regNameMeta, value);
                        }
                    } else {
                        // meta values are optional. Internally they are treated as 0
                        meta = (j.get("meta") != null) ? j.get("meta").getAsInt(): 0;

                        // if not an oredict entry then it should be a registry entry
                        JsonElement registryName = j.get("registryName");
                        regNameMeta = new HashMap<>();
                        regNameMeta.put(new ResourceLocation(registryName.getAsString()), meta);
                        oreValues.put(regNameMeta, value);
                    }
                }
                MuseLogger.logDebug(string);
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
        return oreValues;
    }

    public static void extractRecipes() {
        String key = "Auto-extract recipes";
        //TODO: fix loading logic to account for first run
        if (general.autoExtractRecipes) {
            general.autoExtractRecipes = false;
            boolean found=false;
            // Thermal Expansion
            if(ModCompatibility.isThermalExpansionLoaded()) {
                found=true;
                copyRecipe("mps-thermalexpansion.recipes");
            }

            if (ModCompatibility.isTechRebornLoaded()) {
                found=true;
                copyRecipe("mps-TechReborn.recipes");

                // GregTech 2
            } else if (ModCompatibility.isGregTechLoaded()) {
                found=true;
                copyRecipe("mps-GT5.recipes");

                // Industrialcraft 2 Exp
            } else if (ModCompatibility.isIndustrialCraftExpLoaded()) {
                found=true;
                copyRecipe("mps-ic2.recipes");

                // Industrialcraft Classic
            } else if (ModCompatibility.isIndustrialCraftClassicLoaded()) {
                found=true;
                copyRecipe("mps-ic2-classic.recipes");
            }

            // EnderIO
            if (ModCompatibility.isEnderIOLoaded()) {
                found=true;
                copyRecipe("mps-enderio.recipes");
            }

            // Vanilla
            if(!found) {
                copyRecipe("mps-vanilla.recipes");
            }
        }
    }

    /** ModelSpec --------------------------------------------------------------------------------- */
    static boolean overwriteModelSpec = true; //FIXME: add config setting
    public static void extractModelSpecFiles() {
        if (!modelSpecFolder.exists())
            modelSpecFolder.mkdir();

        String specJarFolderString = "/assets/powersuits/modelspec/";
        URL specJarFolderURL = MPSSettings.class.getResource(specJarFolderString);
        if (specJarFolderURL != null) {
            try {
                File dir = new File(specJarFolderURL.toURI());
                for (File specFile :dir.listFiles()) {
                    File target= new File(specJarFolderURL.getPath(), specFile.getName());
                    if (!target.exists() || overwriteModelSpec)
                        copyFile(specJarFolderString.concat(specFile.getName()), new StringBuilder(modelSpecFolder.getAbsolutePath()).append("/").append(specFile.getName()).toString());
                }
            } catch (URISyntaxException e) {
                MuseLogger.logException("failed to read ModelSpec dir from jar", e);
            }
        }
    }

    @SideOnly(Side.CLIENT)
    public static void loadModelSpecs(@Nullable TextureStitchEvent event) {
        for (File specFile : modelSpecFolder.listFiles()) {
            ModelSpecXMLReader.parseFile(specFile, event);
        }
    }

//    @Mod.EventBusSubscriber(modid = MODID)
//    public static class ConfigSyncHandler {
//        @SubscribeEvent
//        public static void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event) {
//            if (event.getModID().equals(MODID)) {
//                ConfigManager.sync(MODID, Config.Type.INSTANCE);
//                MuseLogger.logInfo("Configuration has been saved.");
//            }
//        }
//    }
}
