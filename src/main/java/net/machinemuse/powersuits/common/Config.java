package net.machinemuse.powersuits.common;

import com.google.gson.Gson;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import net.machinemuse.api.IModularItem;
import net.machinemuse.api.IPowerModule;
import net.machinemuse.api.ModuleManager;
import net.machinemuse.numina.basemod.Numina;
import net.machinemuse.numina.general.MuseLogger;
import net.machinemuse.powersuits.common.proxy.CommonProxy;
import net.machinemuse.powersuits.powermodule.armor.*;
import net.machinemuse.powersuits.powermodule.energy.*;
import net.machinemuse.powersuits.powermodule.misc.*;
import net.machinemuse.powersuits.powermodule.movement.*;
import net.machinemuse.powersuits.powermodule.tool.*;
import net.machinemuse.powersuits.powermodule.weapon.*;
import net.machinemuse.utils.MuseStringUtils;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.config.Configuration;
import org.lwjgl.input.Keyboard;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Initial attempt at storing all tweakable/configurable values in one class.
 * This got really messy really fast so it's in the process of being
 * reworked.
 *
 * @author MachineMuse
 *
 * Ported to Java by lehjr on 11/14/16.
 */
public class Config {
    public static final String RESOURCE_PREFIX = "powersuits:";
    public static final String TEXTURE_PREFIX = "powersuits:textures/";
    public static final String SOUND_PREFIX = RESOURCE_PREFIX + "sound/";
    public static final String LANG_PREFIX = RESOURCE_PREFIX + "lang/";
    public static final String SEBK_ARMOR_PATH = TEXTURE_PREFIX + "models/sebkarmor.png";
    public static final String SEBK_ARMORPANTS_PATH = TEXTURE_PREFIX + "models/sebkarmorpants.png";
    public static final String TINKERTABLE_TEXTURE_PATH = TEXTURE_PREFIX + "models/tinkertable_tx.png";
    public static final String ARMOR_TEXTURE_PATH = TEXTURE_PREFIX + "models/diffuse.png";
    public static final String BLANK_ARMOR_MODEL_PATH = TEXTURE_PREFIX + "models/blankarmor.png";
    public static final String SEBK_TOOL_TEXTURE = TEXTURE_PREFIX + "models/tool.png";
    public static final String LIGHTNING_TEXTURE = TEXTURE_PREFIX + "gui/lightning-medium.png";
    public static final String CITIZENJOE_ARMOR_PATH = TEXTURE_PREFIX + "models/joearmor.png";
    public static final String CITIZENJOE_ARMORPANTS_PATH = TEXTURE_PREFIX + "models/joearmorpants.png";
    public static final String GLASS_TEXTURE = TEXTURE_PREFIX + "gui/glass.png";
    static File configFolder = null;
    private static MPSCreativeTab mpsCreativeTab;

    private static Configuration config = null;
    public static boolean canUseShaders = false;

    public static boolean keybindHUDon() {
        return config.get("HUD", "Display HUD", true).getBoolean();
    }

    public static double keybindHUDx() {
        return config.get("HUD", "x position", 8.0).getDouble();
    }

    public static double keybindHUDy() {
        return config.get("HUD", "y position", 32.0).getDouble();
    }

    public static boolean toggleModuleSpam() {
        return config.get("HUD", "Chat message when toggling modules", false).getBoolean();
    }

    public static double getWeightCapacity() {
        return config.get(Configuration.CATEGORY_GENERAL, "Weight Limit (grams)", 25000.0).getDouble();
    }

    /**
     * Called in post-init. Extracts recipes if the configuration value is not found.
     */
    // TODO: a better idea might be to extract all of the recipes and then only read the ones that are valid.
    // This would save on requests for recipe downloads

