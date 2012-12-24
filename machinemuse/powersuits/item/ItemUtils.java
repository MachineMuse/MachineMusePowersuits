package machinemuse.powersuits.item;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import machinemuse.powersuits.augmentation.AugManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

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
	 * Scans a specified inventory for modular items.
	 * 
	 * @param inv
	 *            IInventory to scan.
	 * @return A List of inventory slots containing an IModularItem
	 */
	public static List<Integer> getModularItemSlotsInInventory(IInventory inv) {
		ArrayList<Integer> slots = new ArrayList<Integer>();

		for (int i = 0; i < inv.getSizeInventory(); i++) {
			ItemStack stack = inv.getStackInSlot(i);
			if (stack != null && stack.getItem() instanceof IModularItem) {
				slots.add(i);
			}
		}
		return slots;
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
	 * 
	 * 
	 * @param player
	 * @return
	 */
	public static List<NBTTagCompound> getPlayerAugs(EntityPlayer player) {
		List<NBTTagCompound> augs = new ArrayList<NBTTagCompound>();
		List<ItemStack> items = getModularItemsInInventory(player.inventory);
		Iterator<ItemStack> iter = items.iterator();
		while (iter.hasNext()) {
			NBTTagCompound itemAugs = getItemAugs(iter.next());
			for (Object ob : itemAugs.getTags()) {
				if (ob instanceof NBTTagCompound) {
					augs.add((NBTTagCompound) ob);
				}
			}
		}

		return augs;
	}

	/**
	 * 
	 * 
	 * @param next
	 * @return
	 */
	public static NBTTagCompound getItemAugs(ItemStack stack) {
		NBTTagCompound augs = null;
		if (stack.hasTagCompound()) {
			NBTTagCompound stackTag = stack.getTagCompound();
			if (stackTag.hasKey(AugManager.nbtPrefix)) {
				augs = stackTag.getCompoundTag(AugManager.nbtPrefix);
			} else {
				augs = new NBTTagCompound();
				stackTag.setCompoundTag(AugManager.nbtPrefix, augs);
			}
		} else {
			NBTTagCompound stackTag = new NBTTagCompound();
			stack.setTagCompound(stackTag);
			augs = new NBTTagCompound();
			stackTag.setCompoundTag(AugManager.nbtPrefix, augs);
		}
		return augs;
	}

	/**
	 * @param next
	 * @return
	 */
	public static List<NBTTagCompound> getItemAugsWithPadding(ItemStack stack) {
		List<String> validAugs = AugManager.getValidAugsForItem(stack
				.getItem());
		NBTTagCompound itemAugs = getItemAugs(stack);
		List<NBTTagCompound> augsList = new ArrayList<NBTTagCompound>();
		for (String validAug : validAugs) {
			if (itemAugs.hasKey(validAug)) {
				NBTTagCompound matchedAug = itemAugs.getCompoundTag(validAug);
				augsList.add(matchedAug);
			} else {
				NBTTagCompound newAug = AugManager.newAugOfType(validAug);
				augsList.add(newAug);
				itemAugs.setCompoundTag(validAug, newAug);

			}
		}
		return augsList;
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
	public static List<Integer> deleteFromInventory(List<ItemStack> cost,
			InventoryPlayer inventory) {
		List<Integer> slots = new LinkedList<Integer>();
		for (int j = 0; j < cost.size(); j++) {
			ItemStack stackInCost = cost.get(j);
			int remaining = stackInCost.stackSize;
			for (int i = 0; i < inventory.getSizeInventory(); i++) {
				ItemStack stackInInventory = inventory.getStackInSlot(i);
				if (stackInInventory != null) {
					if (stackInInventory.itemID == stackInCost.itemID) {
						if (stackInInventory.stackSize > remaining) {
							stackInInventory.stackSize -= remaining;
							remaining = 0;
							slots.add(i);
							break;
						} else {
							remaining -= stackInInventory.stackSize;
							inventory.setInventorySlotContents(i, null);
						}
						slots.add(i);
					}
				}
			}
		}
		return slots;
	}

	/**
	 * Sums the weights of augmentations in the list.
	 * 
	 * @param playerAugs
	 * @return
	 */
	public static float getTotalWeight(List<NBTTagCompound> playerAugs) {
		float weight = 0;
		for (NBTTagCompound aug : playerAugs) {
			if (aug.hasKey(AugManager.WEIGHT)) {
				weight += aug.getFloat(AugManager.WEIGHT);
			}
		}
		return weight;
	}
}
