package net.machinemuse.powersuits.capabilities;

import net.machinemuse.numina.api.constants.NuminaNBTConstants;
import net.machinemuse.numina.api.module.IModuleManager;
import net.machinemuse.numina.utils.item.MuseItemUtils;
import net.machinemuse.powersuits.api.electricity.ElectricConversions;
import net.minecraft.item.ItemStack;
import net.minecraftforge.energy.EnergyStorage;

import javax.annotation.Nonnull;

/**
 * Note that this class does not update any NBT tag data itself, but rather is just part of a wrapper for
 * power storage devices in the item's inventory
 *
 */
public class ForgeEnergyItemContainerWrapper extends EnergyStorage {
    ItemStack container;
    IModuleManager moduleManager;

    /** TODO: need to set an NNBT tag for the max value instead of recalculating over and over.
     *
     * @param container
     * @param moduleManagerIn
     */
    public ForgeEnergyItemContainerWrapper(@Nonnull ItemStack container, IModuleManager moduleManagerIn) {
        // not working?!?!?!
        super(ElectricConversions.museEnergyToRF(moduleManagerIn.computeModularProperty(container, NuminaNBTConstants.MAXIMUM_ENERGY)));
        this.moduleManager = moduleManagerIn;
        this.container = container;
    }

    @Override
    public int receiveEnergy(int energyProvided, boolean simulate) {
        if (!canReceive())
            return 0;

        // getting energy here with getEnergyStored() sets it for later use
        int energyReceived = Math.min(getMaxEnergyStored() - getEnergyStored(), Math.min(this.maxReceive, energyProvided));
        if (!simulate) {
            energy += energyReceived;
            MuseItemUtils.setDoubleOrRemove(container, NuminaNBTConstants.CURRENT_ENERGY, Math.min(
                    ElectricConversions.museEnergyFromRF(energy), getMaxEnergyStored()));
        }
        return energyReceived;
    }

    @Override
    public int extractEnergy(int energyRequested, boolean simulate) {
        if (!canExtract())
            return 0;

        // getting energy here with getEnergyStored() sets it for later use
        int energyExtracted = Math.min(getEnergyStored(), Math.min(this.maxExtract, energyRequested));
        if (!simulate) {
            energy -= energyExtracted;
            MuseItemUtils.setDoubleOrRemove(container, NuminaNBTConstants.CURRENT_ENERGY, Math.min(
                    ElectricConversions.museEnergyFromRF(energy), getMaxEnergyStored()));
        }
        return energyExtracted;
    }

    @Override
    public int getEnergyStored() {
        this.energy = ElectricConversions.museEnergyToRF(MuseItemUtils.getDoubleOrZero(container, NuminaNBTConstants.CURRENT_ENERGY));
        return energy;
    }

    @Override
    public int getMaxEnergyStored() {
        this.capacity = this.maxExtract =
                this.maxReceive = ElectricConversions.museEnergyToRF(moduleManager.computeModularProperty(container, NuminaNBTConstants.MAXIMUM_ENERGY));
        return capacity;
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