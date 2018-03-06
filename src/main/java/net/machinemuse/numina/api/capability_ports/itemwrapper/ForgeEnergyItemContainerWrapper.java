package net.machinemuse.numina.api.capability_ports.itemwrapper;

import net.machinemuse.numina.api.capability_ports.inventory.IModularItemCapability;
import net.machinemuse.numina.api.energy.adapater.ElectricAdapter;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.energy.EnergyStorage;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nonnull;

/**
 * Note that this class does not update any NBT tag data itself, but rather is just part of a wrapper for
 * power storage devices in the item's inventory
 *
 */
public class ForgeEnergyItemContainerWrapper extends EnergyStorage {
    IItemHandler itemHandler;
    ItemStack container;

    public ForgeEnergyItemContainerWrapper(@Nonnull ItemStack container, IItemHandler itemHandler) {
        super(0);
        this.container = container;
        this.itemHandler = itemHandler;
    }

    @Override
    public int receiveEnergy(int energyProvided, boolean simulate) {
        if (itemHandler != null && itemHandler instanceof IModularItemCapability) {
            int energyLeft = energyProvided;
            for (ItemStack module : ((IModularItemCapability) itemHandler).getInstallledModules()) {
                if (energyLeft > 0) {
                    ElectricAdapter adapter = ElectricAdapter.wrap(module);
                    energyLeft = adapter != null ? energyLeft - adapter.receiveEnergy(energyLeft, simulate) : energyLeft;
                } else
                    break;
            }
            return energyProvided - energyLeft;
        }
        return 0;
    }

    @Override
    public int extractEnergy(int energyRequested, boolean simulate) {
        if (itemHandler != null && itemHandler instanceof IModularItemCapability) {
            int energyLeft = energyRequested;
            for (ItemStack module : ((IModularItemCapability) itemHandler).getInstallledModules()) {
                if (energyLeft > 0) {
                    ElectricAdapter adapter = ElectricAdapter.wrap(module);
                    energyLeft = adapter != null ? energyLeft - adapter.extractEnergy(energyLeft, simulate) : energyLeft;
                } else
                    break;
            }
            return energyRequested - energyLeft;
        }
        return 0;
    }

    @Override
    public int getEnergyStored() {
        int energyStored = 0;

        if (itemHandler != null && itemHandler instanceof IModularItemCapability) {
            for (ItemStack module : ((IModularItemCapability) itemHandler).getInstallledModules()) {
                    ElectricAdapter adapter = ElectricAdapter.wrap(module);
                    energyStored += adapter != null ? adapter.getEnergyStored() : 0;
            }
        }
        return energyStored;
    }

    @Override
    public int getMaxEnergyStored() {
        int maxEnergyStored = 0;

        if (itemHandler != null && itemHandler instanceof IModularItemCapability) {
            for (ItemStack module : ((IModularItemCapability) itemHandler).getInstallledModules()) {
                ElectricAdapter adapter = ElectricAdapter.wrap(module);
                maxEnergyStored += adapter != null ? adapter.getMaxEnergyStored() : 0;
            }
        }
        return maxEnergyStored;
    }

    @Override
    public boolean canExtract() {
        return getEnergyStored() > 0;
    }

    @Override
    public boolean canReceive() {
        return getMaxEnergyStored() > getEnergyStored();
    }
}