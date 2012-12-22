/**
 * 
 */
package machinemuse.powersuits.augmentation;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

/**
 * @author MachineMuse
 * 
 */
public class AugmentationBattery extends Augmentation {
	protected float energy;
	protected static final String STR_ENERGY = "CurrentEnergy";

	/**
	 * @param nbtTagCompound
	 */
	public AugmentationBattery(NBTTagCompound tag) {
		this.nbtTag = tag;
		this.level = 0;
	}

	@Override
	public String getName() {
		return "Electric Battery";
	}

	public float getAvailableEnergy() {
		return energy;
	}

	public float getMaxEnergy() {
		return 5000 * 12 ^ level;
	}

	public float setAvailableEnergy() {
		return energy;
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
		AugmentationBattery aug = new AugmentationBattery(tag);
		if (tag.hasKey(STR_LEVEL)) {
			aug.level = tag.getInteger(STR_LEVEL);
		} else {
			aug.level = 0;
			tag.setInteger(STR_LEVEL, 0);
		}
		if (tag.hasKey(STR_ENERGY)) {
			aug.energy = tag.getFloat(STR_ENERGY);
		} else {
			aug.energy = 0;
			tag.setFloat(STR_ENERGY, 0);
		}
		return aug;
	}
}
