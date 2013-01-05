package net.machinemuse.powersuits.tinker;

import net.minecraft.item.ItemStack;

/**
 * Callback used for displaying item info. Create anonymous
 * 
 */
public interface ITinkerPropertyCallback {
	public double getValue(ItemStack stack);
}
