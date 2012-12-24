package machinemuse.powersuits.trash;

import net.minecraft.block.Block;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;

@Deprecated
public class SlotArmorInTinkerTable extends Slot {
	/**
	 * The armor type that can be placed on that slot, it uses the same values
	 * of armorType field on ItemArmor.
	 */
	final int armorType;
	final ContainerTinkerTable table;
	private ItemStack stackCache;

	/**
	 * Constructor. Takes an inventory and an index, x and y locations for the
	 * icon, and armorType.
	 */
	SlotArmorInTinkerTable(ContainerTinkerTable table, IInventory inventory,
			int index, int x, int y,
			int armorType)
	{
		super(inventory, index, x, y);
		this.armorType = armorType;
		this.table = table;
		this.stackCache = this.getStack();
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
			if (stack.getItem() instanceof ItemArmor) {
				if (((ItemArmor) stackType).armorType == this.armorType) {
					return true;
				} else {
					return false;
				}
			} else if (stackType.shiftedIndex == Block.pumpkin.blockID
					&& this.armorType == 0) {
				return true;
			} else if (stackType.shiftedIndex == Item.skull.shiftedIndex
					&& this.armorType == 0) {
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
		return 15 + this.armorType * 16;
	}

	public void onSlotChanged() {
		super.onSlotChanged();
		table.update();
	}
}
