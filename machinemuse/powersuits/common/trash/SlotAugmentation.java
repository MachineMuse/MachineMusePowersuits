package machinemuse.powersuits.common.trash;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;

public class SlotAugmentation extends Slot {

	public SlotAugmentation(IInventory par1iInventory, int par2, int par3,
			int par4) {
		super(par1iInventory, par2, par3, par4);
	}

	/**
	 * Returns the maximum stack size for a given slot (usually the same as
	 * getInventoryStackLimit(), but 1 in the case of armor slots)
	 */
	public int getSlotStackLimit()
	{
		return 1;
	}

	/**
	 * Check if the stack is a valid item for this slot. Always true beside for
	 * the armor slots.
	 */
	public boolean isItemValid(ItemStack stack)
	{
		if (stack == null) {
			return false;
		} else {
			Item stackType = (Item) stack.getItem();
			if (stack.getItem() instanceof ItemAugmentation) {
				return true;
			} else {
				return false;
			}
		}
	}

	@SideOnly(Side.CLIENT)
	/**
	 * Returns the icon index on items.png that is used as background image of the slot.
	 */
	public int getBackgroundIconIndex()
	{
		return 18;
	}

}
