package net.machinemuse.numina.api.energy.adapater;

import mekanism.api.energy.IEnergizedItem;
import net.machinemuse.numina.api.energy.ElectricConversions;
import net.minecraft.item.ItemStack;

public class MekanismElectricAdapter extends ElectricAdapter {
    private final ItemStack itemStack;
    private final IEnergizedItem item;

    public MekanismElectricAdapter(final ItemStack itemStack) {
        this.itemStack = itemStack;
        this.item = (IEnergizedItem) itemStack.getItem();
    }

    @Override
    public int getCurrentMPSEnergy() {
        return this.item.canSend(this.itemStack) ? ElectricConversions.museEnergyFromMek((int) this.item.getEnergy(this.itemStack)) : 0;
    }

    @Override
    public int getMaxMPSEnergy() {
        return this.item.canSend(this.itemStack) ? ElectricConversions.museEnergyFromMek(this.item.getMaxEnergy(this.itemStack)) : 0;
    }

    @Override
    public int drainMPSEnergy(int requested) {
        if (!this.item.canSend(this.itemStack))
            return 0;
        double mekRequested = ElectricConversions.museEnergyToMek(requested);
        double available = this.item.canSend(this.itemStack) ? (this.item.getEnergy(this.itemStack)) : 0;

        if (available > mekRequested) {
            this.item.setEnergy(this.itemStack, available - mekRequested);
            return requested;
        } else {
            this.item.setEnergy(this.itemStack, 0);
            return ElectricConversions.museEnergyFromMek(available);
        }
    }

    @Override
    public int giveMPSEnergy(int provided) {
        if (!this.item.canReceive(this.itemStack))
            return 0;
        double mekProvided = ElectricConversions.museEnergyToMek(provided);
        double available = this.item.canSend(this.itemStack) ? (this.item.getEnergy(this.itemStack)) : 0;
        double max = this.item.getMaxEnergy(this.itemStack);

        if (available + mekProvided < max) {
            this.item.setEnergy(this.itemStack, available + mekProvided);
            return provided;
        } else {
            this.item.setEnergy(this.itemStack, max);
            return ElectricConversions.museEnergyFromMek(max - available);
        }
    }
}