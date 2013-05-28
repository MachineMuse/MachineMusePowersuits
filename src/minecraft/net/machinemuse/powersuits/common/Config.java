package net.machinemuse.powersuits.common;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import net.machinemuse.api.IModularItem;
import net.machinemuse.api.IPowerModule;
import net.machinemuse.api.ModuleManager;
import net.machinemuse.powersuits.block.BlockLuxCapacitor;
import net.machinemuse.powersuits.block.BlockTinkerTable;
import net.machinemuse.powersuits.item.ItemComponent;
import net.machinemuse.powersuits.powermodule.armor.BasicPlatingModule;
import net.machinemuse.powersuits.powermodule.armor.DiamondPlatingModule;
import net.machinemuse.powersuits.powermodule.armor.EnergyShieldModule;
import net.machinemuse.powersuits.powermodule.armor.HeatSinkModule;
import net.machinemuse.powersuits.powermodule.energy.AdvancedBatteryModule;
import net.machinemuse.powersuits.powermodule.energy.BasicBatteryModule;
import net.machinemuse.powersuits.powermodule.energy.EliteBatteryModule;
import net.machinemuse.powersuits.powermodule.misc.*;
import net.machinemuse.powersuits.powermodule.movement.*;
import net.machinemuse.powersuits.powermodule.tool.*;
import net.machinemuse.powersuits.powermodule.weapon.BladeLauncherModule;
import net.machinemuse.powersuits.powermodule.weapon.MeleeAssistModule;
import net.machinemuse.powersuits.powermodule.weapon.PlasmaCannonModule;
import net.machinemuse.powersuits.powermodule.weapon.RailgunModule;
import net.machinemuse.utils.MuseStringUtils;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraftforge.common.Configuration;
import org.lwjgl.input.Keyboard;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Initial attempt at storing all tweakable/configurable values in one class.
 * This got really messy really fast so it's in the process of being
 * reworked.
 *
 * @author MachineMuse
 */
public class Config {
    public static final String RESOURCE_PREFIX = "/mods/mmmPowersuits/";
    public static final String TEXTURE_PREFIX = RESOURCE_PREFIX + "textures/";
    public static final String SOUND_PREFIX = RESOURCE_PREFIX + "sound/";
    // public static final String SEBK_ICON_PATH =
    // "/mods/mmmPowersuits//machinemuse/sebkicons.png";
    public static final String SEBK_ARMOR_PATH = TEXTURE_PREFIX + "models/sebkarmor.png";
    public static final String SEBK_ARMORPANTS_PATH = TEXTURE_PREFIX + "models/sebkarmorpants.png";
    // public static final String WC_ICON_PATH =
    // "/resources/machinemuse/watericons.png";
    public static final String TINKERTABLE_TEXTURE_PATH = TEXTURE_PREFIX + "models/tinkertable_tx.png";
    public static final String ARMOR_TEXTURE_PATH = TEXTURE_PREFIX + "models/diffuse.png";
    public static final String BLANK_ARMOR_MODEL_PATH = TEXTURE_PREFIX + "models/blankarmor.png";
    // public static final String MUSE_ICON_PATH =
    // "/resources/machinemuse/museicons.png";
    public static final String SEBK_TOOL_TEXTURE = TEXTURE_PREFIX + "models/tool.png";
    public static final String LIGHTNING_TEXTURE = TEXTURE_PREFIX + "gui/lightning-medium.png";
    public static final String CITIZENJOE_ARMOR_PATH = TEXTURE_PREFIX + "models/joearmor.png";
    public static final String CITIZENJOE_ARMORPANTS_PATH = TEXTURE_PREFIX + "models/joearmorpants.png";
    public static final String GLASS_TEXTURE = TEXTURE_PREFIX + "gui/glass.png";

    public static int helmID;
    public static int chestID;
    public static int legsID;
    public static int bootsID;
    public static int fistID;

    private static Configuration config;

    /**
     * Called in the pre-init phase of initialization, informs Forge that we
     * want the following blockIDs.
     *
     * @param config The Forge configuration object which will handle such
     *               requests.
     */
    public static void init(Configuration config) {
        Config.config = config;
        config.load();

        BlockTinkerTable.assignedBlockID = config.getBlock("Power Armor Tinker Table", 2477).getInt();
        BlockLuxCapacitor.assignedBlockID = config.getBlock("Lux Capacitor", 2478).getInt();

        ItemComponent.assignedItemID = config.getItem("Power Armor Component", 24770).getInt();
        helmID = config.getItem("Power Armor Head", 24771).getInt();
        chestID = config.getItem("Power Armor Torso", 24772).getInt();
        legsID = config.getItem("Power Armor Legs", 24773).getInt();
        bootsID = config.getItem("Power Armor Feet", 24774).getInt();
        fistID = config.getItem("Power Tool", 24775).getInt();

        config.save();
    }

    /**
     * The packet channel for this mod. We will only listen for and send packets
     * on this 'channel'. Max of 16 characters.
     *
     * @return
     */
    public static String getNetworkChannelName() {
        return "mmmPowerSuits";
    }

    /**
     * The default creative tab to add all these items to. This behaviour may
     * change if more items are added, but for now there are only 5 items and 1
     * block, so misc is the most appropriate target.
     *
     * @return
     */
    public static CreativeTabs getCreativeTab() {
        return MuseCreativeTab.instance();
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
        // Clamp this value between 0 and 6 armor points.
        // The default of 6 will allow 24% reduction per piece.
        return Math.max(0.0, config.get(Configuration.CATEGORY_GENERAL, "Maximum Armor per Piece", 6.0).getDouble(6.0));
    }

