package net.machinemuse.powersuits.trash;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;

/**
 * Modules that implement this interface will limit the player by slowing them
 * down if they have too much total weight.
 * 
 */
public interface IPowerModuleWeight {
	/**
	 * Returns the weight of the module in kg. More than 25kg of equipment will
	 * start to slow a player down.
	 * 
	 * @return Module's weight in kg
	 */
	public abstract float getWeight(
			EntityPlayer player, NBTTagCompound moduleTag);

}
