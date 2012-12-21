package machinemuse.powersuits.augmentation;

import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

/**
 * An Augmentation is a module that can be slotted into the power armor or tool.
 * 
 * @author MachineMuse
 * 
 */
public abstract class Augmentation {
	/**
	 * Default compound for all NBTtag keys on itemstacks. Needed in case some
	 * other mod adds NBT data to these items.
	 * 
	 * @return
	 */
	public static String nbtPrefix = "mmmpsmod";

	public static Augmentation fromNBTTag(NBTTagCompound tag) {
		int id = tag.getInteger("AugID");

		switch (id) {
		case 1:
			return new AugmentationArmorPlating(tag);
		default:
			return null;
		}
	}

	/**
	 * Returns the name of this Augmentation.
	 * 
	 * @return
	 */
	public abstract String getName();

	/**
	 * The amount that this Aug contributes to the weight factor. Mostly used in
	 * determining speed penalty for now.
	 * 
	 * @return
	 */
	public abstract float getWeight();

	/**
	 * Returns a list of material requirements to upgrade the augmentation to
	 * the next level.
	 * 
	 * @param level
	 *            the level you would be upgrading to
	 * @return a list of ItemStacks describing the cost.
	 */
	public abstract List<ItemStack> getUpgradeCost(int level);

	/**
	 * Returns a list of materials that would be refunded from downgrading or
	 * removing this augmentation.
	 * 
	 * @param level
	 *            the level you would be downgrading to
	 * @return a list of ItemStacks describing the cost.
	 */
	public abstract List<ItemStack> getDowngradeRefund(int level);

	public abstract NBTTagCompound getNBTTag();

	public abstract Augmentation newAug();
}
