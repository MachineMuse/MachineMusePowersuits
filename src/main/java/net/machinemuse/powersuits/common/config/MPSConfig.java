package net.machinemuse.powersuits.common.config;

import com.google.gson.*;
import jline.internal.Nullable;
import net.machinemuse.numina.api.module.ModuleManager;
import net.machinemuse.numina.utils.MuseLogger;
import net.machinemuse.powersuits.client.render.modelspec.ModelSpecXMLReader;
import net.machinemuse.powersuits.common.InstallCost;
import net.machinemuse.powersuits.common.MPSCreativeTab;
import net.machinemuse.powersuits.proxy.CommonProxy;
import net.machinemuse.numina.utils.string.MuseStringUtils;
import net.minecraft.client.resources.I18n;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
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

/**
 * All of the settings for both server and client in one place
 */
public class MPSConfig {
    /**
     * Creative tab
     */
    private static CreativeTabs mpsCreativeTab;
    public static CreativeTabs getCreativeTab() {
        if (mpsCreativeTab == null)
            mpsCreativeTab = new MPSCreativeTab();
        return mpsCreativeTab;
    }

    /** General ----------------------------------------------------------------------------------- */
    public boolean allowConflictingKeybinds() {
        return MPSSettings.general.allowConflictingKeybinds;
    }

    public boolean autoExtractRecipes() {
        return MPSSettings.general.autoExtractRecipes;
    }

    public static double getMaximumArmorPerPiece() {
        return MPSSettings.getServerSettings() != null ? MPSSettings.getServerSettings().maximumArmorPerPiece : MPSSettings.general.getMaximumArmorPerPiece;
    }

    public boolean use24hClock() {
        return MPSSettings.general.use24hClock;
    }

    public static double getMaximumFlyingSpeedmps() {
        return MPSSettings.getServerSettings() != null ? MPSSettings.getServerSettings().maximumFlyingSpeedmps : MPSSettings.general.getMaximumFlyingSpeedmps;
    }

    public double getBaseMaxHeat(){
        return MPSSettings.getServerSettings() != null ? MPSSettings.getServerSettings().baseMaxHeat : MPSSettings.general.baseMaxHeat;
    }

    public static boolean useOldAutoFeeder() {
        return MPSSettings.getServerSettings() != null ? MPSSettings.getServerSettings().useOldAutoFeeder : MPSSettings.general.useOldAutoFeeder;
    }

    public boolean useCheatyLeatherRecipe() {
        return MPSSettings.getServerSettings() != null ? MPSSettings.getServerSettings().useCheatyLeatherRecipe : MPSSettings.general.useCheatyLeatherRecipe;
    }

    public boolean useHUDStuff() {
        return MPSSettings.general.useHUDStuff;
    }

    public double getWeightCapacity() {
        return MPSSettings.getServerSettings() != null ? MPSSettings.getServerSettings().getWeightCapacity : MPSSettings.general.getWeightCapacity;
    }

    public double getSalvageChance() {
        return MPSSettings.getServerSettings() != null ? MPSSettings.getServerSettings().getSalvageChance : MPSSettings.general.getSalvageChance;
    }

    public boolean useAdvancedOreScannerMessage() {
        return MPSSettings.getServerSettings() != null ? MPSSettings.getServerSettings().useAdvancedOreScannerMessage : MPSSettings.general.useAdvancedOreScannerMessage;
    }



    /** Energy ------------------------------------------------------------------------------------ */
    public int maxEnergyLVCapacitor() {
        return MPSSettings.getServerSettings() != null ? MPSSettings.getServerSettings().maxEnergyLVCapacitor : MPSSettings.energy.maxEnergyLVCapacitor;
    }

    public int maxEnergyMVCapacitor() {
        return MPSSettings.getServerSettings() != null ? MPSSettings.getServerSettings().maxEnergyMVCapacitor : MPSSettings.energy.maxEnergyMVCapacitor;
    }

    public int maxEnergyHVCapacitor() {
        return MPSSettings.getServerSettings() != null ? MPSSettings.getServerSettings().maxEnergyHVCapacitor : MPSSettings.energy.maxEnergyHVCapacitor;
    }

