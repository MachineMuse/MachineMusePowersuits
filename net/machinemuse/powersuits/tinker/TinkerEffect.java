package net.machinemuse.powersuits.tinker;

import net.minecraft.nbt.NBTTagCompound;

public abstract class TinkerEffect {
	public abstract void applyEffect(NBTTagCompound properties);

	public abstract double simEffectMin(NBTTagCompound properties);

	public abstract double simEffectMax(NBTTagCompound properties);

	@Override
	public abstract String toString();
}
