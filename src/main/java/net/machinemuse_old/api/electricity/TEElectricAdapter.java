package net.machinemuse_old.api.electricity;

import cofh.api.energy.IEnergyContainerItem;
import net.minecraft.item.ItemStack;

/**
 * Ported to Java by lehjr on 11/4/16.
 */
public class TEElectricAdapter extends ElectricAdapter {
    private final ItemStack stack;
    private final IEnergyContainerItem item;

    public TEElectricAdapter(final ItemStack stack) {
        this.stack = stack;
        this.item = (IEnergyContainerItem)stack.getItem();
    }

    public ItemStack stack() {
        return this.stack;
    }

    public IEnergyContainerItem item() {
        return this.item;
    }

    @Override
    public double getCurrentMPSEnergy() {
        return ElectricConversions.museEnergyFromRF(this.item().getEnergyStored(this.stack()));
    }

    @Override
    public double getMaxMPSEnergy() {
        return ElectricConversions.museEnergyFromRF(this.item().getMaxEnergyStored(this.stack()));
    }

    @Override
    public double drainMPSEnergy(final double requested) {
        return ElectricConversions.museEnergyFromRF(this.item().extractEnergy(this.stack(), ElectricConversions.museEnergyToRF(requested), false));
    }

    @Override
    public double giveMPSEnergy(final double provided) {
        return ElectricConversions.museEnergyFromRF(this.item().receiveEnergy(this.stack(), ElectricConversions.museEnergyToRF(provided), false));
    }
}