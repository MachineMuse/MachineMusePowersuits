package net.machinemuse.powersuits.capabilities;

import net.machinemuse.numina.api.constants.NuminaNBTConstants;
import net.machinemuse.numina.api.module.IModuleManager;
import net.machinemuse.numina.utils.item.MuseItemUtils;
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
        super(moduleManagerIn.getOrSetModularPropertyInteger(container, NuminaNBTConstants.MAXIMUM_ENERGY));
        this.moduleManager = moduleManagerIn;
        this.container = container;
    }

    @Override
    public int receiveEnergy(int energyProvided, boolean simulate) {
        if (!canReceive())
            return 0;

//        // getting energy here with getEnergyStored() sets it for later use
//        this.energy = getEnergyStored();
//        this.capacity = getMaxEnergyStored();


//        int energyReceived = Math.min(getMaxEnergyStored() - getEnergyStored(), Math.min(this.maxReceive, energyProvided));
        int energyReceived = Math.min(this.capacity - this.energy, Math.min(this.maxReceive, energyProvided));

        if (!simulate) {
            this.energy += energyReceived;
            MuseItemUtils.setIntegerOrRemove(container, NuminaNBTConstants.CURRENT_ENERGY, Math.min(this.energy, getMaxEnergyStored()));
        }
        return energyReceived;
    }

    @Override
    public int extractEnergy(int energyRequested, boolean simulate) {
        if (!canExtract())
            return 0;

        // getting energy here with getEnergyStored() sets it for later use
//        int energyExtracted = Math.min(getEnergyStored(), Math.min(this.maxExtract, energyRequested));
        int energyExtracted = Math.min(energy, Math.min(this.maxExtract, energyRequested));

        if (!simulate) {
            energy -= energyExtracted;
            MuseItemUtils.setIntegerOrRemove(container, NuminaNBTConstants.CURRENT_ENERGY, Math.min(energy, getMaxEnergyStored()));
        }
        return energyExtracted;
    }

    @Override
    public int getEnergyStored() {
        this.energy = Math.min(this.capacity, (int) Math.round(MuseItemUtils.getIntOrZero(container, NuminaNBTConstants.CURRENT_ENERGY)));
        return  this.energy;
    }

    @Override
    public int getMaxEnergyStored() {
        this.capacity = this.maxExtract =
                this.maxReceive = moduleManager.getOrSetModularPropertyInteger(container, NuminaNBTConstants.MAXIMUM_ENERGY);
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