    public int maxEnergyEVCapacitor() {
        return MPSSettings.getServerSettings() != null ? MPSSettings.getServerSettings().maxEnergyEVCapacitor : MPSSettings.energy.maxEnergyEVCapacitor;
    }

    public double rfEnergyPerIC2EU() {
        return MPSSettings.getServerSettings() != null ? MPSSettings.getServerSettings().rfEnergyPerIC2EU : MPSSettings.energy.rfEnergyPerIC2EU;
    }

    /** Heat -------------------------------------------------------------------------------------- */
    public int maxHeatPowerFist() {
        return MPSSettings.getServerSettings() != null ? MPSSettings.getServerSettings().maxHeatPowerFist : MPSSettings.heat.maxHeatPowerFist;
    }

    public int maxHeatPowerArmorHelmet() {
        return MPSSettings.getServerSettings() != null ? MPSSettings.getServerSettings().maxHeatPowerArmorHelmet : MPSSettings.heat.maxHeatPowerArmorHelmet;
    }

    public int maxHeatPowerArmorChestplate() {
        return MPSSettings.getServerSettings() != null ? MPSSettings.getServerSettings().maxHeatPowerArmorChestplate : MPSSettings.heat.maxHeatPowerArmorChestplate;
    }

    public int maxHeatPowerArmorLeggings() {
        return MPSSettings.getServerSettings() != null ? MPSSettings.getServerSettings().maxHeatPowerArmorLeggings : MPSSettings.heat.maxHeatPowerArmorLeggings;
    }

    public int maxHeatPowerArmorBoots() {
        return MPSSettings.getServerSettings() != null ? MPSSettings.getServerSettings().maxHeatPowerArmorBoots : MPSSettings.heat.maxHeatPowerArmorBoots;
    }











    /** Inventory --------------------------------------------------------------------------------- */
    public int getMaxModuleCountPowerFist() {
        return MPSSettings.getServerSettings() != null ? MPSSettings.getServerSettings().maxModuleCountPowerFist : MPSSettings.inventory.maxModuleCountPowerFist;
    }

    public int getMaxModuleCountArmorHelmet() {
        return MPSSettings.getServerSettings() != null ? MPSSettings.getServerSettings().maxModuleCountArmorHelmet : MPSSettings.inventory.maxModuleCountArmorHelmet ;
    }

    public int getMaxModuleCountArmorChest() {
        return MPSSettings.getServerSettings() != null ? MPSSettings.getServerSettings().maxModuleCountArmorChest : MPSSettings.inventory.maxModuleCountArmorChest;
    }

    public int getMaxModuleCountArmorLegs() {
        return MPSSettings.getServerSettings() != null ? MPSSettings.getServerSettings().maxModuleCountArmorLegs : MPSSettings.inventory.maxModuleCountArmorLegs;
    }

    public int  getMaxModuleCountArmorFeet() {
        return MPSSettings.getServerSettings() != null ? MPSSettings.getServerSettings().maxModuleCountArmorFeet : MPSSettings.inventory.maxModuleCountArmorFeet;
    }

    /** HUD Settings ------------------------------------------------------------------------------ */
    public boolean toggleModuleSpam() {
        return MPSSettings.hud.toggleModuleSpam;
    }

    public boolean keybindHUDon() {
        return MPSSettings.hud.keybindHUDon;
    }

    public double keybindHUDx() {
        return MPSSettings.hud.keybindHUDx;
    }

    public double keybindHUDy() {
        return MPSSettings.hud.keybindHUDy;
    }

    public boolean useGraphicalMeters() {
        return MPSSettings.hud.useGraphicalMeters;
    }

    /** Graphics Settings ------------------------------------------------------------------------- */
    public int glowMultiplier() {
        return MPSSettings.graphics.glowMultiplier;
    }

    public boolean useShaders() {
        return MPSSettings.graphics.useShaders;
    }

    /** Fonts ------------------------------------------------------------------------------------- */
    public boolean useCustomFonts() {
        return MPSSettings.font.useCustomFonts;
    }

