package net.machinemuse.api.electricity;

import cofh.api.energy.IEnergyContainerItem;
import mekanism.api.energy.IEnergizedItem;
import net.minecraft.item.ItemStack;


public class MekanismElectricAdapter extends ElectricAdapter {
    private final ItemStack stack;
    private final IEnergyContainerItem item;

    public MekanismElectricAdapter(final ItemStack stack) {
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
    public double getCurrentEnergy() {
    	IEnergizedItem item = (IEnergizedItem)this.stack.getItem();
    	return item.canSend(this.stack) ? ElectricConversions.museEnergyFromRF(this.item().getEnergyStored(this.stack())) : 0;
    }

    @Override
    public double getMaxEnergy() {
        return ElectricConversions.museEnergyFromRF(this.item().getMaxEnergyStored(this.stack()));
    }

    @Override
    public double drainEnergy(final double requested) {
        return ElectricConversions.museEnergyFromRF(this.item().extractEnergy(this.stack(), ElectricConversions.museEnergyToRF(requested), false));
    }

    @Override
    public double giveEnergy(final double provided) {
        return ElectricConversions.museEnergyFromRF(this.item().receiveEnergy(this.stack(), ElectricConversions.museEnergyToRF(provided), false));
    }
}
