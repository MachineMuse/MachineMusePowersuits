package net.machinemuse.numina.api.constants;

public class NuminaConstants {
    // Mod
    public static final String MODID = "numina";
    public static final String NAME = "Numina";
    public static final String VERSION = "1.10.0";

    // Misc
    public static final String RESOURCE_PREFIX = MODID + ":";
    public static final String TEXTURE_PREFIX = RESOURCE_PREFIX + "textures/";
    public static final String LIGHTNING_TEXTURE = TEXTURE_PREFIX + "gui/lightning-medium.png";

    // Config
    public static final String CONFIG_FOLDER = "/machinemuse/";
    public static final String CONFIG_FILE = CONFIG_FOLDER +  MODID;
    public static final String CONFIG_PREFIX = "config." + MODID + ".";
    public static final String CONFIG_USE_FOV_FIX = CONFIG_PREFIX + "useFOVFix";
    public static final String CONFIG_FOV_FIX_DEAULT_STATE = CONFIG_PREFIX + "FOVFixDefaultState";
    public static final String CONFIG_USE_SOUNDS = CONFIG_PREFIX + "useSounds";
    public static final String CONFIG_DEBUGGING_INFO = CONFIG_PREFIX + "useDebuggingInfo";


    public static final String CONFIG_MEK_J_TO_RF_RATIO = CONFIG_PREFIX + "mekanismJToRFRatio";
    public static final String CONFIG_IC2_EU_TO_RF_RATIO = CONFIG_PREFIX + "industrialCraft2ToRFRatio";
    public static final String CONFIG_RS_TO_RF_RATIO = CONFIG_PREFIX +"refinedStorageToRFRatio";
    public static final String CONFIG_AE_TO_RF_RATIO = CONFIG_PREFIX + "appledEnergisticsToRFRatio";

    public static final String CONFIG_TIER_1_ENERGY_LVL = CONFIG_PREFIX + "tier1EnergyLevel";
    public static final String CONFIG_TIER_2_ENERGY_LVL = CONFIG_PREFIX + "tier2EnergyLevel";
    public static final String CONFIG_TIER_3_ENERGY_LVL = CONFIG_PREFIX + "tier3EnergyLevel";
    public static final String CONFIG_TIER_4_ENERGY_LVL = CONFIG_PREFIX + "tier4EnergyLevel";

    // String for overheat damage
    public static final String OVERHEAT_DAMAGE = "Overheat";
}