    public double fontDetail() {
        return MPSSettings.font.fontDetail;
    }

    public String fontURI() {
        return MPSSettings.font.fontURI;
    }

    public String fontName() {
        return MPSSettings.font.fontName;
    }

    public boolean fontAntiAliasing() {
        return MPSSettings.font.fontAntiAliasing;
    }

    /** Models ------------------------------------------------------------------------------------ */
    public boolean modelSetUp() {
        return MPSSettings.getServerSettings() != null ? MPSSettings.getServerSettings().modelSetup : MPSSettings.modelconfig.modelSetup;
    }

    public static boolean allowHighPollyArmorModels() {
        return MPSSettings.getServerSettings() != null ? MPSSettings.getServerSettings().allowHighPollyArmorModels : MPSSettings.modelconfig.allowHighPollyArmorModels;
    }

    public static boolean allowCustomHighPollyArmor() {
        return MPSSettings.getServerSettings() != null ? MPSSettings.getServerSettings().allowCustomHighPollyArmorModels : MPSSettings.modelconfig.allowCustomHighPollyArmor;
    }

    public static boolean allowHighPollyPowerFistModels() {
        return MPSSettings.getServerSettings() != null ? MPSSettings.getServerSettings().allowHighPollyPowerFistModels : MPSSettings.modelconfig.allowHighPollyPowerFistModels;
    }

    public boolean allowCustomPowerFistModels() {
        return MPSSettings.getServerSettings() != null ? MPSSettings.getServerSettings().allowCustomPowerFistModels : MPSSettings.modelconfig.allowCustomPowerFistModels;
    }

    public boolean allowCustomHighPollyPowerFistModels() {
        return MPSSettings.getServerSettings() != null ? MPSSettings.getServerSettings().allowCustomHighPollyPowerFistModels : MPSSettings.modelconfig.allowCustomHighPollyPowerFistModels;
    }

    /** Modules ----------------------------------------------------------------------------------- */





    public boolean getModuleAllowedorDefault(String name, boolean allowed) {
        if (MPSSettings.getServerSettings() != null) {
            if (!MPSSettings.getServerSettings().allowedModules.containsKey(name)) {
                MPSSettings.getServerSettings().allowedModules.put(name, allowed);
                System.out.println("Module not found in server config: " + name);
            }
            return MPSSettings.getServerSettings().allowedModules.get(name);

        } else {
            if (!MPSSettings.modules.allowedModules.containsKey(name))
                MPSSettings.modules.allowedModules.put(name, allowed);
            return MPSSettings.modules.allowedModules.get(name);
        }
    }
//
//    public Byte getPropertyByteOrDefault(String s, byte defaultVal) {
//        if (MPSSettings.getServerSettings() != null) {
//            if (!MPSSettings.getServerSettings().propertyBytes.containsKey(s))
//                MPSSettings.getServerSettings().propertyBytes.put(s, defaultVal);
//            return MPSSettings.getServerSettings().propertyBytes.get(s);
//
//        } else {
//            if (!MPSSettings.module.propertyBytes.containsKey(s))
//                MPSSettings.module.propertyBytes.put(s, defaultVal);
//            return MPSSettings.module.propertyBytes.get(s);
//        }
//    }
//
    public int getPropertyIntOrDefault(String s, int defaultVal) {
        if (MPSSettings.getServerSettings() != null) {
            if (!MPSSettings.getServerSettings().propertyInteger.containsKey(s))
                MPSSettings.getServerSettings().propertyInteger.put(s, defaultVal);
            return MPSSettings.getServerSettings().propertyInteger.get(s);

        } else {
            if (!MPSSettings.modules.propertyInteger.containsKey(s))
                MPSSettings.modules.propertyInteger.put(s, defaultVal);
            return MPSSettings.modules.propertyInteger.get(s);
        }
    }

