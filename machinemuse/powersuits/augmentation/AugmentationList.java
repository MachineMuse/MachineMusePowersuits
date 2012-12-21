package machinemuse.powersuits.augmentation;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import machinemuse.powersuits.item.IModularItem;
import machinemuse.powersuits.item.ItemUtil;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

/**
 * An instance of this class provides an abstraction of the NBTTag data
 * associated with an IModularItem stack.
 * 
 * @author MachineMuse
 */
public class AugmentationList {
	NBTTagCompound tag;
	List<Augmentation> validAugs;
	HashMap<Augmentation, Augmentation> augCache;

	/**
	 * Constructor takes an ItemStack as an argument and, if the ItemStack is a
	 * valid IModularItem, initializes values in a lazy manner.
	 * 
	 * @param itemStack
	 */
	public AugmentationList(ItemStack itemStack) {
		IModularItem item = ItemUtil.getAsModular(itemStack.getItem());

		if (item != null) {
			if (itemStack.hasTagCompound()) {

				this.tag = itemStack.getTagCompound().getCompoundTag(
						Augmentation.nbtPrefix);
				validAugs = item.getValidAugs();
				augCache = new HashMap<Augmentation, Augmentation>();
			} else {
				NBTTagCompound nbt = new NBTTagCompound();
				nbt.setCompoundTag(Augmentation.nbtPrefix,
						new NBTTagCompound());
				itemStack.setTagCompound(nbt);
			}
		}
	}

	public void writeTags() {
		Iterator<Augmentation> iter = augCache.values().iterator();

		while (iter.hasNext()) {
			Augmentation current = iter.next();
			tag.setCompoundTag(current.getName(), current.getNBTTag());
		}
	}

	public Augmentation getAugData(Augmentation aug) {
		if (augCache.containsKey(aug)) {
			return augCache.get(aug);
		}
		Augmentation augdata;
		if (tag.hasKey(aug.getName())) {
			augdata = aug.fromNBTTag(tag.getCompoundTag(aug.getName()));
			augCache.put(aug, augdata);
			return augdata;
		}
		augdata = aug.newAug();
		augCache.put(aug, augdata);
		return augdata;
	}

	public int getCurrentArmorValue() {

		return 0;
	}
}
