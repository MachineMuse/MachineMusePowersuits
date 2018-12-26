package net.machinemuse.numina.common.config;

import net.machinemuse.numina.capabilities.energy.adapter.ElectricAdapter;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public enum NuminaConfig {
    INSTANCE;

    private static NuminaServerSettings serverSettings;

    public static boolean useSounds() {
        return NuminaSettings.useSounds;
    }

    public static boolean isDebugging() {
        return NuminaSettings.isDebugging;
    }

    public static boolean useFOVFix() {
        return NuminaSettings.useFOVFix;
    }

    public static boolean fovFixDefaultState() {
        return NuminaSettings.fovFixDefaultState;
    }

    /**
     * Energy ------------------------------------------------------------------------------------
     */
    // 1 RF = 0.1 MJ (Mekanism)
    public static double getMekRatio() {
        return getServerSettings() != null ? getServerSettings().mekRatio : NuminaSettings.mekRatio;
    }

    // 1 RF = 0.25 EU
    public static double getIC2Ratio() {
        return getServerSettings() != null ? getServerSettings().ic2Ratio : NuminaSettings.ic2Ratio;
    }

    // (Refined Storage) 1 RS = 1 RF
    public static double getRSRatio() {
        return getServerSettings() != null ? getServerSettings().rsRatio : NuminaSettings.rsRatio;
    }

    // 1 RF = 0.5 AE
    public static double getAE2Ratio() {
        return getServerSettings() != null ? getServerSettings().ae2Ratio : NuminaSettings.ae2Ratio;
    }

    public static int getTier1MaxRF() {
        return getServerSettings() != null ? getServerSettings().maxTier1 : NuminaSettings.maxTier1;
    }

    public static int getTier2MaxRF() {
        return getServerSettings() != null ? getServerSettings().maxTier2 : NuminaSettings.maxTier2;
    }

    public static int getTier3MaxRF() {
        return getServerSettings() != null ? getServerSettings().maxTier3 : NuminaSettings.maxTier3;
    }

    public static int getTier4MaxRF() {
        return getServerSettings() != null ? getServerSettings().maxTier4 : NuminaSettings.maxTier4;
    }

    /**
     * Used for getting the tier of an ItemStack. Used for various functions
     */
    public static int getTierForItem(@Nonnull ItemStack itemStack) {
        ElectricAdapter adapter = ElectricAdapter.wrap(itemStack);
        if (adapter != null) {
            int maxEnergy = adapter.getMaxEnergyStored();
            if (maxEnergy <= getTier1MaxRF())
                return 1;
            else if (maxEnergy <= getTier2MaxRF())
                return 2;
            else if (maxEnergy <= getTier3MaxRF())
                return 3;
            else if (maxEnergy <= getTier4MaxRF())
                return 4;
            return 5;
        }
        return 0;
    }

    @Nullable
    public static final NuminaServerSettings getServerSettings() {
        return INSTANCE.serverSettings;
    }

    public static void setServerSettings(@Nullable final NuminaServerSettings serverSettings) {
        INSTANCE.serverSettings = serverSettings;
    }
}