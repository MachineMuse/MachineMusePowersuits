package net.machinemuse.powersuits.common.config;


import com.google.gson.*;
import jline.internal.Nullable;
import net.machinemuse.api.ModuleManager;
import net.machinemuse.numina.api.item.IModule;
import net.machinemuse.numina.general.MuseLogger;
import net.machinemuse.powersuits.client.modelspec.ModelSpecXMLReader;
import net.machinemuse.powersuits.common.InstallCost;
import net.machinemuse.powersuits.common.MPSConstants;
import net.machinemuse.powersuits.common.MPSCreativeTab;
import net.machinemuse.powersuits.common.ModCompatibility;
import net.machinemuse.powersuits.common.items.modules.armor.*;
import net.machinemuse.powersuits.common.items.modules.cosmetic.HighPolyArmor;
import net.machinemuse.powersuits.common.items.modules.energy.*;
import net.machinemuse.powersuits.common.items.modules.misc.*;
import net.machinemuse.powersuits.common.items.modules.movement.*;
import net.machinemuse.powersuits.common.items.modules.tool.*;
import net.machinemuse.powersuits.common.items.modules.weapon.*;
import net.machinemuse.powersuits.proxy.CommonProxy;
import net.machinemuse.utils.MuseStringUtils;
import net.minecraft.client.resources.I18n;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreDictionary;
import org.lwjgl.input.Keyboard;

import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static net.machinemuse.powersuits.common.MPSConstants.MODID;
import static net.machinemuse.powersuits.common.event.EventRegisterItems.*;

@Config(modid = MODID, name = MPSConstants.CONFIG_FILE, category = "")
@Config.LangKey(MPSConstants.CONFIG_TITLE)
public class MPSSettings {
    private MPSSettings() {}

    private static CreativeTabs mpsCreativeTab;
    public static CreativeTabs getCreativeTab() {
        if (mpsCreativeTab == null)
            mpsCreativeTab = new MPSCreativeTab();
        return mpsCreativeTab;
    }


    public static General general = new General();
    public static class General {
        // General
        @Config.LangKey(MPSConstants.CONFIG_GENERAL_ALLOW_CONFLICTING_KEYBINDS)
        @Config.Comment("Allow Conflicting Keybinds")
        @Config.RequiresWorldRestart
        public boolean allowConflictingKeybinds = true;

        @Config.LangKey(MPSConstants.CONFIG_GENERAL_AUTO_EXTRACT_RECIPES)
        @Config.Comment("Auto-extract recipes")
        @Config.RequiresWorldRestart
        public boolean autoExtractRecipes = false;

        /**
         * The maximum amount of armor contribution allowed per armor piece. Total
         * armor when the full set is worn can never exceed 4 times this amount.
         */
        @Config.LangKey(MPSConstants.CONFIG_GENERAL_GET_MAX_ARMOR_PER_PIECE)
        @Config.Comment("Maximum Armor per Piece")
        @Config.RangeDouble(min = 0, max = 8.0)
        @Config.RequiresWorldRestart
        public double getMaximumArmorPerPiece = 6.0;


        @Config.LangKey(MPSConstants.CONFIG_GENERAL_USE_24_HOUR_CLOCK)
        @Config.Comment("Use a 24h clock instead of 12h")
        public boolean use24hClock = false;


        @Config.LangKey(MPSConstants.CONFIG_GENERAL_MAX_FLYING_SPEED)
        @Config.Comment("Maximum flight speed (in m/s)")
        public double getMaximumFlyingSpeedmps = 25.0;


        @Config.LangKey(MPSConstants.CONFIG_GENERAL_BASE_MAX_HEAT)
        @Config.Comment("Base Heat Cap")
        public double baseMaxHeat = 50.0;


        @Config.LangKey(MPSConstants.CONFIG_GENERAL_WEIGHT_LIMIT)
        @Config.Comment("Weight Limit (grams)")
        public static double getWeightCapacity = 25000.0;


        @Config.LangKey(MPSConstants.CONFIG_GENERAL_SALVAGE_CHANCE)
        @Config.Comment("Chance of each item being returned when salvaged")
        @Config.RangeDouble(min = 0, max = 1.0)
        public double getSalvageChance = 0.9;


