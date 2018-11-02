package net.machinemuse.powersuits.common.config;

import net.machinemuse.numina.api.module.EnumModuleCategory;
import net.machinemuse.numina.common.Numina;
import net.machinemuse.powersuits.common.MPSCreativeTab;
import net.machinemuse.powersuits.item.ItemComponent;
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
import java.util.Objects;
import java.util.TreeMap;

public enum MPSConfig {
    INSTANCE;

    /**
     * Creative tab ------------------------------------------------------------------------------
     */
    public static CreativeTabs mpsCreativeTab = new MPSCreativeTab();
    /**
     * Server side settings setup ----------------------------------------------------------------
     */
    private static MPSServerSettings serverSettings;
    /**
     * The annotation based config system lacks the ability to handle entries not set at runtime.
     * Writes the missing values to a file
     */
    Map<String, Double> missingModuleDoubles = new HashMap<>();
    /**
     * The annotation based config system lacks the ability to handle entries not set at runtime.
     * Writes the missing values to a file
     */
    Map<String, Integer> missingModuleIntegers = new HashMap<>();

    /**
     * Config folder -----------------------------------------------------------------------------
     */
    @Nullable
    public static File getConfigFolder() {
        return Numina.INSTANCE.configDir;
    }

    @Nullable
    public static final MPSServerSettings getServerSettings() {
        return serverSettings;
    }

    public static void setServerSettings(@Nullable final MPSServerSettings serverSettingsIn) {
        serverSettings = serverSettingsIn;
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

    public static int rfValueOfComponent(@Nonnull ItemStack stackInCost) {
        if (!stackInCost.isEmpty() && stackInCost.getItem() instanceof ItemComponent) {
            switch (stackInCost.getItemDamage() - ItemComponent.lvcapacitor.getItemDamage()) {
                case 0:
                    return 200000 * stackInCost.getCount();
                case 1:
                    return 1000000 * stackInCost.getCount();
                case 2:
                    return 7500000 * stackInCost.getCount();
                case 3:
                    return 10000000 * stackInCost.getCount();
                default:
                    return 0;
            }
        }
        return 0;
    }

    /**
     * Models ------------------------------------------------------------------------------------
     */
    public static boolean allowHighPollyArmorModels() {
//        return getServerSettings() != null ? getServerSettings().allowHighPollyArmorModels : MPSSettings.modelconfig.allowHighPollyArmorModels;
        return true;
    }

    public static boolean doAdditionalInfo() {
        return FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT && Keyboard.isKeyDown(Keyboard.KEY_LSHIFT);
    }

    /**
     * HUD Settings ------------------------------------------------------------------------------
     */
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

    /**
     * General -----------------------------------------------------------------------------------
     */
    // Client side settings
    public boolean use24hClock() {
        return MPSSettings.general.use24hClock;
    }

    public boolean allowConflictingKeybinds() {
        return MPSSettings.general.allowConflictingKeybinds;
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

//    public double getMaxModules(@Nonnull ItemStack itemStack) {
//        if (itemStack.getItem() instanceof ItemPowerFist) {
//            return getServerSettings() != null ? getServerSettings().maxModulesPowerFist: MPSSettings.general.maxModulesPowerFist;
//        }
//
//        if (itemStack.getItem() instanceof ItemPowerArmorHelmet) {
//            return getServerSettings() != null ? getServerSettings().maxModulesHelmet: MPSSettings.general.maxModulesHelmet;
//        }
//
//        if (itemStack.getItem() instanceof ItemPowerArmorChestplate) {
//            return getServerSettings() != null ? getServerSettings().maxModulesChestplate : MPSSettings.general.maxModulesChestplate;
//        }
//
//        if (itemStack.getItem() instanceof ItemPowerArmorLeggings) {
//            return getServerSettings() != null ? getServerSettings().maxModulesLeggings : MPSSettings.general.maxModulesLeggings ;
//        }
//
//        if (itemStack.getItem() instanceof ItemPowerArmorBoots) {
//            return getServerSettings() != null ? getServerSettings().maxModulesFeet : MPSSettings.general.maxModulesFeet;
//        }
//
//        return 0;
//    }

    /**
     * Limits -------------------------------------------------------------------------------------
     */
    public static int getMaxModulesOfType(EnumModuleCategory category) {
        switch (category) {
            case CATEGORY_ARMOR:
                return getServerSettings() != null ? getServerSettings().maxArmorModules : MPSSettings.limits.maxArmorModules;

            case CATEGORY_ENERGY_STORAGE:
                return getServerSettings() != null ? getServerSettings().maxEnergyStorageModules : MPSSettings.limits.maxEnergyStorageModules;

            case CATEGORY_ENERGY_GENERATION:
                return getServerSettings() != null ? getServerSettings().maxEnergyGenModules : MPSSettings.limits.maxEnergyGenModules;

            case CATEGORY_TOOL:
                return getServerSettings() != null ? getServerSettings().maxToolModules : MPSSettings.limits.maxToolModules;

            case CATEGORY_WEAPON:
                return getServerSettings() != null ? getServerSettings().maxWeaponModules : MPSSettings.limits.maxWeaponModules;

            case CATEGORY_MOVEMENT:
                return getServerSettings() != null ? getServerSettings().maxMovementModules : MPSSettings.limits.maxMovementModules;

            case CATEGORY_COSMETIC:
                return getServerSettings() != null ? getServerSettings().maxCosmeticModules : MPSSettings.limits.maxCosmeticModules;

            case CATEGORY_VISION:
                return getServerSettings() != null ? getServerSettings().maxVisionModules : MPSSettings.limits.maxVisionModules;

            case CATEGORY_ENVIRONMENTAL:
                return getServerSettings() != null ? getServerSettings().maxEnvironmentalModules : MPSSettings.limits.maxEnvironmentalModules;

            case CATEGORY_SPECIAL:
                return getServerSettings() != null ? getServerSettings().maxSpecialModules : MPSSettings.limits.maxSpecialModules;

            case CATEGORY_MINING_ENHANCEMENT:
                return getServerSettings() != null ? getServerSettings().maxMiningEnhancementModules : MPSSettings.limits.maxMiningEnhancementModules;

            default:
                return 0;
        }
    }

    /**
     * Modules -----------------------------------------------------------------------------------
     */
    public boolean getModuleAllowedorDefault(String name, boolean allowed) {
        // empty enhancement module
        if (Objects.equals("emptyEnhancement", name))
            return true;

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

    public void configDoubleKVGen() {
        StringBuilder stringBuilder = new StringBuilder();

        for (Map.Entry<String, Double> line : new TreeMap<>(missingModuleDoubles).entrySet()) { // treemap sorts the keys
            stringBuilder.append("put( \"").append(line.getKey()).append("\", ").append(line.getValue()).append("D );\n");
        }

        try {
            String output = stringBuilder.toString();
            if(output != null && !output.isEmpty())
                FileUtils.writeStringToFile(new File(getConfigFolder(), "missingConfigDoubles.txt"), output, Charset.defaultCharset(), false);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return;
    }

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
}
