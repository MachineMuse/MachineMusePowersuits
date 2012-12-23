package machinemuse.powersuits.augmentation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import machinemuse.powersuits.item.IModularItem;
import machinemuse.powersuits.item.ItemUtils;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
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
		IModularItem item = ItemUtils.getAsModular(itemStack.getItem());
		CraftingManager.getInstance().getRecipeList();
		if (item != null) {
			augCache = new HashMap<Augmentation, Augmentation>();
			if (itemStack.hasTagCompound()) {

				this.tag = itemStack.getTagCompound().getCompoundTag(
						Augmentation.nbtPrefix);
				validAugs = item.getValidAugs();
			} else {
				NBTTagCompound itemTag = new NBTTagCompound();
				this.tag = new NBTTagCompound();
				itemTag.setCompoundTag(Augmentation.nbtPrefix,
						this.tag);
				itemStack.setTagCompound(itemTag);
				validAugs = item.getValidAugs();

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
		tag.setCompoundTag(aug.getName(), augdata.getNBTTag());
		return augdata;
	}

	public int getCurrentArmorValue() {

		return 0;
	}

	/**
	 * @return
	 */
	public List<Augmentation> getAllAugData() {
		List<Augmentation> augData = new ArrayList<Augmentation>();
		Iterator<Augmentation> iter = validAugs.iterator();
		while (iter.hasNext()) {
			augData.add(getAugData(iter.next()));
		}
		return augData;
	}
}