        @Config.LangKey(MPSConstants.CONFIG_GENERAL_USE_ADVANCED_ORE_SCANNER_MESSAGE)
        @Config.Comment("Use Detailed Ore Scanner Message")
        public static boolean useAdvancedOreScannerMessage = true;


        @Config.LangKey(MPSConstants.CONFIG_GENERAL_USE_OLD_AUTOFEEDER)
        @Config.Comment("Use Old Auto Feeder Method")
        public static boolean useOldAutoFeeder = false;


        @Config.LangKey(MPSConstants.CONFIG_GENERAL_USE_CHEATY_LEATHER)
        @Config.Comment("Use Cheaty Leather Recipe (Requires Thermal Expansion)")
        public static boolean useCheatyLeatherRecipe = true;


        @Config.LangKey(MPSConstants.CONFIG_GENERAL_USE_HUD)
        @Config.Comment("Use HUD for certain modules (Auto Feeder, Compass, Clock, etc.")
        public static boolean useHUDStuff = true;
    }

    /*
    1 MJ = 25 J = 10 RF = 2,5 EU
    1 J = 0,04 MJ = 0,4 RF = 0,1 EU
    1 RF = 0,1 MJ = 2,5 J = 0,25 EU
    1 EU = 10 J = 0,4 MJ = 4 RF
     */

    @Config.RequiresWorldRestart
    public static final Energy energy = new Energy();
    public static class Energy {
        // basic capacitor (100KJ or 1M-RF)
        @Config.LangKey(MPSConstants.CONFIG_MAX_ENERGY_LV_CAPACITOR)
        @Config.Comment("Amount of RF energy that the LV Capacitor can store.")
        @Config.RangeInt(min = 0)
        public int maxEnergyLVCapacitor = (int) (1 * Math.pow(10, 6));

        // advanced capacitor (500KJ or 5M-RF)
        @Config.LangKey(MPSConstants.CONFIG_MAX_ENERGY_MV_CAPACITOR)
        @Config.Comment("Amount of RF energy that the MV Capacitor can store.")
        @Config.RangeInt(min = 0)
        public int maxEnergyMVCapacitor = (int) (5 * Math.pow(10, 6));

        // elite capacitor (5MJ or 50M-RF)
        @Config.LangKey(MPSConstants.CONFIG_MAX_ENERGY_HV_CAPACITOR)
        @Config.Comment("Amount of RF energy that the HV Capacitor can store.")
        @Config.RangeInt(min = 0)
        public int maxEnergyHVCapacitor = (int) (5 * Math.pow(10, 7));

        // ultimate capacitor (10MJ or 100M-RF)
        @Config.LangKey(MPSConstants.CONFIG_MAX_ENERGY_EV_CAPACITOR)
        @Config.Comment("Amount of RF energy that the EV Capacitor can store.")
        @Config.RangeInt(min = 0)
        public int maxEnergyEVCapacitor = (int) (1 * Math.pow(10, 8));


        @Config.LangKey(MPSConstants.CONFIG_RF_ENERGY_PER_IC2_EU)
        @Config.Comment("RF Energy per IC2 EU")
//        @Config.RangeDouble(min = 0)
        public double rfEnergyPerIC2EU = 0.25;
    }







    public static HUD hud = new HUD();
    public static class HUD {
        @Config.LangKey(MPSConstants.CONFIG_HUD_TOGGLE_MODULE_SPAM)
        @Config.Comment("Chat message when toggling modules")
        @Config.RequiresWorldRestart
        public boolean toggleModuleSpam = false;


        @Config.LangKey(MPSConstants.CONFIG_HUD_DISPLAY_HUD)
        @Config.Comment("Display HUD")
        @Config.RequiresWorldRestart
        public boolean keybindHUDon = true;


        @Config.LangKey(MPSConstants.CONFIG_HUD_KEYBIND_HUD_X)
        @Config.Comment("x position")
        @Config.RangeDouble(min = 0)
        @Config.RequiresWorldRestart
        public double keybindHUDx = 8.0;


