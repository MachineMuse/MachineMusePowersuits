package net.machinemuse_old.api;

import net.minecraft.nbt.NBTTagCompound;

public interface IPropertyModifier {
	double applyModifier(NBTTagCompound moduleTag, double value);
}
