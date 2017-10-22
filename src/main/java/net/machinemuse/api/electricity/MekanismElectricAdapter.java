package net.machinemuse.api.electricity;

import mekanism.api.energy.IEnergizedItem;
import net.minecraft.item.ItemStack;

// TODO: add configuration for setting Mekanism to MPS energy ratio
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
        return this.item().canSend(this.stack()) ? (ElectricConversions.museEnergyFromMek((int) this.item().getEnergy(this.stack()))) : 0;
    }

    @Override
    public double getMaxMPSEnergy() {
        return ElectricConversions.museEnergyFromMek(this.item().getMaxEnergy(this.stack()));
    }

    @Override
    public double drainMPSEnergy(final double requested) {
        double mekRequested = ElectricConversions.museEnergyToMek(requested);
        double available = this.item().canSend(this.stack()) ? (this.item().getEnergy(this.stack())) : 0;

        if (available > mekRequested) {
            this.item().setEnergy(this.stack(), available - mekRequested);
            return requested;
        } else {
            this.item().setEnergy(this.stack(), 0);
            return ElectricConversions.museEnergyFromMek(available);
        }
    }

    @Override
    public double giveMPSEnergy(final double provided) {
        double mekProvided = ElectricConversions.museEnergyToMek(provided);
        double available = this.item().canSend(this.stack()) ? (this.item().getEnergy(this.stack())) : 0;
        double max = this.item().getMaxEnergy(this.stack());

        if (available + mekProvided < max) {
            this.item().setEnergy(this.stack(), available + mekProvided);
            return provided;
        } else {
            this.item().setEnergy(this.stack(), max);
            return ElectricConversions.museEnergyFromMek(max - available);
        }
    }
}