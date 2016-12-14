package net.machinemuse.api.electricity;

import cofh.api.energy.IEnergyContainerItem;
import net.minecraft.item.ItemStack;

/**
 * Ported to Java by lehjr on 11/4/16.
 */
public class TEElectricAdapter extends ElectricAdapter {
    ItemStack stack;
    IEnergyContainerItem item;

    public TEElectricAdapter(ItemStack stack) {
        this.stack = stack;
        this.item = (IEnergyContainerItem)stack.getItem();
    }

    @Override
    public double getCurrentEnergy() {
        return ElectricConversions.museEnergyFromRF(item.getEnergyStored(stack));
    }

    @Override
    public double getMaxEnergy() {
        return ElectricConversions.museEnergyFromRF(item.getMaxEnergyStored(stack));
    }

    @Override
    public double drainEnergy(double requested) {
        return ElectricConversions.museEnergyFromRF(item.extractEnergy(stack, ElectricConversions.museEnergyToRF(requested), false));
    }

    @Override
    public double giveEnergy(double provided) {
        return ElectricConversions.museEnergyFromRF(item.receiveEnergy(stack, ElectricConversions.museEnergyToRF(provided), false));
    }
}
