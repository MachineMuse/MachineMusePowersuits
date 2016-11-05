package net.machinemuse.api.electricity;

import net.minecraft.item.ItemStack;

/**
 * Ported to Java by lehjr on 11/4/16.
 */
public class MuseElectricAdapter extends ElectricAdapter {
    private final ItemStack stack;
    private final MuseElectricItem item;

    public MuseElectricAdapter(final ItemStack stack) {
        this.stack = stack;
        this.item = (MuseElectricItem)stack.getItem();
    }

    public ItemStack stack() {
        return this.stack;
    }

    public MuseElectricItem item() {
        return this.item;
    }

    @Override
    public double getCurrentEnergy() {
        return this.item().getCurrentEnergy(this.stack());
    }

    @Override
    public double getMaxEnergy() {
        return this.item().getMaxEnergy(this.stack());
    }

    @Override
    public double drainEnergy(final double requested) {
        return this.item().drainEnergyFrom(this.stack(), requested);
    }

    @Override
    public double giveEnergy(final double provided) {
        return this.item().giveEnergyTo(this.stack(), provided);
    }
}
