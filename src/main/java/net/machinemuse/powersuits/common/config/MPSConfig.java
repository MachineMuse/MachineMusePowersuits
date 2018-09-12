package net.machinemuse.powersuits.common.config;

import net.machinemuse.numina.common.Numina;
import net.machinemuse.powersuits.common.MPSCreativeTab;
import net.machinemuse.powersuits.item.armor.ItemPowerArmorBoots;
import net.machinemuse.powersuits.item.armor.ItemPowerArmorChestplate;
import net.machinemuse.powersuits.item.armor.ItemPowerArmorHelmet;
import net.machinemuse.powersuits.item.armor.ItemPowerArmorLeggings;
import net.machinemuse.powersuits.item.tool.ItemPowerFist;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;
import org.apache.commons.io.FileUtils;
import org.lwjgl.input.Keyboard;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public enum MPSConfig {
    INSTANCE;

    /** Config folder ----------------------------------------------------------------------------- */
    @Nullable
    public static File getConfigFolder() {
        return Numina.INSTANCE.configDir;
    }

    /** Server side settings setup ---------------------------------------------------------------- */
    private static MPSServerSettings serverSettings;
    public static void setServerSettings(@Nullable final MPSServerSettings serverSettingsIn) {
        serverSettings = serverSettingsIn;
    }

    @Nullable
    public static final MPSServerSettings getServerSettings() {
        return serverSettings;
    }


    /** Creative tab ------------------------------------------------------------------------------ */
    public static CreativeTabs mpsCreativeTab = new MPSCreativeTab();

    /** HUD Settings ------------------------------------------------------------------------------ */
    public boolean useHUDStuff() {
        return MPSSettings.hud.useHUDStuff;
    }

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

    /** General ----------------------------------------------------------------------------------- */
    // Client side settings
    public boolean use24hClock() {
        return MPSSettings.general.use24hClock;
    }

    public boolean allowConflictingKeybinds() {
        return MPSSettings.general.allowConflictingKeybinds;
    }

    // Server side settings
    public static double getMaximumFlyingSpeedmps() {
        return getServerSettings() != null ? getServerSettings().maximumFlyingSpeedmps : MPSSettings.general.getMaximumFlyingSpeedmps;
    }

    public static boolean useOldAutoFeeder() {
        return getServerSettings() != null ? getServerSettings().useOldAutoFeeder : MPSSettings.general.useOldAutoFeeder;
    }

    public static double getMaximumArmorPerPiece() {
        return getServerSettings() != null ? getServerSettings().maximumArmorPerPiece : MPSSettings.general.getMaximumArmorPerPiece;
    }

    // TODO: 100%
    public double getSalvageChance() {
        return getServerSettings() != null ? getServerSettings().getSalvageChance : MPSSettings.general.getSalvageChance;
    }

    public double getBaseMaxHeat(@Nonnull ItemStack itemStack) {
        if (itemStack.getItem() instanceof ItemPowerFist) {
           return getServerSettings() != null ? getServerSettings().baseMaxHeatPowerFist : MPSSettings.general.baseMaxHeatPowerFist;
        }

        if (itemStack.getItem() instanceof ItemPowerArmorHelmet) {
            return getServerSettings() != null ? getServerSettings().baseMaxHeatHelmet : MPSSettings.general.baseMaxHeatHelmet;
        }


        if (itemStack.getItem() instanceof ItemPowerArmorChestplate) {
            return getServerSettings() != null ? getServerSettings().baseMaxHeatChest : MPSSettings.general.baseMaxHeatChest;
        }


        if (itemStack.getItem() instanceof ItemPowerArmorLeggings) {
            return getServerSettings() != null ? getServerSettings().baseMaxHeatLegs : MPSSettings.general.baseMaxHeatLegs;
        }

        if (itemStack.getItem() instanceof ItemPowerArmorBoots) {
            return getServerSettings() != null ? getServerSettings().baseMaxHeatFeet : MPSSettings.general.baseMaxHeatFeet;
        }

       return 0;
    }

    /** Modules ----------------------------------------------------------------------------------- */
    public boolean getModuleAllowedorDefault(String name, boolean allowed) {
        return getServerSettings() != null ? getServerSettings().allowedModules.getOrDefault(name, allowed) : MPSSettings.modules.allowedModules.getOrDefault(name, allowed);
    }

    public double getPropertyDoubleOrDefault(String name, double value) {
        //TODO: use this after porting finished
        //return getServerSettings() != null ? getServerSettings().propertyDouble.getOrDefault(name, getValue) : MPSSettings.modules.propertyDouble.getOrDefault(name, getValue);
        if (getServerSettings() != null) {
            if (getServerSettings().propertyDouble.isEmpty() || !getServerSettings().propertyDouble.containsKey(name)) {
                System.out.println("Property config values missing: ");
                System.out.println("property: " + name);
                System.out.println("getValue: " + value);
//                getServerSettings().propertyDouble.put(name, getValue);
                missingModuleDoubles.put(name, value);

            }
            return getServerSettings().propertyDouble.getOrDefault(name, value);
        } else {
            if (MPSSettings.modules.propertyDouble.isEmpty() || !MPSSettings.modules.propertyDouble.containsKey(name)) {
                System.out.println("Property config values missing: ");
                System.out.println("property: " + name);
                System.out.println("getValue: " + value);
//                MPSSettings.modules.propertyDouble.put(name, getValue);
                missingModuleDoubles.put(name, value);
            }
            return MPSSettings.modules.propertyDouble.getOrDefault(name, value);
        }
    }

    public int getPropertyIntegerOrDefault(String name, int value) {
        //TODO: use this after porting finished
        //return getServerSettings() != null ? getServerSettings().propertyDouble.getOrDefault(name, getValue) : MPSSettings.modules.propertyDouble.getOrDefault(name, getValue);
        if (getServerSettings() != null) {
            if (getServerSettings().propertyInteger.isEmpty() || !getServerSettings().propertyInteger.containsKey(name)) {
                System.out.println("Property config values missing: ");
                System.out.println("property: " + name);
                System.out.println("getValue: " + value);
//                getServerSettings().propertyInteger.put(name, getValue);
                missingModuleIntegers.put(name, value);
            }
            return getServerSettings().propertyInteger.getOrDefault(name, value);
        } else {
            if (MPSSettings.modules.propertyInteger.isEmpty() || !MPSSettings.modules.propertyInteger.containsKey(name)) {
                System.out.println("Property config values missing: ");
                System.out.println("property: " + name);
                System.out.println("getValue: " + value);
//                MPSSettings.modules.propertyInteger.put(name, getValue);
                missingModuleIntegers.put(name, value);
            }
            return MPSSettings.modules.propertyInteger.getOrDefault(name, value);
        }
    }



    /** Models ------------------------------------------------------------------------------------ */
    public boolean modelSetUp() {
//        return getServerSettings() != null ? getServerSettings().modelSetup : MPSSettings.modelconfig.modelSetup;
        return true;
    }

    public static boolean allowHighPollyArmorModels() {
//        return getServerSettings() != null ? getServerSettings().allowHighPollyArmorModels : MPSSettings.modelconfig.allowHighPollyArmorModels;
        return true;
    }

    public static boolean allowCustomHighPollyArmor() {
//        return getServerSettings() != null ? getServerSettings().allowCustomHighPollyArmorModels : MPSSettings.modelconfig.allowCustomHighPollyArmor;
        return true;
    }

    public static boolean allowHighPollyPowerFistModels() {
//        return getServerSettings() != null ? getServerSettings().allowHighPollyPowerFistModels : MPSSettings.modelconfig.allowHighPollyPowerFistModels;
        return true;
    }

    public boolean allowCustomPowerFistModels() {
//        return getServerSettings() != null ? getServerSettings().allowCustomPowerFistModels : MPSSettings.modelconfig.allowCustomPowerFistModels;
        return true;
    }

    public boolean allowCustomHighPollyPowerFistModels() {
//        return getServerSettings() != null ? getServerSettings().allowCustomHighPollyPowerFistModels : MPSSettings.modelconfig.allowCustomHighPollyPowerFistModels;
        return true;
    }



    /** Energy ------------------------------------------------------------------------------------ */




    // 1 RF = 0.1 MJ (Mekanism)
    public static double getMekRatio() {
        return getServerSettings() != null ? getServerSettings().mekRatio : MPSSettings.energy.mekRatio;
    }

    // 1 RF = 0.25 EU
    public static double getIC2Ratio() {
        return getServerSettings() != null ? getServerSettings().ic2Ratio : MPSSettings.energy.ic2Ratio;
    }

    // (Refined Storage) 1 RS = 1 RF
    public static double getRSRatio() {
        return getServerSettings() != null ? getServerSettings().refinedStorageRatio : MPSSettings.energy.refinedStorageRatio;
    }

    // 1 RF = 0.5 AE
    public static double getAE2Ratio() {
        return getServerSettings() != null ? getServerSettings().ae2Ratio : MPSSettings.energy.ae2Ratio;
    }

    public static int getTier1MaxRF() {
        return getServerSettings() != null ? getServerSettings().maxTier1 : MPSSettings.energy.maxTier1;
    }

    public static int getTier2MaxRF() {
        return getServerSettings() != null ? getServerSettings().maxTier2 : MPSSettings.energy.maxTier2;
    }

    public static int getTier3MaxRF() {
        return getServerSettings() != null ? getServerSettings().maxTier3 : MPSSettings.energy.maxTier3;
    }

    public static int getTier4MaxRF() {
        return getServerSettings() != null ? getServerSettings().maxTier4 : MPSSettings.energy.maxTier4;
    }

    /**
     * The annotation based config system lacks the ability to handle
     * Writes the missing values to a file
     */
    Map<String, Double> missingModuleDoubles = new HashMap<>();
    public void configDoubleKVGen() {
        StringBuilder stringBuilder = new StringBuilder();

        for (Map.Entry<String, Double> line : new TreeMap<>(missingModuleDoubles).entrySet()) { // treemap sorts the keys
            stringBuilder.append("put( \"").append(line.getKey()).append("\", ").append(line.getValue()).append("D );\n");
        }

        try {
            FileUtils.writeStringToFile(new File(getConfigFolder(), "missingConfigDoubles.txt"), stringBuilder.toString(), Charset.defaultCharset(), false);
            } catch (IOException e) {
            e.printStackTrace();
        }
        return;
    }

    /**
     * The annotation based config system lacks the ability to handle
     * Writes the missing values to a file
     */
    Map<String, Integer> missingModuleIntegers = new HashMap<>();
    public void configIntegerKVGen() {
        StringBuilder stringBuilder = new StringBuilder();

        for (Map.Entry<String, Integer> line : new TreeMap<>(missingModuleIntegers).entrySet()) { // treemap sorts the keys
            stringBuilder.append("put( \"").append(line.getKey()).append("\", ").append(line.getValue()).append("D );\n");
        }

        try {
            FileUtils.writeStringToFile(new File(getConfigFolder(), "missingConfigIntegers.txt"), stringBuilder.toString(), Charset.defaultCharset(), false);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return;
    }


    public static boolean doAdditionalInfo() {
        return FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT && Keyboard.isKeyDown(Keyboard.KEY_LSHIFT);
    }
}
