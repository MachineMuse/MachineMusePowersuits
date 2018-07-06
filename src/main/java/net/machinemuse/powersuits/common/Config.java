package net.machinemuse.powersuits.common;

import com.google.gson.*;
import net.machinemuse.numina.api.item.IModularItem;
import net.machinemuse.numina.api.module.IPowerModule;
import net.machinemuse.numina.common.Numina;
import net.machinemuse.numina.utils.MuseLogger;
import net.machinemuse.powersuits.api.module.ModuleManager;
import net.machinemuse.powersuits.common.proxy.CommonProxy;
import net.machinemuse.powersuits.powermodule.armor.*;
import net.machinemuse.powersuits.powermodule.cosmetic.*;
import net.machinemuse.powersuits.powermodule.energy.*;
import net.machinemuse.powersuits.powermodule.misc.*;
import net.machinemuse.powersuits.powermodule.movement.*;
import net.machinemuse.powersuits.powermodule.tool.*;
import net.machinemuse.powersuits.powermodule.weapon.*;
import net.machinemuse.powersuits.utils.MuseStringUtils;
import net.minecraft.client.resources.I18n;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreDictionary;
import org.lwjgl.input.Keyboard;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.*;



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
    private static Configuration config = null;


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
        InputStream src = CommonProxy.class.getClassLoader().getResourceAsStream(inFile);
        File dest = new File(Numina.INSTANCE.configDir, "/recipes/" + inFile);
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


    public static boolean useAdvancedOreScannerMessage() {
        return config.get(Configuration.CATEGORY_GENERAL, "Use Detailed Ore Scanner Message", true).getBoolean(true);
    }

    public static Map<Map<ResourceLocation, Integer>, Integer> getOreValues() {
        return readOreValues();
    }


    public static void addModule(IPowerModule module) {
        ModuleManager.INSTANCE.addModule(module);
    }

    /**
     * Load all the modules in the config file into memory. Eventually. For now,
     * they are hardcoded.
     */
    public static void loadPowerModules() {
        List<IModularItem> ARMORONLY = Arrays.asList(
                (IModularItem)MPSItems.INSTANCE.powerArmorHead,
                (IModularItem)MPSItems.INSTANCE.powerArmorTorso,
                (IModularItem)MPSItems.INSTANCE.powerArmorLegs,
                (IModularItem)MPSItems.INSTANCE.powerArmorFeet);

        List<IModularItem> ALLITEMS = Arrays.asList(
                (IModularItem)MPSItems.INSTANCE.powerArmorHead,
                (IModularItem)MPSItems.INSTANCE.powerArmorTorso,
                (IModularItem)MPSItems.INSTANCE.powerArmorLegs,
                (IModularItem)MPSItems.INSTANCE.powerArmorFeet,
                (IModularItem)MPSItems.INSTANCE.powerFist);

        List<IModularItem> HEADONLY = Collections.singletonList((IModularItem)MPSItems.INSTANCE.powerArmorHead);
        List<IModularItem> TORSOONLY = Collections.singletonList((IModularItem)MPSItems.INSTANCE.powerArmorTorso);
        List<IModularItem> LEGSONLY = Collections.singletonList((IModularItem)MPSItems.INSTANCE.powerArmorLegs);
        List<IModularItem> FEETONLY = Collections.singletonList((IModularItem)MPSItems.INSTANCE.powerArmorFeet);
        List<IModularItem> TOOLONLY = Collections.singletonList((IModularItem)MPSItems.INSTANCE.powerFist);

        // FIXME: these need to be sorted
        /* Armor -------------------------------- */
        addModule(new BasicPlatingModule(ARMORONLY));
        addModule(new DiamondPlatingModule(ARMORONLY));
        addModule(new EnergyShieldModule(ARMORONLY));
        addModule(new HeatSinkModule(ARMORONLY));


        /* Cosmetic ----------------------------- */
        addModule(new TransparentArmorModule(ARMORONLY));
        addModule(new CosmeticGlowModule(ARMORONLY));
        addModule(new CitizenJoeStyle(ARMORONLY));
        addModule(new HighPolyArmor(ARMORONLY));
//        addModule(new TintModule(TOOLONLY));
        addModule(new TintModule(ALLITEMS));


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
        //addModule(new CoalGenerator(TORSOONLY)); //doesn't seem to be working


        /* Legs --------------------------------- */
        addModule(new SprintAssistModule(LEGSONLY));
        addModule(new JumpAssistModule(LEGSONLY));
        addModule(new SwimAssistModule(LEGSONLY));
        addModule(new KineticGeneratorModule(LEGSONLY));
        addModule(new ClimbAssistModule(LEGSONLY)); // OBSOLETE due to changes in Minecraft


        /* Feet --------------------------------- */
        addModule(new JetBootsModule(FEETONLY));
        addModule(new ShockAbsorberModule(FEETONLY));
    }

    public static Configuration getConfig() {
        return config;
    }





    public static double baseMaxHeat() {
        return config.get(Configuration.CATEGORY_GENERAL, "Base Heat Cap", 50.0).getDouble(50.0);
    }

    public static void addCustomInstallCosts() {
        try {
            File installCostFile = new File(Numina.INSTANCE.configDir, "custominstallcosts.json");
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
                        if(!stack.isEmpty()) {
                            ModuleManager.INSTANCE.addCustomInstallCost(moduleName, stack);
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

    /*
     * Ore Value file parser for the OreScannerModule
     */
    private static Map<Map<ResourceLocation, Integer>, Integer> readOreValues() {
        Map<Map<ResourceLocation, Integer>, Integer> oreValues = new HashMap<>();
        String oreValuesFileName = "oreValues.json";
        try {
            File oreValuesFile = new File(Numina.INSTANCE.configDir, oreValuesFileName);
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
}