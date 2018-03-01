package net.machinemuse.numina.api.capability_ports.itemwrapper;

import net.machinemuse.numina.api.constants.NuminaNBTConstants;
import net.machinemuse.powersuits.utils.MuseItemUtils;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.energy.EnergyStorage;

import javax.annotation.Nonnull;

public class ForgeEnergyItemWrapper extends EnergyStorage implements INBTSerializable<NBTTagCompound> {
    ItemStack container;

    public ForgeEnergyItemWrapper(@Nonnull ItemStack container, int capacity) {
        super(capacity);
        this.container = container;
    }

    public ForgeEnergyItemWrapper(@Nonnull ItemStack container,int capacity, int maxTransfer) {
        super(capacity, maxTransfer);
        this.container = container;
    }

    public ForgeEnergyItemWrapper(@Nonnull ItemStack container,int capacity, int maxReceive, int maxExtract) {
        super(capacity, maxReceive, maxExtract);
        this.container = container;
    }

    public ForgeEnergyItemWrapper(@Nonnull ItemStack container,int capacity, int maxReceive, int maxExtract, int energy) {
        super(capacity, maxReceive, maxExtract, energy);
        this.container = container;
    }

    @Override
    public int receiveEnergy(final int maxReceive, boolean simulate) {
        final int energyReceived = super.receiveEnergy(maxReceive, simulate);
        if (!simulate && energyReceived != 0) {
            updateContainerNBT();
        }
        return energyReceived;
    }

    @Override
    public int extractEnergy(final int maxExtract, boolean simulate) {
        final int energyExtracted = super.extractEnergy(maxExtract, simulate);
        if (!simulate && energyExtracted != 0) {
            updateContainerNBT();
        }
        return energyExtracted;
    }


    private void updateContainerNBT() {
        NBTTagCompound containerNBT = MuseItemUtils.getMuseItemTag(container);
        NBTTagCompound nbt = serializeNBT();

        if (nbt.hasKey(NuminaNBTConstants.CURRENT_ENERGY, Constants.NBT.TAG_INT))
            containerNBT.setInteger(NuminaNBTConstants.CURRENT_ENERGY, nbt.getInteger(NuminaNBTConstants.CURRENT_ENERGY));
        if(nbt.hasKey(NuminaNBTConstants.MAXIMUM_ENERGY, Constants.NBT.TAG_INT))
            containerNBT.setInteger(NuminaNBTConstants.MAXIMUM_ENERGY, nbt.getInteger(NuminaNBTConstants.MAXIMUM_ENERGY));
    }

    // can be called in the capability provider under getCapability to update values if needed.
    public void updateFromNBT() {
        updateContainerNBT();
    }

    @Override
    public NBTTagCompound serializeNBT() {
        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setInteger(NuminaNBTConstants.CURRENT_ENERGY, energy);
        nbt.setInteger(NuminaNBTConstants.MAXIMUM_ENERGY, capacity);
        return nbt;
    }

    @Override
    public void deserializeNBT(NBTTagCompound nbt) {
        if (nbt.hasKey(NuminaNBTConstants.CURRENT_ENERGY, Constants.NBT.TAG_INT))
            this.energy = nbt.getInteger(NuminaNBTConstants.CURRENT_ENERGY);
        if(nbt.hasKey(NuminaNBTConstants.MAXIMUM_ENERGY, Constants.NBT.TAG_INT))
            capacity = maxExtract = maxReceive = nbt.getInteger(NuminaNBTConstants.MAXIMUM_ENERGY);
    }
}
