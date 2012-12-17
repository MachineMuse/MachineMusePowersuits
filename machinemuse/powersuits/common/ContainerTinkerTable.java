package machinemuse.powersuits.common;

import java.util.ArrayList;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ContainerTinkerTable extends Container {
	EntityPlayer player;
	private World world;
	private int posX;
	private int posY;
	private int posZ;
	private final int inventoryTop = 145;
	public InventoryModularItem[] itemInventories;
	public SlotArmorInTinkerTable[] armorslots;
	public ArrayList<SlotAugmentation> augslots;

	public ContainerTinkerTable(EntityPlayer player, World world, int x, int y,
			int z) {
		this.player = player;
		this.world = world;
		this.posX = x;
		this.posY = y;
		this.posZ = z;
		int id = 0;
		int slotx = 0;
		int sloty = 0;

		armorslots = new SlotArmorInTinkerTable[4];
		itemInventories = new InventoryModularItem[5];
		augslots = new ArrayList<SlotAugmentation>();

		for (int i = 0; i < 4; i++) {
			armorslots[i] = new SlotArmorInTinkerTable(this,
					player.inventory,
					player.inventory.getSizeInventory() - 1 - i,
					8,
					8 + i * 18,
					i);
			this.addSlotToContainer(armorslots[i]);
		}

		bindPlayerInventory(player);

		this.update();
	}

	public void update() {
		this.inventorySlots.removeAll(augslots);
		augslots.clear();
		for (int i = 0; i < 4; i++) {
			ItemStack stack = armorslots[i].getStack();
			if (stack != null) {
				if (stack.getItem() instanceof ItemPowerArmor) {
					itemInventories[i] = InventoryModularItem
							.fromItemStack(stack, this);
					this.bindItemInventory(itemInventories[i], i);
				}
			}
		}
	}

	public void bindItemInventory(InventoryModularItem inv, int row) {
		for (int i = 0; i < inv.getSizeInventory(); ++i) {
			SlotAugmentation slot = new SlotAugmentation(
					inv,
					i,
					32 + i * 18,
					9 + row * 18);
			this.addSlotToContainer(slot);
			this.augslots.add(slot);
		}
	}

	public void bindPlayerInventory(EntityPlayer player) {
		for (int i = 0; i < 3; ++i) {
			for (int j = 0; j < 9; ++j) {
				this.addSlotToContainer(new Slot(
						player.inventory,
						9 + j + i * 9,
						8 + j * 18,
						inventoryTop + i * 18));
			}
		}

		for (int i = 0; i < 9; ++i) {
			this.addSlotToContainer(new Slot(
					player.inventory,
					i,
					8 + i * 18,
					inventoryTop + 58));
		}
	}

	/**
	 * Callback for when the crafting gui is closed.
	 */
	public void onCraftGuiClosed(EntityPlayer par1EntityPlayer)
	{
	}

	public boolean canInteractWith(EntityPlayer par1EntityPlayer)
	{
		return par1EntityPlayer.getDistanceSq((double) this.posX + 0.5D,
				(double) this.posY + 0.5D, (double) this.posZ + 0.5D) <= 64.0D;
	}

	/**
	 * Called when a player shift-clicks on a slot. You must override this or
	 * you will crash when someone does that.
	 */
	public ItemStack transferStackInSlot(EntityPlayer par1EntityPlayer, int par2)
	{
		ItemStack var3 = null;
		Slot var4 = (Slot) this.inventorySlots.get(par2);

		if (var4 != null && var4.getHasStack())
		{
			ItemStack var5 = var4.getStack();
			var3 = var5.copy();

			if (par2 == 0)
			{
				if (!this.mergeItemStack(var5, 10, 46, true))
				{
					return null;
				}

				var4.onSlotChange(var5, var3);
			}
			else if (par2 >= 10 && par2 < 37)
			{
				if (!this.mergeItemStack(var5, 37, 46, false))
				{
					return null;
				}
			}
			else if (par2 >= 37 && par2 < 46)
			{
				if (!this.mergeItemStack(var5, 10, 37, false))
				{
					return null;
				}
			}
			else if (!this.mergeItemStack(var5, 10, 46, false))
			{
				return null;
			}

			if (var5.stackSize == 0)
			{
				var4.putStack((ItemStack) null);
			}
			else
			{
				var4.onSlotChanged();
			}

			if (var5.stackSize == var3.stackSize)
			{
				return null;
			}

			var4.onPickupFromSlot(par1EntityPlayer, var5);
		}

		return var3;
	}

}
