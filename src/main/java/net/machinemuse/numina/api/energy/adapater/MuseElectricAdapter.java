package net.machinemuse.numina.api.energy.adapater;

import net.machinemuse.numina.api.energy.IMuseElectricItem;
import net.minecraft.item.ItemStack;

/**
 * Ported to Java by lehjr on 11/4/16.
 */
public class MuseElectricAdapter extends ElectricAdapter {
    private ItemStack itemStack;
    private IMuseElectricItem item;

    public MuseElectricAdapter(ItemStack itemStack) {
        this.itemStack = itemStack;
        this.item = (IMuseElectricItem) itemStack.getItem();
    }

    @Override
    public int getCurrentMPSEnergy() {
        return this.item.getCurrentMPSEnergy(this.itemStack);
    }

    @Override
    public int getMaxMPSEnergy() {
        return this.item.getMaxMPSEnergy(this.itemStack);
    }

    @Override
    public int drainMPSEnergy(int requested) {
        return this.item.drainMPSEnergyFrom(this.itemStack, requested);
    }

    @Override
    public int giveMPSEnergy(int provided) {
        return this.item.giveMPSEnergyTo(this.itemStack, provided);
    }
}