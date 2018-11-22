package net.machinemuse.numina.api.constants;

public class NuminaNBTConstants {
    // main mod NBT tag
    public static final String TAG_ITEM_PREFIX = "MMModItem";// Machine Muse Mod
    public static final String TAG_MODULE_PREFIX = "MMModModule";// Machine Muse Mod

    public static final String TAG_RENDER = "render";
    public static final String TAG_COLOURS = "colours";
    public static final String TAG_ONLINE = "Active";
    public static final String TAG_MODE = "mode";
    public static final String TAG_MODEL = "model";
    public static final String TAG_PART = "part";
    public static final String TAG_VALUES = "commonValues"; // commonly used values that would normally be recalculated several times a minute.
    public static final String FLUID_NBT_KEY = "fluid";


    // ModularItemHandler tag
    public static final String TAG_MODULES = "modules";

    // energy
    public static final String MAXIMUM_ENERGY = "maxEnergy";
    public static final String CURRENT_ENERGY = "currEnergy";

    // heat
    public static final String CURRENT_HEAT = "curHeat";
    public static final String MAXIMUM_HEAT = "maxHeat";
}
