package machinemuse.powersuits.augmentation;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class AugmentationArmorPlating extends Augmentation {
	protected int durability;
	protected static final String STR_DURABILITY = "Durability";

	public AugmentationArmorPlating(NBTTagCompound tag) {
		this.nbtTag = tag;
		level = 0;
	}

	@Override
	public String getName() {
		return "Armor Plating";
	}

	@Override
	public float getWeight() {
		return 1;
	}

	@Override
	public List<ItemStack> getUpgradeCost() {
		List<ItemStack> list = new ArrayList<ItemStack>();
		list.add(new ItemStack(Item.redstone, 4));
		return list;
	}

	@Override
	public List<ItemStack> getDowngradeRefund() {
		List<ItemStack> list = new ArrayList<ItemStack>();
		list.add(new ItemStack(Item.redstone, 3));
		return list;
	}

	@Override
	public Augmentation newAug() {
		return new AugmentationArmorPlating(new NBTTagCompound());
	}

	@Override
	public Augmentation fromNBTTag(NBTTagCompound tag) {
		AugmentationArmorPlating aug = new AugmentationArmorPlating(tag);
		if (tag.hasKey(STR_LEVEL)) {
			aug.level = tag.getInteger(STR_LEVEL);
		} else {
			aug.level = 0;
			tag.setInteger(STR_LEVEL, 0);
		}
		if (tag.hasKey(STR_DURABILITY)) {
			aug.durability = tag.getInteger(STR_DURABILITY);
		} else {
			aug.durability = 100;
			tag.setInteger(STR_DURABILITY, 100);
		}
		return aug;
	}
}
