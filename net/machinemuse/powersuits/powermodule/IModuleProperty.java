package net.machinemuse.powersuits.powermodule;

import net.minecraft.nbt.NBTTagCompound;

public interface IModuleProperty {
	public double computeProperty(NBTTagCompound moduleTag);
	
	public String getString(NBTTagCompound moduleTag);
}
