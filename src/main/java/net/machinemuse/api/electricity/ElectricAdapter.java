package net.machinemuse.api.electricity;

import cofh.redstoneflux.api.IEnergyContainerItem;
import ic2.api.item.IElectricItem;
import net.machinemuse.powersuits.common.ModCompatibility;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;

/**
 * Ported to Java by lehjr on 11/4/16.
 */
public abstract class ElectricAdapter {
    public static ElectricAdapter wrap(@Nonnull ItemStack stack) {
        if (stack.isEmpty())
            return null;
        Item i = stack.getItem();
        if (i instanceof IMuseElectricItem) {
            return new MuseElectricAdapter(stack);
        } else if (ModCompatibility.isRFAPILoaded() && i instanceof IEnergyContainerItem) {
            return new TEElectricAdapter(stack);
        } else if (ModCompatibility.isIndustrialCraftLoaded() && i instanceof IElectricItem) {
            return new IC2ElectricAdapter(stack);
        } else {
            return null;
        }
    }

    public abstract double getCurrentMPSEnergy();

    public abstract double getMaxMPSEnergy();

    public abstract double drainMPSEnergy(double requested);

    public abstract double giveMPSEnergy(double provided);
}