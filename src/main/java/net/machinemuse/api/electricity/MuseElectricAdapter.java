package net.machinemuse.api.electricity;

import net.minecraft.item.ItemStack;

/**
 * Ported to Java by lehjr on 11/4/16.
 */
public class MuseElectricAdapter extends ElectricAdapter {
    private ItemStack stack;
    private IMuseElectricItem item;

    public MuseElectricAdapter(ItemStack stack) {
        this.stack = stack;
        this.item = (IMuseElectricItem)stack.getItem();
    }

    public ItemStack stack() {
        return this.stack;
    }

    public IMuseElectricItem item() {
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
