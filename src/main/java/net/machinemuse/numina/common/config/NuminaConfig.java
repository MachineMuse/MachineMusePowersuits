package net.machinemuse.numina.common.config;

import javax.annotation.Nullable;

public enum NuminaConfig {
    INSTANCE;

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

    public static double getMekRatio() {
        return getServerSettings() != null ? getServerSettings().mekRatio : NuminaSettings.mekRatio;
    }

    public static double getIC2Ratio() {
        return getServerSettings() != null ? getServerSettings().ic2Ratio : NuminaSettings.ic2Ratio;
    }

    public static double getRSRatio() {
        return getServerSettings() != null ? getServerSettings().rsRatio : NuminaSettings.rsRatio;
    }

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

    private static NuminaServerSettings serverSettings;
    public static void setServerSettings(@Nullable final NuminaServerSettings serverSettings) {
        INSTANCE.serverSettings = serverSettings;
    }

    @Nullable
    public static final NuminaServerSettings getServerSettings() {
        return INSTANCE.serverSettings;
    }
}