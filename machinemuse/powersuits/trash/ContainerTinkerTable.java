package machinemuse.powersuits.trash;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import machinemuse.powersuits.item.ItemPowerArmor;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.world.World;

@Deprecated
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

	/**
	 * Adds a recipe. See spreadsheet on first page for details.
	 */
	public void addRecipe(ItemStack outputStack, Object... recipeParameters)
	{
		String reshapedString = "";
		int currentArg = 0;
		int var5 = 0;
		int var6 = 0;

		if (recipeParameters[currentArg] instanceof String[])
		{
			String[] nextRecipeString = (String[]) ((String[]) recipeParameters[currentArg++]);

			for (int i = 0; i < nextRecipeString.length; ++i)
			{
				String nextChar = nextRecipeString[i];
				++var6;
				var5 = nextChar.length();
				reshapedString = reshapedString + nextChar;
			}
		}
		else
		{
			while (recipeParameters[currentArg] instanceof String)
			{
				String var11 = (String) recipeParameters[currentArg++];
				++var6;
				var5 = var11.length();
				reshapedString = reshapedString + var11;
			}
		}

		HashMap var12;

		for (var12 = new HashMap(); currentArg < recipeParameters.length; currentArg += 2)
		{
			Character var13 = (Character) recipeParameters[currentArg];
			ItemStack var14 = null;

			if (recipeParameters[currentArg + 1] instanceof Item)
			{
				var14 = new ItemStack((Item) recipeParameters[currentArg + 1]);
			}
			else if (recipeParameters[currentArg + 1] instanceof Block)
			{
				var14 = new ItemStack((Block) recipeParameters[currentArg + 1],
						1, -1);
			}
			else if (recipeParameters[currentArg + 1] instanceof ItemStack)
			{
				var14 = (ItemStack) recipeParameters[currentArg + 1];
			}

			var12.put(var13, var14);
		}

		ItemStack[] var15 = new ItemStack[var5 * var6];

		for (int var16 = 0; var16 < var5 * var6; ++var16)
		{
			char var10 = reshapedString.charAt(var16);

			if (var12.containsKey(Character.valueOf(var10)))
			{
				var15[var16] = ((ItemStack) var12.get(Character.valueOf(var10)))
						.copy();
			}
			else
			{
				var15[var16] = null;
			}
		}

		List<IRecipe> recipes = new ArrayList<IRecipe>();
		recipes.add(new ShapedRecipes(var5, var6, var15,
				outputStack));
	}
}
