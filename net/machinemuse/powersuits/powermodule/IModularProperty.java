package net.machinemuse.powersuits.powermodule;

import net.minecraft.item.ItemStack;

public interface IModularProperty {
	public double computeProperty(ItemStack stack);
}
