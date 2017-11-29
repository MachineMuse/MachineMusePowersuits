package net.machinemuse.numina.common;

public class NuminaConstants {
    // Mod
    public static final String MODID = "numina";
    public static final String NAME = "Numina";
    public static final String VERSION = "@numina_version@";

    // Misc
    public static final String RESOURCE_PREFIX = MODID + ":";
    public static final String TEXTURE_PREFIX = RESOURCE_PREFIX + "textures/";
    public static final String LIGHTNING_TEXTURE = TEXTURE_PREFIX + "gui/lightning-medium.png";

    // Config
    public static final String CONFIG_FILE = "machinemuse/" +  MODID;
    public static final String CONFIG_PREFIX = "config." + MODID + ".";
    public static final String CONFIG_USE_FOV_FIX = CONFIG_PREFIX + "useFOVFix";
    public static final String CONFIG_FOV_FIX_DEAULT_STATE = CONFIG_PREFIX + "FOVFixDefaultState";
    public static final String CONFIG_USE_SOUNDS = CONFIG_PREFIX + "useSounds";
    public static final String CONFIG_DEBUGGING_INFO = CONFIG_PREFIX + "useDebuggingInfo";

}
