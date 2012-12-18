package machinemuse.powersuits.common.augmentation;

import java.util.List;

import net.minecraft.item.ItemStack;

public abstract class Augmentation {
	public abstract String getName();

	public abstract float getWeight();

	public abstract boolean isValidForSlot(int slot);

	public abstract List<ItemStack> getMaterialCost();

	public abstract List<ItemStack> getMaterialRefund();
}
