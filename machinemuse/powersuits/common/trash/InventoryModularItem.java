package machinemuse.powersuits.common.trash;

import java.util.ArrayList;

import machinemuse.powersuits.common.item.ItemPowerArmor;
import machinemuse.powersuits.common.item.ItemPowerTool;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class InventoryModularItem implements IInventory {

	/** List of the stacks in the crafting matrix. */
	private ArrayList<ItemStack> stackList;
	private ItemStack associatedWith;
	private ContainerTinkerTable container;

	/** the width of the crafting inventory */
	public InventoryModularItem(ContainerTinkerTable container)
	{
		this.container = container;
		this.stackList = new ArrayList<ItemStack>();
	}

	/**
	 * Creates a new Inventory from the nbt tag list associated with an
	 * itemStack.
	 */
	public static InventoryModularItem fromItemStack(ItemStack stack,
			ContainerTinkerTable container) {
		InventoryModularItem inv = null;
		if (stack.getItem() instanceof ItemPowerArmor) {
			NBTTagCompound tags = stack.getTagCompound();
			if (tags != null) {
				inv = fromNBT(tags, container);
			} else {
				inv = new InventoryModularItem(container);
			}
			inv.associatedWith = stack;
			inv.addEmptySlot();
		} else if (stack.getItem() instanceof ItemPowerTool) {
			NBTTagCompound tags = stack.getTagCompound();
			if (tags != null) {
				inv = fromNBT(tags, container);
			} else {
				inv = new InventoryModularItem(container);
			}
			inv.associatedWith = stack;
			inv.addEmptySlot();
		}
		return inv;
	}

	/**
	 * Creates a new Inventory from an nbt tag list.
	 */
	public static InventoryModularItem fromNBT(NBTTagCompound tags,
			ContainerTinkerTable container) {
		InventoryModularItem inv = new InventoryModularItem(container);
		int[] moduleIDs = tags.getIntArray("Contents");
		for (int i = 0; i < moduleIDs.length; i++) {
			inv.stackList.add(new ItemStack(ItemAugmentation.index, 1,
					moduleIDs[i]));
		}
		return inv;
	}

	/**
	 * Creates an NBT tag list describing the inventory in its current state,
	 * and associates it with the given ItemStack.
	 */
	public void toNBTOfStack(ItemStack stack) {
		if (stack != null) {
			stack.setTagCompound(toNBT());
		}
	}

	/**
	 * Creates an NBT tag list describing the inventory in its current state.
	 */
	public NBTTagCompound toNBT() {
		NBTTagCompound nbt = new NBTTagCompound();
		int[] moduleIDs = new int[stackList.size()];
		for (int i = 0; i < stackList.size(); i++) {
			if (stackList.get(i) != null) {
				moduleIDs[i] = stackList.get(i).getItemDamage();
			} else {
				moduleIDs[i] = -1;
			}
		}
		nbt.setIntArray("Contents", moduleIDs);
		return nbt;
	}

	/**
	 * Returns the number of slots in the inventory.
	 */
	public int getSizeInventory()
	{
		return this.stackList.size();
	}

	/**
	 * Returns the stack in slot i
	 */
	public ItemStack getStackInSlot(int par1)
	{
		return par1 >= this.getSizeInventory() ? null : this.stackList
				.get(par1);
	}

	/**
	 * Returns the name of the inventory.
	 */
	public String getInvName()
	{
		return "container.crafting";
	}

	/**
	 * When some containers are closed they call this on each slot, then drop
	 * whatever it returns as an EntityItem - like when you close a workbench
	 * GUI.
	 */
	public ItemStack getStackInSlotOnClosing(int par1)
	{
		if (this.stackList.get(par1) != null)
		{
			ItemStack var2 = this.stackList.get(par1);
			this.stackList.remove(par1);
			return var2;
		}
		else
		{
			return null;
		}
	}

	/**
	 * Removes from an inventory slot (first arg) up to a specified number
	 * (second arg) of items and returns them in a new stack.
	 */
	public ItemStack decrStackSize(int index, int number)
	{
		if (this.stackList.get(index) != null)
		{
			ItemStack var3;

			if (this.stackList.get(index).stackSize <= number)
			{
				var3 = this.stackList.get(index);
				this.stackList.remove(index);
				return var3;
			}
			else
			{
				var3 = this.stackList.get(index).splitStack(number);

				if (this.stackList.get(index).stackSize == 0)
				{
					this.stackList.remove(index);
				}

				return var3;
			}
		}
		else
		{
			return null;
		}
	}

	/**
	 * Sets the given item stack to the specified slot in the inventory (can be
	 * crafting or armor sections).
	 */
	public void setInventorySlotContents(int index, ItemStack stack)
	{
		this.stackList.set(index, stack);
	}

	/**
	 * Returns the maximum stack size for a inventory slot. Seems to always be
	 * 64, possibly will be extended. *Isn't this more of a set than a get?*
	 */
	public int getInventoryStackLimit()
	{
		return 1;
	}

	/**
	 * Called when an the contents of an Inventory change, usually
	 */
	public void onInventoryChanged() {
		if (this.associatedWith != null) {
			this.toNBTOfStack(associatedWith);
		}
		container.update();
	}

	public void addEmptySlot() {
		this.stackList.add(null);
	}

	/**
	 * Do not make give this method the name canInteractWith because it clashes
	 * with Container
	 */
	public boolean isUseableByPlayer(EntityPlayer par1EntityPlayer)
	{
		return true;
	}

	public void openChest() {
	}

	public void closeChest() {
	}

}
