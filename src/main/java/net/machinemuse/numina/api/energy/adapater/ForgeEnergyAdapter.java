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
    public int getEnergyStored() {
        return energyStorage != null ? energyStorage.getEnergyStored() : 0;
    }

    @Override
    public int getMaxEnergyStored() {
        return energyStorage != null ? energyStorage.getMaxEnergyStored() : 0;
    }

    @Override
    public int extractEnergy(int requested, boolean simulate) {
        if(requested == 0)
            return 0;

        System.out.println("can drain: " + energyStorage.canExtract());

        int energyExtracted = energyStorage != null ? energyStorage.extractEnergy(requested, simulate) : 0;
        if (energyStorage == null)
            System.out.println("Energy storage is null!!!!");

        System.out.println("energy requested " + requested);
        System.out.println("simulate: " + simulate);

        System.out.println("energy extracted: " + energyExtracted);
        return energyExtracted;



//        return energyStorage != null ? energyStorage.extractEnergy(requested, simulate) : 0;
    }

    @Override
    public int receiveEnergy(int provided, boolean simulate) {
        int recievedEnergy= energyStorage != null ? energyStorage.receiveEnergy(provided, simulate) : 0;

        if (energyStorage == null)
            System.out.println("Energy storage is null!!!!");

        System.out.println("energy provided " + provided);
        System.out.println("simulate: " + simulate);


        System.out.println("recieved Energy: " + recievedEnergy);
        return recievedEnergy;

//        return energyStorage != null ? energyStorage.receiveEnergy(provided, simulate) : 0;
    }
}