        @Config.LangKey(MPSConstants.CONFIG_HUD_KEYBIND_HUD_Y)
        @Config.Comment("y position")
        @Config.RangeDouble(min = 0)
        @Config.RequiresWorldRestart
        public double keybindHUDy = 32.0;

        @Config.LangKey(MPSConstants.CONFIG_HUD_USE_GRAPHICAL_METERS)
        @Config.Comment("Use Graphical Meters")
        @Config.RequiresWorldRestart
        public static boolean useGraphicalMeters = true;

    }

    public static final Graphics graphics = new Graphics();
    public static class Graphics {
        @Config.LangKey(MPSConstants.CONFIG_GRAPHICS_GLOW_MULTIPLIER)
        @Config.Comment("Bloom Multiplier")
        public int glowMultiplier = 3;

        @Config.LangKey(MPSConstants.CONFIG_GRAPHICS_USE_SHADERS)
        @Config.Comment("Use Pixel/Vertex Shaders")
        public boolean useShaders = true;
    }

    public static Font font = new Font();
    public static class Font {
        @Config.LangKey(MPSConstants.CONFIG_FONT_USE_CUSTOM_FONTS)
        @Config.Comment("Use Custom Font Engine")
        public boolean useCustomFonts = true;


        @Config.LangKey(MPSConstants.CONFIG_FONT_DETAIL_MULTIPLIER)
        @Config.Comment("Font Detail Multiplier")
        public double fontDetail = 4;


        @Config.LangKey(MPSConstants.CONFIG_FONT_URI)
        @Config.Comment("Font URI")
        public String fontURI = MPSConstants.RESOURCE_PREFIX + "fonts/cra.ttf";


        @Config.LangKey(MPSConstants.CONFIG_FONT_NAME)
        @Config.Comment("Native Font Name (Overrides URI)")
        public String fontName = "";

        @Config.LangKey(MPSConstants.CONFIG_USE_FONT_ANTI_ALIASING)
        @Config.Comment("Font Anti-Aliasing")
        public boolean fontAntiAliasing = false;
    }

    // TOTO: Server side configs
    public static ModelConfig modelconfig = new ModelConfig();
    public static class ModelConfig{
        @Config.Comment("Use this to enable model transform mapping from keyboard")
        public boolean modelSetup = false;

        @Config.Comment("Enable high polly armor models")
        public boolean allowHighPollyArmorModels = false;

        @Config.Comment("Enable custom high polly armor models")
        public boolean allowCustomHighPollyArmor = false;

        // TODO: ? this would use boxes but ... meh
//        @Config.Comment("Enable high polly PowerFist models")
//        public boolean allowHighPollyPowerFistModels = false;

        @Config.Comment("Enable custom PowerFist models")
        public boolean allowCustomPowerFistModels = false;
    }

    public static boolean allowHighPollyArmorModels() {
        return serverSettings != null ? serverSettings.allowHighPollyArmorModels : modelconfig.allowHighPollyArmorModels;
    }

    public static boolean allowCustomHighPollyArmor() {
        return serverSettings != null ? serverSettings.allowCustomHighPollyArmorModels : modelconfig.allowCustomPowerFistModels;
    }

    public static boolean allowCustomPowerFistModels() {
        return serverSettings != null ? serverSettings.allowCustomPowerFistModels : modelconfig.allowCustomPowerFistModels;
    }






