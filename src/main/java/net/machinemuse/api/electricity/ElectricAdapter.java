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
    public static ElectricAdapter wrap(ItemStack stack) {
        if (stack == null)
            return null;
        Item i = stack.getItem();
        if (i instanceof IMuseElectricItem) {
            return new MuseElectricAdapter(stack);
        } else if (ModCompatibility.isRFAPILoaded() && i instanceof IEnergyContainerItem) {
            return new TEElectricAdapter(stack);
        } else if (ModCompatibility.isIndustrialCraftLoaded() && i instanceof IElectricItem) {
            return new IC2ElectricAdapter(stack);
        } else if (ModCompatibility.isAppengLoaded() && i instanceof IAEItemPowerStorage) {
            return new AE2ElectricAdapter(stack);
        } else {
            return null;
        }
    }

    public abstract double getCurrentEnergy();

    public abstract double getMaxEnergy();

    public abstract double drainEnergy(double requested);

    public abstract double giveEnergy(double provided);
}