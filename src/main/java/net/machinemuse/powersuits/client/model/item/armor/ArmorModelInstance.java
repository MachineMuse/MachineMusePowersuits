package net.machinemuse.powersuits.client.model.item.armor;

import net.machinemuse.numina.basemod.MuseLogger;
import net.machinemuse.numina.misc.ModCompatibility;
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
                    MuseLogger.logger.info("Attempting to load SmartMoving armor model.");
                    instance = Class.forName("net.machinemuse.powersuits.client.model.item.armor.SMovingArmorModel").asSubclass(ModelBiped.class).newInstance();
                    MuseLogger.logger.info("SmartMoving armor model loaded successfully!");
                } catch (Exception e) {
                    MuseLogger.logger.info("Smart Moving armor model did not loadButton successfully. Either Smart Moving is not installed, or there was another problem.");
                    instance = new HighPolyArmor();
                }
            }
            else {
                instance = new HighPolyArmor();
            }
        }
        return instance;
    }
}