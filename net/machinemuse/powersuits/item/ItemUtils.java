package net.machinemuse.powersuits.item;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import net.machinemuse.powersuits.common.Config;
import net.machinemuse.powersuits.tinker.TinkerAction;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class ItemUtils {
	public static final String nbtPrefix = "mmmpsmod";

	public static List<TinkerAction> getValidTinkersForItem(
			EntityPlayer player, ItemStack stack) {
		List<TinkerAction> validActions = new ArrayList();
		for (TinkerAction action : Config.getTinkerings().values()) {
			if (action.validate(player, stack)) {
				validActions.add(action);
			}
		}
		return validActions;
	}

	public static NBTTagCompound getItemModularProperties(ItemStack stack) {
		if (stack == null) {
			return null;
		}
		NBTTagCompound properties = null;
		if (stack.hasTagCompound()) {
			NBTTagCompound stackTag = stack.getTagCompound();
			if (stackTag.hasKey(nbtPrefix)) {
				properties = stackTag
						.getCompoundTag(nbtPrefix);
			} else {
				properties = new NBTTagCompound();
				properties.setCompoundTag(nbtPrefix,
						properties);
			}
		} else {
			NBTTagCompound stackTag = new NBTTagCompound();
			stack.setTagCompound(stackTag);
			properties = new NBTTagCompound();
			stackTag.setCompoundTag(nbtPrefix, properties);
		}
		return properties;
	}

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
	 * Checks the given NBTTag and returns the value if it exists, otherwise 0.
	 */
	public static double getDoubleOrZero(NBTTagCompound itemProperties,
			String string) {
		double value = 0;
		if (itemProperties != null) {
			if (itemProperties.hasKey(string)) {
				value = itemProperties.getDouble(string);
			}
		}
		return value;
	}

	/**
	 * Bouncer for succinctness. Checks the item's modular properties and
	 * returns the value if it exists, otherwise 0.
	 */
	public static double getDoubleOrZero(ItemStack stack, String string) {
		return getDoubleOrZero(getItemModularProperties(stack), string);
	}

	/**
	 * Sets the value of the given nbt tag, or removes it if the value would be
	 * zero.
	 */
	public static void setDoubleOrRemove(NBTTagCompound itemProperties,
			String string,
			double value) {
		if (itemProperties != null) {
			if (value == 0) {
				itemProperties.removeTag(string);
			} else {
				itemProperties.setDouble(string, value);
			}
		}
	}

	/**
	 * Sets the given itemstack's modular property, or removes it if the value
	 * would be zero.
	 */
	public static void setDoubleOrRemove(ItemStack stack, String string,
			double value) {
		setDoubleOrRemove(getItemModularProperties(stack), string, value);
	}

	public static String getStringOrNull(NBTTagCompound itemProperties,
			String key) {
		String value = null;
		if (itemProperties != null) {
			if (itemProperties.hasKey(key)) {
				value = itemProperties.getString(key);
			}
		}
		return value;
	}

	public static String getStringOrNull(ItemStack stack, String key) {
		return getStringOrNull(getItemModularProperties(stack), key);
	}

	public static void setStringOrNull(NBTTagCompound itemProperties,
			String key, String value) {
		if (itemProperties != null) {
			if (value.equals("")) {
				itemProperties.removeTag(key);
			} else {
				itemProperties.setString(key, value);
			}
		}
	}

	public static void setStringOrNull(ItemStack stack, String key, String value) {
		setStringOrNull(getItemModularProperties(stack),
				key, value);
	}

	public static double getAvailableEnergy(EntityPlayer player) {
		double avail = 0;
		for (ItemStack stack : getModularItemsInInventory(player.inventory)) {
			avail += ((IModularItem) stack.getItem()).getJoules(stack);
		}
		return avail;
	}

	public static double getMaxEnergy(EntityClientPlayerMP player) {
		double max = 0;
		for (ItemStack stack : getModularItemsInInventory(player.inventory)) {
			max += ((IModularItem) stack.getItem()).getMaxJoules(stack);
		}
		return max;
	}
}
