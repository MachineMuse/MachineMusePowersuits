package net.machinemuse.powersuits.client.render.item;

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
                instance = new SMovingArmorModel();
            } catch (Throwable e) {
                instance = new VanillaArmorModel();
            }
        }
        return instance;
    }
}