    public static double getMaximumFlyingSpeedmps() {
        return config.get(Configuration.CATEGORY_GENERAL, "Maximum flight speed (in m/s)", 25.0).getDouble(25.0);
    }

    public static boolean useMouseWheel() {
        return config.get(Configuration.CATEGORY_GENERAL, "Use Mousewheel to change modes", true).getBoolean(true);
    }

    /**
     * Whether or not to print debugging info.
     *
     * @return
     */
    public static boolean isDebugging() {
        return config.get(Configuration.CATEGORY_GENERAL, "Debugging info", false).getBoolean(false);
    }

    public static void addModule(IPowerModule module) {
        ModuleManager.addModule(module);
    }

    /**
     * Load all the modules in the config file into memory. Eventually. For now,
     * they are hardcoded.
     */
    public static void loadPowerModules() {
        // loadModularProperties();
        List<IModularItem> ARMORONLY = Arrays.asList((IModularItem) ModularPowersuits.powerArmorHead, ModularPowersuits.powerArmorTorso,
                ModularPowersuits.powerArmorLegs, ModularPowersuits.powerArmorFeet);
        List<IModularItem> ALLITEMS = Arrays.asList((IModularItem) ModularPowersuits.powerArmorHead, ModularPowersuits.powerArmorTorso,
                ModularPowersuits.powerArmorLegs, ModularPowersuits.powerArmorFeet, ModularPowersuits.powerTool);
        List<IModularItem> HEADONLY = Collections.singletonList((IModularItem) ModularPowersuits.powerArmorHead);
        List<IModularItem> TORSOONLY = Collections.singletonList((IModularItem) ModularPowersuits.powerArmorTorso);
        List<IModularItem> LEGSONLY = Collections.singletonList((IModularItem) ModularPowersuits.powerArmorLegs);
        List<IModularItem> FEETONLY = Collections.singletonList((IModularItem) ModularPowersuits.powerArmorFeet);
        List<IModularItem> TOOLONLY = Collections.singletonList((IModularItem) ModularPowersuits.powerTool);

        // Armor
        addModule(new BasicPlatingModule(ARMORONLY));
        addModule(new DiamondPlatingModule(ARMORONLY));
        addModule(new EnergyShieldModule(ARMORONLY));
        addModule(new HeatSinkModule(ARMORONLY));

        // Tool
        addModule(new AxeModule(TOOLONLY));
        addModule(new PickaxeModule(TOOLONLY));
        addModule(new ShovelModule(TOOLONLY));
        addModule(new ShearsModule(TOOLONLY));
        addModule(new HoeModule(TOOLONLY));
        addModule(new LuxCapacitor(TOOLONLY));
        addModule(new OmniWrenchModule(TOOLONLY));
        addModule(new FieldTinkerModule(TOOLONLY));

        // Weapon
        addModule(new MeleeAssistModule(TOOLONLY));
        addModule(new PlasmaCannonModule(TOOLONLY));
        addModule(new RailgunModule(TOOLONLY));
        // addModule(new SonicWeaponModule(TOOLONLY));
        addModule(new BladeLauncherModule(TOOLONLY));

        // Energy
        addModule(new BasicBatteryModule(ALLITEMS));
        addModule(new AdvancedBatteryModule(ALLITEMS));
        addModule(new EliteBatteryModule(ALLITEMS));

        // Movement
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

        // Vision
        addModule(new BinocularsModule(HEADONLY));
        addModule(new NightVisionModule(HEADONLY));

        // Special
        addModule(new FlightControlModule(HEADONLY));
        addModule(new InvisibilityModule(TORSOONLY));
        addModule(new BlinkDriveModule(TOOLONLY));
        addModule(new DiamondPickUpgradeModule(TOOLONLY));
        addModule(new AquaAffinityModule(TOOLONLY));
        addModule(new CoolingSystemModule(TORSOONLY));

        // Cosmetic
        addModule(new TintModule(TOOLONLY));
        addModule(new TransparentArmorModule(ARMORONLY));
        addModule(new CosmeticGlowModule(ARMORONLY));

    }

    /**
     * An enum to describe the various GUI windows which can appear. IDs are
     * less important here since this data isn't saved or synced.
     *
     * @author MachineMuse
     */
    public static enum Guis {
        GuiTinkerTable,
        GuiSuitManager,
        GuiPortableCrafting
    }

    public static Configuration getConfig() {
        return config;
    }

    public static boolean doAdditionalInfo() {
        if (FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT) {
            if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
                return true;
            }
        }
        return false;
    }

    public static Object additionalInfoInstructions() {
        String message = "Press SHIFT for more information.";
        message = MuseStringUtils.wrapMultipleFormatTags(message, MuseStringUtils.FormatCodes.Grey, MuseStringUtils.FormatCodes.Italic);
        return message;
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

    public static boolean useSounds() {
        return config.get(Configuration.CATEGORY_GENERAL, "Use Sounds", true).getBoolean(true);
    }

    public static double fontDetail() {
        return config.get("Font", "Font Detail Multiplier", 4).getDouble(4);
    }

    public static String fontURI() {
        // Chemical Reaction A -BRK-, retreived from http://www.fontpalace.com/
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

    public static boolean canUseShaders = false;
}
