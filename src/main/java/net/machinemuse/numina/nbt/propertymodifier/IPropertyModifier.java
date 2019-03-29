package net.machinemuse.numina.nbt.propertymodifier;

import net.minecraft.nbt.NBTTagCompound;

public interface IPropertyModifier<T> {
    T applyModifier(NBTTagCompound moduleTag, double value);
}