package machinemuse.powersuits.item;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import machinemuse.powersuits.augmentation.Augmentation;
import machinemuse.powersuits.augmentation.AugmentationList;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class ItemUtils {
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

	/**
	 * Attempts to cast an item to IModularItem, returns null if fails
	 */
	public static IModularItem getAsModular(Item item) {
		if (item instanceof IModularItem) {
			return (IModularItem) item;
		} else {
			return null;
		}
	}

	/**
	 * @param player
	 * @return
	 */
	public static List<Augmentation> getPlayerAugs(EntityPlayer player) {
		List<Augmentation> augs = new ArrayList<Augmentation>();
		List<ItemStack> items = getModularItemsInInventory(player.inventory);
		Iterator<ItemStack> iter = items.iterator();
		while (iter.hasNext()) {
			AugmentationList itemAugs = new AugmentationList(iter.next());
			augs.addAll(itemAugs.getAllAugData());
		}

		return augs;
	}

	/**
	 * Checks if the player has a copy of all of the items in
	 * workingUpgradeCost.
	 * 
	 * @param workingUpgradeCost
	 * @param inventory
	 * @return
	 */
	public static boolean hasInInventory(List<ItemStack> workingUpgradeCost,
			InventoryPlayer inventory) {
		for (int j = 0; j < workingUpgradeCost.size(); j++) {
			ItemStack stackInCost = workingUpgradeCost.get(j);
			int found = 0;
			for (int i = 0; i < inventory.getSizeInventory(); i++) {
				ItemStack stackInInventory = inventory.getStackInSlot(i);
				if (stackInInventory != null) {
					if (stackInInventory.itemID == stackInCost.itemID) {
						found += stackInInventory.stackSize;
					}
				}
			}
			if (found < stackInCost.stackSize) {
				return false;
			}
		}
		return true;
	}

	/**
	 * @param workingUpgradeCost
	 * @param inventory
	 */
	public static void deleteFromInventory(List<ItemStack> workingUpgradeCost,
			InventoryPlayer inventory) {
		for (int j = 0; j < workingUpgradeCost.size(); j++) {
			ItemStack stackInCost = workingUpgradeCost.get(j);
			int remaining = stackInCost.stackSize;
			for (int i = 0; i < inventory.getSizeInventory(); i++) {
				ItemStack stackInInventory = inventory.getStackInSlot(i);
				if (stackInInventory != null) {
					if (stackInInventory.itemID == stackInCost.itemID) {
						if (stackInInventory.stackSize > remaining) {
							stackInInventory.stackSize -= remaining;
							remaining = 0;
						} else {
							remaining -= stackInInventory.stackSize;
							stackInInventory.stackSize = 0;
						}
					}
				}
			}
		}
	}
}
