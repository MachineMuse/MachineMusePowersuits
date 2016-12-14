package net.machinemuse.api.electricity;

import ic2.api.item.ElectricItem;
import ic2.api.item.IElectricItem;
import net.minecraft.item.ItemStack;

/**
 * Ported to Java by lehjr on 11/4/16.
 */
public class IC2ElectricAdapter extends ElectricAdapter {
    ItemStack stack;
    IElectricItem item;

    public IC2ElectricAdapter(ItemStack stack) {
        this.stack = stack;
        this.item = (IElectricItem)stack.getItem();
    }

    @Override
    public double getCurrentEnergy() {
        return ElectricConversions.museEnergyFromEU(ElectricItem.manager.getCharge(stack));
    }

    @Override
    public double getMaxEnergy() {
        return ElectricConversions.museEnergyFromEU(item.getMaxCharge(stack));
    }

    @Override
    public double drainEnergy(double requested) {
        return ElectricConversions.museEnergyFromEU(ElectricItem.manager.discharge(stack, ElectricConversions.museEnergyToEU(requested), getTier(), true, false, false));
    }

    @Override
    public double giveEnergy(double provided) {
        return ElectricConversions.museEnergyFromEU(ElectricItem.manager.charge(stack, ElectricConversions.museEnergyToEU(provided), getTier(), true, false));
    }

    public int getTier() {
        return item.getTier(stack);
    }
}