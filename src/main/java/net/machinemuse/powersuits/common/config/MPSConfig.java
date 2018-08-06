package net.machinemuse.powersuits.common.config;

import jline.internal.Nullable;
import net.machinemuse.numina.common.Numina;
import net.machinemuse.powersuits.common.Config;
import net.machinemuse.powersuits.common.MPSCreativeTab;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;
import org.apache.commons.io.FileUtils;
import org.lwjgl.input.Keyboard;

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
        System.out.println("setting new server side settings");

        serverSettings = serverSettingsIn;
    }

    @Nullable
    public static final MPSServerSettings getServerSettings() {
        return serverSettings;
    }


    /** Creative tab ------------------------------------------------------------------------------ */
    private static CreativeTabs mpsCreativeTab;

    public static CreativeTabs getCreativeTab() {
        if (mpsCreativeTab == null)
            mpsCreativeTab = new MPSCreativeTab();
        return mpsCreativeTab;
    }


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

    // TODO: drop this
    public double getWeightCapacity() {
        return getServerSettings() != null ? getServerSettings().getWeightCapacity : MPSSettings.general.getWeightCapacity;
    }

    public static double getMaximumArmorPerPiece() {
        return getServerSettings() != null ? getServerSettings().maximumArmorPerPiece : MPSSettings.general.getMaximumArmorPerPiece;
    }

    // TODO: 100%
    public double getSalvageChance() {
        return getServerSettings() != null ? getServerSettings().getSalvageChance : MPSSettings.general.getSalvageChance;
    }

    public double baseMaxHeat() {
        return getServerSettings() != null ? getServerSettings().baseMaxHeat : MPSSettings.general.baseMaxHeat;
    }

    /** Modules ----------------------------------------------------------------------------------- */
    public boolean getModuleAllowedorDefault(String name, boolean allowed) {
        return getServerSettings() != null ? getServerSettings().allowedModules.getOrDefault(name, allowed) : MPSSettings.modules.allowedModules.getOrDefault(name, allowed);
    }

    public double getPropertyDoubleOrDefault(String name, double value) {
        //TODO: use this after porting finished
        //return getServerSettings() != null ? getServerSettings().propertyDouble.getOrDefault(name, value) : MPSSettings.modules.propertyDouble.getOrDefault(name, value);
        if (getServerSettings() != null) {
            if (getServerSettings().propertyDouble.isEmpty() || !getServerSettings().propertyDouble.containsKey(name)) {
                System.out.println("Property config values missing: ");
                System.out.println("property: " + name);
                System.out.println("value: " + value);
//                getServerSettings().propertyDouble.put(name, value);
                missingModuleDoubles.put(name, value);

            }
            return getServerSettings().propertyDouble.getOrDefault(name, value);
        } else {
            if (MPSSettings.modules.propertyDouble.isEmpty() || !MPSSettings.modules.propertyDouble.containsKey(name)) {
                System.out.println("Property config values missing: ");
                System.out.println("property: " + name);
                System.out.println("value: " + value);
//                MPSSettings.modules.propertyDouble.put(name, value);
                missingModuleDoubles.put(name, value);
            }
            return MPSSettings.modules.propertyDouble.getOrDefault(name, value);
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
    // 1MJ (MPS) = 1 MJ (Mekanism)
    public static double getMekRatio() {
        return getServerSettings() != null ? getServerSettings().mekRatio : MPSSettings.energy.mekRatio;
    }

    // 1 MJ = 2.5 EU
    // 1 EU = 0.4 MJ
    public static double getIC2Ratio() {
        return getServerSettings() != null ? getServerSettings().ic2Ratio : MPSSettings.energy.ic2Ratio;
    }

    // 1 MJ = 10 RF
    // 1 RF = 0.1 MJ
    public static double getRFRatio() {
        return getServerSettings() != null ? getServerSettings().rfRatio : MPSSettings.energy.rfRatio;
    }

    // (Refined Storage) 1 RS = 1 RF
    public static double getRSRatio() {
        return getServerSettings() != null ? getServerSettings().refinedStorageRatio : MPSSettings.energy.refinedStorageRatio;
    }

    // 1 MJ = 5 AE
    // 1 AE = 0.2 MJ
    public static double getAE2Ratio() {
        return getServerSettings() != null ? getServerSettings().ae2Ratio : MPSSettings.energy.ae2Ratio;
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

    public static boolean doAdditionalInfo() {
        return FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT && Keyboard.isKeyDown(Keyboard.KEY_LSHIFT);
    }
}
