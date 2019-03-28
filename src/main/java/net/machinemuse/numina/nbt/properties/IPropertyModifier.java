package net.machinemuse.numina.nbt.properties;

import net.minecraft.nbt.NBTTagCompound;

public interface IPropertyModifier<T> {
    T applyModifier(NBTTagCompound moduleTag, double value);
}