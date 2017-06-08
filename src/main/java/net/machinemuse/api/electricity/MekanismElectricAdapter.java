package net.machinemuse.api.electricity;

import mekanism.api.energy.IEnergizedItem;
import net.minecraft.item.ItemStack;


public class MekanismElectricAdapter extends ElectricAdapter {
    private final ItemStack stack;
    private final IEnergizedItem item;

    public MekanismElectricAdapter(final ItemStack stack) {
        this.stack = stack;
        this.item = (IEnergizedItem)stack.getItem();
    }

    public ItemStack stack() {
        return this.stack;
    }

    public IEnergizedItem item() {
        return this.item;
    }

    @Override
    public double getCurrentMPSEnergy() {
        return this.item().canSend(this.stack()) ? (this.item().getEnergy(this.stack())) : 0;
    }

    @Override
    public double getMaxMPSEnergy() {
        return this.item().getMaxEnergy(this.stack());
    }

    @Override
    public double drainMPSEnergy(final double requested) {
        double available = this.item().canSend(this.stack()) ? (this.item().getEnergy(this.stack())) : 0;
        if (available > requested) {
            this.item().setEnergy(this.stack(), available - requested);
            return requested;
        } else {
            this.item().setEnergy(this.stack(), 0);
            return available;
        }
    }

    @Override
    public double giveMPSEnergy(final double provided) {
        double available = this.item().canSend(this.stack()) ? (this.item().getEnergy(this.stack())) : 0;
        double max = this.item().getMaxEnergy(this.stack());

        if (available + provided < max) {
            this.item().setEnergy(this.stack(), available + provided);
            return provided;
        } else {
            this.item().setEnergy(this.stack(), max);
            return max - available;
        }
    }
}