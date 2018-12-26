package net.machinemuse.numina.capabilities.energy.adapter;

import cofh.redstoneflux.api.IEnergyContainerItem;
import net.minecraft.item.ItemStack;

/**
 * Ported to Java by lehjr on 11/4/16.
 */
public class TEElectricAdapter extends ElectricAdapter {
    private final ItemStack stack;
    private final IEnergyContainerItem item;

    public TEElectricAdapter(final ItemStack stack) {
        this.stack = stack;
        this.item = (IEnergyContainerItem) stack.getItem();
    }

    public ItemStack stack() {
        return this.stack;
    }

    public IEnergyContainerItem item() {
        return this.item;
    }

    @Override
    public int getEnergyStored() {
        return this.item().getEnergyStored(this.stack);
    }

    @Override
    public int getMaxEnergyStored() {
        return this.item().getMaxEnergyStored(this.stack);
    }

    @Override
    public int extractEnergy(int requested, boolean simulate) {
        return this.item().extractEnergy(this.stack, requested, simulate);
    }

    @Override
    public int receiveEnergy(int provided, boolean simulate) {
        return this.item().receiveEnergy(this.stack, provided, simulate);
    }
}