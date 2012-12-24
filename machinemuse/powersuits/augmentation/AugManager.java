package machinemuse.powersuits.augmentation;

import java.util.ArrayList;
import java.util.List;

import machinemuse.powersuits.common.MuseLogger;
import machinemuse.powersuits.item.ItemPowerArmor;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

/**
 * An Augmentation is a module that can be slotted into the power armor or tool.
 * 
 * @author MachineMuse
 * 
 */
public abstract class AugManager {
	/**
	 * Default compound for all NBTtag keys on itemstacks. Needed in case some
	 * other mod adds NBT data to these items.
	 * 
	 * @return
	 */
	public static final String nbtPrefix = "mmmpsmod";

	/**
	 * Names of augs
	 */
	public static final String STONEARMORPLATING = "Stone Armor Plating";
	public static final String IRONARMORPLATING = "Iron Armor Plating";
	public static final String DIAMONDARMORPLATING = "Diamond Armor Plating";
	public static final String NICADBATTERY = "Nickel-Cadmium Battery";
	public static final String LIIONBATTERY = "Lithium-Ion Battery";
	public static final String CARBONTUBEBATTERY = "Carbon Nanotube Battery";

	/**
	 * Names of aug properties
	 */
	public static final String LEVEL = "Level";
	public static final String WEIGHT = "Weight";
	public static final String NAME = "Name";
	public static final String DURABILITY = "Durability";

	public static int getLevel(NBTTagCompound aug) {
		if (aug.hasKey(LEVEL)) {
			return aug.getInteger(LEVEL);
		} else {
			return 0;
		}
	}

	public static void setLevel(NBTTagCompound aug, int level) {
		aug.setInteger(LEVEL, level);
	}

	public static String getName(NBTTagCompound aug) {
		return aug.getString(NAME);
	}

	/**
	 * The amount that the Aug contributes to the weight factor. Mostly used in
	 * determining speed penalty for now.
	 * 
	 * @return
	 */
	public static float getWeight(NBTTagCompound aug) {
		String augname = aug.getString(NAME);
		if (augname.equals(STONEARMORPLATING)
				|| augname.equals(IRONARMORPLATING)
				|| augname.equals(DIAMONDARMORPLATING)) {
			return 10.0F * getLevel(aug);
		}
		return 0;
	}

	/**
	 * Returns a list of material requirements to upgrade the augmentation to
	 * the next level.
	 * 
	 * @param level
	 *            the level you would be upgrading to
	 * @return a list of ItemStacks describing the cost.
	 */
	public static List<ItemStack> getInstallCost(String augType) {
		List<ItemStack> cost = new ArrayList<ItemStack>();
		cost.add(new ItemStack(Item.redstone, 3));
		if (augType.equals(STONEARMORPLATING)) {
			cost.add(new ItemStack(Block.cobblestone, 10));
		}
		return cost;
	}

	/**
	 * Returns a list of materials that would be refunded from downgrading or
	 * removing this augmentation.
	 * 
	 * @param level
	 *            the level you would be downgrading to
	 * @return a list of ItemStacks describing the cost.
	 */
	public static List<ItemStack> getDowngradeRefund(String augType) {
		List<ItemStack> refund = new ArrayList<ItemStack>();
		refund.add(new ItemStack(Item.redstone, 3));
		return refund;
	}

	/**
	 * @param validAug
	 * @return
	 */
	public static NBTTagCompound newAugOfType(String validAug) {
		NBTTagCompound newAug = new NBTTagCompound();
		if (validAug.equals(STONEARMORPLATING)) {
			newAug.setString(NAME, STONEARMORPLATING);
			newAug.setInteger(DURABILITY, 100);
		} else if (validAug.equals(IRONARMORPLATING)) {
			newAug.setString(NAME, IRONARMORPLATING);
			newAug.setInteger(DURABILITY, 100);
		} else if (validAug.equals(DIAMONDARMORPLATING)) {
			newAug.setString(NAME, DIAMONDARMORPLATING);
			newAug.setInteger(DURABILITY, 100);
		} else if (validAug.equals(NICADBATTERY)) {
			newAug.setString(NAME, NICADBATTERY);
		} else if (validAug.equals(LIIONBATTERY)) {
			newAug.setString(NAME, LIIONBATTERY);
		} else if (validAug.equals(CARBONTUBEBATTERY)) {
			newAug.setString(NAME, CARBONTUBEBATTERY);
		}
		return newAug;
	}

	/**
	 * @param item
	 * @return
	 */
	public static List<String> getValidAugsForItem(Item item) {
		List<String> validAugs = new ArrayList<String>();
		if (item instanceof ItemPowerArmor) {
			validAugs.add(STONEARMORPLATING);
			validAugs.add(IRONARMORPLATING);
			validAugs.add(DIAMONDARMORPLATING);

		}
		validAugs.add(NICADBATTERY);
		validAugs.add(LIIONBATTERY);
		validAugs.add(CARBONTUBEBATTERY);
		return validAugs;
	}

	/**
	 * @param itemAugs
	 * @param aug
	 */
	public static void upgrade(NBTTagCompound itemAugs, String aug) {
		MuseLogger.logDebug("Upgrading " + aug);
		if (itemAugs.hasKey(aug)) {
			NBTTagCompound augTag = itemAugs.getCompoundTag(aug);
			setLevel(augTag, getLevel(augTag) + 1);
		} else {
			NBTTagCompound augTag = newAugOfType(aug);
			itemAugs.setCompoundTag(aug, augTag);
			setLevel(augTag, getLevel(augTag) + 1);
		}
	}

	/**
	 * @param aug
	 * @return
	 */
	public static List<String> getTooltipFor(NBTTagCompound aug) {
		List<String> tooltiplines = new ArrayList<String>();
		tooltiplines.add("Augmentation: ");
		tooltiplines.add(aug.getString(NAME));
		tooltiplines.add("Level: " + aug.getInteger(LEVEL));
		return tooltiplines;
	}
}
