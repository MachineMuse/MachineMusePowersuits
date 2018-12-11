package net.machinemuse.numina.nbt;

import net.minecraft.nbt.NBTTagCompound;

public interface IPropertyModifierDouble extends IPropertyModifier<Double> {
    @Override
    Double applyModifier(NBTTagCompound moduleTag, double value);
}
