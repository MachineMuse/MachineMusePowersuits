package net.machinemuse.api.electricity;

import appeng.api.implementations.items.IAEItemPowerStorage;
import cofh.api.energy.IEnergyContainerItem;
import ic2.api.item.IElectricItem;
import net.machinemuse.powersuits.common.ModCompatibility;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

/**
 * Ported to Java by lehjr on 11/4/16.
 */
public abstract class ElectricAdapter {
    public static ElectricAdapter wrap(final ItemStack stack) {
        if (stack == null) {
            return null;
        }
        final Item i = stack.getItem();
        return (i instanceof MuseElectricItem) ? new MuseElectricAdapter(stack) : ((ModCompatibility.isRFAPILoaded() && i instanceof IEnergyContainerItem) ? new TEElectricAdapter(stack) : ((ModCompatibility.isIndustrialCraftLoaded() && i instanceof IElectricItem) ? new IC2ElectricAdapter(stack) : ((ModCompatibility.isAppengLoaded() && i instanceof IAEItemPowerStorage) ? new AE2ElectricAdapter(stack) : null)));
    }

    public abstract double getCurrentEnergy();

    public abstract double getMaxEnergy();

    public abstract double drainEnergy(double requested);

    public abstract double giveEnergy(double provided);
}