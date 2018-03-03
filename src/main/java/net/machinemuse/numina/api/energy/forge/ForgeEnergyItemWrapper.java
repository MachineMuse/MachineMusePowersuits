package net.machinemuse.numina.api.energy.forge;

import net.machinemuse.numina.api.constants.NuminaNBTConstants;
import net.machinemuse.powersuits.utils.MuseItemUtils;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.energy.EnergyStorage;

import javax.annotation.Nonnull;

public class ForgeEnergyItemWrapper extends EnergyStorage implements IEnergyStorageExtended, INBTSerializable<NBTTagCompound> {
    ItemStack container;

    public ForgeEnergyItemWrapper(@Nonnull ItemStack container, int capacity) {
        super(capacity);
        this.container = container;
    }

    public ForgeEnergyItemWrapper(@Nonnull ItemStack container, int capacity, int maxTransfer, int energy) {
        super(capacity, maxTransfer, maxTransfer, energy);
        this.container = container;
    }


//  public EnergyStorage(int capacity, int maxReceive, int maxExtract, int energy)
    public ForgeEnergyItemWrapper(@Nonnull ItemStack container, int capacity, int maxReceive, int maxExtract, int energy) {
        super(capacity, maxReceive, maxExtract, energy);
        this.container = container;
    }

    public void updateFromNBT() {
        final NBTTagCompound nbt = MuseItemUtils.getMuseItemTag(container);
        NBTTagCompound nbtOut = new NBTTagCompound();
        if(nbt.hasKey(NuminaNBTConstants.CURRENT_ENERGY, Constants.NBT.TAG_INT)) {
            nbtOut.setInteger(NuminaNBTConstants.CURRENT_ENERGY, nbt.getInteger(NuminaNBTConstants.CURRENT_ENERGY));
            if(nbt.hasKey(NuminaNBTConstants.MAXIMUM_ENERGY, Constants.NBT.TAG_INT))
                nbtOut.setInteger(NuminaNBTConstants.MAXIMUM_ENERGY, nbt.getInteger(NuminaNBTConstants.MAXIMUM_ENERGY));
            deserializeNBT(nbtOut);
        }
    }

    // --------------------------------------------------------------------- //
    // IEnergyStorage

    @Override
    public int receiveEnergy(final int maxReceive, final boolean simulate) {
        final int energyReceived = super.receiveEnergy(maxReceive, simulate);
        if (!simulate && energyReceived != 0) {
            final NBTTagCompound nbt = MuseItemUtils.getMuseItemTag(container);
            NBTTagCompound nbtIn = serializeNBT();
            nbt.setInteger(NuminaNBTConstants.CURRENT_ENERGY, nbtIn.getInteger(NuminaNBTConstants.CURRENT_ENERGY));
            nbt.setInteger(NuminaNBTConstants.MAXIMUM_ENERGY, nbtIn.getInteger(NuminaNBTConstants.MAXIMUM_ENERGY));
        }
        return energyReceived;
    }

    @Override
    public int extractEnergy(final int maxExtract, final boolean simulate) {
        final int energyExtracted = super.extractEnergy(maxExtract, simulate);
        if (!simulate && energyExtracted != 0) {
            final NBTTagCompound nbt = MuseItemUtils.getMuseItemTag(container);
            NBTTagCompound nbtIn = serializeNBT();
            nbt.setInteger(NuminaNBTConstants.CURRENT_ENERGY, nbtIn.getInteger(NuminaNBTConstants.CURRENT_ENERGY));
            nbt.setInteger(NuminaNBTConstants.MAXIMUM_ENERGY, nbtIn.getInteger(NuminaNBTConstants.MAXIMUM_ENERGY));
        }
        return energyExtracted;
    }

    // --------------------------------------------------------------------- //
    // INBTSerializable




    @Override
    public void setEnergy(int currentEnergy) {

    }

    @Override
    public void setCapacity(int maxEnergy) {

    }

    @Override
    public void setMaxReceive(int maxReceive) {

    }

    @Override
    public void setMaxExtract(int maxExtract) {

    }

    @Override
    public NBTTagCompound serializeNBT() {
        NBTTagCompound nbtOut = new NBTTagCompound();
        nbtOut.setInteger(NuminaNBTConstants.CURRENT_ENERGY, energy);
        nbtOut.setInteger(NuminaNBTConstants.MAXIMUM_ENERGY, capacity);
        return nbtOut;
    }

    @Override
    public void deserializeNBT(NBTTagCompound nbt) {
        if(nbt.hasKey(NuminaNBTConstants.CURRENT_ENERGY, Constants.NBT.TAG_INT))
            energy = nbt.getInteger(NuminaNBTConstants.CURRENT_ENERGY);
        if(nbt.hasKey(NuminaNBTConstants.MAXIMUM_ENERGY, Constants.NBT.TAG_INT))
            capacity =  nbt.getInteger(NuminaNBTConstants.MAXIMUM_ENERGY);

        if(capacity > 0) {
            if (maxReceive == 0)
                maxReceive = maxExtract = capacity;
        }
    }
}