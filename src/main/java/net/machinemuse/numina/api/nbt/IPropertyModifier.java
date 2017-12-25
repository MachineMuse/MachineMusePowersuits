package net.machinemuse.numina.api.nbt;

import net.minecraft.nbt.NBTTagCompound;

public interface IPropertyModifier<T> {
	T applyModifier(NBTTagCompound moduleTag, T value);
}

