package net.machinemuse.numina.api.energy.adapater;

import cofh.redstoneflux.api.IEnergyContainerItem;
import net.minecraft.item.ItemStack;

/**
 * Ported to Java by lehjr on 11/4/16.
 */
public class TEElectricAdapter extends ElectricAdapter {
    private final ItemStack itemStack;
    private final IEnergyContainerItem item;

    public TEElectricAdapter(final ItemStack stack) {
        this.itemStack = stack;
        this.item = (IEnergyContainerItem)stack.getItem();
    }

    public ItemStack itemStack() {
        return this.itemStack;
    }

    public IEnergyContainerItem item() {
        return this.item;
    }

    @Override
    public int getEnergyStored() {
        return this.item().getEnergyStored(this.itemStack);
    }

    @Override
    public int getMaxEnergyStored() {
        return this.item().getMaxEnergyStored(this.itemStack);
    }

    @Override
    public int extractEnergy(int requested, boolean simulate) {
        return this.item().extractEnergy(this.itemStack, requested, simulate);
    }

    @Override
    public int receiveEnergy(int provided, boolean simulate) {
        return this.item().receiveEnergy(this.itemStack, provided, simulate);
    }
}