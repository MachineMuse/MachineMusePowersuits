package net.machinemuse.numina.api.energy;

import net.machinemuse.numina.api.module.ModuleManager;
import net.machinemuse.numina.common.config.NuminaConfig;
import net.machinemuse.powersuits.common.ModCompatibility;
import net.machinemuse.utils.ElectricItemUtils;
import net.minecraft.item.ItemStack;

/**
 * Author: MachineMuse (Claire Semple)
 * Created: 4:51 AM, 4/28/13
 *
 * Ported to Java by lehjr on 11/4/16.
 */
public final class ElectricConversions {



    /* Thermal Expansion -------------------------------------------------------------------------- */
    public static int museEnergyToRF(final double museEnergy) {
        return (int)Math.ceil(museEnergy / ModCompatibility.getRFRatio());
    }

    public static double museEnergyFromRF(final int rf) {
        return rf * ModCompatibility.getRFRatio();
    }









    /** Industrialcraft 2 -------------------------------------------------------------------------- */
    public static final String IC2_TIER = "IC2 Tier";

    public static int getTier(final ItemStack stack) {
        return ElectricItemUtils.getTierForItem(stack);
    }

    public static double museEnergyToEU(final double museEnergy) {
        return museEnergy / NuminaConfig.getIC2Ratio();
    }

    public static int museEnergyFromEU(final double eu) {
        return (int) Math.round(eu * NuminaConfig.getIC2Ratio());
    }

    /** Mekanism ------------------------------------------------------------------------------------ */
    public static double museEnergyToMek(final double museEnergy) {
        return Math.ceil(museEnergy / NuminaConfig.getMekRatio());
    }

    public static int museEnergyFromMek(final double mj) {
        return (int) Math.round(mj * NuminaConfig.getMekRatio());
    }

    /** Applied Energistics 2 --------------------------------------------------------------------- */
    public static double museEnergyToAE(final double museEnergy) {
        return museEnergy / NuminaConfig.getAE2Ratio();
    }

    public static int museEnergyFromAE(final double ae) {
        return (int) Math.round(ae * NuminaConfig.getAE2Ratio());
    }
}