package net.machinemuse.numina.api.capability_ports.itemwrapper;

import net.machinemuse.numina.api.energy.adapater.ElectricAdapter;
import net.minecraft.item.ItemStack;
import net.minecraftforge.energy.EnergyStorage;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nonnull;

public class ForgeEnergyItemContainerWrapper extends EnergyStorage implements IEnergyStorage {
    IItemHandler itemHandler;
    ItemStack container;

    public ForgeEnergyItemContainerWrapper(@Nonnull ItemStack container, IItemHandler itemHandler) {
        super(0);
        this.container = container;
        this.itemHandler = itemHandler;
    }

    @Override
    public int receiveEnergy(int maxReceive, boolean simulate) {
        if (itemHandler != null) {
            ItemStack container2 = itemHandler.getStackInSlot(0);
            ElectricAdapter adapter = ElectricAdapter.wrap(container2);
            return adapter != null ? adapter.receiveEnergy(maxReceive, simulate) : 0;
        }
        return 0;
    }

    @Override
    public int extractEnergy(int maxExtract, boolean simulate) {
        System.out.println("extracting energy: " + maxExtract);


        if (itemHandler != null) {

            System.out.println("extracting energy: " + maxExtract);


            ItemStack container2 = itemHandler.getStackInSlot(0);
            ElectricAdapter adapter = ElectricAdapter.wrap(container2);

            int drained = adapter != null ? adapter.extractEnergy(maxReceive, simulate) : 0;

            System.out.println("drained: " + drained);
            return drained;


//            return adapter != null ? adapter.extractEnergy(maxReceive, simulate) : 0;
        }
        return 0;
    }

    @Override
    public int getEnergyStored() {
        if (itemHandler != null) {
            ItemStack container2 = itemHandler.getStackInSlot(0);
            ElectricAdapter adapter = ElectricAdapter.wrap(container2);
            return adapter != null ? adapter.getEnergyStored() : 0;
        }
        return 0;
    }

    @Override
    public int getMaxEnergyStored() {
        if (itemHandler != null) {
            ItemStack container2 = itemHandler.getStackInSlot(0);
            ElectricAdapter adapter = ElectricAdapter.wrap(container2);
            return adapter != null ? adapter.getMaxEnergyStored() : 0;
        }
        return 0;
    }

    @Override
    public boolean canExtract() {
        if (itemHandler != null) {
            ItemStack container2 = itemHandler.getStackInSlot(0);
            ElectricAdapter adapter = ElectricAdapter.wrap(container2);
            return adapter != null ? adapter.getEnergyStored() > 0 : false;
        }
        return false;
    }

    @Override
    public boolean canReceive() {
        if (itemHandler != null) {
            ItemStack container2 = itemHandler.getStackInSlot(0);
            ElectricAdapter adapter = ElectricAdapter.wrap(container2);
            return adapter != null ? (adapter.getMaxEnergyStored() - adapter.getEnergyStored()) > 0 : false;
        }
        return false;
    }
}