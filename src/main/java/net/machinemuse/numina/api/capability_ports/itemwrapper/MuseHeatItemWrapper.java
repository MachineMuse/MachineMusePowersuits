package net.machinemuse.numina.api.capability_ports.itemwrapper;

import net.machinemuse.numina.api.capability_ports.heat.HeatStorage;
import net.machinemuse.numina.api.constants.NuminaNBTConstants;
import net.machinemuse.numina.utils.nbt.NuminaNBTUtils;
import net.machinemuse.powersuits.utils.MuseItemUtils;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.INBTSerializable;

import javax.annotation.Nonnull;

public class MuseHeatItemWrapper extends HeatStorage implements INBTSerializable<NBTTagCompound> {
    protected final ItemStack container;

    public MuseHeatItemWrapper(@Nonnull ItemStack container, int capacity) {
        super(capacity);
        this.container = container;
    }

    public MuseHeatItemWrapper(@Nonnull ItemStack container, int capacity, int maxTransfer) {
        super(capacity, maxTransfer);
        this.container = container;
    }

    public MuseHeatItemWrapper(@Nonnull ItemStack container, int capacity, int maxTransfer, int heat) {
        super(capacity, maxTransfer, heat);
        this.container = container;
    }

    private void updateContainerNBT() {
        NBTTagCompound containerNBT = NuminaNBTUtils.getMuseItemTag(container);
        NBTTagCompound nbt = serializeNBT();

        if (nbt.hasKey(NuminaNBTConstants.CURRENT_HEAT, Constants.NBT.TAG_INT))
            containerNBT.setInteger(NuminaNBTConstants.CURRENT_HEAT, nbt.getInteger(NuminaNBTConstants.CURRENT_HEAT));
        if(nbt.hasKey(NuminaNBTConstants.MAXIMUM_HEAT, Constants.NBT.TAG_INT))
            containerNBT.setInteger(NuminaNBTConstants.MAXIMUM_HEAT, nbt.getInteger(NuminaNBTConstants.MAXIMUM_HEAT));
    }

    @Override
    public int receiveHeat(final int maxReceive, boolean simulate) {
        final int heatReceived = super.receiveHeat(maxReceive, simulate);
        if (!simulate && heatReceived != 0) {
            updateContainerNBT();
        }
        return heatReceived;
    }

    @Override
    public int extractHeat(final int maxExtract, boolean simulate) {
        final int heatExtracted = super.extractHeat(maxExtract, simulate);
        if (!simulate && heatExtracted != 0) {
            updateContainerNBT();
        }
        return heatExtracted;
    }

    // can be called in the capability provider under getCapability to update values if needed.
    public void updateFromNBT() {
        updateContainerNBT();
    }

    @Override
    public NBTTagCompound serializeNBT() {
        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setInteger(NuminaNBTConstants.CURRENT_HEAT, heat);
        nbt.setInteger(NuminaNBTConstants.MAXIMUM_HEAT, capacity);
        return nbt;
    }

    @Override
    public void deserializeNBT(NBTTagCompound nbt) {
        if (nbt.hasKey(NuminaNBTConstants.CURRENT_HEAT, Constants.NBT.TAG_INT))
            this.heat = nbt.getInteger(NuminaNBTConstants.CURRENT_HEAT);
        if(nbt.hasKey(NuminaNBTConstants.MAXIMUM_HEAT, Constants.NBT.TAG_INT))
            capacity = maxTransfer = nbt.getInteger(NuminaNBTConstants.MAXIMUM_HEAT);
    }
}