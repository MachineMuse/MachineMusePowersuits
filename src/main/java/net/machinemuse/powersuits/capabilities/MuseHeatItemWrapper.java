package net.machinemuse.powersuits.capabilities;

import net.machinemuse.numina.capabilities.heat.HeatStorage;
import net.machinemuse.numina.constants.NuminaNBTConstants;
import net.machinemuse.numina.item.MuseItemUtils;
import net.machinemuse.numina.module.IModuleManager;
import net.machinemuse.numina.nbt.MuseNBTUtils;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.INBTSerializable;

import javax.annotation.Nonnull;

public class MuseHeatItemWrapper extends HeatStorage implements INBTSerializable<NBTTagCompound> {
    IModuleManager moduleManager;
    ItemStack container;
    double baseMaxHeat;

    public MuseHeatItemWrapper(@Nonnull ItemStack container, double baseMaxHeat, IModuleManager moduleManagerIn) {
        super(moduleManagerIn.getOrSetModularPropertyDouble(container, NuminaNBTConstants.MAXIMUM_HEAT) + baseMaxHeat);
        this.container = container;
        this.moduleManager = moduleManagerIn;
        this.baseMaxHeat = baseMaxHeat;
    }

    @Override
    public double receiveHeat(double heatProvided, boolean simulate) {
        if (!canReceive())
            return 0;
        double heatReceived = super.receiveHeat(heatProvided, simulate);
        if (!simulate && heatReceived > 0) {
            NBTTagCompound nbt = serializeNBT();
            if (nbt.hasKey(NuminaNBTConstants.CURRENT_HEAT, Constants.NBT.TAG_DOUBLE))
                heat = nbt.getDouble(NuminaNBTConstants.CURRENT_HEAT);
            else
                heat = 0;
            capacity = maxExtract = maxReceive = nbt.getDouble(NuminaNBTConstants.MAXIMUM_HEAT);
            MuseItemUtils.setDoubleOrRemove(container, NuminaNBTConstants.CURRENT_HEAT, heat);
        }
        return heatReceived;
    }

    @Override
    public double extractHeat(double heatRequested, boolean simulate) {
        if (!canExtract())
            return 0;
        double heatExtracted = super.extractHeat(heatRequested, simulate);
        if (!simulate) {
            NBTTagCompound nbt = serializeNBT();
            if (nbt.hasKey(NuminaNBTConstants.CURRENT_HEAT, Constants.NBT.TAG_DOUBLE))
                heat = nbt.getDouble(NuminaNBTConstants.CURRENT_HEAT);
            else
                heat = 0;
            capacity = maxExtract = maxReceive = nbt.getDouble(NuminaNBTConstants.MAXIMUM_HEAT);
            MuseItemUtils.setDoubleOrRemove(container, NuminaNBTConstants.CURRENT_HEAT, heat);
        }
        return heatExtracted;
    }

    public void updateFromNBT() {
        NBTTagCompound itemNBT = MuseNBTUtils.getMuseItemTag(container);
        NBTTagCompound outNBT = new NBTTagCompound();
        capacity = maxExtract = maxReceive = moduleManager.getOrSetModularPropertyDouble(container, NuminaNBTConstants.MAXIMUM_HEAT) + baseMaxHeat;
        heat = Math.round(MuseItemUtils.getDoubleOrZero(itemNBT, NuminaNBTConstants.CURRENT_HEAT));

        outNBT.setDouble(NuminaNBTConstants.MAXIMUM_HEAT, capacity);
        if (heat > 0)
            outNBT.setDouble(NuminaNBTConstants.CURRENT_HEAT, heat);
        deserializeNBT(outNBT);
    }

    // INBTSerializable ---------------------------------------------------------------------------
    @Override
    public NBTTagCompound serializeNBT() {
        NBTTagCompound nbt = new NBTTagCompound();
        if (heat > 0)
            nbt.setDouble(NuminaNBTConstants.CURRENT_HEAT, heat);
        nbt.setDouble(NuminaNBTConstants.MAXIMUM_HEAT, capacity);
        return nbt;
    }

    @Override
    public void deserializeNBT(final NBTTagCompound nbt) {
        if (nbt.hasKey(NuminaNBTConstants.CURRENT_HEAT, Constants.NBT.TAG_DOUBLE))
            heat = nbt.getDouble(NuminaNBTConstants.CURRENT_HEAT);
        else
            heat = 0;
        capacity = maxExtract = maxReceive = nbt.getDouble(NuminaNBTConstants.MAXIMUM_HEAT);
    }
}