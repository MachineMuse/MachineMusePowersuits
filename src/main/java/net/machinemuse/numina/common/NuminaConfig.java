package net.machinemuse.numina.common;

import net.machinemuse.numina.general.MuseLogger;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import java.io.File;

/**
 * Author: MachineMuse (Claire Semple)
 * Created: 11:31 AM, 9/3/13
 *
 * Ported to Java by lehjr on 11/14/16.
 */
public final class NuminaConfig {
    static boolean hasWarned = false;
    static Configuration config = null;

    public static void init(final FMLPreInitializationEvent event) {
        if (config == null) {
            File configFile = new File(event.getModConfigurationDirectory() + "/machinemuse/numina.cfg");
            config = new Configuration(configFile);
        }
        // Initialize config file
        isDebugging();
        useFOVFix();
        useSounds();
        config.save();
    }

    public static void warnOnce(final String s) {
        if (!hasWarned) {
            MuseLogger.logError("WARNING: unlocalizedName is deprecated; please use registryName or itemStackName instead!");
            hasWarned = true;
        }
    }

    public static boolean fovFixDefaultState() {
        return getConfigBoolean(Configuration.CATEGORY_GENERAL, "Default state of FOVfix on login (enabled = true, disabled = false)", true);
    }

    public static boolean useFOVFix() {
        return getConfigBoolean(Configuration.CATEGORY_GENERAL, "Ignore speed boosts for field of view", true);
    }

    public static boolean isDebugging() {
        return getConfigBoolean(Configuration.CATEGORY_GENERAL, "Debugging info", false);
    }

    public static boolean useSounds() {
        return getConfigBoolean(Configuration.CATEGORY_GENERAL, "Use sounds", true);
    }

    public static boolean getConfigBoolean(String category, String name, boolean default1) {
        Property ret = config.get(category, name, default1);
        return ret.getBoolean(default1);
    }
}