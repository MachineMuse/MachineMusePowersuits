package net.machinemuse.numina.nbt;

import net.minecraft.nbt.NBTTagCompound;

public interface IPropertyModifier<T> {
    T applyModifier(NBTTagCompound moduleTag, double value);
}