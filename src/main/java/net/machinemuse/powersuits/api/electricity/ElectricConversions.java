package net.machinemuse.powersuits.api.electricity;

import net.machinemuse.powersuits.api.module.ModuleManager;
import net.machinemuse.powersuits.common.ModCompatibility;
import net.machinemuse.powersuits.common.config.MPSConfig;
import net.minecraft.item.ItemStack;

/**
 * Author: MachineMuse (Claire Semple)
 * Created: 4:51 AM, 4/28/13
 *
 * Ported to Java by lehjr on 11/4/16.
 */
public final class ElectricConversions {
    static MPSConfig config = MPSConfig.INSTANCE;

    /* Industrialcraft 2 -------------------------------------------------------------------------- */
    public static final String IC2_TIER = "IC2 Tier";

    public static int getTier(final ItemStack stack) {
        return (int) ModuleManager.INSTANCE.computeModularProperty(stack, IC2_TIER);
    }

    public static double museEnergyToEU(final double museEnergy) {
        return museEnergy / config.getIC2Ratio();
    }

    public static double museEnergyFromEU(final double eu) {
        return eu * config.getIC2Ratio();
    }


    /* Thermal Expansion -------------------------------------------------------------------------- */
    public static int museEnergyToRF(final double museEnergy) {
        return (int)Math.ceil(museEnergy / config.getRFRatio());
    }

    public static double museEnergyFromRF(final int rf) {
        return rf * config.getRFRatio();
    }

    /* Mekanism ------------------------------------------------------------------------------------ */
    public static double museEnergyToMek(final double museEnergy) { // no current conversion rate
        return Math.ceil(museEnergy / config.getMekRatio());
    }

    public static double museEnergyFromMek(final double mj) { // no current conversion rate
        return mj * config.getMekRatio();
    }

    /* Applied Energistics 2 ---------------------------------------------------------------------- */
    public static double museEnergyFromAE(final double ae) {
        return ae * config.getAE2Ratio();
    }

    public static double museEnergyToAE(final double museEnergy) {
        return museEnergy / config.getAE2Ratio();
    }
}