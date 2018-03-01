package net.machinemuse.numina.api.energy.adapater;

import net.minecraft.item.ItemStack;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

public class ForgeEnergyAdapter extends ElectricAdapter {
    private final ItemStack itemStack;
    private final IEnergyStorage energyStorage;

    public ForgeEnergyAdapter(ItemStack itemStack) {
        this.itemStack = itemStack;
        energyStorage = itemStack.getCapability(CapabilityEnergy.ENERGY, null);
    }

    @Override
    public int getCurrentMPSEnergy() {
        return energyStorage != null ? energyStorage.getEnergyStored() : 0;
    }

    @Override
    public int getMaxMPSEnergy() {
        return energyStorage != null ? energyStorage.getMaxEnergyStored() : 0;
    }

    @Override
    public int drainMPSEnergy(int requested) {
        return energyStorage != null ? energyStorage.extractEnergy(requested, false) : 0;
    }

    @Override
    public int giveMPSEnergy(int provided) {
        return energyStorage != null ? energyStorage.receiveEnergy(provided, false) : 0;
    }
}