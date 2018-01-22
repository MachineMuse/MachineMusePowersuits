package net.machinemuse.powersuits.common.capabilities.energy;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagInt;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.energy.EnergyStorage;

public final class MuseEnergyStorage extends EnergyStorage implements INBTSerializable<NBTTagInt> {
    private static final String TAG_ENERGY = "energy";

    private final ItemStack container;

    public MuseEnergyStorage(final ItemStack container) {
        // TODO: energy based on stack in and maybe add an internal buffer and add capacitor to the recipes for armor, fist, ...
        // Energy can be changed later

        super(0);
        this.container = container;
    }

    public MuseEnergyStorage(final ItemStack container, int capacity, int maxReceive, int maxExtract, int energy) {
        super(capacity, maxReceive, maxExtract, energy);
        this.container = container;
    }


    public void updateFromNBT() {
        final NBTTagCompound nbt = container.getTagCompound();
        if (nbt != null && nbt.hasKey(TAG_ENERGY, net.minecraftforge.common.util.Constants.NBT.TAG_INT)) {
            deserializeNBT((NBTTagInt) nbt.getTag(TAG_ENERGY));
        }
    }

    // --------------------------------------------------------------------- //
    // IEnergyStorage

    @Override
    public int receiveEnergy(final int maxReceive, final boolean simulate) {
        final int energyReceived = super.receiveEnergy(maxReceive, simulate);
        if (!simulate && energyReceived != 0) {
            container.setTagInfo(TAG_ENERGY, serializeNBT());
        }
        return energyReceived;
    }

    @Override
    public int extractEnergy(final int maxExtract, final boolean simulate) {
        final int energyExtracted = super.extractEnergy(maxExtract, simulate);
        if (!simulate && energyExtracted != 0) {
            container.setTagInfo(TAG_ENERGY, serializeNBT());
        }
        return energyExtracted;
    }

    // --------------------------------------------------------------------- //
    // INBTSerializable

    @Override
    public NBTTagInt serializeNBT() {
        return new NBTTagInt(energy);
    }

    @Override
    public void deserializeNBT(final NBTTagInt nbt) {
        energy = nbt.getInt();
    }
}
