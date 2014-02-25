package net.machinemuse.powersuits.client.render.item;

import net.machinemuse.numina.general.MuseLogger;

/**
 * Author: MachineMuse (Claire Semple)
 * Created: 10:01 PM, 11/07/13
 */
public class ArmorModelInstance {
    private static ArmorModel instance = null;

    public static ArmorModel getInstance() {
        if (instance == null) {
//            instance = new VanillaArmorModel();
            try {
                MuseLogger.logInfo("Attempting to load Smart Moving armor model.");
                instance = new SMovingArmorModel();
                MuseLogger.logInfo("Smart Moving armor model loaded successfully!");
            } catch (Throwable e) {
                MuseLogger.logInfo("Smart Moving armor model did not load successfully. Either Smart Moving is not installed, or there was another problem.");
                instance = new VanillaArmorModel();
            }
        }
        return instance;
    }
}
