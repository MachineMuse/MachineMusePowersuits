package net.machinemuse.powersuits.capabilities;

import net.machinemuse.numina.constants.NuminaNBTConstants;
import net.machinemuse.numina.item.MuseItemUtils;
import net.machinemuse.numina.module.IModuleManager;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.energy.EnergyStorage;

import javax.annotation.Nonnull;

/**
 * Note that this class does not update any NBT tag data itself, but rather is just part of a wrapper for
 * power storage devices in the item's inventory
 */
public class ForgeEnergyItemWrapper extends EnergyStorage implements INBTSerializable<NBTTagCompound> {
    ItemStack container;
    IModuleManager moduleManager;

    /**
     * TODO: need to set an NNBT tag for the max getValue instead of recalculating over and over.
     *
     * @param container
     * @param moduleManagerIn
     */
    public ForgeEnergyItemWrapper(@Nonnull ItemStack container, IModuleManager moduleManagerIn) {
        super((int) moduleManagerIn.getOrSetModularPropertyDouble(container, NuminaNBTConstants.MAXIMUM_ENERGY));
        this.moduleManager = moduleManagerIn;
        this.container = container;
    }

    @Override
    public int receiveEnergy(int energyProvided, boolean simulate) {
        if (!canReceive())
            return 0;

        int energyReceived = super.receiveEnergy(energyProvided, simulate);

        if (!simulate && energyReceived > 0) {
            NBTTagCompound nbt = serializeNBT();
            if (nbt.hasKey(NuminaNBTConstants.CURRENT_ENERGY, Constants.NBT.TAG_INT))
                energy = nbt.getInteger(NuminaNBTConstants.CURRENT_ENERGY);
            else
                energy = 0;
            MuseItemUtils.setDoubleOrRemove(container, NuminaNBTConstants.CURRENT_ENERGY, energy); // TODO: switch to int
        }
        return energyReceived;
    }

    @Override
    public int extractEnergy(int energyRequested, boolean simulate) {
        if (!canExtract())
            return 0;

        int energyExtracted = super.extractEnergy(energyRequested, simulate);

        if (!simulate && energyExtracted > 0) {
            NBTTagCompound nbt = serializeNBT();
            if (nbt.hasKey(NuminaNBTConstants.CURRENT_ENERGY, Constants.NBT.TAG_INT))
                energy = nbt.getInteger(NuminaNBTConstants.CURRENT_ENERGY);
            else
                energy = 0;
            MuseItemUtils.setDoubleOrRemove(container, NuminaNBTConstants.CURRENT_ENERGY, energy); // TODO: switch to int
        }
        return energyExtracted;
    }

    public void updateFromNBT() {
        capacity = maxExtract = maxReceive = (int) moduleManager.getOrSetModularPropertyDouble(container, NuminaNBTConstants.MAXIMUM_ENERGY);
        energy = Math.min(capacity, (int) Math.round(MuseItemUtils.getDoubleOrZero(container, NuminaNBTConstants.CURRENT_ENERGY)));
    }

    @Override
    public NBTTagCompound serializeNBT() {
        NBTTagCompound nbt = new NBTTagCompound();
        if (energy > 0)
            nbt.setInteger(NuminaNBTConstants.CURRENT_ENERGY, energy);
        nbt.setInteger(NuminaNBTConstants.MAXIMUM_ENERGY, capacity);
        return nbt;
    }

    @Override
    public void deserializeNBT(NBTTagCompound nbt) {
        if (nbt.hasKey(NuminaNBTConstants.CURRENT_ENERGY, Constants.NBT.TAG_INT))
            energy = nbt.getInteger(NuminaNBTConstants.CURRENT_ENERGY);
        else
            energy = 0;
        capacity = maxExtract = maxReceive = nbt.getInteger(NuminaNBTConstants.MAXIMUM_ENERGY);
    }
}