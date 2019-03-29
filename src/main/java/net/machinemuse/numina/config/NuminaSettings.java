package net.machinemuse.numina.config;

import net.machinemuse.numina.basemod.MuseLogger;
import net.machinemuse.numina.constants.NuminaConstants;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import static net.machinemuse.numina.constants.NuminaConstants.*;

@Config(modid = NuminaConstants.MODID, name = CONFIG_FILE)
public class NuminaSettings {

    public static General general = new General();
    public static class General {
        @Config.LangKey(CONFIG_USE_FOV_FIX)
        @Config.Comment("Ignore speed boosts for field of view")
        public boolean useFOVFix = true;


        @Config.LangKey(CONFIG_FOV_FIX_DEAULT_STATE)
        @Config.Comment("Default state of FOVfix on login (enabled = true, disabled = false)")
        public boolean fovFixDefaultState = true;


        @Config.LangKey(CONFIG_USE_SOUNDS)
        @Config.Comment("Use sounds")
        public boolean useSounds = true;


        @Config.LangKey(CONFIG_DEBUGGING_INFO)
        @Config.Comment("Debugging info")
        public boolean isDebugging = false;

        @Config.LangKey(CONFIG_MEK_J_TO_RF_RATIO)
        @Config.Comment("Mekanism Joules equals how many RF")
        public double mekRatio = 0.4;

        // 1 RF = 0.25 EU
        // 1 EU = 4 RF
        @Config.LangKey(CONFIG_IC2_EU_TO_RF_RATIO)
        @Config.Comment("IndustrialCraft2 EU equals how many RF")
        @Config.RequiresWorldRestart
        public double ic2Ratio = 4D;


        @Config.LangKey(CONFIG_RS_TO_RF_RATIO)
        @Config.Comment("Refined PlayerFOVStateStorage  energy equals how many RF")
        @Config.RequiresWorldRestart
        public double rsRatio = 1D;


        @Config.LangKey(CONFIG_AE_TO_RF_RATIO)
        @Config.Comment("Applied Energistics AE energy equals how many RF")
        @Config.RequiresWorldRestart
        public double ae2Ratio = 2D;


        // (100KJ or 1M-RF)
        @Config.LangKey(CONFIG_TIER_1_ENERGY_LVL)
        @Config.Comment("Maximum amount of RF energy for Tier 1.")
        @Config.RangeInt(min = 0)
        public int maxTier1 = (int) (1 * Math.pow(10, 6));

        // advanced capacitor (500KJ or 5M-RF)
        @Config.LangKey(CONFIG_TIER_2_ENERGY_LVL)
        @Config.Comment("Maximum amount of RF energy for Tier 2.")
        @Config.RangeInt(min = 0)
        public int maxTier2 = (int) (5 * Math.pow(10, 6));

        // elite capacitor (5MJ or 50M-RF)
        @Config.LangKey(CONFIG_TIER_3_ENERGY_LVL)
        @Config.Comment("Maximum amount of RF energy for Tier 3.")
        @Config.RangeInt(min = 0)
        public int maxTier3 = (int) (5 * Math.pow(10, 7));

        // ultimate capacitor (10MJ or 100M-RF)
        @Config.LangKey(CONFIG_TIER_4_ENERGY_LVL)
        @Config.Comment("Maximum amount of RF energy for Tier 4.")
        @Config.RangeInt(min = 0)
        public int maxTier4 = (int) (1 * Math.pow(10, 8));
    }

    @Mod.EventBusSubscriber(modid = NuminaConstants.MODID)
    public class ConfigSyncHandler {
        @SubscribeEvent
        public void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event) {
            if (event.getModID().equals(MODID)) {
                ConfigManager.load(MODID, Config.Type.INSTANCE);
                MuseLogger.logger.info("Configuration has been saved.");
            }
        }
    }
}