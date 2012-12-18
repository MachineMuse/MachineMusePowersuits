package machinemuse.powersuits.common.augmentation;

import java.util.ArrayList;

import machinemuse.powersuits.common.Config;
import machinemuse.powersuits.common.item.IModularItem;
import net.minecraft.nbt.NBTTagCompound;

/**
 * An instance of this class provides an abstraction of the NBTTag data
 * associated with an IModularItem stack.
 * 
 * @author MachineMuse
 */
public class AugmentationList {
	NBTTagCompound tag;
	ArrayList<Augmentation> augList;

	public AugmentationList(IModularItem item, NBTTagCompound tag) {
		this.tag = tag;

		AugmentationTypes[] validAugs = item.getValidAugTypes();

		augList = new ArrayList<Augmentation>();
		for (int i = 0; i < validAugs.length; i++)
		{
			if (tag.hasKey(Config.getModuleNBTTagPrefix() + i)) {
				NBTTagCompound augData = tag.getCompoundTag(Config
						.getModuleNBTTagPrefix() + i);
				augList.add(Augmentation.fromNBTTag(augData));
			}
		}
	}

	public void writeTag(NBTTagCompound tag) {

		for (int i = 0; i < augList.size(); i++) {
			tag.setCompoundTag(Config.getModuleNBTTagPrefix() + i,
					augList.get(i).getNBTTag());
		}
		tag.setCompoundTag(Config.getModuleNBTTagPrefix() + augList.size(),
				null);
	}

	public int getCurrentArmorValue() {

		return 0;
	}
}
