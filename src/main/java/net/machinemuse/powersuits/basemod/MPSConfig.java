package net.machinemuse.powersuits.basemod;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import net.machinemuse.powersuits.constants.MPSConfigConstants;
import net.machinemuse.powersuits.item.armor.ItemPowerArmorBoots;
import net.machinemuse.powersuits.item.armor.ItemPowerArmorChestplate;
import net.machinemuse.powersuits.item.armor.ItemPowerArmorHelmet;
import net.machinemuse.powersuits.item.armor.ItemPowerArmorLeggings;
import net.machinemuse.powersuits.item.tool.ItemPowerFist;
import net.machinemuse.powersuits.misc.CosmeticPresetSaveLoad;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public enum MPSConfig {
    INSTANCE;
    static MPSItems mpsItems = MPSItems.INSTANCE;

    public static final ClientConfig CLIENT_CONFIG;
    public static final ForgeConfigSpec CLIENT_SPEC;

    public static final ServerConfig SERVER_CONFIG;
    public static final ForgeConfigSpec SERVER_SPEC;

    static File clientFile;
    static File serverFile;
    static File configFolder = null;

    @Nullable
    public static File getConfigFolder() {
        return configFolder;
    }

    static {
        final Pair<ClientConfig, ForgeConfigSpec> clientSpecPair = new ForgeConfigSpec.Builder().configure(ClientConfig::new);
        CLIENT_SPEC = clientSpecPair.getRight();
        CLIENT_CONFIG = clientSpecPair.getLeft();
        clientFile = setupConfigFile("powersuits-client-only.toml");
        CLIENT_SPEC.setConfig(CommentedFileConfig.of(clientFile));

        final Pair<ServerConfig, ForgeConfigSpec> serverSpecPair = new ForgeConfigSpec.Builder().configure(ServerConfig::new);
        SERVER_SPEC = serverSpecPair.getRight();
        SERVER_CONFIG = serverSpecPair.getLeft();
        serverFile = setupConfigFile("powersuits-common.toml");
        SERVER_SPEC.setConfig(CommentedFileConfig.of(serverFile));
    }

    static File setupConfigFile(String fileName) {
        Path configFile = Paths.get("config/machinemuse").resolve(ModularPowersuits.MODID).resolve(fileName);
        File cfgFile = configFile.toFile();
        try {
            if (!cfgFile.getParentFile().exists())
                cfgFile.getParentFile().mkdirs();
            if (!cfgFile.exists())
                cfgFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (configFolder == null)
            configFolder = cfgFile.getParentFile();

        return cfgFile;
    }

    /**
     * [ CLIENT-ONLY ] -------------------------------------------------------------------------------------------
     */
    // HUD ----------------------------------------------------------------------------------------
    public static ForgeConfigSpec.BooleanValue
            HUD_USE_GRAPHICAL_METERS,
            HUD_TOGGLE_MODULE_SPAM,
            HUD_DISPLAY_HUD,
            HUD_USE_24_HOUR_CLOCK;

    // General ------------------------------------------------------------------------------------
    public static ForgeConfigSpec.BooleanValue
            GENERAL_ALLOW_CONFLICTING_KEYBINDS;

    public static ForgeConfigSpec.DoubleValue
            HUD_KEYBIND_HUD_X,
            HUD_KEYBIND_HUD_Y;


    /**
     * [ SEVER/CLIENT ]-------------------------------------------------------------------------------------------
     */
    // Cosmetics -------------------------------------------------------------------------------------------------
//           Note: these are controlled by the server because the legacy settings can create a vast number
//      of NBT Tags for tracking the settings for each individual model part.
    public static ForgeConfigSpec.BooleanValue
            COSMETIC_USE_LEGACY_COSMETIC_SYSTEM,
            COSMETIC_ALLOW_HIGH_POLLY_ARMOR_MODELS,
            COSMETIC_ALLOW_POWER_FIST_CUSTOMIZATOIN;


    // BATTERY MODULES ----------------------------------------------------------
    public static ForgeConfigSpec.IntValue
            BATTERY_MODULE_BASIC_MAX_ENERGY,
            BATTERY_MODULE_BASIC_MAX_TRAMSFER,
            BATTERY_MODULE_ADVANCED_MAX_ENERGY,
            BATTERY_MODULE_ADVANCED_MAX_TRAMSFER,
            BATTERY_MODULE_ELITE_MAX_ENERGY,
            BATTERY_MODULE_ELITE_MAX_TRAMSFER,
            BATTERY_MODULE_ULTIMATE_MAX_ENERGY,
            BATTERY_MODULE_ULTIMATE_MAX_TRAMSFER;

//    public static ForgeConfigSpec.ConfigValue<Map<? extends String,? extends Boolean>> MODULES_ALLOWED;




    /**
     * Client only settings
     */
    public static class ClientConfig {
        ClientConfig(ForgeConfigSpec.Builder builder) {
            // HUD ------------------------------------------------------------------------------------
            builder.comment("HUD settings").push("HUD");
            HUD_USE_GRAPHICAL_METERS = builder
                    .comment("Use Graphical Meters")
                    .translation(MPSConfigConstants.CONFIG_HUD_USE_GRAPHICAL_METERS)
                    .define("useGraphicalMeters", true);

            HUD_TOGGLE_MODULE_SPAM = builder
                    .comment("Chat message when toggling module")
                    .translation(MPSConfigConstants.CONFIG_HUD_TOGGLE_MODULE_SPAM)
                    .define("toggleModuleSpam", false);

            HUD_DISPLAY_HUD = builder
                    .comment("Display HUD")
                    .translation(MPSConfigConstants.CONFIG_HUD_DISPLAY_HUD)
                    .define("keybind_HUD_on", true);

            HUD_KEYBIND_HUD_X = builder
                    .comment("x position")
                    .translation(MPSConfigConstants.CONFIG_HUD_KEYBIND_HUD_X)
                    .defineInRange("keybindHUDx", 8.0, 0, Double.MAX_VALUE);

            HUD_KEYBIND_HUD_Y = builder
                    .comment("x position")
                    .translation(MPSConfigConstants.CONFIG_HUD_KEYBIND_HUD_Y)
                    .defineInRange("keybindHUDy", 32.0, 0, Double.MAX_VALUE);

            HUD_USE_24_HOUR_CLOCK = builder
                    .comment("Use a 24h clock instead of 12h")
                    .translation(MPSConfigConstants.CONFIG_HUD_USE_24_HOUR_CLOCK)
                    .define("use24hClock", false);
            builder.pop();

            builder.comment("General settings").push("General");
            GENERAL_ALLOW_CONFLICTING_KEYBINDS = builder
                    .comment("Allow Conflicting Keybinds")
                    .translation(MPSConfigConstants.CONFIG_GENERAL_ALLOW_CONFLICTING_KEYBINDS)
                    .define("allowConflictingKeybinds", false);
        }
    }


    static Map<String, ForgeConfigSpec.BooleanValue> testMap = new HashMap();

    /**
     * Settings that are controlled by the server and synced to client
     */
    public static class ServerConfig {


        public ServerConfig(ForgeConfigSpec.Builder builder) {
            // Cosmetics ------------------------------------------------------------------------------
            builder.comment("Model cosmetic settings").push("Cosmetic");

            COSMETIC_USE_LEGACY_COSMETIC_SYSTEM = builder
                    .comment("Use legacy cosmetic configuration instead of cosmetic presets")
                    .translation(MPSConfigConstants.CONFIG_COSMETIC_USE_LEGACY_COSMETIC_SYSTEM)
                    .worldRestart()
                    .define("useLegacyCosmeticSystem", true);

            COSMETIC_ALLOW_HIGH_POLLY_ARMOR_MODELS = builder
                    .comment("Allow high polly armor models instead of just skins")
                    .translation(MPSConfigConstants.CONFIG_COSMETIC_ALLOW_HIGH_POLLY_ARMOR_MODELS)
                    .define("allowHighPollyArmorModuels", true);

            COSMETIC_ALLOW_POWER_FIST_CUSTOMIZATOIN = builder
                    .comment("Allow PowerFist model to be customized")
                    .translation(MPSConfigConstants.CONFIG_COSMETIC_ALLOW_POWER_FIST_CUSTOMIZATOIN)
                    .define("allowPowerFistCustomization", true);
            builder.pop();

            /**
             * Modules --------------------------------------------------------------------------------
             */
            builder.comment("Module Specific Settings").push("Module");
            // Armor -----------------------------------------------------------------------------------
//            testMap.put(mpsItems.MODULE_LEATHER_PLATING__REGNAME,
//                            builder
//                            .comment("Allow PowerFist model to be customized")
//                            .translation(MPSConfigConstants.CONFIG_COSMETIC_ALLOW_POWER_FIST_CUSTOMIZATOIN)
//                            .define(mpsItems.MODULE_LEATHER_PLATING__REGNAME, true)
//            );





            // Energy ---------------------------------------------------------------------------------
            builder.push("Energy Modules");

            // Energy Storage  ------------------------------------------------------------------------
            builder.push("Energy Storage Modules");

            BATTERY_MODULE_BASIC_MAX_ENERGY = builder
                    .comment("Max Energy for Basic Battery Module")
                    .translation(MPSConfigConstants.CONFIG_MODULES_ENERGY_STORAGE_BASIC_BATTERY_MAX_ENERGY)
                    .defineInRange("basicBatteryMaxEnergy", 100000, 0, Integer.MAX_VALUE/4);
            BATTERY_MODULE_BASIC_MAX_TRAMSFER = builder
                    .comment("Max Energy Transfer for Basic Battery Module")
                    .translation(MPSConfigConstants.CONFIG_MODULES_ENERGY_STORAGE_BASIC_BATTERY_MAX_TRAMSFER)
                    .defineInRange("basicBatteryMaxTransfer", 12500, 100, Integer.MAX_VALUE);

            BATTERY_MODULE_ADVANCED_MAX_ENERGY = builder
                    .comment("Max Energy for Advanced Battery Module")
                    .translation(MPSConfigConstants.CONFIG_MODULES_ENERGY_STORAGE_ADVANCED__MAX_ENERGY)
                    .defineInRange("advancedBatteryMaxEnergy", 5000000, 100000, Integer.MAX_VALUE/3);
            BATTERY_MODULE_ADVANCED_MAX_TRAMSFER = builder
                    .comment("Max Energy Transfer for Advanced Battery Module")
                    .translation(MPSConfigConstants.CONFIG_MODULES_ENERGY_STORAGE_ADVANCED_MAX_TRAMSFER)
                    .defineInRange("advancedBatteryMaxTransfer", 625000, 100000, Integer.MAX_VALUE/3);

            BATTERY_MODULE_ELITE_MAX_ENERGY = builder
                    .comment("Max Energy for Elite Battery Module")
                    .translation(MPSConfigConstants.CONFIG_MODULES_ENERGY_STORAGE_ELITE__MAX_ENERGY)
                    .defineInRange("eliteBattery.maxEnergy.base", 50000000, 500000, Integer.MAX_VALUE);
            BATTERY_MODULE_ELITE_MAX_TRAMSFER = builder
                    .comment("Max Energy Transfer for Elite Battery Module")
                    .translation(MPSConfigConstants.CONFIG_MODULES_ENERGY_STORAGE_ELITE_MAX_TRAMSFER)
                    .defineInRange("eliteBatteryMaxTransfer", 6250000, 150000, Integer.MAX_VALUE/2);

            BATTERY_MODULE_ULTIMATE_MAX_ENERGY = builder
                    .comment("Max Energy for Ultimate Battery Module")
                    .translation(MPSConfigConstants.CONFIG_MODULES_ENERGY_STORAGE_ULTIMATE__MAX_ENERGY)
                    .defineInRange("eliteBattery.maxEnergy.base", 100000000, 1000000, Integer.MAX_VALUE);
            BATTERY_MODULE_ULTIMATE_MAX_TRAMSFER = builder
                    .comment("Max Energy Transfer for Ultimate Battery Module")
                    .translation(MPSConfigConstants.CONFIG_MODULES_ENERGY_STORAGE_ULTIMATE_MAX_TRAMSFER)
                    .defineInRange("ultimateBatteryMaxTransfer", 5000000, 100000000, Integer.MAX_VALUE);

            builder.pop();

            // Energy Storage  ------------------------------------------------------------------------
            builder.push("Energy Generation Modules");

            builder.pop();

            builder.push("Allowed Modules");
//            MODULES_ALLOWED = builder
//                    .comment("AllowdModules")
//                    .translation("I aint got no learnin")
//                    .defineInList();



            builder.pop(); // end energy modules

        }
    }

    public boolean getModuleAllowedorDefault(ResourceLocation regName, boolean defaultVal) {
        return defaultVal;
    }

    public boolean getModuleAllowedorDefault(String regName, boolean defaultVal) {
        return getModuleAllowedorDefault(new ResourceLocation(regName), defaultVal);
    }

    public double getPropertyDoubleOrDefault(String key, double multiplier) {
        return multiplier;
    }

    public int getPropertyIntegerOrDefault(String key, int multiplier) {
        return multiplier;
    }


    // fixme!!
    public boolean isModuleAllowed(ResourceLocation regName) {



        return true;
    }

    public static NBTTagCompound getPresetNBTFor(@Nonnull ItemStack itemStack, String presetName) {
        Map<String, NBTTagCompound> map = getCosmeticPresets(itemStack);
        return map.get(presetName);
    }

    public static BiMap<String, NBTTagCompound> getCosmeticPresets(@Nonnull ItemStack itemStack) {
        Item item  = itemStack.getItem();
//        if (item instanceof ItemPowerFist)
//            return getServerSettings() != null ? getServerSettings().cosmeticPresetsPowerFist : MPSSettings.cosmetics.getCosmeticPresetsPowerFist();
//        else if (item instanceof ItemPowerArmorHelmet)
//            return getServerSettings() != null ? getServerSettings().cosmeticPresetsPowerArmorHelmet : MPSSettings.cosmetics.getCosmeticPresetsPowerArmorHelmet();
//        else if (item instanceof ItemPowerArmorChestplate)
//            return getServerSettings() != null ? getServerSettings().cosmeticPresetsPowerArmorChestplate : MPSSettings.cosmetics.getCosmeticPresetsPowerArmorChestplate();
//        else if (item instanceof ItemPowerArmorLeggings)
//            return getServerSettings() != null ? getServerSettings().cosmeticPresetsPowerArmorLeggings : MPSSettings.cosmetics.getCosmeticPresetsPowerArmorLeggings();
//        else if (item instanceof ItemPowerArmorBoots)
//            return getServerSettings() != null ? getServerSettings().cosmeticPresetsPowerArmorBoots : MPSSettings.cosmetics.getCosmeticPresetsPowerArmorBoots();
        return HashBiMap.create();
    }

    public void updateCosmeticInfo(ResourceLocation location, String name, NBTTagCompound cosmeticInfo) {
        Item item = ForgeRegistries.ITEMS.getValue(location);

        if (item instanceof ItemPowerFist)
            cosmeticPresetsPowerFist.put(name, cosmeticInfo);
        else if (item instanceof ItemPowerArmorHelmet)
            cosmeticPresetsPowerArmorHelmet.put(name, cosmeticInfo);
        else if (item instanceof ItemPowerArmorChestplate)
            cosmeticPresetsPowerArmorChestplate.put(name, cosmeticInfo);
        else if (item instanceof ItemPowerArmorLeggings)
            cosmeticPresetsPowerArmorLeggings.put(name, cosmeticInfo);
        else if (item instanceof ItemPowerArmorBoots)
            cosmeticPresetsPowerArmorBoots.put(name, cosmeticInfo);
    }

    private BiMap<String, NBTTagCompound> cosmeticPresetsPowerFist = HashBiMap.create();
    public BiMap<String, NBTTagCompound> getCosmeticPresetsPowerFist() {
        if (cosmeticPresetsPowerFist.isEmpty() && !COSMETIC_ALLOW_POWER_FIST_CUSTOMIZATOIN.get())
            cosmeticPresetsPowerFist = CosmeticPresetSaveLoad.loadPresetsForItem(MPSItems.INSTANCE.powerFist, 0);
        return cosmeticPresetsPowerFist;
    }

    private BiMap<String, NBTTagCompound> cosmeticPresetsPowerArmorHelmet = HashBiMap.create();
    public BiMap<String, NBTTagCompound> getCosmeticPresetsPowerArmorHelmet() {
        if (cosmeticPresetsPowerArmorHelmet.isEmpty() && !COSMETIC_USE_LEGACY_COSMETIC_SYSTEM.get())
            cosmeticPresetsPowerArmorHelmet = CosmeticPresetSaveLoad.loadPresetsForItem(MPSItems.INSTANCE.powerArmorHead, 0);
        return cosmeticPresetsPowerArmorHelmet;
    }

    private BiMap<String, NBTTagCompound> cosmeticPresetsPowerArmorChestplate = HashBiMap.create();
    public BiMap<String, NBTTagCompound> getCosmeticPresetsPowerArmorChestplate() {
        if(cosmeticPresetsPowerArmorChestplate.isEmpty() && !COSMETIC_USE_LEGACY_COSMETIC_SYSTEM.get())
            cosmeticPresetsPowerArmorChestplate = CosmeticPresetSaveLoad.loadPresetsForItem(MPSItems.INSTANCE.powerArmorTorso, 0);
        return cosmeticPresetsPowerArmorChestplate;
    }

    private BiMap<String, NBTTagCompound> cosmeticPresetsPowerArmorLeggings = HashBiMap.create();
    public BiMap<String, NBTTagCompound> getCosmeticPresetsPowerArmorLeggings() {
        if(cosmeticPresetsPowerArmorLeggings.isEmpty() && !COSMETIC_USE_LEGACY_COSMETIC_SYSTEM.get())
            cosmeticPresetsPowerArmorLeggings = CosmeticPresetSaveLoad.loadPresetsForItem(MPSItems.INSTANCE.powerArmorLegs, 0);
        return cosmeticPresetsPowerArmorLeggings;
    }

    private BiMap<String, NBTTagCompound>  cosmeticPresetsPowerArmorBoots = HashBiMap.create();
    public BiMap<String, NBTTagCompound> getCosmeticPresetsPowerArmorBoots() {
        if(cosmeticPresetsPowerArmorBoots.isEmpty() && !COSMETIC_USE_LEGACY_COSMETIC_SYSTEM.get())
            cosmeticPresetsPowerArmorBoots = CosmeticPresetSaveLoad.loadPresetsForItem(MPSItems.INSTANCE.powerArmorFeet, 0);
        return cosmeticPresetsPowerArmorBoots;
    }
}