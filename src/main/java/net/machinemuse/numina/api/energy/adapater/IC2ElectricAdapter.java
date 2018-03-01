package net.machinemuse.numina.api.energy.adapater;

import ic2.api.item.ElectricItem;
import ic2.api.item.IElectricItem;
import net.machinemuse.numina.api.energy.ElectricConversions;
import net.minecraft.item.ItemStack;

/**
 * Ported to Java by lehjr on 11/4/16.
 */
public class IC2ElectricAdapter extends ElectricAdapter {
    private final ItemStack itemStack;
    private final IElectricItem item;

    public IC2ElectricAdapter(final ItemStack itemStack) {
        this.itemStack = itemStack;
        this.item = (IElectricItem) this.itemStack.getItem();
    }

    public int getTier() {
        return this.item.getTier(this.itemStack);
    }

    @Override
    public int getCurrentMPSEnergy() {
        return ElectricConversions.museEnergyFromEU(ElectricItem.manager.getCharge(this.itemStack));
    }

    @Override
    public int getMaxMPSEnergy() {
        return ElectricConversions.museEnergyFromEU(this.item.getMaxCharge(this.itemStack));
    }

    @Override
    public int drainMPSEnergy(int requested) {
        return ElectricConversions.museEnergyFromEU(ElectricItem.manager.discharge(this.itemStack, ElectricConversions.museEnergyToEU(requested), this.getTier(), true, false, false));
    }

    @Override
    public int giveMPSEnergy(int provided) {
        return ElectricConversions.museEnergyFromEU(ElectricItem.manager.charge(this.itemStack, ElectricConversions.museEnergyToEU(provided), this.getTier(), true, false));
    }
}