package machinemuse.powersuits.augmentation;

import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class AugmentationArmorPlating extends Augmentation {

	public AugmentationArmorPlating() {

	}

	public AugmentationArmorPlating(NBTTagCompound tag) {
		// TODO Auto-generated constructor stub
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
	public List<ItemStack> getUpgradeCost(int level) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ItemStack> getDowngradeRefund(int level) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public NBTTagCompound getNBTTag() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Augmentation newAug() {
		// TODO Auto-generated method stub
		return new AugmentationArmorPlating();
	}

}
