package net.machinemuse.api.electricity;

import appeng.api.config.AccessRestriction;
import appeng.api.implementations.items.IAEItemPowerStorage;
import net.minecraft.item.ItemStack;

/**
 * Ported to Java by lehjr on 11/4/16.
 */
public class AE2ElectricAdapter extends ElectricAdapter {
    ItemStack stack;
    IAEItemPowerStorage item;

    public AE2ElectricAdapter(ItemStack stack) {
        this.stack = stack;
        item = (IAEItemPowerStorage) stack.getItem();
    }

    @Override
    public double getCurrentEnergy() {
        return ElectricConversions.museEnergyFromAE(item.getAECurrentPower(stack));
    }

    @Override
    public double getMaxEnergy() {
        return ElectricConversions.museEnergyFromAE(item.getAEMaxPower(stack));
    }

    @Override
    public double drainEnergy(double requested) {
        return ElectricConversions.museEnergyFromAE(item.extractAEPower(stack, ElectricConversions.museEnergyToAE(requested)));
    }

    @Override
    public double giveEnergy(double provided) {
        return ElectricConversions.museEnergyFromAE(item.injectAEPower(stack, ElectricConversions.museEnergyToAE(provided)));
    }

    public AccessRestriction getPowerFlow(ItemStack stack) {
        return  AccessRestriction.READ_WRITE;
    }
}
