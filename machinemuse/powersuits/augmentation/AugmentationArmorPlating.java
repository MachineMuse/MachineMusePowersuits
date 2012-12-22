package machinemuse.powersuits.augmentation;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class AugmentationArmorPlating extends Augmentation {
	protected int durability;

	public AugmentationArmorPlating() {
		level = 0;
	}

	public AugmentationArmorPlating(NBTTagCompound tag) {
		this.nbtTag = tag;
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
		// TODO Auto-generated method stub
		return new AugmentationArmorPlating();
	}

	@Override
	public Augmentation fromNBTTag(NBTTagCompound tag) {
		AugmentationArmorPlating aug = new AugmentationArmorPlating();
		if (tag.hasKey("level")) {
			aug.level = tag.getInteger("Level");
		} else {
			aug.level = new Integer(0);
		}
		if (tag.hasKey("durability")) {
			aug.durability = tag.getInteger("Durability");
		} else {
			aug.durability = new Integer(0);
		}
		return aug;
	}
}
