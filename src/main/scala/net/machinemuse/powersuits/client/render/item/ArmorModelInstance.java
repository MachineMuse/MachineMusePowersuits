package net.machinemuse.powersuits.client.render.item;

import net.machinemuse.powersuits.common.ModCompatability;
import net.machinemuse.numina.general.MuseLogger;

/**
 * Author: MachineMuse (Claire Semple)
 * Created: 10:01 PM, 11/07/13
 */
public class ArmorModelInstance {
        private static ArmorModel instance = null;

        public static ArmorModel getInstance() {
                if (instance == null) {
                        if (ModCompatability.isSmartMovingLoaded() && ModCompatability.isRenderPlayerAPILoaded()) {
                                try {
                                        MuseLogger.logInfo("Attempting to load SmartMoving armor model.");
                                        instance = new SMovingArmorModel();
                                        MuseLogger.logInfo("SmartMoving armor model loaded successfully!");
                                } catch (Throwable e) {
                                        e.printStackTrace();
                                        MuseLogger.logError("SmartMoving armor model did not load successfully. Either SmartMoving and RenderPlayerAPI were not installed correctly, or there was another problem.");
                                        MuseLogger.logError("Please ensure SmartMoving and RenderPlayerAPI are installed correctly and are the correct version for this version of Minecraft.");
                                        MuseLogger.logError("This problem is usually caught on client initialization and is fatal under normal circumstances, however somehow the client still considers RenderPlayerAPI to be loaded.");
                                        MuseLogger.logError(" -- WARNING -- Falling back to broken Vanilla model. Modular Powersuits armor model -WILL NOT- rotate. You have been warned. -- WARNING -- ");
                                        instance = new VanillaArmorModel();
                                }
                        } else {
                                instance = new VanillaArmorModel();
                        }
                }
                return instance;
        }
}
