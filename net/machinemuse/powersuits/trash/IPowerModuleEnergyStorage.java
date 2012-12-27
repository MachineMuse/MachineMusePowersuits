package net.machinemuse.powersuits.trash;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;

public interface IPowerModuleEnergyStorage {
	public static final String MAXENERGY = "Maximum Energy";
	public static final String CURRENERGY = "Current Energy";

	public float getStoredEnergy(EntityPlayer player, NBTTagCompound batteryTag);

	public float getMaxEnergy(EntityPlayer player, NBTTagCompound batteryTag);

	public float changeEnergy(EntityPlayer player, NBTTagCompound batteryTag,
			float amount);
}
