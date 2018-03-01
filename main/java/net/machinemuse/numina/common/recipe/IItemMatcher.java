package net.machinemuse.numina.common.recipe;

import net.minecraft.item.ItemStack;

/**
 * Author: MachineMuse (Claire Semple)
 * Created: 2:51 PM, 11/4/13
 */
public interface IItemMatcher {
    boolean matchesItem(ItemStack stack);
}
