package net.machinemuse.powersuits.api.electricity.adapter;

import net.machinemuse.powersuits.api.electricity.ElectricConversions;
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

    //    @Override
//    public int getEnergyStored() {
//        return energyStorage != null ? energyStorage.getEnergyStored() : 0;
//    }

    @Override
    public double getCurrentMPSEnergy() {
        return energyStorage != null ? ElectricConversions.museEnergyFromRF(energyStorage.getEnergyStored()) : 0;
    }

    //    @Override
//    public int getMaxEnergyStored() {
//        return energyStorage != null ? energyStorage.getMaxEnergyStored() : 0;
//    }

    @Override
    public double getMaxMPSEnergy() {
        return energyStorage != null ? ElectricConversions.museEnergyFromRF(energyStorage.getMaxEnergyStored()) : 0;
    }

    //    @Override
//    public int extractEnergy(int requested, boolean simulate) {
//        if(requested == 0)
//            return 0;
//        return energyStorage != null ? energyStorage.extractEnergy(requested, simulate) : 0;
//    }

    @Override
    public double drainMPSEnergy(double requested) {
        if(requested == 0)
            return 0;
        return energyStorage != null ? energyStorage.extractEnergy(ElectricConversions.museEnergyToRF(requested), false) : 0;
    }

    //    @Override
//    public int receiveEnergy(int provided, boolean simulate) {
//        return energyStorage != null ? energyStorage.receiveEnergy(provided, simulate) : 0;
//    }

    @Override
    public double giveMPSEnergy(double provided) {
        return energyStorage != null ? energyStorage.receiveEnergy(ElectricConversions.museEnergyToRF(provided), false) : 0;
    }
}