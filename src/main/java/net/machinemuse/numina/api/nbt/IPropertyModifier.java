package net.machinemuse.numina.api.nbt;

import net.minecraft.nbt.NBTTagCompound;

public interface IPropertyModifier {
	double applyModifier(NBTTagCompound moduleTag, double value);
}
