package net.machinemuse.api.electricity;

import ic2.api.item.ElectricItem;
import ic2.api.item.IElectricItem;
import net.minecraft.item.ItemStack;

/**
 * Ported to Java by lehjr on 11/4/16.
 */
public class IC2ElectricAdapter extends ElectricAdapter {
    private final ItemStack stack;
    private final IElectricItem item;

    public IC2ElectricAdapter(final ItemStack stack) {
        this.stack = stack;
        this.item = (IElectricItem)stack.getItem();
    }

    public ItemStack stack() {
        return this.stack;
    }

    public IElectricItem item() {
        return this.item;
    }

    @Override
    public double getCurrentEnergy() {
        return ElectricConversions.museEnergyFromEU(ElectricItem.manager.getCharge(this.stack()));
    }

    @Override
    public double getMaxEnergy() {
        return ElectricConversions.museEnergyFromEU(this.item().getMaxCharge(this.stack()));
    }

    @Override
    public double drainEnergy(final double requested) {
        return ElectricConversions.museEnergyFromEU(ElectricItem.manager.discharge(this.stack(), ElectricConversions.museEnergyToEU(requested), this.getTier(), true, false, false));
    }

    @Override
    public double giveEnergy(final double provided) {
        return ElectricConversions.museEnergyFromEU(ElectricItem.manager.charge(this.stack(), ElectricConversions.museEnergyToEU(provided), this.getTier(), true, false));
    }

    public int getTier() {
        return this.item().getTier(this.stack());
    }
}