    public double getPropertyDoubleOrDefault(String s, double defaultVal) {
//        System.out.println("property: " + s);
//        System.out.println("value: " + defaultVal);

        if (MPSSettings.getServerSettings() != null) {
//            System.out.println("made it here");

            if (!MPSSettings.getServerSettings().propertyDouble.containsKey(s)) {
//                System.out.println("made it here");
                MPSSettings.getServerSettings().propertyDouble.put(s, defaultVal);
            }
//            System.out.println("made it here");
            return MPSSettings.getServerSettings().propertyDouble.get(s);

        } else {
//            System.out.println("made it here");
            if (!MPSSettings.modules.propertyDouble.containsKey(s)) {
//                System.out.println("made it here");
                MPSSettings.modules.propertyDouble.put(s, defaultVal);
            }
//            System.out.println("made it here");
            return MPSSettings.modules.propertyDouble.get(s);
        }
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





//
//        /**
//         * Called in post-init. Extracts recipes if the configuration value is not found.
//         */
//        // TODO: a better idea might be to extract all of the recipes and then only read the ones that are valid.
//        // This would save on requests for recipe downloads
//
//        public static void extractRecipes() {
//            String key = "Auto-extract recipes";
//            if (!config.hasKey(Configuration.CATEGORY_GENERAL, key) || config.get(Configuration.CATEGORY_GENERAL, key, false).getBoolean()) {
//                config.get(Configuration.CATEGORY_GENERAL, key, false);
//                boolean found=false;
//                // Thermal Expansion
//                if(ModCompatibility.isThermalExpansionLoaded()) {
//                    found=true;
//                    copyRecipe("mps-thermalexpansion.recipes");
//                }
//
//                if (ModCompatibility.isTechRebornLoaded()) {
//                    found=true;
//                    copyRecipe("mps-TechReborn.recipes");
//
//                    // GregTech 2
//                } else if (ModCompatibility.isGregTechLoaded()) {
//                    found=true;
//                    copyRecipe("mps-GT5.recipes");
//
//                    // Industrialcraft 2 Exp
//                } else if (ModCompatibility.isIndustrialCraftExpLoaded()) {
//                    found=true;
//                    copyRecipe("mps-ic2.recipes");
//
//                    // Industrialcraft Classic
//                } else if (ModCompatibility.isIndustrialCraftClassicLoaded()) {
//                    found=true;
//                    copyRecipe("mps-ic2-classic.recipes");
//                }
//
//                // EnderIO
//                if (ModCompatibility.isEnderIOLoaded()) {
//                    found=true;
//                    copyRecipe("mps-enderio.recipes");
//                }
//
//                // Vanilla
//                if(!found) {
//                    copyRecipe("mps-vanilla.recipes");
//                }
//            }
//        }
//
//        public static void copyRecipe(String inFile) {
//            InputStream src = CommonProxy.class.getClassLoader().getResourceAsStream(inFile);
//            File dest = new File(Numina.configDir.toString() + "/machinemuse_old/recipes/" + inFile);
//            if(!dest.exists()) {
//                try {
//                    Files.copy(src, dest.toPath());
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//
//
//
//        /**
//         * The packet channel for this mod. We will only listen for and send packets
//         * on this 'channel'. Max of 16 characters.
//         *
//         * @return
//         */
//        public static String getNetworkChannelName() {
//            return "powerSuits";
//        }
//
//

//
//        public static void addModule(IModuleAsItem module) {
//            ModuleManager.addModule(module);
//        }
//

//

//

//


    //

//





















    public static boolean doAdditionalInfo() {
        return FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT && Keyboard.isKeyDown(Keyboard.KEY_LSHIFT);
    }

    @SideOnly(Side.CLIENT)
    public static String additionalInfoInstructions() {
        String message = I18n.format("tooltip.pressShift");
        return MuseStringUtils.wrapMultipleFormatTags(message, MuseStringUtils.FormatCodes.Grey, MuseStringUtils.FormatCodes.Italic);
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
        modelSpecFolder = new File(configFolder.getAbsolutePath().concat("/modelspec/"));
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

    /** Instance creation ------------------------------------------------------------------------- */
    private static MPSConfig INSTANCE;
    public static MPSConfig getInstance() {
        if (INSTANCE == null) synchronized (MPSConfig.class) {
            if (INSTANCE == null) INSTANCE = new MPSConfig();
        }
        return INSTANCE;
    }
    private MPSConfig() {}
}
