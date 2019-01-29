package net.machinemuse.powersuits.client.model.item.armor;

import net.machinemuse.numina.common.ModCompatibility;
import net.machinemuse.numina.utils.MuseLogger;
import net.minecraft.client.model.ModelBiped;

/**
 * Author: MachineMuse (Claire Semple)
 * Created: 10:01 PM, 11/07/13
 */
public class ArmorModelInstance {
    private static ModelBiped instance = null;

    public static ModelBiped getInstance() {
        if (instance == null) {
            if (ModCompatibility.isRenderPlayerAPILoaded()) {
                try {
                    MuseLogger.logInfo("Attempting to loadButton SmartMoving armor model.");
                    instance = new SMovingArmorModel();
                    MuseLogger.logInfo("SmartMoving armor model loaded successfully!");
                } catch (Exception e) {
                    MuseLogger.logInfo("Smart Moving armor model did not loadButton successfully. Either Smart Moving is not installed, or there was another problem.");
                    instance = new HighPolyArmor();
                }
            } else {
                instance = new HighPolyArmor();
            }
        }
        return instance;
    }
}