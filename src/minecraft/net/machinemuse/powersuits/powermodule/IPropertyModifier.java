package net.machinemuse.powersuits.powermodule;

import net.minecraft.nbt.NBTTagCompound;

public interface IPropertyModifier {
	public double applyModifier(NBTTagCompound moduleTag, double value);
}
