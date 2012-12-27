package net.machinemuse.powersuits.trash;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;

public interface IPowerModuleArmor {
	/**
	 * Returns the current armor value provided by the module.
	 * 
	 * @return
	 */
	public abstract float getArmorValue(
			EntityPlayer player, NBTTagCompound moduleTag);

	/**
	 * Perform the appropriate calculations for when a player with the module is
	 * dealt damage - either reduce durability, or drain the energy net, or
	 * something else perhaps.
	 * 
	 * @return
	 */
	public float damageArmor(EntityPlayer player, NBTTagCompound tag,
			float damageAmount);
}
