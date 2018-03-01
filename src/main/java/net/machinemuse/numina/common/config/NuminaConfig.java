package net.machinemuse.numina.common.config;

public class NuminaConfig {
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
        return NuminaSettings.getServerSettings() != null ? NuminaSettings.getServerSettings().mekRatio : NuminaSettings.mekRatio;
    }

    public static double getIC2Ratio() {
        return NuminaSettings.getServerSettings() != null ? NuminaSettings.getServerSettings().ic2Ratio : NuminaSettings.ic2Ratio;
    }

    public static double getRSRatio() {
        return NuminaSettings.getServerSettings() != null ? NuminaSettings.getServerSettings().rsRatio : NuminaSettings.rsRatio;
    }

    public static double getAE2Ratio() {
        return NuminaSettings.getServerSettings() != null ? NuminaSettings.getServerSettings().ae2Ratio : NuminaSettings.ae2Ratio;
    }

    public static int getTier1MaxRF() {
        return NuminaSettings.getServerSettings() != null ? NuminaSettings.getServerSettings().maxTier1 : NuminaSettings.maxTier1;
    }

    public static int getTier2MaxRF() {
        return NuminaSettings.getServerSettings() != null ? NuminaSettings.getServerSettings().maxTier2 : NuminaSettings.maxTier2;
    }

    public static int getTier3MaxRF() {
        return NuminaSettings.getServerSettings() != null ? NuminaSettings.getServerSettings().maxTier3 : NuminaSettings.maxTier3;
    }

    public static int getTier4MaxRF() {
        return NuminaSettings.getServerSettings() != null ? NuminaSettings.getServerSettings().maxTier4 : NuminaSettings.maxTier4;
    }





}
