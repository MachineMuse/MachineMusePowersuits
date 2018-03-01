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
    public int getCurrentMPSEnergy() {
        return this.item().getEnergyStored(this.itemStack);
    }

    @Override
    public int getMaxMPSEnergy() {
        return this.item().getMaxEnergyStored(this.itemStack);
    }

    @Override
    public int drainMPSEnergy(int requested) {
        return this.item().extractEnergy(this.itemStack, requested, false);
    }

    @Override
    public int giveMPSEnergy(int provided) {
        return this.item().receiveEnergy(this.itemStack, provided, false);
    }
}