    /**
     * Currently maps need to be initialized and populated at runtime otherwise the values are not read from the config file
     *
     * TODO: move to server config
     */
    public static Modules modules = new Modules();
    public static class Modules {
        @Config.LangKey(MPSConstants.CONFIG_MODULES)
        @Config.Comment("Whether or not specified module is allowed")
        public Map<String, Boolean> allowedModules = //new HashMap<>();
                new HashMap<String, Boolean>() {{
                    /* Armor -------------------------------- */
                    put(MPSConstants.MODULE_BASIC_PLATING,true);
                    put(MPSConstants.MODULE_DIAMOND_PLATING, true);
                    put(MPSConstants.MODULE_ENERGY_SHIELD, true );
                    put(MPSConstants.MODULE_HEAT_SINK, true);


                    /* Cosmetic ----------------------------- */
//                    put(MPSConstants.MODULE_TRANSPARENT_ARMOR, true);
//                    put(MPSConstants.MODULE_GLOW, true);
//                    put(MPSConstants.CITIZEN_JOE_STYLE, true);
                    put(MPSConstants.HIGH_POLY_ARMOR, true);
//                    put(MPSConstants.MODULE_TINT, true);


                    /* Energy ------------------------------- */
                    put(MPSConstants.MODULE_BATTERY_BASIC, true);
                    put(MPSConstants.MODULE_BATTERY_ADVANCED, true);
                    put(MPSConstants.MODULE_BATTERY_ELITE, true);
                    put(MPSConstants.MODULE_BATTERY_ULTIMATE, true);


                    /* Power Fist --------------------------- */
                    put(MPSConstants.MODULE_AXE, true);
                    put(MPSConstants.MODULE_PICKAXE, true);
                    put(MPSConstants.MODULE_DIAMOND_PICK_UPGRADE, true);
                    put(MPSConstants.MODULE_SHOVEL, true);
                    put(MPSConstants.MODULE_SHEARS, true);
                    put(MPSConstants.MODULE_HOE, true);
                    put(MPSConstants.MODULE_LUX_CAPACITOR, true);
                    put(MPSConstants.MODULE_FIELD_TINKER, true);
                    put(MPSConstants.MODULE_MELEE_ASSIST, true);
                    put(MPSConstants.MODULE_PLASMA_CANNON, true);
                    put(MPSConstants.MODULE_RAILGUN, true);
                    put(MPSConstants.MODULE_BLADE_LAUNCHER, true);
                    put(MPSConstants.MODULE_BLINK_DRIVE, true);
                    put(MPSConstants.MODULE_AQUA_AFFINITY, true);
                    put(MPSConstants.MODULE_PORTABLE_CRAFTING, true);
                    put(MPSConstants.MODULE_ORE_SCANNER, true);
                    put(MPSConstants.MODULE_LEAF_BLOWER, true);
                    put(MPSConstants.MODULE_FLINT_AND_STEEL, true);
                    put(MPSConstants.MODULE_LIGHTNING, true);
                    put(MPSConstants.MODULE_DIMENSIONAL_RIFT, true);


                    /* Helmet ------------------------------- */
                    put(MPSConstants.MODULE_WATER_ELECTROLYZER, true);
                    put(MPSConstants.MODULE_NIGHT_VISION, true);
                    put(MPSConstants.BINOCULARS_MODULE, true);
                    put(MPSConstants.MODULE_FLIGHT_CONTROL, true);
                    put(MPSConstants.MODULE_SOLAR_GENERATOR, true);
                    put(MPSConstants.MODULE_AUTO_FEEDER, true);
                    put(MPSConstants.MODULE_CLOCK, true);
                    put(MPSConstants.MODULE_COMPASS, true);
                    put(MPSConstants.MODULE_ADVANCED_SOLAR_GENERATOR, true);


                    /* Chestplate --------------------------- */
                    put(MPSConstants.MODULE_PARACHUTE, true);
                    put(MPSConstants.MODULE_GLIDER, true);
                    put(MPSConstants.MODULE_JETPACK, true);
                    put(MPSConstants.MODULE_ACTIVE_CAMOUFLAGE, true);
                    put(MPSConstants.MODULE_COOLING_SYSTEM, true);
                    put(MPSConstants.MODULE_MAGNET, true);
                    put(MPSConstants.MODULE_THERMAL_GENERATOR, true);
                    put(MPSConstants.MODULE_MOB_REPULSOR, true);
                    put(MPSConstants.MODULE_WATER_TANK, true);
                    put(MPSConstants.MODULE_NITROGEN_COOLING_SYSTEM, true);
                    put(MPSConstants.MODULE_MECH_ASSISTANCE, true);
//                    put(MPSConstants.MODULE_COAL_GEN, false);


                    /* Legs --------------------------------- */
                    put(MPSConstants.MODULE_SPRINT_ASSIST, true);
                    put(MPSConstants.MODULE_JUMP_ASSIST, true);
                    put(MPSConstants.MODULE_SWIM_BOOST, true);
                    put(MPSConstants.MODULE_KINETIC_GENERATOR, true);
                    put(MPSConstants.MODULE_CLIMB_ASSIST, true);


                    /* Feet --------------------------------- */
                    put(MPSConstants.MODULE_JETBOOTS, true);
                    put(MPSConstants.MODULE_SHOCK_ABSORBER, true);
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


        @Config.LangKey(MPSConstants.CONFIG_MODULE_PROPERTY_DOUBLES)
        @Config.Comment("Value of specified property")
        public Map<String, Double> propertyDouble = new HashMap<>();

        public double getPropertyDoubleorDefault(String name, double value) {
            if (propertyDouble.isEmpty() ||!propertyDouble.containsKey(name))
                propertyDouble.put(name, value);
            return propertyDouble.get(name);
        }

        public double getPropertyDoubleor(String name) {
            if (propertyDouble.isEmpty() ||!propertyDouble.containsKey(name))
                propertyDouble.put(name, 1.0);
            return propertyDouble.get(name);
        }
    }

    private static ServerSettings serverSettings;
    public static void setServerSettings(@Nullable final ServerSettings serverSettings) {
        MPSSettings.serverSettings = serverSettings;
    }

    @Nullable
    public static final ServerSettings getServerSettings() {
        return serverSettings;
    }


//========================================================




    public static void addModule(IModule module) {
//        if(modules.isEmpty())
//            modules.put(module.getDataName(), true);
//        else if(!modules.keySet().contains(module.getDataName()))
//            modules.put(module.getDataName(), true);
//        else
        ModuleManager.addModule(module);
//        try {
//            getOrDefaultModuleAllowed(module.getDataName(), true);
//        } catch (Exception e) {
//            System.out.println("failed to add module: " + e.getMessage());
//        }
    }

    /**
     * Load all the modules in the config file into memory. Eventually. For now,
     * they are hardcoded.
     */
    public static void loadPowerModules() {


        // FIXME: these need to be sorted
        /* Armor -------------------------------- */
        addModule(new BasicPlatingModule(ARMORONLY));
        addModule(new DiamondPlatingModule(ARMORONLY));
        addModule(new EnergyShieldModule(ARMORONLY));
        addModule(new HeatSinkModule(ARMORONLY));


        /* Cosmetic ----------------------------- */
//        addModule(new TransparentArmorModule(ARMORONLY));
//        addModule(new CosmeticGlowModule(ARMORONLY));
//        addModule(new CitizenJoeStyle(ARMORONLY)); // obsolete, use directly in cosmetic menu
        addModule(new HighPolyArmor(ARMORONLY));
//        addModule(new TintModule(TOOLONLY));
//        addModule(new TintModule(ALLITEMS)); // obsolete, set colors in cosmetic menu


        /* Energy ------------------------------- */
        addModule(new BasicBatteryModule(ALLITEMS));
        addModule(new AdvancedBatteryModule(ALLITEMS));
        addModule(new EliteBatteryModule(ALLITEMS));
        addModule(new UltimateBatteryModule(ALLITEMS));


        /* Power Fist --------------------------- */
        addModule(new AxeModule(TOOLONLY));
        addModule(new PickaxeModule(TOOLONLY));
        addModule(new DiamondPickUpgradeModule(TOOLONLY));
        addModule(new ShovelModule(TOOLONLY));
        addModule(new ShearsModule(TOOLONLY));
        addModule(new HoeModule(TOOLONLY));
        addModule(new LuxCapacitor(TOOLONLY));
        addModule(new FieldTinkerModule(TOOLONLY));
        addModule(new MeleeAssistModule(TOOLONLY));
        addModule(new PlasmaCannonModule(TOOLONLY));
        addModule(new RailgunModule(TOOLONLY));
        addModule(new BladeLauncherModule(TOOLONLY));
        addModule(new BlinkDriveModule(TOOLONLY));
        addModule(new AquaAffinityModule(TOOLONLY));
        addModule(new InPlaceAssemblerModule(TOOLONLY));
        addModule(new OreScannerModule(TOOLONLY));
        addModule(new LeafBlowerModule(TOOLONLY));
        addModule(new FlintAndSteelModule(TOOLONLY));
        addModule(new LightningModule(TOOLONLY));
        addModule(new DimensionalRiftModule(TOOLONLY));


        /* Helmet ------------------------------- */
        addModule(new WaterElectrolyzerModule(HEADONLY));
        addModule(new NightVisionModule(HEADONLY));
        addModule(new BinocularsModule(HEADONLY));
        addModule(new FlightControlModule(HEADONLY));
        addModule(new SolarGeneratorModule(HEADONLY));
        addModule(new AutoFeederModule(HEADONLY));
        addModule(new ClockModule(HEADONLY));
        addModule(new CompassModule(HEADONLY));
        addModule(new AdvancedSolarGenerator(HEADONLY));


        /* Chestplate --------------------------- */
        addModule(new ParachuteModule(TORSOONLY));
        addModule(new GliderModule(TORSOONLY));
        addModule(new JetPackModule(TORSOONLY));
        addModule(new InvisibilityModule(TORSOONLY));
        addModule(new CoolingSystemModule(TORSOONLY));
        addModule(new MagnetModule(TORSOONLY));
        addModule(new ThermalGeneratorModule(TORSOONLY));
        addModule(new MobRepulsorModule(TORSOONLY));
        addModule(new WaterTankModule(TORSOONLY));
        addModule(new NitrogenCoolingSystem(TORSOONLY));
        addModule(new MechanicalAssistance(TORSOONLY));
        //addModule(new CoalGenerator(TORSOONLY)); // unfinished


        /* Legs --------------------------------- */
        addModule(new SprintAssistModule(LEGSONLY));
        addModule(new JumpAssistModule(LEGSONLY));
        addModule(new SwimAssistModule(LEGSONLY));
        addModule(new KineticGeneratorModule(LEGSONLY));
        addModule(new ClimbAssistModule(LEGSONLY));


        /* Feet --------------------------------- */
        addModule(new JetBootsModule(FEETONLY));
        addModule(new ShockAbsorberModule(FEETONLY));
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
                            ModuleManager.addCustomInstallCost(moduleName, stack);
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

    public static boolean doAdditionalInfo() {
        return FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT && Keyboard.isKeyDown(Keyboard.KEY_LSHIFT);
    }

    @SideOnly(Side.CLIENT)
    public static String additionalInfoInstructions() {
        String message = I18n.format("tooltip.pressShift");
        return MuseStringUtils.wrapMultipleFormatTags(message, MuseStringUtils.FormatCodes.Grey, MuseStringUtils.FormatCodes.Italic);
    }

    public static Map<Map<ResourceLocation, Integer>, Integer> getOreValues() {
        return readOreValues();
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
                        List<ItemStack> stacks = OreDictionary.getOres(oredictName.getAsString());
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







    static File configFolder = null;
    static File recipeFolder = null;
    static File modelSpecFolder = null;
    public static void setConfigFolderBase(FMLPreInitializationEvent event) {
        configFolder = new File(event.getModConfigurationDirectory().getAbsolutePath() + "/machinemuse");
        recipeFolder = new File(configFolder.getAbsolutePath().concat("/recipes/"));
        modelSpecFolder = new File(configFolder.getAbsolutePath().concat("/modelSpec/"));
    }

    static boolean overwriteModelSpec = true; //FIXME: add config setting

    /** ModelSpec --------------------------------------------------------------------------------- */
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

    @Mod.EventBusSubscriber(modid = MODID)
    public static class ConfigSyncHandler {
        @SubscribeEvent
        public static void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event) {
            if (event.getModID().equals(MODID)) {
                ConfigManager.sync(MODID, Config.Type.INSTANCE);
                MuseLogger.logInfo("Configuration has been saved.");
            }
        }
    }
}
