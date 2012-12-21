package machinemuse.powersuits.item;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public abstract class ItemUtil {

	/**
	 * Scans a specified inventory for modular items.
	 * 
	 * @param inv
	 *            IInventory to scan.
	 * @return A List of ItemStacks in the inventory which implement
	 *         IModularItem
	 */
	public static List<ItemStack> getModularItemsInInventory(IInventory inv) {
		ArrayList<ItemStack> stacks = new ArrayList<ItemStack>();

		for (int i = 0; i < inv.getSizeInventory(); i++) {
			ItemStack stack = inv.getStackInSlot(i);
			if (stack != null && stack.getItem() instanceof IModularItem) {
				stacks.add(stack);
			}
		}
		return stacks;
	}

	public static IModularItem getAsModular(Item item) {
		if (item instanceof IModularItem) {
			return (IModularItem) item;
		} else {
			return null;
		}
	}
}
