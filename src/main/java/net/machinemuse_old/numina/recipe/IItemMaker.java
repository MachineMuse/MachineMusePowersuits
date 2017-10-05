package net.machinemuse_old.numina.recipe;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;

/**
 * Author: MachineMuse (Claire Semple)
 * Created: 2:53 PM, 11/4/13
 */
public interface IItemMaker {
    ItemStack makeItem(InventoryCrafting inventoryCrafting);
    ItemStack getRecipeOutput();
}
