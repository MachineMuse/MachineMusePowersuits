package net.machinemuse.numina.common;


import net.machinemuse.numina.utils.MuseLogger;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import static net.machinemuse.numina.common.NuminaConstants.*;

@Config(modid = MODID, name = CONFIG_FILE)
public class NuminaSettings {
    @Config.LangKey(CONFIG_USE_FOV_FIX)
    @Config.Comment("Ignore speed boosts for field of view")
    @Config.RequiresWorldRestart
    public static boolean useFOVFix = true;


    @Config.LangKey(CONFIG_FOV_FIX_DEAULT_STATE)
    @Config.Comment("Default state of FOVfix on login (enabled = true, disabled = false)")
    @Config.RequiresWorldRestart
    public static boolean fovFixDefaultState = true;


    @Config.LangKey(CONFIG_USE_SOUNDS)
    @Config.Comment("Use sounds")
    @Config.RequiresWorldRestart
    public static boolean useSounds = true;


    @Config.LangKey(CONFIG_DEBUGGING_INFO)
    @Config.Comment("Debugging info")
    @Config.RequiresWorldRestart
    public static boolean isDebugging = false;


    @Mod.EventBusSubscriber
    public static class ConfigSyncHandler {
        @SubscribeEvent
        public static void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event) {
            if (event.getModID().equals(MODID)) {
                ConfigManager.sync(MODID, Config.Type.INSTANCE);
                MuseLogger.logInfo("Configuration has been saved.");
            }
        }
    }
}
