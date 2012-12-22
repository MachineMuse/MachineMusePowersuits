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
	protected NBTTagCompound nbtTag;

	/**
	 * Default compound for all NBTtag keys on itemstacks. Needed in case some
	 * other mod adds NBT data to these items.
	 * 
	 * @return
	 */
	public static String nbtPrefix = "mmmpsmod";

	protected int level;

	public int getLevel() {
		return level;
	}

	public void upgrade() {
		level += 1;
		if (nbtTag != null) {
			nbtTag.setInteger("level", level);
		}
	}

	public void downgrade() {
		level -= 1;
	}

	/**
	 * Allows the items to create Augmentation instances.
	 * 
	 * @param tag
	 * @return
	 */
	public abstract Augmentation fromNBTTag(NBTTagCompound tag);

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
	public abstract List<ItemStack> getUpgradeCost();

	/**
	 * Returns a list of materials that would be refunded from downgrading or
	 * removing this augmentation.
	 * 
	 * @param level
	 *            the level you would be downgrading to
	 * @return a list of ItemStacks describing the cost.
	 */
	public abstract List<ItemStack> getDowngradeRefund();

	public NBTTagCompound getNBTTag() {
		return nbtTag;
	}

	public abstract Augmentation newAug();
}
