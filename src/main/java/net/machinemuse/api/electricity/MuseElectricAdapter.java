package net.machinemuse.api.electricity;

import net.minecraft.item.ItemStack;

/**
 * Ported to Java by lehjr on 11/4/16.
 */
public class MuseElectricAdapter extends ElectricAdapter {
    ItemStack stack;
    IMuseElectricItem item;

    public MuseElectricAdapter(ItemStack stack) {
        this.stack = stack;
        this.item = (IMuseElectricItem)stack.getItem();
    }

    @Override
    public double getCurrentEnergy() {
        return item.getCurrentEnergy(stack);
    }

    @Override
    public double getMaxEnergy() {
        return item.getMaxEnergy(stack);
    }

    @Override
    public double drainEnergy(double requested) {
        return item.drainEnergyFrom(stack, requested);
    }

    @Override
    public double giveEnergy(double provided) {
        return item.giveEnergyTo(stack, provided);
    }
}