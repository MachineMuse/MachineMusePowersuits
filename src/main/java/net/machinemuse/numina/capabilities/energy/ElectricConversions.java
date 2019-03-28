package net.machinemuse.numina.capabilities.energy;

import net.machinemuse.numina.config.NuminaConfig;
import net.minecraft.item.ItemStack;

/**
 * Author: MachineMuse (Claire Semple)
 * Created: 4:51 AM, 4/28/13
 * <p>
 * Ported to Java by lehjr on 11/4/16.
 */
public class ElectricConversions {
    /**
     * Industrialcraft 2 --------------------------------------------------------------------------
     */
    public static final String IC2_TIER = "IC2 Tier";
    static NuminaConfig config = NuminaConfig.INSTANCE;

    public static int getTier(final ItemStack stack) {
        return NuminaConfig.INSTANCE.getTierForItem(stack);
    }

    public static double museEnergyToEU(final double museEnergy) {
        return museEnergy / config.getIC2Ratio();
    }

    public static int museEnergyFromEU(final double eu) {
        return (int) Math.round(eu * config.getIC2Ratio());
    }

    /**
     * Mekanism ------------------------------------------------------------------------------------
     */
    public static double museEnergyToMek(final double museEnergy) { // no current conversion rate
        return Math.ceil(museEnergy / config.getMekRatio());
    }

    public static int museEnergyFromMek(final double mj) { // no current conversion rate
        return (int) Math.round(mj * config.getMekRatio());
    }

    /**
     * Applied Energistics 2 ----------------------------------------------------------------------
     */
    public static double museEnergyFromAE(final double ae) {
        return ae * config.getAE2Ratio();
    }

    public static double museEnergyToAE(final double museEnergy) {
        return museEnergy / config.getAE2Ratio();
    }
}