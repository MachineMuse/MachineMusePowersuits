package net.machinemuse.powersuits.client.render.item;

import net.machinemuse.numina.general.MuseLogger;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.nbt.NBTTagCompound;

/**
 * Author: MachineMuse (Claire Semple)
 * Created: 10:01 PM, 11/07/13
 */
public class ArmorModelInstance {
    private static ModelBiped instance = null;
    private static NBTTagCompound renderSpec;

    public static ModelBiped getInstance() {
        // TODO: Actual (skinned) vanilla model as low bandwitdth alternative
        if (instance == null) {
            try {
                MuseLogger.logInfo("Attempting to load Smart Moving armor model.");
                instance = SMovingArmorModel.getInstance();
                MuseLogger.logInfo("Smart Moving armor model loaded successfully!");
            } catch (Throwable e) {
                MuseLogger.logInfo("Smart Moving armor model did not load successfully. Either Smart Moving is not installed, or there was another problem.");
                instance = VanillaArmorModel.getInstance();
            }
        }
        return instance;
    }
}