package net.machinemuse.powersuits.powermodule.property;

import net.minecraft.nbt.NBTTagCompound;

public interface IPropertyModifier {
	public double applyModifier(NBTTagCompound moduleTag, double value);
}
