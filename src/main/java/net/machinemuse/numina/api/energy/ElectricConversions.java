package net.machinemuse.api.electricity;

import net.machinemuse.api.ModuleManager;
import net.machinemuse.powersuits.common.ModCompatibility;
import net.minecraft.item.ItemStack;

/**
 * Author: MachineMuse (Claire Semple)
 * Created: 4:51 AM, 4/28/13
 *
 * Ported to Java by lehjr on 11/4/16.
 */
public final class ElectricConversions {

    /* Industrialcraft 2 -------------------------------------------------------------------------- */
    public static final String IC2_TIER = "IC2 Tier";

    public static int getTier(final ItemStack stack) {
        return (int) ModuleManager.computeModularProperty(stack, IC2_TIER);
    }

    public static double museEnergyToEU(final double museEnergy) {
        return museEnergy / ModCompatibility.getIC2Ratio();
    }

    public static double museEnergyFromEU(final double eu) {
        return eu * ModCompatibility.getIC2Ratio();
    }


    /* Thermal Expansion -------------------------------------------------------------------------- */
    public static int museEnergyToRF(final double museEnergy) {
        return (int)Math.ceil(museEnergy / ModCompatibility.getRFRatio());
    }

    public static double museEnergyFromRF(final int rf) {
        return rf * ModCompatibility.getRFRatio();
    }

    /* Mekanism ------------------------------------------------------------------------------------ */
    public static double museEnergyToMek(final double museEnergy) { // no current conversion rate
        return Math.ceil(museEnergy / ModCompatibility.getMekRatio());
    }

    public static double museEnergyFromMek(final double mj) { // no current conversion rate
        return mj * ModCompatibility.getMekRatio();
    }

    /* Applied Energistics 2 ---------------------------------------------------------------------- */
    public static double museEnergyFromAE(final double ae) {
        return ae * ModCompatibility.getAE2Ratio();
    }

    public static double museEnergyToAE(final double museEnergy) {
        return museEnergy / ModCompatibility.getAE2Ratio();
    }
}