    public static void extractRecipes() {
        String key = "Auto-extract recipes";
        if (!config.hasKey(Configuration.CATEGORY_GENERAL, key) || config.get(Configuration.CATEGORY_GENERAL, key, false).getBoolean()) {
            config.get(Configuration.CATEGORY_GENERAL, key, false);
            boolean found=false;
            // Thermal Expansion
            if(ModCompatibility.isThermalExpansionLoaded()) {
                found=true;
                copyRecipe("mps-thermalexpansion.recipes");
            }
            // Industrialcraft 2
            if (ModCompatibility.isIndustrialCraftLoaded()) {
                found=true;
                copyRecipe("mps-ic2.recipes");
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
        InputStream src = CommonProxy.class.getClassLoader().getResourceAsStream(inFile);
        File dest = new File(Numina.configDir.toString() + "/machinemuse/recipes/" + inFile);
        if(!dest.exists()) {
            try {
                Files.copy(src, dest.toPath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Called in the pre-init phase of initialization, informs Forge that we
     * want the following blockIDs.
     *
     * @param configuration The Forge configuration object which will handle such
     *               requests.
     */
    public static void init(Configuration configuration) {
        config = configuration;
        config.load();
        config.save();
    }

    /**
     * The packet channel for this mod. We will only listen for and send packets
     * on this 'channel'. Max of 16 characters.
     *
     * @return
     */
    public static String getNetworkChannelName() {
        return "powerSuits";
    }

    /**
     * The default creative tab to add all these items to. This behaviour may
     * change if more items are added, but for now there are only 5 items and 1
     * block, so misc is the most appropriate target.
     *
     * @return
     */
    public static CreativeTabs getCreativeTab() {
        if (mpsCreativeTab == null)
            mpsCreativeTab = new MPSCreativeTab();
        return mpsCreativeTab;
    }

    /**
     * Chance of each item being returned when salvaged.
     *
     * @return percent chance, 0.0 to 1.0
     */
    public static double getSalvageChance() {
        return config.get(Configuration.CATEGORY_GENERAL, "Salvage Ratio", 0.9).getDouble(0.9);
    }

    /**
     * The maximum amount of armor contribution allowed per armor piece. Total
     * armor when the full set is worn can never exceed 4 times this amount.
     *
     * @return
     */
    public static double getMaximumArmorPerPiece() {
        return Math.max(0.0, config.get(Configuration.CATEGORY_GENERAL, "Maximum Armor per Piece", 6.0).getDouble(6.0));
    }

    public static double getMaximumFlyingSpeedmps() {
        return config.get(Configuration.CATEGORY_GENERAL, "Maximum flight speed (in m/s)", 25.0).getDouble(25.0);
    }

    public static boolean useMouseWheel() {
        return config.get(Configuration.CATEGORY_GENERAL, "Use Mousewheel to change modes", true).getBoolean(true);
    }

    public static void addModule(IPowerModule module) {
        ModuleManager.addModule(module);
    }

    /**
     * The power drain for the Applied Energistics2 and ExtraCells2 wireless terminals
     */
    public static double appengMultiplier() {
        return config.get(Configuration.CATEGORY_GENERAL, "Energy per AE", 5.0).getDouble(5.0);
    }

    public static boolean useAdvancedOreScannerMessage() {
        return config.get(Configuration.CATEGORY_GENERAL, "Use Detailed Ore Scanner Message", true).getBoolean(true);
    }

    public static boolean useOldAutoFeeder() {
        return config.get(Configuration.CATEGORY_GENERAL, "Use Old Auto Feeder Method", false).getBoolean(false);
    }

    public static boolean useCheatyLeatherRecipe() {
        return config.get(Configuration.CATEGORY_GENERAL, "Use Cheaty Leather Recipe (Requires Thermal Expansion)", true).getBoolean(true);
    }

    public static boolean useHUDStuff() {
        return config.get(Configuration.CATEGORY_GENERAL, "Use HUD for certain modules (Auto Feeder, Compass, Clock, etc.", true).getBoolean(true);
    }

    public static boolean use24hClock() {
        return config.get(Configuration.CATEGORY_GENERAL, "Use a 24h clock instead of 12h", false).getBoolean(false);
    }

    /**
     * Load all the modules in the config file into memory. Eventually. For now,
     * they are hardcoded.
     */
    public static void loadPowerModules() {
        List<IModularItem> ARMORONLY = Arrays.asList(
                (IModularItem)MPSItems.getInstance().powerArmorHead,
                (IModularItem)MPSItems.getInstance().powerArmorTorso,
                (IModularItem)MPSItems.getInstance().powerArmorLegs,
                (IModularItem)MPSItems.getInstance().powerArmorFeet);

        List<IModularItem> ALLITEMS = Arrays.asList(
                (IModularItem)MPSItems.getInstance().powerArmorHead,
                (IModularItem)MPSItems.getInstance().powerArmorTorso,
                (IModularItem)MPSItems.getInstance().powerArmorLegs,
                (IModularItem)MPSItems.getInstance().powerArmorFeet,
                (IModularItem)MPSItems.getInstance().powerTool);

        List<IModularItem> HEADONLY = Collections.singletonList((IModularItem)MPSItems.getInstance().powerArmorHead);
        List<IModularItem> TORSOONLY = Collections.singletonList((IModularItem)MPSItems.getInstance().powerArmorTorso);
        List<IModularItem> LEGSONLY = Collections.singletonList((IModularItem)MPSItems.getInstance().powerArmorLegs);
        List<IModularItem> FEETONLY = Collections.singletonList((IModularItem)MPSItems.getInstance().powerArmorFeet);
        List<IModularItem> TOOLONLY = Collections.singletonList((IModularItem)MPSItems.getInstance().powerTool);

        addModule(new BasicPlatingModule(ARMORONLY));
        addModule(new DiamondPlatingModule(ARMORONLY));
        addModule(new EnergyShieldModule(ARMORONLY));
        addModule(new HeatSinkModule(ARMORONLY));
        addModule(new AxeModule(TOOLONLY));
        addModule(new PickaxeModule(TOOLONLY));
        addModule(new ShovelModule(TOOLONLY));
        addModule(new ShearsModule(TOOLONLY));
        addModule(new HoeModule(TOOLONLY));
        addModule(new LuxCapacitor(TOOLONLY));
//        addModule(new OmniWrenchModule(TOOLONLY)) // Requires COFH lib/core
        addModule(new FieldTinkerModule(TOOLONLY));
        addModule(new MeleeAssistModule(TOOLONLY));
        addModule(new PlasmaCannonModule(TOOLONLY));
        addModule(new RailgunModule(TOOLONLY));
        addModule(new BladeLauncherModule(TOOLONLY));
        addModule(new BasicBatteryModule(ALLITEMS));
        addModule(new AdvancedBatteryModule(ALLITEMS));
        addModule(new EliteBatteryModule(ALLITEMS));
        addModule(new ParachuteModule(TORSOONLY));
        addModule(new GliderModule(TORSOONLY));
        addModule(new JetPackModule(TORSOONLY));
        addModule(new SprintAssistModule(LEGSONLY));
        addModule(new JumpAssistModule(LEGSONLY));
        addModule(new SwimAssistModule(LEGSONLY));
        addModule(new ClimbAssistModule(LEGSONLY));
        addModule(new JetBootsModule(FEETONLY));
        addModule(new ShockAbsorberModule(FEETONLY));
        addModule(new WaterElectrolyzerModule(HEADONLY));
        addModule(new NightVisionModule(HEADONLY));
        addModule(new BinocularsModule(HEADONLY));
        addModule(new FlightControlModule(HEADONLY));
        addModule(new InvisibilityModule(TORSOONLY));
        addModule(new BlinkDriveModule(TOOLONLY));
        addModule(new DiamondPickUpgradeModule(TOOLONLY));
        addModule(new AquaAffinityModule(TOOLONLY));
        addModule(new CoolingSystemModule(TORSOONLY));
        addModule(new TintModule(TOOLONLY));
        addModule(new TransparentArmorModule(ARMORONLY));
        addModule(new CosmeticGlowModule(ARMORONLY));
        addModule(new InPlaceAssemblerModule(TOOLONLY));
        addModule(new KineticGeneratorModule(LEGSONLY));
        addModule(new SolarGeneratorModule(HEADONLY));
        addModule(new AutoFeederModule(HEADONLY));
        addModule(new MagnetModule(TORSOONLY));
        addModule(new OreScannerModule(TOOLONLY));
        addModule(new LeafBlowerModule(TOOLONLY));
        addModule(new ThermalGeneratorModule(TORSOONLY));
        addModule(new MobRepulsorModule(TORSOONLY));
        addModule(new FlintAndSteelModule(TOOLONLY));
        addModule(new ClockModule(HEADONLY));
        addModule(new CompassModule(HEADONLY));
        addModule(new LightningModule(TOOLONLY));
        addModule(new WaterTankModule(TORSOONLY));
        addModule(new DimensionalRiftModule(TOOLONLY));
        addModule(new AdvancedSolarGenerator(HEADONLY));
        addModule(new NitrogenCoolingSystem(TORSOONLY));
        addModule(new MechanicalAssistance(TORSOONLY));
//        //addModule(new CoalGenerator(TORSOONLY)); //doesn't seem to be working
    }

    public static Configuration getConfig() {
        return config;
    }

    public static boolean doAdditionalInfo() {
        return FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT && Keyboard.isKeyDown(Keyboard.KEY_LSHIFT);
    }

    public static String additionalInfoInstructions() {
        String message = StatCollector.translateToLocal("tooltip.pressShift");
        return MuseStringUtils.wrapMultipleFormatTags(message, MuseStringUtils.FormatCodes.Grey, MuseStringUtils.FormatCodes.Italic);
    }

    public static boolean useGraphicalMeters() {
        return config.get(Configuration.CATEGORY_GENERAL, "Use Graphical Meters", true).getBoolean(true);
    }

    public static double baseMaxHeat() {
        return config.get(Configuration.CATEGORY_GENERAL, "Base Heat Cap", 50.0).getDouble(50.0);
    }

    public static boolean allowConflictingKeybinds() {
        return config.get(Configuration.CATEGORY_GENERAL, "Allow Conflicting Keybinds", true).getBoolean(true);
    }

    public static boolean useCustomFonts() {
        return config.get("Font", "Use Custom Font Engine", true).getBoolean(true);
    }

    public static double fontDetail() {
        return config.get("Font", "Font Detail Multiplier", 4).getDouble(4);
    }

    public static String fontURI() {
        return config.get("Font", "Font URI", Config.RESOURCE_PREFIX + "fonts/cra.ttf").getString();
    }

    public static String fontName() {
        return config.get("Font", "Native Font Name (Overrides URI)", "").getString();
    }

    public static boolean fontAntiAliasing() {
        return config.get("Font", "Font Anti-Aliasing", false).getBoolean(false);
    }

    public static int glowMultiplier() {
        return config.get("Graphics", "Bloom Multiplier", 3).getInt(3);
    }

    public static boolean useShaders() {
        return config.get("Graphics", "Use Pixel/Vertex Shaders", true).getBoolean(true);
    }

    public static void setConfigFolderBase(File folder) {
        configFolder = new File(folder.getAbsolutePath() + "/machinemuse");
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
                    Item item = GameRegistry.findItem(cost.modId, cost.itemName);
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
}