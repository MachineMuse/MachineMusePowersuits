package net.machinemuse.powersuits.powermodule;

import net.minecraft.nbt.NBTTagCompound;

public interface IModuleProperty {
	public double computeProperty(NBTTagCompound moduleTag);
	
	public String getName();
	public String getUnits();
	
}
