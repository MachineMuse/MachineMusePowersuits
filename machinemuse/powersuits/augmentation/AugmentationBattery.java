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
	float energy;

	public AugmentationBattery() {

	}

	@Override
	public String getName() {
		return "Electric Battery";
	}

	@Override
	public float getWeight() {
		return 1;
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
		return new AugmentationBattery();
	}

	@Override
	public Augmentation fromNBTTag(NBTTagCompound tag) {
		// TODO Auto-generated method stub
		return null;
	}

}
