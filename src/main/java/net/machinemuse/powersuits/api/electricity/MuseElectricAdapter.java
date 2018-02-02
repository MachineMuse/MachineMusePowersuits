package net.machinemuse.powersuits.api.electricity;

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
    public double getCurrentMPSEnergy() {
        return this.item().getCurrentMPSEnergy(this.stack());
    }

    @Override
    public double getMaxMPSEnergy() {
        return this.item().getMaxMPSEnergy(this.stack());
    }

    @Override
    public double drainMPSEnergy(final double requested) {
        return this.item().drainMPSEnergyFrom(this.stack(), requested);
    }

    @Override
    public double giveMPSEnergy(final double provided) {
        return this.item().giveMPSEnergyTo(this.stack(), provided);
    }
}
